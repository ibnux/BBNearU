package com.nux.bb.near.u.track.util;

/*
 *  Code by iBNuX.net April 2010
 *  +6285624644268
 *  me@ibnux.net
 */

import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import net.rim.blackberry.api.browser.Browser;
import net.rim.blackberry.api.browser.BrowserSession;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.EventLogger;

public class Util {
	public static String GetVer(){
		return "1.0.18";
	}
	public static String GetURL(){
		return "http://service.bbnearu.com/";
	}
	public static String GetURLChat(){
		return GetURL()+"chats/";
	}
	public static String GetPath(){
		return "file:///store/home/user/";
	}

	public static long getUidLog(){
		return 0x8f706dde8b46ead6L;
	}
	public static void Log(String data){
		if(Integer.toHexString(DeviceInfo.getDeviceId()).equals("2300375f"))
		EventLogger.logEvent( getUidLog(), data.getBytes() );
	}

	public static void BukaWeb(String Url){
		BrowserSession bs = Browser.getDefaultSession();
		bs.displayPage(Url);
		bs.showBrowser();
	}

    public static boolean tulisFile(String path,String txt){
        try {
            FileConnection fconn = (FileConnection)Connector.open(path,Connector.READ_WRITE);
            if (!fconn.exists()){
                fconn.create();  // create the file if it doesn't exist
        	}else{
        		fconn.delete();
        		fconn.create();
        	}
            OutputStream os =fconn.openOutputStream();
            os.write(txt.getBytes());
            os.close();
            fconn.close();
            return true;
        }
        catch (IOException ioe) {
            return false;
        }
    }
   public static String[] split(String original, String separator) {
        Vector nodes = new Vector();

        // Parse nodes into vector
        int index = original.indexOf(separator);
        while(index>=0) {
            nodes.addElement( original.substring(0, index) );
            original = original.substring(index+separator.length());
            index = original.indexOf(separator);
        }
        // Get the last node
        nodes.addElement( original );

        // Create splitted string array
        String[] result = new String[ nodes.size() ];
        if( nodes.size()>0 ) {
            for(int loop=0; loop<nodes.size(); loop++)
            result[loop] = (String)nodes.elementAt(loop);
        }
        return result;
    }

}
