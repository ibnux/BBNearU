package com.nux.bb.near.u.track.obj;

import net.rim.device.api.system.Display;

public class Maps {
	private String UrlMaps = "http://maps.google.com/maps/api/staticmap?";
	private String Lat="",Lon="",mapType="";
	private int w = Display.getWidth(),h = Display.getHeight(),Zoom = 15;
	public Maps() {}

	/**
	 * set coordinat
	 * @param Lat
	 * @param Lon
	 */
	public void setCoord(String Lat,String Lon){
		this.Lat = Lat;
		this.Lon = Lon;
	}
	
	public void setZoom(int i){
		this.Zoom = i;
	}
	
	public void setMapType(int n){
		String[] mapstype = {"roadmap","satellite","hybrid","terrain"};
		this.mapType = mapstype[n];
	}
	
	public String getUrlMaps() {
		String maps = UrlMaps+
		"center="+Lat+","+Lon+
		"&zoom="+Zoom+
		"&size="+getWidth()+"x"+getHeight()+
		"&maptype="+mapType+
		"&markers=icon:http://bit.ly/fqpIO7|color:white|label:X|"+Lat+","+Lon+
		"&sensor=false";
		return maps;
	}

	public void setWidth(int w) {
		this.w = w;
	}

	public int getWidth() {
		return w;
	}

	public void setHeight(int h) {
		this.h = h;
	}

	public int getHeight() {
		return h;
	}
}
