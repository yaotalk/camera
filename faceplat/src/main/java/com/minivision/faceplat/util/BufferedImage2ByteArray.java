package com.minivision.faceplat.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BufferedImage2ByteArray {

	public static byte[] bufferedImage2ByteArray(BufferedImage image_data) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			switch (image_data.getType()) {
			case 1:
				ImageIO.write(image_data, "bmp", bos);
				break;
			case 5:
				ImageIO.write(image_data, "jpg", bos);
				break;
			case 6:
				ImageIO.write(image_data, "png", bos);
				break;
			case 13:
				ImageIO.write(image_data, "gif", bos);
				break;
			default:
				ImageIO.write(image_data, "jpg", bos);
			}
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				bos.close();
			} catch (IOException localIOException3) {
			}
			bos = null;
		}
	}
}
