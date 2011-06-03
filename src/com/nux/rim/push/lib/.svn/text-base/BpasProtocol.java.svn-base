package com.nux.rim.push.lib;

import com.nux.rim.push.cmd.NetworkCommand.Transaction;

/**
 * Network protocol for BPAS server. Its implementation depends on handheld firmware and will use the latest API available.
 */
public interface BpasProtocol {

    /**
     * Registers with BPAS server
     */
    public void register( Transaction tx ) throws Exception;

    /**
     * Unregisters from BPAS server
     */
    public void unregister( Transaction tx ) throws Exception;

}
