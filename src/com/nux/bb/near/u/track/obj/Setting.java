package com.nux.bb.near.u.track.obj;

import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.util.Persistable;

public class Setting implements Persistable{
	private boolean aktif = true,setWall = true,sound = true,test=false,Stuju=false;
	private String interval = "60";
	private int zoom = 13,MapsType = 0,sizeFont = 20;
	private String lat="-6.175969",lon="106.827836",total = "0", Nick = DeviceInfo.getDeviceName()+(Integer.toHexString(DeviceInfo.getDeviceId())+"").substring(0, 4);

	public void setAktif(boolean aktif) {
		this.aktif = aktif;
	}
	public boolean isAktif() {
		return aktif;
	}
	public void setInterval(String interval) {
		this.interval = interval;
	}
	public String getInterval() {
		return interval;
	}
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}
	public int getZoom() {
		return zoom;
	}
	public void setMapsType(int mapsType) {
		MapsType = mapsType;
	}
	public int getMapsType() {
		return MapsType;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLat() {
		return lat;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getLon() {
		return lon;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getTotal() {
		return total;
	}
	public void setSetWall(boolean setWall) {
		this.setWall = setWall;
	}
	public boolean isSetWall() {
		return setWall;
	}
	/**
	 * @param nick the nick to set
	 */
	public void setNick(String nick) {
		Nick = nick;
	}
	/**
	 * @return the nick
	 */
	public String getNick() {
		return Nick;
	}
	public void setSound(boolean sound) {
		this.sound = sound;
	}
	public boolean isSound() {
		return sound;
	}
	public void setSizeFont(int sizeFont) {
		this.sizeFont = sizeFont;
	}
	public int getSizeFont() {
		return sizeFont;
	}
	public void setTest(boolean test) {
		this.test = test;
	}
	public boolean isTest() {
		return test;
	}
	/**
	 * @param stuju the stuju to set
	 */
	public void setStuju(boolean stuju) {
		Stuju = stuju;
	}
	/**
	 * @return the stuju
	 */
	public boolean isStuju() {
		return Stuju;
	}
}
