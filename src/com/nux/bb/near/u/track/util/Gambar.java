package com.nux.bb.near.u.track.util;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;

public class Gambar {
	public static Bitmap getGambar(String res,int w, int h){
		EncodedImage encoded = EncodedImage.getEncodedImageResource(res);
		encoded = ImageUtil.bestFit2(encoded, w, h);
		return encoded.getBitmap();
	}
}
