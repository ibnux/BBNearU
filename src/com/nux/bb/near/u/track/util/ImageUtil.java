package com.nux.bb.near.u.track.util;
/**
 * @author ibnu maksum
 */
import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.EncodedImage;

public class ImageUtil {

	/**
	 * resize picture
	 * @param image
	 * @param maxWidth
	 * @param maxHeight
	 * @return
	 */
	public static EncodedImage bestFit2(EncodedImage image, int maxWidth, int maxHeight)
    {

        // getting image properties
        int w = image.getWidth();
        int h = image.getHeight();

        if(w < maxWidth && h < maxHeight) return image; //image is smaller than the desidered size...no resize!


        int numeratorW = Fixed32.toFP(w);
        int denominatorW = Fixed32.toFP(maxWidth);
        int scaleW = Fixed32.div(numeratorW, denominatorW);

        int numeratorH = Fixed32.toFP(h);
        int denominatorH = Fixed32.toFP(maxHeight);
        int scaleH = Fixed32.div(numeratorH, denominatorH);

        if(scaleH > scaleW) {
            return image.scaleImage32(scaleH, scaleH);
        } else
        {
            return image.scaleImage32(scaleW, scaleW);
        }

    }
}
