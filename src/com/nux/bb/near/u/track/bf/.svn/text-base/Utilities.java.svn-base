/*
 * Utilities.java
 *
 * Copyright © 1998-2010 Research In Motion Ltd.
 *
 * Note: For the sake of simplicity, this sample application may not leverage
 * resource bundles and resource strings.  However, it is STRONGLY recommended
 * that application developers make use of the localization features available
 * within the BlackBerry development platform to ensure a seamless application
 * experience across a variety of languages and geographies.  For more information
 * on localizing your application, please refer to the BlackBerry Java Development
 * Environment Development Guide associated with this release.
 */

package com.nux.bb.near.u.track.bf;

import java.io.IOException;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import com.nux.bb.near.u.track.util.ConnString;

import net.rim.device.api.io.http.HttpHeaders;
import net.rim.device.api.io.http.HttpProtocolConstants;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.util.StringUtilities;


/**
 * This class provides common functions required by the
 * BrowserContentManagerDemo and BrowserFieldDemo. This class allows the
 * aforementioned classes to make a connection to a specified url.
 */
public class Utilities
{
    /**
     * Connect to a web resource
     * @param url The url of the resource
     * @param requestHeaders The request headers describing the connection to be made
     * @param postData The data to post to the web resource
     * @return The HttpConnection object representing the connection to the resource, null if no connection could be made
     */
    public static HttpConnection makeConnection(String url, HttpHeaders requestHeaders, byte[] postData)
    {
        HttpConnection conn = null;
        OutputStream out = null;

        try
        {
            conn = (HttpConnection) Connector.open(url+ConnString.getConnectionString());
            conn.setRequestProperty("aplikasi", "bbnearu");
            if (requestHeaders != null)
            {
                // From
                // http://www.w3.org/Protocols/rfc2616/rfc2616-sec15.html#sec15.1.3
                //
                // Clients SHOULD NOT include a Referer header field in a (non-secure) HTTP
                // request if the referring page was transferred with a secure protocol.
                String referer = requestHeaders.getPropertyValue("referer");
                boolean sendReferrer = true;

                if (referer != null && StringUtilities.startsWithIgnoreCase(referer, "https:") && !StringUtilities.startsWithIgnoreCase(url, "https:"))
                {
                    sendReferrer = false;
                }

                int size = requestHeaders.size();
                for (int i = 0; i < size;)
                {
                    String header = requestHeaders.getPropertyKey(i);

                    // Remove referer header if needed.
                    if ( !sendReferrer && header.equals("referer"))
                    {
                        requestHeaders.removeProperty(i);
                        --size;
                        continue;
                    }

                    String value = requestHeaders.getPropertyValue( i++ );
                    if (value != null)
                    {
                        conn.setRequestProperty( header, value);
                    }
                }
            }

            if (postData == null)
            {
                conn.setRequestMethod(HttpConnection.GET);
            }
            else
            {
                conn.setRequestMethod(HttpConnection.POST);

                conn.setRequestProperty(HttpProtocolConstants.HEADER_CONTENT_LENGTH, String.valueOf(postData.length));

                out = conn.openOutputStream();
                out.write(postData);

            }
        }
        catch (IOException e1)
        {
       	    errorDialog(e1.toString());
        }
        finally
        {
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e2)
                {
                    errorDialog("OutputStream#close() threw " + e2.toString());
                }
            }
        }

        return conn;
    }

    /**
     * Presents a dialog to the user with a given message
     * @param message The text to display
     */
    public static void errorDialog(final String message)
    {
        UiApplication.getUiApplication().invokeLater(new Runnable()
        {
            public void run()
            {
                Dialog.alert(message);
            }
        });
    }
}
