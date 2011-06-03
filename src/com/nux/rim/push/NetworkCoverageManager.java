//#preprocess
/**
 * AUTO_COPYRIGHT_SUB_TAG
 */
package com.nux.rim.push;

import com.nux.rim.push.cmd.NetworkCommand.Transaction;

import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.system.CoverageStatusListener;

/**
 * Listens for network coverage and provides blocking mechanism for network commands
 * 
 */
public class NetworkCoverageManager implements CoverageStatusListener {

    private static NetworkCoverageManager instance = new NetworkCoverageManager();
    
    private Transaction current;

    private NetworkCoverageManager() {
        CoverageInfo.addListener( this );
    }

    public static NetworkCoverageManager getInstance() {
        return instance;
    }

    public void coverageStatusChanged( int newCoverage ) {
        // called when device coverage changes
        // check whether we are in coverage and if so then start processing commands
        if( hasCoverage() ) {
            Transaction currentLocal = current;
            if( currentLocal != null ) {
                currentLocal.resumeAndNotify();
            }
        }
    }

    /**
     * Blocks the current thread until there is network coverage or the given transaction is canceled
     * @param tx current transaction
     */
    public void waitForNetworkCoverage( Transaction tx ) {
        if( current != null && current != tx ) {
            current.cancelAndNotify();
        }
        current = tx;
        while( !current.isCancelled() && !hasCoverage() ) {
            current.waitForNotification();
        }
        current = null;
    }

    /**
     * Check whether device is in coverage for BPAS & CP registration
     */
    public static boolean hasCoverage() {
        boolean bisCoverage = CoverageInfo.isCoverageSufficient( CoverageInfo.COVERAGE_BIS_B );
        boolean carrierCoverage = false;
//#ifdef HANDHELD_VERSION_42
        carrierCoverage = CoverageInfo.isCoverageSufficient( CoverageInfo.COVERAGE_CARRIER );
//#else
        carrierCoverage = CoverageInfo.isCoverageSufficient( CoverageInfo.COVERAGE_DIRECT );
//#endif
        return bisCoverage || carrierCoverage;
    }
}
