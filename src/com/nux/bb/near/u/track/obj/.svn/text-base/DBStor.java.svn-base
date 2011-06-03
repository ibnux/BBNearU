package com.nux.bb.near.u.track.obj;

import java.util.Vector;


public class DBStor {
	private static String _setting ="com.nux.wp.setting";
	private static String _roomFav ="com.nux.wp.setting.room.favourite";

	public static void set_setting(Setting setting) {
		Persistentstore ps = new Persistentstore();
		ps.setObject(_setting, setting);
	}

	public static Setting get_setting() {
		Persistentstore ps = new Persistentstore();
		Setting setting = (Setting)ps.getObject(_setting);
		if(setting==null){
			setting = new Setting();
		}
		return setting;
	}

	/**
	 * @param _roomFav the _roomFav to set
	 */
	public static void set_roomFav(Vector roomFav) {
		Persistentstore ps = new Persistentstore();
		ps.setObject(_roomFav, roomFav);
	}

	/**
	 *
	 * @return Vector roomFav
	 */
	public static Vector get_roomFav() {
		Persistentstore ps = new Persistentstore();
		Vector roomFav = (Vector)ps.getObject(_roomFav);
		if(roomFav==null){
			roomFav = new Vector();
		}
		return roomFav;
	}
}
