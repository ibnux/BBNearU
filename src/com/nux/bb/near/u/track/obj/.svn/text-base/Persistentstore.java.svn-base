package com.nux.bb.near.u.track.obj;

import net.rim.device.api.crypto.SHA1Digest;
import net.rim.device.api.system.CodeSigningKey;
import net.rim.device.api.system.ControlledAccess;
import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;

public class Persistentstore {
	private CodeSigningKey codeSigningKey;

	public Persistentstore() {
		 codeSigningKey = CodeSigningKey.get("ACME");
	}

	public void setObject(String keyString, Object val) {
		 long key = getLongKey(keyString);

		 PersistentObject pObject = PersistentStore.getPersistentObject(key);

		 pObject.setContents(new ControlledAccess(val, codeSigningKey));
		 pObject.commit();
	}

	public Object getObject(String keyString) {
		 Object object;
		 long key = getLongKey(keyString);

		 PersistentObject pObject = PersistentStore.getPersistentObject(key);
		 object = pObject.getContents(codeSigningKey);

		 return object;
	}

	private long getLongKey(String key) {
		 SHA1Digest sha1Digest = new SHA1Digest();

		 sha1Digest.update(key.getBytes());

		 byte[] hashValBytes = sha1Digest.getDigest();

		 long hashValLong = 0;

		 for(int i = 0; i < 8; ++i) {
			  hashValLong |= ((long)(hashValBytes[i]) & 0x0FF) << (8*i);
		 }

		 return hashValLong;
	}
}