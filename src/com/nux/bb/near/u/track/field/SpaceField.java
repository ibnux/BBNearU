package com.nux.bb.near.u.track.field;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;

public class SpaceField extends Field{
	private int n;

	public SpaceField(int n) {
		this.n = n;
	}

	protected void layout(int width, int height) {
		setExtent(10, n);
	}

	protected void paint(Graphics graphics) {
		graphics.setGlobalAlpha(0);
		graphics.fillRect(0, 0, 10, n);
	}
}
