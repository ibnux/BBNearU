package com.nux.bb.near.u.track.ui;

import java.util.Timer;
import java.util.TimerTask;

import com.nux.bb.near.u.track.field.Header;
import com.nux.bb.near.u.track.field.SpaceField;

import net.rim.blackberry.api.invoke.Invoke;
import net.rim.blackberry.api.invoke.MessageArguments;
import net.rim.blackberry.api.mail.Address;
import net.rim.blackberry.api.mail.Message;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.TransitionContext;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiEngineInstance;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class BaseScreen extends MainScreen{
	Bitmap pattern = Bitmap.getBitmapResource("pattern8-pattern-43a.png");
	VerticalFieldManager tabel = new VerticalFieldManager(VERTICAL_SCROLL|VERTICAL_SCROLLBAR);
	Header Judul = new Header();

	int w = Display.getWidth();
    int h = Display.getHeight();
	Timer loadingTimer = new Timer();
    TimerTask loadingTask;
    int imageIndex = 0;
    public Bitmap[] loader = {Bitmap.getBitmapResource("loader/loader0.png"),Bitmap.getBitmapResource("loader/loader1.png"),
    		Bitmap.getBitmapResource("loader/loader2.png"),Bitmap.getBitmapResource("loader/loader3.png"),
    		Bitmap.getBitmapResource("loader/loader4.png"),Bitmap.getBitmapResource("loader/loader5.png"),
    		Bitmap.getBitmapResource("loader/loader6.png"),Bitmap.getBitmapResource("loader/loader7.png"),
    		Bitmap.getBitmapResource("loader/loader8.png"),Bitmap.getBitmapResource("loader/loader9.png"),
    		Bitmap.getBitmapResource("loader/loader10.png"),Bitmap.getBitmapResource("loader/loader11.png")};
    boolean showLoader = false;

	public BaseScreen() {
		super(NO_HORIZONTAL_SCROLL|NO_VERTICAL_SCROLL);
		VerticalFieldManager bgField = new VerticalFieldManager(USE_ALL_WIDTH|USE_ALL_HEIGHT){
			protected void paint(Graphics g) {
				int sizeh = 0;
	        	while(sizeh<Display.getHeight()){
	        		int sizew = 0;
		        	while(sizew<Display.getWidth()){
		        		g.drawBitmap(sizew, sizeh, pattern.getWidth(), pattern.getHeight(), pattern, 0, 0);
		        		sizew += pattern.getWidth();
		        	}
		        	sizeh += pattern.getHeight();
	        	}
				super.paint(g);
			}
		};
		super.add(bgField);
		bgField.add(Judul);
		bgField.add(new SpaceField(5));
		bgField.add(tabel);
		tabel.setPadding(0, 10, 0, 10);
		addMenuItem(fee);
		addMenuItem(about);
		loadingTask = new TimerTask() {

            public void run() {

                invalidate();
                imageIndex++;
                if(imageIndex == loader.length){
                    imageIndex = 0;
                }
            }
        };
        loadingTimer.scheduleAtFixedRate(loadingTask, 100, 100);
        try{
        //transisi
		TransitionContext transition = new TransitionContext(TransitionContext.TRANSITION_FADE);
		transition.setIntAttribute(TransitionContext.ATTR_DURATION, 500);
        transition.setIntAttribute(TransitionContext.ATTR_KIND, TransitionContext.KIND_IN);
        UiEngineInstance engine = Ui.getUiEngineInstance();
        engine.setTransition(null, this, UiEngineInstance.TRIGGER_PUSH, transition);
        }catch(Exception e){}
	}

	public void add(Field field) {
		tabel.add(field);
	}

	MenuItem about = new MenuItem("About",0x000200,10) {
		public void run() {
			Dialog.alert("bbnearu.com by ibnux");
		}
	};
	MenuItem fee = new MenuItem("Send FeedBack",0x000200,14) {

		public void run() {
		try{
			Message m = new Message();
			Address a = new Address("contact@bbnearu.com", "BBNearU");
			Address[] addresses = {a};
			m.addRecipients(net.rim.blackberry.api.mail.Message.RecipientType.TO, addresses);
			m.setSubject("FeedBack BB Near U");
			m.setContent("hi BBNearU,\r\n" +
			DeviceInfo.getDeviceName()+" "+DeviceInfo.getSoftwareVersion()+"\r\n");
			Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES, new MessageArguments(m));
		}catch(Exception e){}

		}
		};
	public void setShowLoader(boolean showLoader) {
        this.showLoader = showLoader;
    }

	public void setJudul(String title) {
		//setBanner(title);
		Judul.setText(title);
	}

    protected void paint(Graphics graphics) {
        super.paint(graphics);
        if (showLoader) {
            graphics.drawBitmap((w-loader[imageIndex].getWidth())/2, (h-loader[imageIndex].getHeight())/2,
            		loader[imageIndex].getWidth(),
            		loader[imageIndex].getHeight(), loader[imageIndex], 0, 0);

        }
    }

    protected boolean onSavePrompt() {
    	return true;
    }

    public boolean onClose() {
    	loadingTask.cancel();
    	loadingTimer.cancel();
    	return super.onClose();
    }
}
