/**
 * AUTO_COPYRIGHT_SUB_TAG
 */
package com.nux.rim.push;

import net.rim.blackberry.api.browser.Browser;
import net.rim.blackberry.api.browser.BrowserSession;
import net.rim.blackberry.api.homescreen.HomeScreen;
import net.rim.blackberry.api.messagelist.ApplicationIcon;
import net.rim.blackberry.api.messagelist.ApplicationIndicator;
import net.rim.blackberry.api.messagelist.ApplicationIndicatorRegistry;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.component.Dialog;

import com.nux.bb.near.u.track.PushMain;
import com.nux.bb.near.u.track.obj.DBStor;
import com.nux.bb.near.u.track.obj.MsgChat;
import com.nux.bb.near.u.track.obj.Setting;
import com.nux.bb.near.u.track.ui.RoomScreen;
import com.nux.bb.near.u.track.util.Util;
import com.nux.rim.push.cmd.NetworkCommand;
import com.nux.rim.push.cmd.RegisterCommand;
import com.nux.rim.push.cmd.UnregisterCommand;
import com.nux.rim.push.lib.PushLibFactory;
import com.nux.rim.push.lib.PushMessageListener;

/**
 * Main controller of the push application.
 * <p>
 * The class performs registration actions, analyzes errors and handles state transitions, uses UI screens to display push
 * messages.
 */
public class PushController {
	static String user = "bbnearu";
    static String pass = "uraenbb";
    private static NetworkCommand currentCommand;
    private static final Object cmdLock = new Object();

    /**
     * Registers current device and application to receive push messages. This method is called when user selects Register menu
     * item and should be called in the application event thread.
     */
    public static void register() {
        if( commandInProgress() ) {
            return;
        }

        // check that configuration is set
        if( invalidSettings() ) {
            return;
        }

        if( !NetworkCoverageManager.hasCoverage() ) {
            int retry = Dialog.ask( Dialog.D_YES_NO, "The BlackBerry smartphone is out of coverage right now. "
                    + "Do you want to schedule automatic re-try when network is accessible?" );
            if( retry == Dialog.NO ) {
                // user will register manually when device has coverage
                Dialog.inform( "Please register when smartphone is in coverage" );
                return;
            }
        }

            // perform the registration
            RegisterCommand command = new RegisterCommand( user, pass );
            executeCommand( command );

    }


    /**
     * Called when application is started either by user or by Push API. If we registered for push messages then start the message
     * listener thread.
     */
    public static void appStarted() {

        // check whether this is the first application launch
        if( !PersistentStorage.isOnceLaunched() ) {
            PersistentStorage.setOnceLaunched( true );
            return;
        }

        // the application may have been started by Push API when a message arrived
        // check the registration status and if registered then start listening thread
        if( PersistentStorage.isRegistered() ) {
            PushMessageListener messageListener = PushLibFactory.getPushMessageListener();
            messageListener.startListening();
        }
    }


    /**
     * Unregisters from receiving push messages
     */
    public static void unregister() {
        // check that configuration is set and no command in progress
        if( commandInProgress() || invalidSettings() ) {
            return;
        }

        if( !NetworkCoverageManager.hasCoverage() ) {
            int retry = Dialog.ask( Dialog.D_YES_NO, "The BlackBerry smartphone is out of coverage right now. "
                    + "Do you want to schedule automatic re-try when network is accessible?" );
            if( retry == Dialog.NO ) {
                // user will register manually when device has coverage
                Dialog.inform( "Please un-register when smartphone is in coverage" );
                return;
            }
        }

            // perform the registration
            UnregisterCommand command = new UnregisterCommand( user, pass, false );
            executeCommand( command );

    }

    public static void updateCountLabels() {    }

    public static void updateIndicator( boolean apa) {
        ApplicationIndicatorRegistry indicatorRegistry = ApplicationIndicatorRegistry.getInstance();
        ApplicationIndicator indicator = indicatorRegistry.getApplicationIndicator();
        if( indicator == null ) {
            ApplicationIcon icon = new ApplicationIcon( EncodedImage.getEncodedImageResource( "chat-new24.png" ) );
            indicator = indicatorRegistry.register( icon, true, true );
        }

        if( apa ) {
            indicator.setVisible( true );
            HomeScreen.updateIcon(Bitmap.getBitmapResource("icon-new.png"), 0);
        } else {
            indicator.setVisible( false );
            HomeScreen.updateIcon(Bitmap.getBitmapResource("icon.png"), 0);
        }
    }

    /**
     * Called when SIM card has changed and our application was unsubscribed automatically by Push API
     */
    public static void onSimChange() {
        Logger.warn( "SIM card has changed. Automatically unregistering ..." );

        PersistentStorage.setRegistered( false );
        PushMessageListener listener = PushLibFactory.getPushMessageListener();
        listener.stopListening();

        Runnable uiUpdater = new Runnable() {
            public void run() {
                // SIM card has changed
                // this can happen if the BlackBerry device was given to another user
                UnregisterCommand command = new UnregisterCommand( null, null, true );
                executeCommand( command );

                // re-authorize the user and re-register if needed
                int registerAgain = Dialog.ask( Dialog.D_YES_NO, "The SIM card has changed and registration is being cancelled. "
                        + "Do you want to re-register for push messages?" );
                if( registerAgain == Dialog.YES ) {
                    register();
                }
            }
        };
        PushUtils.runOnEventThread( uiUpdater );
    }

    /**
     * Called when a new message is pushed
     */
    public static void onMessage( PushMessage message ) {
        byte[] data = message.getData();

        /**int size = data.length;
        if( message.isTextMesasge() ) {
            Logger.log( "Text message pushed, size " + size + " bytes" );
        } else {
            Logger.log( "Image message pushed, size " + size + " bytes" );
        }
        PersistentStorage.incTotalMessageCount();
        PushMessage[] messages = PersistentStorage.getMessages();
        int length = messages.length;
        PushMessage[] extended = new PushMessage[ length + 1 ];
        System.arraycopy( messages, 0, extended, 0, length );
        extended[ length ] = message;
        PersistentStorage.setMessages( extended );*/
        final String txt = new String(data);
        Util.Log(txt);
        if(txt.equals("PushTest")){
        	Runnable uiUpdater = new Runnable() {
                public void run() {
                	Dialog.alert("Push Test Success!");
                	Setting set = DBStor.get_setting();
                	set.setTest(true);
                	DBStor.set_setting(set);
                }
            };
            PushUtils.runOnEventThread( uiUpdater );
            return;
        }
        try{
	        if(txt.startsWith("roomchat")){
	        	//0roomchat|1roomid|2pin|3nick|4url|5text
	        	String[] temp = Util.split(txt, "<ch4t5>");
	        	final String room = temp[1];
	        	final MsgChat mc = new MsgChat(temp[5],temp[3],temp[2],temp[4],System.currentTimeMillis());
	        	final RoomScreen rom = PushMain.getRoomScreen();
	        	Runnable uiUpdater = new Runnable() {
	                public void run() {
	                	//Dialog.alert(txt);
	                    //updateIndicator( 1 );
	                	rom.addNewMessage(room, mc);
	                }
	            };
	            PushUtils.runOnEventThread( uiUpdater );
	        }else if(txt.startsWith("news:")){
	        }else if(txt.startsWith("request:")){
	        }else if(txt.startsWith("update:")){
        		updateIndicator(true);
	        	Runnable uiUpdater = new Runnable() {
	                public void run() {
	                	if(Dialog.ask(Dialog.D_YES_NO,"New version has been added, Update Application?")==Dialog.YES){
	                		BrowserSession bs = Browser.getDefaultSession();
	                		bs.displayPage(txt.substring(7));
	                		bs.showBrowser();
	                		System.exit(0);
	                	}
	                }
	            };
	            PushUtils.runOnEventThread( uiUpdater );
	        }
        }catch(Exception e){
        	Util.Log("push "+e.getMessage());
        }
    }

    /**
     * Check whether application can quit or should remain running in the background
     */
    public static boolean canQuit() {
        synchronized( cmdLock ) {
            if(currentCommand != null) {
                return false;
            }
        }

        PushMessageListener messageListener = PushLibFactory.getPushMessageListener();
        return messageListener.applicationCanQuit();
    }


    private static boolean invalidSettings() {
        String appId = PushConfig.getAppId();
        String cpUrl = PushConfig.getContentProviderUrl();
        String bpasUrl = PushConfig.getBpasUrl();
        if( empty( appId ) || empty( cpUrl ) || empty( bpasUrl ) ) {
            Dialog.inform( "Some of the settings are empty or NULL.\nPlease make sure that all of the following are set"
                    + " in the application settings: application ID, content provider URL and BPAS URL" );
            return true;
        }
        return false;
    }

    private static boolean empty( String s ) {
        return s == null || s.trim().length() == 0;
    }

    private static boolean commandInProgress() {
        NetworkCommand currentLocal;
        synchronized( cmdLock ) {
            currentLocal = currentCommand;
        }
        if( currentLocal == null ) {
            return false;
        }
        String commandName = currentLocal.getCommandName();
        int cancel = Dialog.ask( Dialog.D_YES_NO, "The BlackBerry smartphone is currently performing the following action - "
                + commandName + ".\n Do you want to cancel it before executing new action?" );
        if( cancel == Dialog.YES ) {
            synchronized( cmdLock ) {
                if( currentLocal == currentCommand ) {
                    currentCommand.cancel();
                    currentCommand = null;
                    return false;
                } else {
                    return commandInProgress();
                }
            }
        } else {
            // user doesn't want to cancel current command
            return true;
        }
    }

    public static void executeCommand( NetworkCommand cmd ) {
        synchronized( cmdLock ) {
            if( currentCommand != null ) {
                currentCommand.cancel();
            }
            currentCommand = cmd;
            new Thread( cmd ).start();
        }
    }

    public static void commandExecuted( NetworkCommand cmd, boolean canceled, Exception error ) {
        NetworkCommand commandLocal = null;
        synchronized( cmdLock ) {
            if( currentCommand == cmd ) {
                commandLocal = currentCommand;
                currentCommand = null;
            }
        }
        if( commandLocal == null ) {
            return;
        }

        // check whether we need to listen for push messages
        PushMessageListener messageListener = PushLibFactory.getPushMessageListener();
        if( PersistentStorage.isRegistered() ) {
            messageListener.startListening();
        } else {
        	Setting set = DBStor.get_setting();
        	set.setTest(false);
        	DBStor.set_setting(set);
            messageListener.stopListening();
        }

        boolean autoCommand = false;
        if(commandLocal instanceof UnregisterCommand) {
            UnregisterCommand unregisterCommand = (UnregisterCommand) commandLocal;
            autoCommand = unregisterCommand.isOnSimSwap();
        }

        if( !canceled && !autoCommand ) {
            String name = commandLocal.getCommandName();
            // notify the user
            if( error == null ) {
                Dialog.inform( "Request to " + name + " executed successfully" );
            } else {
                Dialog.inform( "Request to " + name + " failed. Caused by " + error );
            }
        }
    }

    public static void onPortListenError( Exception error ) {
        Dialog.alert( "Can not listen for push messages. "
                + "Network port may be used by another application or blocked by firewall. "
                + "You will NOT receive messages. Details: " + error );
    }
}
