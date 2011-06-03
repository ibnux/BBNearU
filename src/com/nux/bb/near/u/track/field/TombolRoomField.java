package com.nux.bb.near.u.track.field;

import com.nux.bb.near.u.track.obj.RoomChat;
import com.nux.bb.near.u.track.ui.RoomScreen;
import com.nux.bb.near.u.track.ui.UserRoomScreen;
import com.nux.bb.near.u.track.util.Gambar;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.ContextMenu;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;


public class TombolRoomField extends Field{
	private Bitmap gbr = null;
	private Bitmap arr = Bitmap.getBitmapResource("arrow.png");
	private int h = 10;
	private int fh = 35;
	private RoomChat room;
	private String Totalperson = "0";
	private boolean Favourite = false, Active = false;

	public TombolRoomField(RoomChat room,String total,long style) {
		super(style);
		this.setRoom(room);
		this.Totalperson = total;
		this.fh = Font.getDefault().getHeight();
		this.h = h + fh + fh - 10;
	}
	public TombolRoomField(String text,String _gbr,long style) {
		super(style);
		this.gbr = Gambar.getGambar(_gbr, 30, this.h-10);
		//this.text = text;
		this.fh = Font.getDefault().getHeight();
		this.h = h + fh - 10;
	}
	protected void layout(int width, int height) {
		setExtent(width, h);
	}

	protected void paint(Graphics g) {
		if(isFocus()){
			g.setGlobalAlpha(150);
			g.setColor(Color.GREEN);
			g.fillRect(0, 0, getWidth(), h);
			g.setGlobalAlpha(255);
		}
		g.setColor(0);
		if(gbr != null){
			g.drawBitmap((40-gbr.getWidth())/2, (getHeight()-gbr.getHeight())/2, gbr.getWidth(), gbr.getHeight(), gbr, 0, 0);
			if(!Totalperson.equals("0")){
				g.drawText(room.getNamaRoom(), 44, 10/2,DrawStyle.ELLIPSIS,getWidth()-44-arr.getWidth()-12);
				try{
					FontFamily _fontFamily = FontFamily.forName("BBAlpha Serif");
					Font font = _fontFamily.getFont(FontFamily.SCALABLE_FONT, this.getFont().getHeight()-5).derive(Font.ITALIC);
					g.setFont(font);
				}catch(Exception e){}
				g.drawText(Totalperson+" BBers", 54, (10/2)+fh,DrawStyle.ELLIPSIS,getWidth()-44-arr.getWidth()-12);
			}else{
				g.drawText(room.getNamaRoom(), 44, (h-fh)/2,DrawStyle.ELLIPSIS,getWidth()-44-arr.getWidth()-12);
			}
		}else{
			if(!Totalperson.equals("0")){
				g.drawText(room.getNamaRoom(), 4, 10/2,DrawStyle.ELLIPSIS,getWidth()-4-arr.getWidth()-12);
				try{
					FontFamily _fontFamily = FontFamily.forName("BBAlpha Serif");
					Font font = _fontFamily.getFont(FontFamily.SCALABLE_FONT, this.getFont().getHeight()-5).derive(Font.ITALIC);
					g.setFont(font);
				}catch(Exception e){}
				g.drawText(Totalperson+" BBers", 14, (10/2)+fh,DrawStyle.ELLIPSIS,getWidth()-44-arr.getWidth()-12);
			}else{
				g.drawText(room.getNamaRoom(), 4, (h-fh)/2,DrawStyle.ELLIPSIS,getWidth()-4-arr.getWidth()-12);
			}
		}
		g.drawBitmap((getWidth()-arr.getWidth())-10, (h-arr.getHeight())/2, arr.getWidth(), arr.getHeight(), arr, 0, 0);
	}

	public String getLabel(){
		return getRoom().getNamaRoom();
	}
	public boolean isFocusable() {
		return true;
	}
	protected void drawFocus(Graphics graphics, boolean on) {}

	protected void onFocus(int direction) {
		invalidate();
		super.onFocus(direction);
	}
	protected void onUnfocus() {
		invalidate();
		super.onUnfocus();
	}

	protected boolean navigationClick(int status, int time) {
		fieldChangeNotify(1);
		return true;
	}
	protected boolean keyChar(char c, int status, int time) {
		if(c==Characters.ENTER){
			fieldChangeNotify(1);
			return true;
		}
		return super.keyChar(c, status, time);
	}
	/**
	 * @param room the room to set
	 */
	public void setRoom(RoomChat room) {
		this.room = room;
	}
	/**
	 * @return the room
	 */
	public RoomChat getRoom() {
		return room;
	}
	public void setGbr(String gbr) {
		if(gbr==null){
			this.gbr = null;
		}else{
			this.gbr = Gambar.getGambar(gbr, 30, this.h-10);
		}
		invalidate();
	}
	public boolean isGbr() {
		if(gbr==null)
			return false;
		else
			return true;
	}

	public void setTotalperson(String totalperson) {
		this.Totalperson = totalperson;
		invalidate();
	}
	public String getTotalperson() {
		return Totalperson;
	}
	/**
	 * @param favourite the favourite to set
	 */
	public void setFavourite(boolean favourite) {
		Favourite = favourite;
	}
	/**
	 * @return the favourite
	 */
	public boolean isFavourite() {
		return Favourite;
	}
	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		Active = active;
	}
	/**
	 * @return the active
	 */
	public boolean isActive() {
		return Active;
	}

	protected void makeContextMenu(ContextMenu contextMenu) {
		super.makeContextMenu(contextMenu);
		if(isFavourite()){
			contextMenu.addItem(removeFav);
		}else if(isActive()){
			contextMenu.addItem(leave);
			contextMenu.addItem(addtoFav);
		}else{
			contextMenu.addItem(addtoFav);
		}
		contextMenu.addItem(ShowAllUser);
	}

	public TombolRoomField getField(){
		return this;
	}

	MenuItem addtoFav = new MenuItem("Add To Favourite",0x0002000,0) {
		public void run() {
			RoomScreen rs = (RoomScreen)getScreen();
			rs.addToFav(getField());
		}
	};
	MenuItem removeFav = new MenuItem("Remove from Favourite",0x0002000,0) {
		public void run() {
			RoomScreen rs = (RoomScreen)getScreen();
			rs.removeFromFav(getRoom(),getField());
		}
	};
	MenuItem leave = new MenuItem("Leave room",0x0002000,0) {
		public void run() {
			RoomScreen rs = (RoomScreen)getScreen();
			rs.leaveGroup(getRoom().getIdRoom());
		}
	};
	MenuItem ShowAllUser = new MenuItem("Show Users",0x0002000,1) {
		public void run() {
			UiApplication.getUiApplication().pushScreen(new UserRoomScreen(getRoom().getNamaRoom(),getRoom().getIdRoom()));
		}
	};

}
