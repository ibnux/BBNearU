package com.nux.bb.near.u.track.ui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Vector;

import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.xml.parsers.DocumentBuilder;
import net.rim.device.api.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nux.bb.near.u.track.field.ManagerField;
import com.nux.bb.near.u.track.field.SpaceField;
import com.nux.bb.near.u.track.obj.DBStor;
import com.nux.bb.near.u.track.util.InetConn;
import com.nux.bb.near.u.track.util.Util;
import com.nux.bb.near.u.track.util.WebDataCallback;


public class addRoomChat extends BaseScreen implements WebDataCallback, FieldChangeListener{
	String url = "http://maps.googleapis.com/maps/api/geocode/xml?latlng="+DBStor.get_setting().getLat()+
	","+ DBStor.get_setting().getLon() +"&sensor=false";
	String[] kota;
	ManagerField mf1 = new ManagerField();

	ObjectChoiceField pilih = new ObjectChoiceField();
	ButtonField add = new ButtonField("Add This Room",ButtonField.CONSUME_CLICK|FIELD_HCENTER);
	ButtonField ref = new ButtonField("Refresh",ButtonField.CONSUME_CLICK|FIELD_HCENTER);
	public addRoomChat() {
		setJudul("Add Chat Room");
		InetConn.getWebData(url, this);
		setShowLoader(true);
		mf1.add(new SpaceField(8));
		mf1.add(pilih);
		mf1.add(add);
		mf1.add(ref);
		mf1.add(new SpaceField(8));
		pilih.setLabel("Room: ");
		pilih.setPadding(0,5,0,5);
		add(mf1);
		ref.setChangeListener(this);
		add.setChangeListener(this);
	}

	public void callback(String data) {
		setShowLoader(false);
		add.setEditable(true);
		if(data.startsWith("Exception:")){
			Dialog.alert("failed to connect to server");
		}else if(data.trim().startsWith("<?")){
			parsingXML(data);
		}else if(data.trim().startsWith("done")){
			Dialog.alert("Room has been added.");
		}else if(data.trim().startsWith("exist")){
			Dialog.alert("failed: Room exist.");
		}else{
			Dialog.alert("Internal system error, please try again later.");
		}

	}

	private void parsingXML(String data) {
		try{
			    InputStream is = new ByteArrayInputStream(data.getBytes());
	            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder builder = factory.newDocumentBuilder();
	            Document document = builder.parse( is );
	            Element rootElement = document.getDocumentElement();
	            rootElement.normalize();

	            NodeList list= document.getElementsByTagName("short_name");

	            Vector city = new Vector();
	            for (int check=0;check<list.getLength();check++)
	            {
	                Node value=list.item(check).getChildNodes().item(0);
	                if(!city.contains(value.getNodeValue())){
	                	city.addElement(value.getNodeValue());
	                }
	            }
	            int sz = city.size();
	            if(sz>4){
	            	sz = sz-3;
	            }
	            kota = new String[sz];
	            for(int n=0;n<sz;n++){
	            	kota[n] = (String)city.elementAt(n);
	            }
	            pilih.setChoices(kota);
		}catch ( Exception e )
	    {
	        System.out.println( e.toString() );
	    }
	}

	public void fieldChanged(Field field, int context) {
		if(field==ref){
			InetConn.getWebData(url, this);
			setShowLoader(true);
		}else{
			String url = Util.GetURLChat()+"addRoom.php?pin="+Integer.toHexString(DeviceInfo.getDeviceId())+"&lat="+DBStor.get_setting().getLat()+
			"&lon="+DBStor.get_setting().getLon();
			if(pilih.getSelectedIndex()>-1){
				setShowLoader(true);
				add.setEditable(false);
				InetConn.PostWebData(url, "nama="+kota[pilih.getSelectedIndex()], this);
			}
		}
	}
}
