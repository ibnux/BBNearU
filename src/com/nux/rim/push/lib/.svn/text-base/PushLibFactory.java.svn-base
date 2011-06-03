package com.nux.rim.push.lib;

import net.rim.device.api.ui.UiApplication;


public class PushLibFactory {

    private static Object lib;

    static {
        lib = new PushLib43();
    }

    public static UiApplication getUiApplication() {
        return (UiApplication) lib;
    }
    public static BpasProtocol getBpasProtocol() {
        return (BpasProtocol) lib;
    }

    public static PushMessageListener getPushMessageListener() {
        return (PushMessageListener) lib;
    }
}
