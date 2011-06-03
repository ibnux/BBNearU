/**
 * AUTO_COPYRIGHT_SUB_TAG
 */
package com.nux.rim.push.lib;


/**
 * Listener for push messages. The implementation is dependent on the BlackBerry handheld firmware.
 * 
 */
public interface PushMessageListener {

    /**
     * Starts listening for push messages.
     */
    public void startListening();

    /**
     * Stops listening for push messages
     */
    public void stopListening();

    /**
     * Check whether application can quit and continue listening for messages.
     * <p>
     * The Push API starts registered push application automatically which means that the application doesn't have to be running
     * in the background waiting for messages. On devices with no Push API library our application is responsible for listening
     * and can not quit while listening.
     */
    public boolean applicationCanQuit();

}
