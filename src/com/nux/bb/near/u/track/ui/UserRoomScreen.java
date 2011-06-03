package com.nux.bb.near.u.track.ui;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.nux.bb.near.u.track.field.GridFieldManager;
import com.nux.bb.near.u.track.field.LabelFieldBold;
import com.nux.bb.near.u.track.field.ManagerField;
import com.nux.bb.near.u.track.field.SpaceField;
import com.nux.bb.near.u.track.util.InetConn;
import com.nux.bb.near.u.track.util.Util;
import com.nux.bb.near.u.track.util.WebDataCallback;

public class UserRoomScreen extends BaseScreen implements WebDataCallback{
	ManagerField mf1 = new ManagerField();
	String url = "";
	GridFieldManager gfm;
	public UserRoomScreen(String namaroom,String idRoom) {
		setJudul(namaroom + " Users");
		int w1 = Font.getDefault().getAdvance("XXXXXXXXXXX");
		int w2 = Font.getDefault().getAdvance("XXXXX");
		int w3 = Font.getDefault().getAdvance("XX XXX XXXX ");
		gfm = new GridFieldManager(new int[] {w1,w2,w3}, 0);
		add(mf1);
		VerticalFieldManager vfm = new VerticalFieldManager(HORIZONTAL_SCROLL|VERTICAL_SCROLL|FIELD_HCENTER);
		mf1.add(new SpaceField(8));
		mf1.add(vfm);
		vfm.add(gfm);
		vfm.setPadding(0,5,0,5);
		mf1.add(new SpaceField(8));

		gfm.add(new LabelFieldBold("Nick Name",0x424242,FIELD_HCENTER));
		gfm.add(new LabelFieldBold("PIN",0x424242,FIELD_HCENTER));
		gfm.add(new LabelFieldBold("Date join",0x424242,FIELD_HCENTER));

		this.url= Util.GetURLChat()+"listuser.php?id="+idRoom;
		InetConn.getWebData(url, this);
		setShowLoader(true);
	}
	public void callback(String data) {
		setShowLoader(false);
		if(data.startsWith("Exception:")){
			if(Dialog.ask(Dialog.D_YES_NO,"Failed to connect to server, try again?")==Dialog.YES){
				InetConn.getWebData(url, this);
			}else{
				this.close();
			}
		}else if(data.startsWith("done:")){
			parsing(data.substring(5));
		}else{
			Dialog.alert("Internal server error");
			close();
		}
	}

	private void parsing(String data){
		String[] temps = Util.split(data, "<<>>");
		for(int n=0;n<temps.length;n++){
			String temp[] = Util.split(temps[n], "<>");
			gfm.add(new LabelField(temp[0]+" ",FIELD_LEFT|FOCUSABLE));
			gfm.add(new LabelField(temp[1],FIELD_LEFT|FOCUSABLE));
			gfm.add(new LabelField(temp[2],FIELD_LEFT|FOCUSABLE));
		}
	}

	protected boolean onSavePrompt() {
		return true;
	}
}
