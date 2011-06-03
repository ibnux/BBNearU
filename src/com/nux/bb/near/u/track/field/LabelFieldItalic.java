package com.nux.bb.near.u.track.field;

import com.nux.bb.near.u.track.obj.DBStor;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.LabelField;

public class LabelFieldItalic extends LabelField{
	public LabelFieldItalic() {
		super();
	}
	public LabelFieldItalic(String text){
		super(text);
	}
	public LabelFieldItalic(String text,long style){
		super(text,style);
	}
	protected void paint(Graphics graphics) {
		try{
			FontFamily _fontFamily = FontFamily.forName("BBAlpha Serif");
			Font font = _fontFamily.getFont(FontFamily.SCALABLE_FONT, DBStor.get_setting().getSizeFont()-2).derive(Font.ITALIC);
			setFont(font);
		}catch(Exception e){}
		super.paint(graphics);
	}
}
