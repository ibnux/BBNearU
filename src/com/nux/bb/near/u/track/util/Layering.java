package com.nux.bb.near.u.track.util;


import com.nux.bb.near.u.track.obj.DBStor;
import com.nux.bb.near.u.track.obj.Setting;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.system.JPEGEncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;

public class Layering {
	public static EncodedImage setLayer(Bitmap gbr){
		Setting set = DBStor.get_setting();
		Bitmap img = new Bitmap(gbr.getWidth(), gbr.getHeight());
		Bitmap bg = new Bitmap(Font.getDefault().getAdvance(set.getTotal()+"BB"), Font.getDefault().getHeight());
		Graphics g = Graphics.create(img);
		g.drawBitmap(0, 0, gbr.getWidth(), gbr.getHeight(), gbr, 0, 0);
		g.setGlobalAlpha(100);
		g.drawText("http://ibnux.net", 0, 0);
		g.setGlobalAlpha(255);
		g.drawBitmap(0, (gbr.getHeight()-bg.getHeight())/2, bg.getWidth(), bg.getHeight(), bg, 0, 0);
		if(Integer.parseInt(set.getTotal())>1000){
			g.setColor(Color.RED);
		}else if(Integer.parseInt(set.getTotal())>500){
			g.setColor(Color.BLUE);
		}else if(Integer.parseInt(set.getTotal())>100){
			g.setColor(Color.GREEN);
		}else{
			g.setColor(0xC8DE33);
		}
		g.drawText(set.getTotal()+"BB", 0, (gbr.getHeight()-Font.getDefault().getHeight())/2);
		/*if(set.isMoon()){
			Bitmap moon;
			if(set.getMoon().equals("")){
				moon = Bitmap.getBitmapResource("moon.png");
				g.drawBitmap((gbr.getWidth()-moon.getWidth()),(gbr.getHeight()-moon.getHeight())/2,
						moon.getWidth(),moon.getHeight(),moon,0,0);
			}else{
				byte[] moonS = set.getMoon().getBytes();
				moon = EncodedImage.createEncodedImage(moonS, 0, moonS.length).getBitmap();
				g.drawBitmap((gbr.getWidth()-moon.getWidth()),
						(gbr.getHeight()-moon.getHeight())/2,
						moon.getWidth(),
						moon.getHeight(),moon,0,0);
			}
		}
		if(set.isCompass()){
			Bitmap compass = Bitmap.getBitmapResource("compass.png");
			g.drawBitmap(0, (gbr.getHeight()-compass.getHeight())/2,
					compass.getWidth(), compass.getHeight(),compass,0,0);
		}*/
		JPEGEncodedImage jpg = JPEGEncodedImage.encode(img, 100);
		return EncodedImage.createEncodedImage(jpg.getData(), 0,jpg.getData().length);
	}
}
