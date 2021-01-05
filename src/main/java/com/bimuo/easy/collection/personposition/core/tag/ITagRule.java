package com.bimuo.easy.collection.personposition.core.tag;
/**
 * 判断标签的有�??
 * @author yuzhantao
 *
 */
public interface ITagRule {
	/**
	 * 判断标签格式是否有效
	 * @return
	 */
	boolean isValid(String code);
}
