package com.nux.bb.near.u.track.field;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.LabelField;

public class LabelFieldBold extends LabelField{
	int Color = 0;
	private int hf = this.getFont().getHeight();
	public LabelFieldBold() {
		super();
		setfont();
	}
	/**
	 *
	 * @param text
	 * @param style
	 */
	public LabelFieldBold(String text,long style){
		super(text,style);
		setfont();
	}
	public LabelFieldBold(String text, int color,long style){
		super(text,style);
		this.Color = color;
		setfont();
	}
	public LabelFieldBold(String text){
		super(text);
		setfont();
	}
	public LabelFieldBold(String text, int color){
		super(text);
		this.Color = color;
		setfont();
	}
	void setfont(){
		try{
			FontFamily _fontFamily = FontFamily.forName("BBAlpha Serif");
			Font font = _fontFamily.getFont(FontFamily.SCALABLE_FONT, hf).derive(Font.BOLD);
			setFont(font);
		}catch(Exception e){}
	}
	protected void paint(Graphics graphics) {
		graphics.setColor(Color);
		super.paint(graphics);
	}
	public void setHf(int hf) {
		this.hf = hf;
		setfont();
		invalidate();
	}
	public int getHf() {
		return hf;
	}
}
