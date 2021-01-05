package com.bimuo.easy.collection.personposition.core.message;
/**
 * 消息工厂
 * @author yuzhantao
 *
 * @param <S>
 * @param <R>
 */
public interface IMessageFactory<S,R> {
	R parse(S src);
}
