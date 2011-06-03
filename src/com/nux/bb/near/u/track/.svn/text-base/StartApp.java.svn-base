package com.nux.bb.near.u.track;

import java.util.Vector;

import javax.microedition.location.Criteria;
import javax.microedition.location.Location;
import javax.microedition.location.LocationProvider;

import net.rim.blackberry.api.homescreen.HomeScreen;
import net.rim.device.api.system.Application;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;

import com.nux.bb.near.u.track.obj.DBStor;
import com.nux.bb.near.u.track.obj.Setting;
import com.nux.bb.near.u.track.util.InetConn;
import com.nux.bb.near.u.track.util.Layering;
import com.nux.bb.near.u.track.util.Util;
import com.nux.bb.near.u.track.util.WebDataCallback;

public class StartApp extends Application implements WebDataCallback{
	Setting set;
	String url;
	int looping = 0 ;
	public StartApp() {
		bgservice bs = new bgservice();
		bs.start();
	}

	final class bgservice extends Thread{
		private Criteria criteria;
		private LocationProvider provider;
		int loop=0;
		Vector trac;

		public bgservice() {
			criteria = new Criteria();
			criteria.setPreferredResponseTime(16);
			criteria.setCostAllowed(true);
			criteria.setHorizontalAccuracy(Criteria.NO_REQUIREMENT);
			criteria.setVerticalAccuracy(Criteria.NO_REQUIREMENT);
			criteria.setPreferredPowerConsumption(Criteria.POWER_USAGE_LOW);
		}

		public void run() {
			int n=0;
			while(true){
				try{
					set = DBStor.get_setting();
					if(set.getInterval().equals("0"))
						n = 60 * 60 * 1000;
					else
						n = Integer.parseInt(set.getInterval()) * 60 * 1000;
					sleep(n);
					if(set.isAktif()){
						if(DeviceInfo.isSimulator()){
							Application.getApplication().invokeLater(new Runnable(){
								public void run() {
									doUpdate();
								}
							});
						}else{
							setupProvider();
						}
					}
				}catch(Exception e){}
			}
		}
		private void setupProvider() {
			try {
				provider = LocationProvider.getInstance(criteria);
				if (provider != null) {
					Location location = null;
					location = provider.getLocation(100);
					if (location != null) {
						if (location.isValid()) {
							String lat = location.getQualifiedCoordinates().getLatitude()+ "";
							String lon = location.getQualifiedCoordinates().getLongitude()+"";
							Util.Log("get location on "+lat+","+lon);
							set = DBStor.get_setting();
							if(!set.getLat().equals(lat) && !set.getLon().equals(lon))
							{
								set.setLat(lat);
								set.setLon(lon);
								DBStor.set_setting(set);
								Application.getApplication().invokeLater(new Runnable(){
									public void run() {
										doUpdate();
									}
								});
							}
							loop =0;
							shutdownProvider();
						}else{
							sleep(60000);
							loop++;
							if(loop<5){
								shutdownProvider();
								setupProvider();
							}else{
								shutdownProvider();
								loop =0;
								Util.Log("Failed to get location");
							}
						}
					}
				}
			} catch (Exception e) {}
		}
		private void shutdownProvider() {
			provider.setLocationListener(null, 0, 0, 0);
			provider.reset();
			provider = null;
		}
	}

	public void doUpdate(){
		String[] mapstype = {"roadmap","satellite","hybrid","terrain"};
		set = DBStor.get_setting();
		this.url = Util.GetURL()+"nearu.php?p="+Integer.toHexString(DeviceInfo.getDeviceId())+"&t="+DeviceInfo.getDeviceName()+
		"&o="+DeviceInfo.getPlatformVersion()+"&lat="+set.getLat()+"&lon="+set.getLon()+
		"&mt="+mapstype[set.getMapsType()]+"&z="+set.getZoom()+
		"&size="+Display.getWidth()+"x"+Display.getHeight();
		InetConn.getWebData(this.url, this);
	}

	public void callback(String data) {
		if(data.startsWith("Exception")){
			if(this.looping<5){
				this.looping++;
				InetConn.getWebData(this.url, this);
			}else{
				this.looping = 0;
				Util.Log("Failed to connect to server");
			}
		}else if(data.startsWith("url:")){
			this.looping = 0;
			set = DBStor.get_setting();
			String[] temps = Util.split(data.substring(4),":::");
			this.url = temps[1];
			set.setTotal(temps[0]);
			DBStor.set_setting(set);
			if(set.isSetWall()){
				Util.Log("get maps to set for wallpaper");
				InetConn.getWebData(this.url, this);
			}else{
				Util.Log("maps not saved");
			}
		}else{
			this.looping = 0;
			byte[] dataArray = data.getBytes();
			EncodedImage bitmap = EncodedImage.createEncodedImage(dataArray, 0,
					dataArray.length);
			bitmap = Layering.setLayer(bitmap.getBitmap());
			if(Util.tulisFile(Util.GetPath()+"maps.jpg", new String(bitmap.getData()))){
				HomeScreen.setBackgroundImage(Util.GetPath()+"maps.jpg");
				Util.Log("set wallpaper");
			}
		}
	}
}
