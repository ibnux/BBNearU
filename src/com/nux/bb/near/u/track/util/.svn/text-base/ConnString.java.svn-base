package com.nux.bb.near.u.track.util;

import net.rim.device.api.servicebook.ServiceBook;
import net.rim.device.api.servicebook.ServiceRecord;
import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.WLANInfo;

public final class ConnString {
    
    public static String getConnectionString()
    {
        String st = "";
        try{ 
	    //The Device is a simultaor --> TCP
	        if (DeviceInfo.isSimulator()){
	            return "";
        	}else if (WLANInfo.getWLANState() == WLANInfo.WLAN_STATE_CONNECTED) {
	            st =  ";interface=wifi";
	        } else if ((CoverageInfo.getCoverageStatus() & CoverageInfo.COVERAGE_BIS_B) == CoverageInfo.COVERAGE_BIS_B)
	        {
	        
	            // blackberry internet service
	            ServiceRecord rec = getBIBSRecord();
	            if (rec == null){//couldn't find the right record
	                        st = ";deviceside=true";// let the phone try to do the work
	                }else{//found the record, get the id
                        /*st = ";deviceside=false;connectionUID=" + rec.getUid()
                        + ";ConnectionType=mds-public";*/
                        st = ";deviceside=false;ConnectionType=mds-public";
	                }
	        }
	        else if ((CoverageInfo.getCoverageStatus() & CoverageInfo.COVERAGE_MDS) == CoverageInfo.COVERAGE_MDS){
	            st = ";deviceside=false";// use the clients blackberry enterprise server
	        }else{
	            st = ";deviceside=true";// let the phone do the work if it can
	        }
        }catch(Exception e){
            System.out.println(e.toString());
        }
	            return st + ";ConnectionTimeout=120000";
    }
    public static ServiceRecord getBIBSRecord()
    {
    	try{
	        ServiceRecord[] ippps = ServiceBook.getSB().getRecords();
	        for (int i = 0; i < ippps.length; i++)
	        {
	            if (ippps[i].getCid().equals("IPPP"))
	            {
	                if (ippps[i].getName().indexOf("BIBS") >= 0){
	                    return ippps[i];
	                }else if (ippps[i].getName().indexOf("5 Click") >= 0){
	                    return ippps[i];
	                }
	            }
	        }
    	}catch(Exception e){
            System.out.println(e.toString());
        }
        return null;
    }

}
