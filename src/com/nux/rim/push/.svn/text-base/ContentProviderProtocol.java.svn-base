//#preprocess
/**
 * AUTO_COPYRIGHT_SUB_TAG
 */
package com.nux.rim.push;

import java.io.IOException;
import java.util.Random;

import net.rim.device.api.system.DeviceInfo;

import com.nux.rim.push.cmd.NetworkCommand.Transaction;

/**
 * Provides registration functions with Content Provider server using Framework library.
 * <p>
 * Performs network commands in a dedicated thread so the caller is not blocked and may be executed from the UI application event
 * thread.
 */
public class ContentProviderProtocol {

    /**
     * Command to perform user subscription
     */
    public static final int CMD_SUBSCRIBE = 0;
    /**
     * Command to un-subscribe user
     */
    public static final int CMD_UNSUBSCRIBE = 1;
    /**
     * Command to suspend user subscription
     */
    public static final int CMD_SUSPEND = 2;
    /**
     * Command to resume user subscription
     */
    public static final int CMD_RESUME = 3;

    // List of Content Provider & Framework server expected parameters
    private static final String USERNAME_PARAM = "username=";
    private static final String PASSWORD_PARAM = "&password=";
    private static final String PIN_PARAM = "&address=";
    private static final String APP_ID_PARAM = "&appid=";
    private static final String OS_PARAM = "&osversion=";
    private static final String MODEL_PARAM = "&model=";
    private static final String USER_DEFINED_PARAM = "&field1=";

    // List of URLs supported by Content Provider and Frameworks server
    private static final String SUBSCRIBE_URL = "/subscribe?";
    private static final String UNSUBSCRIBE_URL = "/unsubscribe?";
    private static final String SUSPEND_URL = "/suspend?";
    private static final String RESUME_URL = "/resume?";

    /**
     * Performs a command specified by one of the CMD_XXX constants
     */
    public static void performCommand( int cmd, final String username, final String password, Transaction tx ) throws IOException {
        String url = PushConfig.getContentProviderUrl();
        final String cmdName;
        switch( cmd ) {
            case CMD_SUBSCRIBE:
                url += SUBSCRIBE_URL;
                cmdName = "Subscribe";
                break;
            case CMD_UNSUBSCRIBE:
                url += UNSUBSCRIBE_URL;
                cmdName = "Unsubscribe";
                break;
            case CMD_SUSPEND:
                url += SUSPEND_URL;
                cmdName = "Suspend";
                break;
            case CMD_RESUME:
                url += RESUME_URL;
                cmdName = "Resume";
                break;
            default:
                return;
        }

        // append username, password and other parameters
        url += USERNAME_PARAM + username;
        url += PASSWORD_PARAM + password;
        url += APP_ID_PARAM + PushConfig.getAppId();
        url += PIN_PARAM + Integer.toHexString( DeviceInfo.getDeviceId() );
//#ifdef HANDHELD_VERSION_42
        url += OS_PARAM + "4.2.0";
//#else
        url += OS_PARAM + DeviceInfo.getSoftwareVersion();
//#endif
        url += MODEL_PARAM + DeviceInfo.getDeviceName();
        
        // append a user defined parameter
        url += USER_DEFINED_PARAM + new Random().nextInt( 1000 );

        String response;
        try {
            response = PushUtils.request( url, tx );
        } catch( IOException e ) {
            // subscription failed
            if( !tx.isCancelled() ) {
                Logger.warn( "Content Provider network command [" + cmdName + "] failed, caused by " + e.getMessage() );
                throw new IOException( "Network operation [" + cmdName + "] failed. Make sure that Content Provider URL is accessible." );
            }
            return;
        }
        
        checkResult( response );
    }

    /**
     * Checks content provider response and throws an exception if error code was returned
     */
    private static void checkResult( String response ) throws IOException {
        if( response.equals( "rc=200" ) ) {
            // success
            return;
        } else if( response.equals( "rc=10001" ) ) {
            throw new IOException( "The address specified is null or empty or is longer than 40 characters in length" );
        } else if( response.equals( "rc=10011" ) ) {
            throw new IOException( "The OS version specified is null or empty or is longer than 20 characters. "
                    + "If the model specified is null or empty or is longer than 20 characters" );
        } else if( response.equals( "rc=10002" ) ) {
            throw new IOException(
                    "The push application id specified is null or empty or if the push application with the given id cannot be found" );
        } else if( response.equals( "rc=10007" ) ) {
            throw new IOException( "A user's subscription to a push application cannot be found" );
        } else if( response.equals( "rc=10020" ) ) {
            throw new IOException( "The subscriber id specified is null or empty" );
        } else if( response.equals( "rc=10021" ) ) {
            throw new IOException( "A user’s subscription to a push application is inactive, then it cannot be suspended" );
        } else if( response.equals( "rc=10022" ) ) {
            throw new IOException( "A user’s subscription to a push application is inactive, then it cannot be resumed" );
        } else if( response.equals( "rc=10023" ) ) {
            throw new IOException( "The user name specified is null or empty" );
        } else if( response.equals( "rc=10024" ) ) {
            throw new IOException( "The password specified is null or empty" );
        } else if( response.equals( "rc=10025" ) ) {
            throw new IOException( "The subscription is not supported for a push application. "
                    + "This will only be possible for BES push applications which have the bypass subscription flag set to true." );
        } else if( response.equals( "rc=10026" ) ) {
            throw new IOException( "Authentication of the subscriber, based on the username and password they passed in, failed." );
        } else if( response.equals( "rc=10027" ) ) {
            throw new IOException( "The content provider's subscription attempt (subscribe/unsubscribe/suspend/resume) failed" );
        } else if( response.equals( "rc=-9999" ) ) {
            throw new IOException( "Server internal error" );
        }

    }

}
