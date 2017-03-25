package com.minivision.faceplat.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class ResizeImg {

	private static final int MAX_LENGTH = 800;

	public static BufferedImage resizeImage(BufferedImage image_data) {
		int width = image_data.getWidth();
		int height = image_data.getHeight();
		BufferedImage image_new;
		if (width > MAX_LENGTH && height < MAX_LENGTH) {
			image_new = resize(image_data, MAX_LENGTH, (int) height * MAX_LENGTH / width);
		} else if (width < MAX_LENGTH && height > MAX_LENGTH) {
			image_new = resize(image_data, (int) width * MAX_LENGTH / height, MAX_LENGTH);
		} else if (width > MAX_LENGTH && height > MAX_LENGTH) {
			if (width > height) {
				image_new = resize(image_data, MAX_LENGTH, (int) height * MAX_LENGTH / width);
			} else {
				image_new = resize(image_data, (int) width * MAX_LENGTH / height, MAX_LENGTH);
			}
		} else {
			image_new = image_data;
		}
		return image_new;
	}

	private static BufferedImage resize(BufferedImage img, int newW, int newH) {
		int w = img.getWidth();
		int h = img.getHeight();
		BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
		Graphics2D g = dimg.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
		g.dispose();
		return dimg;
	}

}
