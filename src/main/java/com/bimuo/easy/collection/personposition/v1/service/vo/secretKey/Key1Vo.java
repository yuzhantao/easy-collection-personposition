package com.bimuo.easy.collection.personposition.v1.service.vo.secretKey;

import java.io.Serializable;

/**
 * 密钥1实体类
 * 
 * @author Pingfan
 *
 */
public class Key1Vo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String oldPassWord; // 8字节,原验证密钥，密钥错误操作会失败
	private String newPassword; // 8字节,需要更改的新密钥

	public String getOldPassWord() {
		return oldPassWord;
	}

	public void setOldPassWord(String oldPassWord) {
		this.oldPassWord = oldPassWord;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
