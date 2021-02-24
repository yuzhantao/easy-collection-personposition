package com.bimuo.easy.collection.personposition.v1.service.test;

public class SecretKeyToHardwareTest {

	public static void main(String[] args) {
		String oldPassword1 ="66333356";
		String oldPassword2 = "77552257";
		String oldPassword3 = "33992258";
		
		byte[] oldPasswordArr1 = oldPassword1.getBytes();
		for(int i=0; i<oldPassword1.length(); i++) {
			System.out.println(oldPasswordArr1[i]);
		}
	}
}
