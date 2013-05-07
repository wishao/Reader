package com.reader.util;

import java.util.UUID;

public class IDUtil {

	public static String getID() {
		return UUID.randomUUID().toString();
	}

}
