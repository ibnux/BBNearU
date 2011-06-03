/**
 * AUTO_COPYRIGHT_SUB_TAG
 */
package com.nux.rim.push;

import com.nux.bb.near.u.track.util.Util;


/**
 * Simplified logger to ease the debugging process.
 */
public class Logger {

    public static void log( String str ) {
    		Util.Log(str);
    }

    public static void warn( String str ) {
    		Util.Log(str);
    }
}
