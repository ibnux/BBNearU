package com.nux.bb.near.u.track.field;

import com.nux.bb.near.u.track.util.Util;

import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.LabelField;

public class LabelLink extends LabelField{

	private String url;

	public LabelLink(String txt,String url) {
		super(txt,FOCUSABLE|USE_ALL_WIDTH);
		this.url = url;
	}

	protected void paint(Graphics g) {
		int c = g.getColor();
		g.setColor(Color.LIGHTGRAY);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(c);
		super.paint(g);
	}

	protected boolean navigationClick(int status, int time) {
		Util.BukaWeb(url);
		return true;
	}

	protected boolean keyChar(char character, int status, int time) {
		if(character==Characters.ENTER){
			Util.BukaWeb(url);
		}
		return super.keyChar(character, status, time);
	}

}
