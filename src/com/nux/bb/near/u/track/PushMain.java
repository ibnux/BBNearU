/**
 * AUTO_COPYRIGHT_SUB_TAG
 */
package com.nux.bb.near.u.track;

import net.rim.device.api.applicationcontrol.ApplicationPermissions;
import net.rim.device.api.applicationcontrol.ApplicationPermissionsManager;
import net.rim.device.api.system.EventLogger;
import net.rim.device.api.ui.UiApplication;

import com.nux.bb.near.u.track.obj.DBStor;
import com.nux.bb.near.u.track.ui.PopupAgreement;
import com.nux.bb.near.u.track.ui.RoomScreen;
import com.nux.bb.near.u.track.util.Util;
import com.nux.rim.push.PushController;
import com.nux.rim.push.lib.PushLibFactory;

/**
 * Main entry point for the push application. Creates application instance and pushes the welcome screen.
 */
public class PushMain {

	static RoomScreen rs;
	public static void main(String[] args) {

		EventLogger.register(Util.getUidLog(),"BBnearU", EventLogger.VIEWER_STRING);
		if(args.length>0 && args[0].equals("startup")){
			_assertHasPermissions();
			StartApp sa = new StartApp();
			sa.enterEventDispatcher();
		}else{
				rs = new RoomScreen();
				UiApplication application = PushLibFactory.getUiApplication();
				// process events
		        application.pushScreen(rs);
				rs.GetActiveRoom();
		        PushController.appStarted();
				if(!DBStor.get_setting().isStuju()){
					application.pushScreen(new PopupAgreement());
				}
				application.enterEventDispatcher();
		}
    }

	public static RoomScreen getRoomScreen() {
		return rs;
	}

 // ASK FOR PERMISSIONS
	private static void _assertHasPermissions() {

		// Capture the current state of permissions and check against the requirements.
		ApplicationPermissionsManager apm = ApplicationPermissionsManager.getInstance();
		ApplicationPermissions original = apm.getApplicationPermissions();
		boolean permissionsOk = false;
	
		if (original.getPermission(ApplicationPermissions.PERMISSION_LOCATION_DATA) ==
		    ApplicationPermissions.VALUE_ALLOW
		    &&
		    original.getPermission(ApplicationPermissions.PERMISSION_ORGANIZER_DATA) ==
		    ApplicationPermissions.VALUE_ALLOW
		    &&
		    original.getPermission(ApplicationPermissions.PERMISSION_EMAIL) ==
		    ApplicationPermissions.VALUE_ALLOW
		    &&
		    original.getPermission(ApplicationPermissions.PERMISSION_FILE_API) ==
		    ApplicationPermissions.VALUE_ALLOW
		    &&
		    original.getPermission(ApplicationPermissions.PERMISSION_INTERNET) ==
		    ApplicationPermissions.VALUE_ALLOW
		    &&
		    original.getPermission(ApplicationPermissions.PERMISSION_RECORDING) ==
		    ApplicationPermissions.VALUE_ALLOW
		    &&
		    original.getPermission(ApplicationPermissions.PERMISSION_WIFI) ==
		    ApplicationPermissions.VALUE_ALLOW)
		{
			permissionsOk = true;
		}else{
		  ApplicationPermissions permRequest = new ApplicationPermissions();
		  permRequest.addPermission(ApplicationPermissions.PERMISSION_LOCATION_DATA);
		  permRequest.addPermission(ApplicationPermissions.PERMISSION_ORGANIZER_DATA);
		  permRequest.addPermission(ApplicationPermissions.PERMISSION_EMAIL);
		  permRequest.addPermission(ApplicationPermissions.PERMISSION_FILE_API);
		  permRequest.addPermission(ApplicationPermissions.PERMISSION_INTERNET);
		  permRequest.addPermission(ApplicationPermissions.PERMISSION_RECORDING);
		  permRequest.addPermission(ApplicationPermissions.PERMISSION_WIFI);
	
		  permissionsOk = apm.invokePermissionsRequest(permRequest);
	
		}
	
		if (!permissionsOk) {
			System.exit(0);
		}
	}

}
