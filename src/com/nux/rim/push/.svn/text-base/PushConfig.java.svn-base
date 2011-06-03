/**
 * AUTO_COPYRIGHT_SUB_TAG
 */
package com.nux.rim.push;

import net.rim.device.api.util.Persistable;

/**
 * Encapsulates push configuration for the application
 */
public class PushConfig implements Persistable {

    // Default properties used for the demo.
    // You should register with BlackBerry Push API server and obtain
    // similar values for your application.
    
    // TCP port to listen for push messages
    private int port = 29049;
    // Application ID generated during Push API registration
    private String appId = "1046-1kOnofh179i83e7t73";
    // URL to the Push BPAS server
    private String bpasUrl = "http://pushapi.eval.blackberry.com";
    // URL to Content Provider using Frameworks library
    private String contentProviderUrl = "http://push.bbnearu.com";

    private static PushConfig instance;
    
    static {
        instance = PersistentStorage.getConfig();
        if( instance == null ) {
            instance = new PushConfig();
            PersistentStorage.setConfig( instance );
        }
    }

    /**
     * Defines whether application supports application level acknowledgment
     */
    private boolean applicationAcknoledgment = true;

    public static int getPort() {
        return instance.port;
    }

    public static String getAppId() {
        return instance.appId;
    }

    public static String getBpasUrl() {
        return instance.bpasUrl;
    }

    public static boolean isApplicationAcknoledgementSupported() {
        return instance.applicationAcknoledgment;
    }

    public static String getContentProviderUrl() {
        return instance.contentProviderUrl;
    }
    
}
