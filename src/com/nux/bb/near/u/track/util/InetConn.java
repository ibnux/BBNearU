package com.nux.bb.near.u.track.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import net.rim.device.api.system.Application;

public class InetConn {
	/**
	 * send Get HTTP, masuk antrian
	 * @param url
	 * @param callback
	 */
	public static void getWebData(final String url, final WebDataCallback callback)
	{
		try{
			sendGetData(url,callback,false);
		}catch(Exception e){
			callback.callback(e.getMessage());
		}
	}
	/**
	 * tanpa Masuk antrian dulu
	 * @param url
	 * @param callback
	 * @param utf
	 */
	public static void getWebData(final String url, final WebDataCallback callback, boolean utf)
	{
		try{
			sendGetData(url,callback,utf);
		}catch(Exception e){
			callback.callback(e.getMessage());
		}
	}
	public static void sendGetData(final String url, final WebDataCallback callback,final boolean utf) throws IOException
	{
		Thread t = new Thread(new Runnable()
		{
			public void run()
			{
				HttpConnection connection = null;
				InputStream inputStream = null;

				try
				{
					
					connection = (HttpConnection) Connector.open(url+ConnString.getConnectionString(), Connector.READ, true);
					connection.setRequestProperty("aplikasi", "bbnearu");
					inputStream = connection.openInputStream();
					byte[] responseData = new byte[20000];
					int length = 0;
					StringBuffer rawResponse = new StringBuffer();
					while (-1 != (length = inputStream.read(responseData)))
					{
						if(utf){
							rawResponse.append(new String(responseData, 0, length,"UTF-8"));
						}else{
							rawResponse.append(new String(responseData, 0, length));
						}
					}
					int responseCode = connection.getResponseCode();
					if (responseCode != HttpConnection.HTTP_OK)
					{
						throw new IOException("HTTP response code: "
								+ responseCode);
					}

					final String result = rawResponse.toString();
					Application.getApplication().invokeLater(new Runnable()
					{
						public void run()
						{
							callback.callback(result);
						}
					});
				}
				catch (final Exception ex)
				{
					Application.getApplication().invokeLater(new Runnable()
					{
						public void run()
						{
							callback.callback("Exception (" + ex.getClass() + "): " + ex.getMessage());
						}
					});
				}
				finally
				{
					try
					{
						inputStream.close();
						inputStream = null;
						connection.close();
						connection = null;
					}
					catch(Exception e){}
				}
			}
		});
		t.setPriority(Thread.MAX_PRIORITY);
		t.start();
	}

	/**
	 * masuk antrian
	 * @param url
	 * @param params
	 * @param callback
	 */
	public static void PostWebData(String url, String params, WebDataCallback callback)
	{
		try{
			sendPostData(url,params,callback,false);
		}catch(Exception e){
			callback.callback(e.getMessage());
		}
	}
	/**
	 * tanpa masuk antrian
	 * @param url
	 * @param params
	 * @param callback
	 * @param utf
	 */
	public static void PostWebData(String url,String params, WebDataCallback callback, boolean utf)
	{
		try{
			sendPostData(url,params,callback,utf);
		}catch(Exception e){
			callback.callback(e.getMessage());
		}
	}
	public static void sendPostData(final String url,final String params, final WebDataCallback callback,final boolean utf) throws IOException
	{
		Thread t = new Thread(new Runnable()
		{
			public void run()
			{
				HttpConnection httpConn = null;
			    InputStream is = null;
			    OutputStream os = null;
			    try {
			    	httpConn = (HttpConnection)Connector.open(url+ConnString.getConnectionString());
			    	httpConn.setRequestMethod(HttpConnection.POST);
			    	httpConn.setRequestProperty("aplikasi", "bbnearu");
			    	httpConn.setRequestProperty("Accept_Language","en-US");
			    	httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			    	os = httpConn.openOutputStream();
			    	os.write(params.getBytes());
			    	StringBuffer sb = new StringBuffer();
			        is = httpConn.openDataInputStream();
			        int chr;
			        while ((chr = is.read()) != -1)
			          sb.append((char) chr);

			        final String result = sb.toString();
					Application.getApplication().invokeLater(new Runnable()
					{
						public void run()
						{
							callback.callback(result);
						}
					});
			    }
				catch (final Exception ex)
				{
					Application.getApplication().invokeLater(new Runnable()
					{
						public void run()
						{
							callback.callback("Exception (" + ex.getClass() + "): " + ex.getMessage());
						}
					});
				}
				finally
				{
					try
					{
						is.close();
						is = null;
						httpConn.close();
						httpConn = null;
					}
					catch(Exception e){}
				}
			}
		});
		t.setPriority(Thread.MAX_PRIORITY);
		t.start();
	}
}
