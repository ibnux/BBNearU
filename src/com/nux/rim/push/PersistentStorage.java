/**
 * AUTO_COPYRIGHT_SUB_TAG
 */
package com.nux.rim.push;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.util.Persistable;

/**
 * Stores persistent data in the push sample application.
 * 
 * <p>
 * Includes the following data - current registration status, message received count, last message received timestamp, push
 * configuration, log messages and SIM card information.
 */
public class PersistentStorage implements Persistable {

    private static long ID = 0x26d0089f35a8e0ecL; // PersistentStorage
    private static PersistentStorage ps;

    // Config settings
    private PushConfig config;

    // SIM card info to detect SIM swap event
    private byte[] simCardInfo;
        
    // Specifies whether the application has been already launched
    // at least once
    private boolean onceLaunched;
    
    // Specifies whether the application has been successfully registered
    // with Content Provider and BPAS server
    private boolean registered;

    static {
        PersistentObject po = PersistentStore.getPersistentObject( ID );
        ps = (PersistentStorage) po.getContents();
        if( ps == null ) {
            ps = new PersistentStorage();
            po.setContents( ps );
            po.commit();
        }
    }

    public static PushConfig getConfig() {
        return ps.config;
    }

    public static void setConfig( PushConfig config ) {
        ps.config = config;
        PersistentObject.commit( ps );
    }
    
    public static byte[] getSimCardInfo() {
        return ps.simCardInfo;
    }

    public static void setSimCardInfo( byte[] simCardInfo ) {
        ps.simCardInfo = simCardInfo;
        PersistentObject.commit( ps );
    }
            
    public static boolean isOnceLaunched() {
        return ps.onceLaunched;
    }

    public static void setOnceLaunched( boolean onceLaunched ) {
        ps.onceLaunched = onceLaunched;
        PersistentObject.commit( ps );
    }
    
    public static boolean isRegistered() {
        return ps.registered;
    }

    public static void setRegistered( boolean registered ) {
        ps.registered = registered;
        PersistentObject.commit( ps );
    }
}
