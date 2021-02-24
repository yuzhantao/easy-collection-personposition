package com.bimuo.easy.collection.personposition.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.nio.ByteBuffer;

public class ByteUtil {

//	private static ByteBuffer buffer = ByteBuffer.allocate(8);

	/**
	 * int转byte
	 * 
	 * @param x
	 * @return
	 */
	public static byte intToByte(int x) {
		return (byte) x;
	}
	
	/**
	 * 十六进制字符转byte数组
	 * @param str
	 * @return
	 */
	public static byte[] hexStringToBytes(String str,byte[] dest,int offset) {
        if(str == null || str.trim().equals("")) {
            return dest;
        }
        if(dest==null){
        	dest = new byte[str.length() / 2];
        	offset=0;
        }
        for(int i = 0; i < str.length() / 2 && i < dest.length; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            dest[offset+i] = (byte) Integer.parseInt(subStr, 16);
        }

        return dest;
    }
	
	public static byte[] hexStringToBytes(String str) {
        byte[] dest = new byte[str.length()/2];
        dest = hexStringToBytes(str,dest,0);
        return dest;
    }

	/**
	 * byte转int
	 * 
	 * @param b
	 * @return
	 */
	public static int byteToInt(byte b) {
		// Java鐨刡yte鏄湁绗�?彿锛岄繃 &0xFF杞负鏃犵鍙�
		return b & 0xFF;
	}

	/**
	 * byte[]转int
	 * 
	 * @param b
	 * @return
	 */
	public static int byteArrayToInt(byte[] b) {
		return b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (b[0] & 0xFF) << 24;
	}

	public static int byteArrayToInt(byte[] b, int index) {
		return b[index + 3] & 0xFF | (b[index + 2] & 0xFF) << 8 | (b[index + 1] & 0xFF) << 16
				| (b[index + 0] & 0xFF) << 24;
	}

	/**
	 * int转byte[]
	 * 
	 * @param a
	 * @return
	 */
	public static byte[] intToByteArray(int a) {
		return new byte[] { (byte) ((a >> 24) & 0xFF), (byte) ((a >> 16) & 0xFF), (byte) ((a >> 8) & 0xFF),
				(byte) (a & 0xFF) };
	}

	/**
	 * short转byte[]
	 * 
	 * @param b
	 * @param s
	 * @param index
	 */
	public static void byteArrToShort(byte b[], short s, int index) {
		b[index + 1] = (byte) (s >> 8);
		b[index + 0] = (byte) (s >> 0);
	}

	/**
	 * byte[]转short
	 * 
	 * @param b
	 * @param index
	 * @return
	 */
	public static short byteArrToShort(byte[] b, int index) {
		return (short) (((b[index + 0] << 8) | b[index + 1] & 0xff));
	}
	
	/**
	 * byte转short
	 * @param hBit 高字节
	 * @param lBit 低字节
	 * @return
	 */
	public static short byteArrToShort(byte hBit,byte lBit) {
		return (short) (((hBit << 8) | lBit & 0xff));
	}

	/**
	 * 16浣峴hort杞琤yte[]
	 * @param s
	 * @param dest
	 * @param destOffset
	 * @return
	 */
	public static byte[] shortToByteArr(int s,byte[] dest,int destOffset,int sort) {
		if(sort==0){ // 楂樹綅鍦ㄥ墠锛屼綆浣嶅湪鍚�
		for (int i = 0; i < 2; i++) {
			int offset = (1 - i) * 8;
			dest[destOffset+i] = (byte) ((s >>> offset) & 0xff);
		}}else{
			for (int i = 0; i < 2; i++) {
				int offset = (1 - i) * 8;
				dest[destOffset+(1-i)] = (byte) ((s >>> offset) & 0xff);
			}
		}
		return dest;
	}
	
	public static byte[] shortToByteArr(short s) {
		byte[] targets = new byte[2];
		for (int i = 0; i < 2; i++) {
			int offset = (targets.length - 1 - i) * 8;
			targets[i] = (byte) ((s >>> offset) & 0xff);
		}
		return targets;
	}

	/**
	 * byte[]�?6浣峴hort
	 * 
	 * @param b
	 * @return
	 */
	public static short byteArrToShort(byte[] b) {
		return byteArrToShort(b, 0);
	}

	/**
	 * long转byte[]
	 * 
	 * @param x
	 * @return
	 */
	public static byte[] longToBytes(long x) {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.putLong(0, x);
		return buffer.array();
	}

	/**
	 * byte[]转long
	 * 
	 * @param bytes
	 * @return
	 */
	public static long bytesToLong(byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.put(bytes, 0, Math.min(8, bytes.length));
		buffer.flip();// need flip
		return buffer.getLong();
	}

	/**
	 * 浠巄yte[]涓娊鍙栨柊鐨刡yte[]
	 * 
	 * @param data
	 *            - 鍏冩暟鎹�?
	 * @param start
	 *            - 寮嬩綅缃�?
	 * @param end
	 *            - 缁撴潫浣嶇疆
	 * @return 鏂癰yte[]
	 */
	public static byte[] getByteArr(byte[] data, int start, int end) {
		byte[] ret = new byte[end - start];
		for (int i = 0; (start + i) < end; i++) {
			ret[i] = data[start + i];
		}
		return ret;
	}

	/**
	 * 娴佽浆鎹负byte[]
	 * 
	 * @param inStream
	 * @return
	 */
	public static byte[] readInputStream(InputStream inStream) {
		ByteArrayOutputStream outStream = null;
		try {
			outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			byte[] data = null;
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			data = outStream.toByteArray();
			return data;
		} catch (IOException e) {
			return null;
		} finally {
			try {
				if (outStream != null) {
					outStream.close();
				}
				if (inStream != null) {
					inStream.close();
				}
			} catch (IOException e) {
				return null;
			}
		}
	}

	/**
	 * byte[]杞琲nputstream
	 * 
	 * @param b
	 * @return
	 */
	public static InputStream readByteArr(byte[] b) {
		return new ByteArrayInputStream(b);
	}

	/**
	 * byte鏁扮粍鍐呮暟瀛楁槸鍚︾浉鍚�
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static boolean isEq(byte[] s1, byte[] s2) {
		int slen = s1.length;
		if (slen == s2.length) {
			for (int index = 0; index < slen; index++) {
				if (s1[index] != s2[index]) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * byte鏁扮粍杞崲涓篠tirng
	 * 
	 * @param s1
	 *            -鏁扮�?
	 * @param encode
	 *            -瀛楃闆�?
	 * @param err
	 *            -杞崲閿欒鏃惰繑鍥炶鏂囧�?
	 * @return
	 */
	public static String getString(byte[] s1, String encode, String err) {
		return getString(s1,0,s1.length,encode,err);
	}
	public static String getString(byte[] s1,int offset,int length, String encode, String err) {
		try {
			return new String(s1,offset,length, encode);
		} catch (UnsupportedEncodingException e) {
			return err == null ? null : err;
		}
	}

	public static String getString(byte[] s1,int offset,int length, String encode) {
		return getString(s1,offset,length, encode, null);
	}
	/**
	 * byte鏁扮粍杞崲涓篠tirng
	 * 
	 * @param s1-鏁扮�?
	 * @param encode-瀛楃闆�?
	 * @return
	 */
	public static String getString(byte[] s1, String encode) {
		return getString(s1, encode, null);
	}

	// 娴嬭�?
	public static void main(String[] args) {
		System.err.println(isEq(new byte[] { 1, 2 }, new byte[] { 1, 2 }));
	}

	/**
	 * byte数组转十六进制字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byteArrToHexString(byte[] b) {
		return byteArrToHexString(b,false);
	}
	
	/**
	 * 字节�?16进制字符�?
	 * @param b			源字节数�?
	 * @param isFormat	转换的字符串是否每两个字�?+�?个空�?
	 * @return
	 */
	public static String byteArrToHexString(byte[] b,boolean isFormat) {
		String result = "";
		if(b!=null){
			for (int i = 0; i < b.length; i++) {
				result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
			}
		}
		if(isFormat) {
			String regex = "(.{2})";
	        return result.toUpperCase().replaceAll (regex, "$1 ");
		}else {
			return result.toUpperCase();
		}
	}
	
	public static String byteArrToHexString(byte[] b,int offset,int len) {
		String result = "";
		for (int i = offset; i < offset+len; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result.toUpperCase();
	}

	/**
	 * 16进制字符串转int
	 * 
	 * @param hexString
	 * @return
	 */
	public static int hexStringToInt(String hexString) {
		return Integer.parseInt(hexString, 16);
	}

	/**
	 * int转二进制
	 * 
	 * @param i
	 * @return
	 */
	public static String intToBinary(int i) {
		return Integer.toBinaryString(i);
	}
	
	/**
     * ip地址转成byte数组
     * @param ipAddr
     * @return byte[]
     */
    public static byte[] ipToBytesByInet(String ipAddr) {
        try {
            return InetAddress.getByName(ipAddr).getAddress();
        } catch (Exception e) {
            throw new IllegalArgumentException(ipAddr + " is invalid IP");
        }
    }
	public static int bytesToInt(byte b[], int offset) 
	{
		return  b[offset+3] & 0xff | (b[offset+2] & 0xff) << 8 
				| (b[offset+1] & 0xff) << 16 | (b[offset] & 0xff) << 24; 		    
	}
	
	public static int bytesToUbyte( byte[] array, int offset )
	{    
		return array[offset] & 0xff; 
	}
    /**
     * 鎶奿nt->ip鍦板�?
     * @param ipInt
     * @return String
     */
    public static String intToIp(int ipInt) {
        return new StringBuilder().append(((ipInt >> 24) & 0xff)).append('.')
                .append((ipInt >> 16) & 0xff).append('.').append(
                        (ipInt >> 8) & 0xff).append('.').append((ipInt & 0xff))
                .toString();
    }
    
	public static void shortToBytes(short n, byte[] array, int offset ) 
	{    
		array[offset+1] = (byte) ( n  & 0xff); 
		array[offset] = (byte) ((n >> 8) & 0xff); 
	}
	
	
	
	
	
}
