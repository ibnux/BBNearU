/**
 * AUTO_COPYRIGHT_SUB_TAG
 */
package com.nux.rim.push.cmd;

import com.nux.rim.push.ContentProviderProtocol;
import com.nux.rim.push.PersistentStorage;
import com.nux.rim.push.lib.BpasProtocol;
import com.nux.rim.push.lib.PushLibFactory;

public class RegisterCommand extends NetworkCommand {

    public RegisterCommand( String username, String password ) {
        super( username, password );
    }

    protected void execute() throws Exception {
        // first we register with Content Provider
        ContentProviderProtocol.performCommand( ContentProviderProtocol.CMD_SUBSCRIBE, username, password, tx );
        // if the registration is successful we register with BPAS
        BpasProtocol bpasProtocol = PushLibFactory.getBpasProtocol();
        bpasProtocol.register( tx );
        // update the registered state
        PersistentStorage.setRegistered( true );
    }

    public String getCommandName() {
        return "register";
    }

}
