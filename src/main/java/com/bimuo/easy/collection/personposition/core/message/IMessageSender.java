package com.bimuo.easy.collection.personposition.core.message;
/**
 * 消息�?
 * @author yuzhantao
 *
 */
public interface IMessageSender<T> {
	void sendMessage(T datas);
}
