package com.nux.bb.near.u.track.ui;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.nux.bb.near.u.track.obj.DBStor;
import com.nux.bb.near.u.track.obj.Setting;
import com.nux.bb.near.u.track.util.Util;

public class PopupAgreement extends PopupScreen implements FieldChangeListener{
	ButtonField privacy = new ButtonField("Privacy Policy",ButtonField.CONSUME_CLICK|FIELD_HCENTER);
	ButtonField tos = new ButtonField("Terms of use",ButtonField.CONSUME_CLICK|FIELD_HCENTER);
	ButtonField agree = new ButtonField("Agree",ButtonField.CONSUME_CLICK|FIELD_HCENTER);
	ButtonField disagree = new ButtonField("Disagree",ButtonField.CONSUME_CLICK|FIELD_HCENTER);

	public PopupAgreement() {
		super(new VerticalFieldManager(VERTICAL_SCROLL));
		VerticalFieldManager vfm = (VerticalFieldManager)getDelegate();
		vfm.add(new RichTextField("to use this application, you must agreed with"));
		vfm.add(privacy);
		vfm.add(new LabelField("and",FIELD_HCENTER));
		vfm.add(tos);
		vfm.add(new SeparatorField());
		vfm.add(new RichTextField("BBNearU offers users the ability to communicate with each other through chat rooms. " +
				"Never disclose personal information about yourself in a public area like ChatRooms. " +
				"Don’t share personal information in a ChatRoom."));
		vfm.add(new SeparatorField());
		HorizontalFieldManager hfm = new HorizontalFieldManager(FIELD_HCENTER);
		hfm.add(agree);
		hfm.add(disagree);
		vfm.add(hfm);

		privacy.setChangeListener(this);
		tos.setChangeListener(this);
		agree.setChangeListener(this);
		disagree.setChangeListener(this);
	}

	public void fieldChanged(Field f, int context) {
		if(f==privacy){
			Util.BukaWeb("http://bbnearu.com/privacy-policy");
		}else if(f==tos){
			Util.BukaWeb("http://bbnearu.com/tos");
		}else if(f==agree){
			Dialog.inform( "Please Activate room chat from setting, to start using chat Room" );
			Setting st = DBStor.get_setting();
			st.setStuju(true);
			DBStor.set_setting(st);
			this.close();
		}else{
			System.exit(0);
		}
	}
}
