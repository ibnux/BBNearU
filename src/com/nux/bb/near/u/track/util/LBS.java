package com.nux.bb.near.u.track.util;

import javax.microedition.location.Criteria;
import javax.microedition.location.Location;
import javax.microedition.location.LocationProvider;

import net.rim.device.api.system.Application;

public class LBS {
	public static void getLocation(final LbsCallback callback){
		Thread tr = new Thread(new Runnable(){
			private Criteria criteria;
			private LocationProvider provider;
			int loop=0;
			
			public void run() {
				criteria = new Criteria();
				criteria.setPreferredResponseTime(16);
				criteria.setCostAllowed(true);
				criteria.setHorizontalAccuracy(Criteria.NO_REQUIREMENT);
				criteria.setVerticalAccuracy(Criteria.NO_REQUIREMENT);
				criteria.setPreferredPowerConsumption(Criteria.POWER_USAGE_LOW);
				Util.Log("Get Location");
				setupProvider();
			}
			private void setupProvider() {
				try {
					provider = LocationProvider.getInstance(criteria);
					if (provider != null) {
						Location location = null;
						location = provider.getLocation(100);
						if (location != null) {	
							if (location.isValid()) {
								final String lat = location.getQualifiedCoordinates().getLatitude()+ "";
								final String lon = location.getQualifiedCoordinates().getLongitude()+"";
								Application.getApplication().invokeLater(new Runnable(){
									public void run() {
										callback.callbackLBS(lat,lon);
									}
								});
								shutdownProvider();
							}else{
								Util.Log("Failed try again");
								Thread.sleep(60000);
								loop++;
								if(loop<5){
									shutdownProvider(); 
									setupProvider();
								}else{
									Application.getApplication().invokeLater(new Runnable(){
										public void run() {
											callback.callbackLBSError("Can't get your Location.");
										}
									});
									loop =0;
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
		});
		tr.start();
	}
}
