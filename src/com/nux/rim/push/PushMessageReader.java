/**
 * AUTO_COPYRIGHT_SUB_TAG
 */
package com.nux.rim.push;

import java.io.ByteArrayInputStream;

import javax.microedition.io.Connection;

import net.rim.device.api.io.Base64InputStream;
import net.rim.device.api.io.http.HttpServerConnection;
import net.rim.device.api.io.http.PushInputStream;
import net.rim.device.api.util.Arrays;

/**
 * Reads incoming push messages and extracts texts and images. Uses main controller to display the data.
 */
public class PushMessageReader {

    // HTTP header property that carries unique push message ID
    private static final String MESSAGE_ID_HEADER = "Push-Message-ID";
    // content type constant for text messages
    private static final String MESSAGE_TYPE_TEXT = "text";
    // content type constant for image messages
    private static final String MESSAGE_TYPE_IMAGE = "image";

    private static final int MESSAGE_ID_HISTORY_LENGTH = 10;
    private static String[] messageIdHistory = new String[ MESSAGE_ID_HISTORY_LENGTH ];
    private static byte historyIndex;

    private static byte[] buffer = new byte[ 15 * 1024 ];
    private static byte[] imageBuffer = new byte[ 10 * 1024 ];

    /**
     * Reads the incoming push message from the given streams in the current thread and notifies controller to display the
     * information.
     */
    public static void process( PushInputStream pis, Connection conn ) {
        Logger.log( "Reading incoming push message ..." );

        try {

            HttpServerConnection httpConn;
            if( conn instanceof HttpServerConnection ) {
                httpConn = (HttpServerConnection) conn;
            } else {
                throw new IllegalArgumentException( "Can not process non-http pushes, expected HttpServerConnection but have "
                        + conn.getClass().getName() );
            }

            String msgId = httpConn.getHeaderField( MESSAGE_ID_HEADER );
            String msgType = httpConn.getType();
            String encoding = httpConn.getEncoding();

            Logger.log( "Message props: ID=" + msgId + ", Type=" + msgType + ", Encoding=" + encoding );
            
            boolean accept = true;
            if( !alreadyReceived( msgId ) ) {
                byte[] binaryData;
                
                if(msgId == null) {
                    msgId = String.valueOf( System.currentTimeMillis() );
                }
                
                if( msgType == null ) {
                    Logger.warn( "Message content type is NULL" );
                    accept = false;
                } else if( msgType.indexOf( MESSAGE_TYPE_TEXT ) >= 0 ) {
                    // a string
                    int size = pis.read( buffer );
                    binaryData = new byte[size];
                    System.arraycopy( buffer, 0, binaryData, 0, size );
                    PushMessage message = new PushMessage(binaryData);
                    PushController.onMessage( message );
                } else if( msgType.indexOf( MESSAGE_TYPE_IMAGE ) >= 0 ) {
                    // an image in binary or Base64 encoding
                    int size = pis.read( buffer );
                    if( encoding != null && encoding.equalsIgnoreCase( "base64" ) ) {
                        // image is in Base64 encoding, decode it
                        Base64InputStream bis = new Base64InputStream( new ByteArrayInputStream( buffer, 0, size ) );
                        size = bis.read( imageBuffer );
                    }
                    
                    binaryData = new byte[ size ];
                    System.arraycopy( buffer, 0, binaryData, 0, size );
                    PushMessage message = new PushMessage(binaryData);
                    PushController.onMessage( message );
                } else {
                    Logger.warn( "Unknown message type " + msgType );
                    accept = false;
                }
            } else {
                Logger.warn( "Received duplicate message with ID " + msgId );
            }

            // perform application acknowledgment
            if( PushConfig.isApplicationAcknoledgementSupported() ) {
                if( accept ) {
                    pis.accept();
                } else {
                    PushUtils.decline( pis, PushInputStream.DECLINE_REASON_USERDCU );
                }
            }
        } catch( Exception e ) {
            Logger.warn( "Failed to process push message: " + e );
        } finally {
            PushUtils.close( conn, pis, null );
        }
    }

    /**
     * Check whether the message with this ID has been already received.
     */
    private static boolean alreadyReceived( String id ) {
        if( id == null ) {
            return false;
        }
        
        if( Arrays.contains( messageIdHistory, id ) ) {
            return true;
        }
        
        // new ID, append to the history (oldest element will be eliminated)
        messageIdHistory[ historyIndex++ ] = id;
        if( historyIndex >= MESSAGE_ID_HISTORY_LENGTH ) {
            historyIndex = 0;
        }
        return false;
    }

}
