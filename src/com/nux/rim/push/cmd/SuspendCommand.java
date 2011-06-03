/**
 * AUTO_COPYRIGHT_SUB_TAG
 */
package com.nux.rim.push.cmd;

import com.nux.rim.push.ContentProviderProtocol;

public class SuspendCommand extends NetworkCommand {

    public SuspendCommand( String username, String password ) {
        super( username, password );
    }

    protected void execute() throws Exception {
        // first we register with Content Provider
        ContentProviderProtocol.performCommand( ContentProviderProtocol.CMD_SUSPEND, username, password, tx );
    }

    public String getCommandName() {
        return "suspend";
    }

}