package com.nux.bb.near.u.track.field;

import com.nux.bb.near.u.track.util.Gambar;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;


public class TombolField extends Field{
	String text;
	Bitmap gbr = null;
	Bitmap arr = Bitmap.getBitmapResource("arrow.png");
	int h = 10;
	int fh = 35;

	public TombolField(String text,long style) {
		super(style);
		this.text = text;
		fh = Font.getDefault().getHeight();
		this.h = h + Math.max(30, fh);
	}
	public TombolField(String text,String _gbr,long style) {
		super(style);
		gbr = Gambar.getGambar(_gbr, 30, 23);
		this.text = text;
		fh = Font.getDefault().getHeight();
		this.h = h + Math.max(30, fh);
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
			g.drawText(text, 44, (h-fh)/2,DrawStyle.ELLIPSIS,getWidth()-44-arr.getWidth()-12);
		}else{
			g.drawText(text, 4, (h-fh)/2,DrawStyle.ELLIPSIS,getWidth()-4-arr.getWidth()-12);
		}
		g.drawBitmap((getWidth()-arr.getWidth())-10, (h-arr.getHeight())/2, arr.getWidth(), arr.getHeight(), arr, 0, 0);
	}

	public String getLabel(){
		return text;
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
}
