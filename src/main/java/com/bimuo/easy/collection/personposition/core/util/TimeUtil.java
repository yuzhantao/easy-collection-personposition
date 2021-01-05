package com.bimuo.easy.collection.personposition.core.util;

public class TimeUtil {
	public static String get4BitTimeStamp() {
		return ByteUtil.byteArrToHexString(ByteUtil.intToByteArray((int) (System.currentTimeMillis()/1000)));
	}
}
