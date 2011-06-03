package com.nux.bb.near.u.track.field;

/*
 * from http://www.coderholic.com/blackberry-webbitmapfield/
 */

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;

import com.nux.bb.near.u.track.ui.BaseScreen;
import com.nux.bb.near.u.track.util.InetConn;
import com.nux.bb.near.u.track.util.Layering;
import com.nux.bb.near.u.track.util.WebDataCallback;

public class WebBitmapField extends BitmapField implements WebDataCallback
{
	private EncodedImage bitmap = null;
	Bitmap kosong = Bitmap.getBitmapResource("icon.png");
	private String url = "";
	private BaseScreen bs;

	public WebBitmapField(long style) {
		super(null,style);
	}

	public WebBitmapField(String url)
	{
		try
		{
			setBitmap(kosong);
			getURL(url);
		}
		catch (Exception e) {}
	}
	public WebBitmapField(String url,long style)
	{
		super(null,style);
		try
		{
			getURL(url);
			setBitmap(kosong);
		}
		catch (Exception e) {}
	}

	public void reload(String url){
		try
		{
			getURL(url);
		}
		catch (Exception e) {}
	}

	private void getURL(String url){
		try
		{
			if(!url.trim().equals("")){
				setUrl(url);
				InetConn.getWebData(url, this);
				this.bs = (BaseScreen) getScreen();
				bs.setShowLoader(true);
			}
		}
		catch (Exception e) {}
	}

	public Bitmap getBitmap()
	{
		if (bitmap == null) return null;
		return bitmap.getBitmap();
	}

	public void callback(final String data)
	{
		try{
			bs.setShowLoader(false);
		}catch(Exception e){}
		if (data.startsWith("Exception"))
			if(Dialog.ask(Dialog.D_YES_NO,"Failed to connect to Gmaps server, Reconnect?")==Dialog.YES){
				InetConn.getWebData(url, this);
			}else{
				//Dialog.alert(data);
			}

		try
		{
			//Dialog.alert(data.substring(20));
			byte[] dataArray = data.getBytes();
			bitmap = EncodedImage.createEncodedImage(dataArray, 0,
					dataArray.length);
			bitmap = Layering.setLayer(bitmap.getBitmap());
			//setBitmap();
			setImage(bitmap);
		}
		catch (final Exception e){}
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrl() {
		return url;
	}

}