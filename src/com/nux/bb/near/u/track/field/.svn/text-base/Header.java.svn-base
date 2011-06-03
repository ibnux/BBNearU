package com.nux.bb.near.u.track.field;

import com.nux.bb.near.u.track.util.Gambar;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;

public class Header extends Field{
	Bitmap logo = Gambar.getGambar("icon.png",40,40);
	int h = logo.getHeight() + 10;
	int ht = 30;
	private String Text = " ";

	public Header() {
		super(USE_ALL_WIDTH);
		try{
			FontFamily _fontFamily = FontFamily.forName("BBAlpha Serif");
			Font font = _fontFamily.getFont(FontFamily.SCALABLE_FONT, Font.getDefault().getHeight()).derive(Font.BOLD);
			ht = font.getHeight();
			setFont(font);
		}catch(Exception e){}
	}

	protected void layout(int width, int height) {
		setExtent(width, h);
	}

	protected void paint(Graphics g) {
		//Variables for drawing the gradient
		int width = getWidth();
		int height = getHeight();
		g.fillRect(0, 0, width, height);
		int[] X_PTS_MAIN = { 0, width, width, 0};
		int[] Y_PTS_MAIN = { 0, 0, height, height };
		int[] drawColors_MAIN = { 0x87888A, 0x87888A,  Color.BLACK, Color.BLACK};
		try {
		//Draw the gradients
		    g.drawShadedFilledPath(X_PTS_MAIN, Y_PTS_MAIN, null, drawColors_MAIN, null);
		} catch (IllegalArgumentException iae) {
		    System.out.println("Bad arguments.");
		}
		g.setColor(Color.WHITE);
		g.drawBitmap((50-logo.getWidth())/2, (h - logo.getHeight())/2, logo.getWidth(), logo.getHeight(), logo, 0, 0);
		g.drawText(Text, 50, (h-ht)/2);
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		Text = text;
		invalidate();
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return Text;
	};


}
