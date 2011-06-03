/**
 * AUTO_COPYRIGHT_SUB_TAG
 */
package com.nux.rim.push;

import net.rim.device.api.util.Persistable;

public class PushMessage implements Persistable {

    private byte[] data;

    public PushMessage(byte[] data) {
        super();
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

}
