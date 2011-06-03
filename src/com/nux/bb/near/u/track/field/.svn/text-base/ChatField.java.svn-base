package com.nux.bb.near.u.track.field;

import java.util.Calendar;
import java.util.Date;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.ContextMenu;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.ActiveRichTextField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.nux.bb.near.u.track.obj.DBStor;
import com.nux.bb.near.u.track.obj.MsgChat;

public class ChatField extends VerticalFieldManager{
	LabelFieldBold nicks;
	LabelFieldItalic tgl;
	private MsgChat msgChat;
	private String nick = "";
	public ChatField(MsgChat msgChat) {
		super(USE_ALL_WIDTH);
		setPadding(0,5,0,5);
		this.nick = msgChat.getNick();
		this.setMsgChat(msgChat);
		if(msgChat.getMessage().equals(nick+" has joined.")){
			ActiveRichTextField rtf = new ActiveRichTextField(msgChat.getMessage()){
				protected void paint(Graphics g) {
					int c = g.getColor();
					g.setColor(Color.LIGHTGREEN);
					g.fillRect(0, 0, getWidth(), getHeight());
					g.setColor(c);
					super.paint(g);
				}
			};
			add(rtf);
		}else if(msgChat.getMessage().equals(nick+" has leave.")){
			ActiveRichTextField rtf = new ActiveRichTextField(msgChat.getMessage()){
				protected void paint(Graphics g) {
					int c = g.getColor();
					g.setColor(Color.YELLOW);
					g.fillRect(0, 0, getWidth(), getHeight());
					g.setColor(c);
					super.paint(g);
				}
			};
			add(rtf);
		}else if(msgChat.isIklan()){
			LabelLink ll= new LabelLink(msgChat.getMessage(),msgChat.getUrl());
			ll.setPadding(4,4,4,4);
			try{
				FontFamily _fontFamily = FontFamily.forName("BBAlpha Serif");
				Font font = _fontFamily.getFont(FontFamily.SCALABLE_FONT, DBStor.get_setting().getSizeFont()).derive(Font.ITALIC);
				ll.setFont(font);
			}catch(Exception e){}
			add(ll);
		}else{

			HorizontalFieldManager hfm = new HorizontalFieldManager();
			nicks = new LabelFieldBold(msgChat.getNick()+"  ",FOCUSABLE|DrawStyle.ELLIPSIS){
				protected void makeContextMenu(ContextMenu contextMenu) {
					super.makeContextMenu(contextMenu);
					contextMenu.addItem(reportuser);
				}
			};
			nicks.setHf(DBStor.get_setting().getSizeFont());
			Date dt = new Date(msgChat.getTimein());
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			String _tgl = cal.get(Calendar.DAY_OF_MONTH)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.YEAR)+
			" "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND);
			tgl = new LabelFieldItalic(_tgl, DrawStyle.ELLIPSIS);
			hfm.add(nicks);
			hfm.add(tgl);
			add(new SeparatorField());
			add(hfm);
			ActiveRichTextField rtf = new ActiveRichTextField(msgChat.getMessage()){
				protected void makeContextMenu(ContextMenu contextMenu) {
					super.makeContextMenu(contextMenu);
					contextMenu.addItem(report);
				}
			};
			try{
				FontFamily _fontFamily = FontFamily.forName("BBAlpha Serif");
				Font font = _fontFamily.getFont(FontFamily.SCALABLE_FONT, DBStor.get_setting().getSizeFont()).derive(Font.ITALIC);
				rtf.setFont(font);
			}catch(Exception e){}
			add(rtf);
		}
	}

	public void setMsgChat(MsgChat msgChat) {
		this.msgChat = msgChat;
	}
	public MsgChat getMsgChat() {
		return msgChat;
	}

	MenuItem report = new MenuItem("Report This Message",0x000100,1) {
		public void run() {
			Dialog.alert("Still in progress :)");
		}
	};
	MenuItem reportuser = new MenuItem("Report This User",0x000100,1) {
		public void run() {
			Dialog.alert("Still in progress :)");
		}
	};
}
