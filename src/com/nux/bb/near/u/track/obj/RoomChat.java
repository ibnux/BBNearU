package com.nux.bb.near.u.track.obj;

import net.rim.device.api.util.Persistable;

public class RoomChat implements Persistable {
	private String NamaRoom = "", IdRoom = "0";

	/**
	 *
	 * @param namaRoom Room name
	 * @param idRoom
	 */
	public RoomChat(String namaRoom,String idRoom) {
		setIdRoom(idRoom);
		setNamaRoom(namaRoom);
	}

	public RoomChat() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param idRoom the idRoom to set
	 */
	public void setIdRoom(String idRoom) {
		IdRoom = idRoom;
	}

	/**
	 * @return the idRoom
	 */
	public String getIdRoom() {
		return IdRoom;
	}

	/**
	 * @param namaRoom the namaRoom to set
	 */
	public void setNamaRoom(String namaRoom) {
		NamaRoom = namaRoom;
	}

	/**
	 * @return the namaRoom
	 */
	public String getNamaRoom() {
		return NamaRoom;
	}
}
