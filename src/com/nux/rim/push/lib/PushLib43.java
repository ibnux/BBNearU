package com.nux.rim.push.lib;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.ServerSocketConnection;

import net.rim.device.api.io.http.HttpServerConnection;
import net.rim.device.api.io.http.MDSPushInputStream;
import net.rim.device.api.io.http.PushInputStream;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.SIMCardException;
import net.rim.device.api.system.SIMCardInfo;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.util.Arrays;

import com.nux.rim.push.Logger;
import com.nux.rim.push.PersistentStorage;
import com.nux.rim.push.PushConfig;
import com.nux.rim.push.PushController;
import com.nux.rim.push.PushMessageReader;
import com.nux.rim.push.PushUtils;
import com.nux.rim.push.cmd.NetworkCommand.Transaction;

/**
 * Library that is used on 4.3+ BlackBerry devices
 * 
 */
public class PushLib43 extends UiApplication implements BpasProtocol, PushMessageListener {

    private MessageReadingThread thread;

    private static final String REGISTER_SUCCESSFUL = "rc=200";
    private static final String DEREGISTER_SUCCESSFUL = REGISTER_SUCCESSFUL;
    private static final String USER_ALREADY_SUBSCRIBED = "rc=10003";
    private static final String ALREADY_UNSUSCRIBED_BY_USER = "rc=10004";
    private static final String ALREADY_UNSUSCRIBED_BY_PROVIDER = "rc=10005";

    // ********* BpasProtocol implementation ********

    public void register( Transaction tx ) throws IOException {
        String registerUrl = formRegisterRequest( PushConfig.getBpasUrl(), PushConfig.getAppId(), null );
        String response = PushUtils.request( registerUrl, tx );
        registerUrl = formRegisterRequest( PushConfig.getBpasUrl(), PushConfig.getAppId(), response );
        response = PushUtils.request( registerUrl, tx );
        if( REGISTER_SUCCESSFUL.equals( response ) || USER_ALREADY_SUBSCRIBED.equals( response ) ) {
            // successfully registered, update the status
            // store the SIM card info so that we cancel listening on SIM swap event
            saveSimCard();
        } else {
            Logger.warn( "BPAS rejected registration" );
            // rejected by BPDS
            throw new IOException( "BPAS server rejected registration. Details: response code - " + response );
        }
    }

    public void unregister( Transaction tx ) throws IOException {
        String unRegisterUrl = formUnRegisterRequest( PushConfig.getBpasUrl(), PushConfig.getAppId(), null );
        String response = PushUtils.request( unRegisterUrl, tx );
        unRegisterUrl = formUnRegisterRequest( PushConfig.getBpasUrl(), PushConfig.getAppId(), response );
        response = PushUtils.request( unRegisterUrl, tx );
        if( DEREGISTER_SUCCESSFUL.equals( response ) || ALREADY_UNSUSCRIBED_BY_USER.equals( response )
                || ALREADY_UNSUSCRIBED_BY_PROVIDER.equals( response ) ) {
            // successfully unregistered
        } else {
            // rejected by BPDS
            Logger.warn( "BPAS rejected un-registration" );
            throw new IOException( "BPAS server rejected de-registration. Details: response code - " + response );
        }
    }
    
    private void saveSimCard() {
        byte[] imsi = null;
        try {
            imsi = SIMCardInfo.getIMSI();
        } catch( SIMCardException e ) {
            Logger.log( "SIM card is not ready, details: " + e.getMessage() );
        }
        PersistentStorage.setSimCardInfo( imsi );
    }

    // ********* PushMessageListener implementation ********

    public boolean applicationCanQuit() {
        // the application may quit if we are not listening for push messages
        return thread == null;
    }

    public void startListening() {
        // start listening thread
        if( thread == null ) {
            // check whether we are starting to listen on the same SIM card
            if( wasSimSwap() ) {
                PushController.onSimChange();
            } else {
                thread = new MessageReadingThread();
                thread.start();
            }
        }
    }

    public void stopListening() {
        if( thread != null ) {
            thread.stopRunning();
            thread = null;
        }
    }

    private static boolean wasSimSwap() {
        byte[] storedSim = PersistentStorage.getSimCardInfo();
        byte[] currentSim = null;
        try {
            currentSim = SIMCardInfo.getIMSI();
        } catch( SIMCardException e ) {
        }
        Logger.log( "Comparing SIM card data, current - " + arrayToString( currentSim ) + ", stored - " + arrayToString( storedSim ) );
        if( storedSim == null ) {
            // we didn't store any SIM card info at the registration time
            if( currentSim == null ) {
                // stored and current SIM are both NULL,
                // assume that this is the same user
                return false;
            } else {
                // stored SIM is NULL while current is not NULL
                return true;
            }
        } else {
            if( currentSim == null ) {
                // SIM card was removed
                return true;
            } else {
                // check that the stored card is the current one
                return !Arrays.equals( storedSim, currentSim );
            }
        }
    }
    
    private static String arrayToString( byte[] arr ) {
        if( arr == null ) {
            return "NULL";
        }
        StringBuffer sb = new StringBuffer( "[" );
        for( int i = 0; i < arr.length && i < 4; i++ ) {
            sb.append( arr[ i ] ).append( ',' );
        }
        sb.append( "...]" );
        return sb.toString();
    }

    /**
     * Thread that processes incoming connections through {@link PushMessageReader}
     */
    private static class MessageReadingThread extends Thread {

        private boolean running;
        private ServerSocketConnection socket;
        private HttpServerConnection conn;
        private InputStream inputStream;
        private PushInputStream pushInputStream;

        public MessageReadingThread() {
            this.running = true;
        }

        public void run() {
            String url = PushUtils.formPushListenUrl();
            Logger.log( "Starting to listen for push messages through '" + url + "'" );

            try {
                socket = (ServerSocketConnection) Connector.open( url );
            } catch( IOException ex ) {
                // can't open the port, probably taken by another application
                onListenError( ex );
            }

            while( running ) {
                try {
                    Object o = socket.acceptAndOpen();
                    conn = (HttpServerConnection) o;
                    inputStream = conn.openInputStream();
                    pushInputStream = new MDSPushInputStream( conn, inputStream );
                    PushMessageReader.process( pushInputStream, conn );
                } catch( Exception e ) {
                    if( running ) {
                        Logger.warn( "Failed to read push message, caused by " + e.getMessage() );
                        running = false;
                    }
                } finally {
                    PushUtils.close( conn, pushInputStream, null );
                }
            }

            Logger.log( "Stopped listening for push messages" );
        }

        public void stopRunning() {
            running = false;
            PushUtils.close( socket, null, null );
        }

        private void onListenError( final Exception ex ) {
            Logger.warn( "Failed to open port, caused by " + ex );
            PushUtils.runOnEventThread( new Runnable() {
                public void run() {
                    PushController.onPortListenError( ex );
                }
            } );
        }
    }

    private static String formRegisterRequest( String bpasUrl, String appId, String token ) {
        StringBuffer sb = new StringBuffer( bpasUrl );
        sb.append( "/mss/PD_subReg?" );
        sb.append( "serviceid=" ).append( appId );
        //#ifdef HANDHELD_VERSION_42
        sb.append( "&osversion=4.2.0" );
        //#else
        sb.append( "&osversion=" ).append( DeviceInfo.getSoftwareVersion() );
        //#endif
        sb.append( "&model=" ).append( DeviceInfo.getDeviceName() );
        if( token != null && token.length() > 0 ) {
            sb.append( "&" ).append( token );
        }
        return sb.toString();
    }

    private static String formUnRegisterRequest( String bpasUrl, String appId, String token ) {
        StringBuffer sb = new StringBuffer( bpasUrl );
        sb.append( "/mss/PD_subDereg?" );
        sb.append( "serviceid=" ).append( appId );
        if( token != null && token.length() > 0 ) {
            sb.append( "&" ).append( token );
        }
        return sb.toString();
    }
}
