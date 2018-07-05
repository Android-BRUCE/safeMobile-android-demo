package com.safe.a360.safemobile.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * ��ȡ���Ĺ���
 * @author BRUCE
 *
 */
public class StreamUtils {
	
	public static String readFromStrem(InputStream in) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		int len = 0;
		byte [] buffer = new byte[1024];
		
		while ((len = in.read(buffer)) != -1) {
			outputStream.write(buffer,0,len);
		}
		String resurt = outputStream.toString();
		in.close();
		outputStream.close();
		return resurt;
		
	}
}
