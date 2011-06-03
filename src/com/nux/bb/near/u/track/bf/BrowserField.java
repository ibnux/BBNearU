package com.nux.bb.near.u.track.bf;

import java.io.IOException;

import javax.microedition.io.HttpConnection;

import net.rim.device.api.browser.field.BrowserContent;
import net.rim.device.api.browser.field.BrowserContentManager;
import net.rim.device.api.browser.field.Event;
import net.rim.device.api.browser.field.RedirectEvent;
import net.rim.device.api.browser.field.RenderingApplication;
import net.rim.device.api.browser.field.RenderingOptions;
import net.rim.device.api.browser.field.RequestedResource;
import net.rim.device.api.browser.field.UrlRequestedEvent;
import net.rim.device.api.io.http.HttpHeaders;
import net.rim.device.api.system.Application;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.component.Status;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class BrowserField extends VerticalFieldManager implements RenderingApplication{
	private String URL = "";
	private String REFERER = "referer";

	private int width = Display.getWidth();
	private int height = Display.getHeight();

    private BrowserContentManager _browserContentManager;
    private HttpConnection _currentConnection;
    private String html = "";

	/**
	 * Standar constructor
	 */
	public BrowserField() {
		super(VERTICAL_SCROLL);
		browserfield();
	}
	/**
	 * Add browser Field with url
	 * @param url
	 */
	public BrowserField(String url) {
		this.URL = url;
		browserfield();
	}
	/**
	 * Add browser field with url and style
	 * @param url
	 * @param style
	 */
	public BrowserField(String url,long style) {
		super(style);
		this.URL = url;
		browserfield();
	}

	private void browserfield(){
		_browserContentManager = new BrowserContentManager(0);
		RenderingOptions renderingOptions = _browserContentManager.getRenderingSession().getRenderingOptions();
		renderingOptions.setProperty(RenderingOptions.CORE_OPTIONS_GUID, RenderingOptions.SHOW_IMAGES_IN_HTML, true);
		renderingOptions.setProperty(RenderingOptions.CORE_OPTIONS_GUID, RenderingOptions.ENABLE_CSS, true);
		renderingOptions.setProperty(RenderingOptions.CORE_OPTIONS_GUID, RenderingOptions.SHOW_TABLES_IN_HTML, true);
		renderingOptions.setProperty(RenderingOptions.CORE_OPTIONS_GUID, RenderingOptions.ENABLE_HTML, true);
		add(_browserContentManager);
	}

	/**
	 * Set url to load
	 * @param url
	 */
	public void setURL(String url){
		this.URL = url;
	}

	/**
	 * Set Referer
	 * @param refer
	 */
	public void setReferer(String refer){
		this.REFERER = refer;
	}

	/**
	 * set width of browser
	 * @param width
	 */
	public void setWidth(int width){
		this.width = width;
	}

	/**
	 * set Height of browser
	 * @param height
	 */
	public void setHeight(int height){
		this.height = height;
	}

	public void setHtml(String html){
		this.html = html;
	}

	/**
	 * open web, please set url before use this
	 */
	public void getURL(){
		openweb();
	}
	/**
	 * open web from given url
	 * @param url
	 */
	public void getURL(String url){
		this.URL = url;
		openweb();
	}

	private void openweb(){
		PrimaryResourceFetchThread thread;
		if(this.URL.equals("")){
			thread = new PrimaryResourceFetchThread(html, null, this);
		}else{
			thread = new PrimaryResourceFetchThread(this.URL, null, null, null, this);
		}
        thread.start();
	}

	public Object eventOccurred(Event event)
    {
        int eventId = event.getUID();

        switch( eventId )
        {
            case Event.EVENT_URL_REQUESTED:
            {
                UrlRequestedEvent urlRequestedEvent = (UrlRequestedEvent) event;

                PrimaryResourceFetchThread thread = new PrimaryResourceFetchThread(urlRequestedEvent.getURL(), urlRequestedEvent
                        .getHeaders(), urlRequestedEvent.getPostData(), event, this);
                thread.start();

                break;
            }
            case Event.EVENT_BROWSER_CONTENT_CHANGED:
            {
                break;
            }
            case Event.EVENT_REDIRECT:
            {
                RedirectEvent e = (RedirectEvent) event;
                String referrer = e.getSourceURL();

                switch( e.getType() )
                {
                    case RedirectEvent.TYPE_SINGLE_FRAME_REDIRECT:
                        // Show redirect message
                        Application.getApplication().invokeAndWait(new Runnable()
                        {
                            public void run()
                            {
                                Status.show("You are being redirected to a different page...");
                            }
                        });

                        break;

                    case RedirectEvent.TYPE_JAVASCRIPT:
                        break;

                    case RedirectEvent.TYPE_META:
                        // MSIE and Mozilla don't send a Referer for META
                        // Refresh
                        referrer = null;
                        break;

                    case RedirectEvent.TYPE_300_REDIRECT:
                        // MSIE, Mozilla, and Opera all send the original
                        // request's Referer as the Referer for the new
                        // request.
                        Object eventSource = e.getSource();
                        if( eventSource instanceof HttpConnection )
                        {
                            referrer = ((HttpConnection) eventSource).getRequestProperty(REFERER);
                        }
                        break;
                }

                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setProperty(REFERER, referrer);
                PrimaryResourceFetchThread thread = new PrimaryResourceFetchThread(e.getLocation(), requestHeaders, null, event, this);
                thread.start();
                break;

            }
            case Event.EVENT_CLOSE:
                System.exit(0);
                break;

            case Event.EVENT_SET_HEADER: // No cache support.
            case Event.EVENT_SET_HTTP_COOKIE: // No cookie support.
            case Event.EVENT_HISTORY: // No history support.
            case Event.EVENT_EXECUTING_SCRIPT: // No progress bar is supported.
            case Event.EVENT_FULL_WINDOW: // No full window support.
            case Event.EVENT_STOP: // No stop loading support.
            default:
        }

        return null;
    }
	public int getAvailableHeight(BrowserContent browserContent) {
		// TODO Auto-generated method stub
		return this.height;
	}
	public int getAvailableWidth(BrowserContent browserContent){
		return this.width;
	}
	public String getHTTPCookie(String url) {
		// TODO Auto-generated method stub
		return null;
	}
	public int getHistoryPosition(BrowserContent browserContent) {
		// TODO Auto-generated method stub
		return 0;
	}


	public HttpConnection getResource(RequestedResource resource, BrowserContent referrer)
    {
        if( resource == null )
        {
            return null;
        }

        // Check if this is cache-only request
        if( resource.isCacheOnly() )
        {
            // No cache support
            return null;
        }

        String url = resource.getUrl();

        if( url == null )
        {
            return null;
        }

        // If referrer is null we must return the connection.
        if( referrer == null )
        {
            HttpConnection connection = Utilities.makeConnection(resource.getUrl(), resource.getRequestHeaders(), null);
            return connection;
        }
        else
        {
            // If referrer is provided we can set up the connection on a
            // separate thread.
            SecondaryResourceFetchThread.enqueue(resource, referrer);
        }

        return null;
    }

    /**
     * @see net.rim.device.api.browser.RenderingApplication#invokeRunnable(java.lang.Runnable)
     */
    public void invokeRunnable(Runnable runnable)
    {
        (new Thread(runnable)).start();
    }

	void processConnection(HttpConnection connection, Event e)
	{
	    // Cancel previous request
	    if( _currentConnection != null )
	    {
	        try
	        {
	            _currentConnection.close();
	        }
	        catch( IOException e1 )
	        {
	        }
	    }

	    // Replace the old connection with the new one
	    _currentConnection = connection;
	    try
	    {
	        _browserContentManager.setContent(connection, this, e);
	    }
	    finally
	    {
	        SecondaryResourceFetchThread.doneAddingImages();
	    }
	}
}



/**
 * A Thread class to fetch content using an http connection
 */
final class PrimaryResourceFetchThread extends Thread
{
    private BrowserField _application;
    private Event _event;
    private byte[] _postData;
    private HttpHeaders _requestHeaders;
    private String _url;
    private String html;

    /**
     * Constructor to create a PrimaryResourceFetchThread which fetches the
     * resource from the specified url.
     *
     * @param url The url to fetch the content from
     * @param requestHeaders The http request headers used to fetch the content
     * @param postData Data which is to be posted to the url
     * @param event The event triggering the connection
     * @param application The application requesting the connection
     */
    PrimaryResourceFetchThread(String url, HttpHeaders requestHeaders, byte[] postData, Event event, BrowserField application)
    {
        _url = url;
        _requestHeaders = requestHeaders;
        _postData = postData;
        _application = application;
        _event = event;
    }

    PrimaryResourceFetchThread(String html, Event event, BrowserField application)
    {
        this.html = html;
		_application = application;
        _event = event;
    }

    /**
     * Connects to the url associated with this object
     *
     * @see java.lang.Thread#run()
     */
    public void run()
    {
    	HttpConnection connection;
    	if(_url==null){
    		connection =  new LocalHttpConn(html);
    	}else{
    		connection = Utilities.makeConnection(_url, _requestHeaders, _postData);
    	}
        _application.processConnection(connection, _event);
    }
}
