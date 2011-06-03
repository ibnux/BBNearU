package com.nux.bb.near.u.track.obj;

public class MsgChat{
	private String message = "", pin="",nick ="",url="";
	private long timein = 0;
	
	/**
	 * 
	 * @param Message
	 * @param nick
	 * @param pin
	 * @param UrlIklan
	 * @param timein
	 */
	public MsgChat(String Message,String nick,String pin, String UrlIklan, long timein) {
		setMessage(Message);
		setPin(pin);
		setTimein(timein);
		setNick(nick);
		setUrl(UrlIklan);
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getPin() {
		return pin;
	}

	public void setTimein(long timein) {
		this.timein = timein;
	}

	public long getTimein() {
		return timein;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getNick() {
		return nick;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public boolean isIklan() {
		if(getUrl().length()>1){
			return true;
		}else{
			return false;
		}
	}
	
	
}
