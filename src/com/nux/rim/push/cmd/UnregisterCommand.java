/**
 * AUTO_COPYRIGHT_SUB_TAG
 */
package com.nux.rim.push.cmd;

import com.nux.rim.push.ContentProviderProtocol;
import com.nux.rim.push.PersistentStorage;
import com.nux.rim.push.lib.BpasProtocol;
import com.nux.rim.push.lib.PushLibFactory;

public class UnregisterCommand extends NetworkCommand {

    private boolean onSimSwap;

    public UnregisterCommand( String username, String password, boolean onSimSwap ) {
        super( username, password );
        this.onSimSwap = onSimSwap;
    }

    protected void execute() throws Exception {
        // update the registered state
        PersistentStorage.setRegistered( false );
        // unsubscribe from our Content Provider
        if( !onSimSwap ) {
            ContentProviderProtocol.performCommand( ContentProviderProtocol.CMD_UNSUBSCRIBE, username, password, tx );
        }
        // unregister from BPAS
        BpasProtocol bpasProtocol = PushLibFactory.getBpasProtocol();
        bpasProtocol.unregister( tx );
    }

    public String getCommandName() {
        return "unregister";
    }

    public boolean isOnSimSwap() {
        return onSimSwap;
    }
    
}
