package com.xplatform.base.framework.mybatis.engine.type;

/**
 * 
 * <STRONG>类描述</STRONG> :文章删除状态枚举类  <p>
 * @version 1.0 <p>
 * @author jiagq@huilan.com<p>
 * 
 * <STRONG>创建时间</STRONG> : 2012-8-30 上午10:01:15<p>
 * <STRONG>修改历史</STRONG> :<p>
 *<pre>
 * 修改人                   修改时间                     修改内容
 * ---------------         -------------------         -----------------------------------
 * jiagq@huilan.com        2012-8-30 上午10:01:15
 *</pre>
 */
public enum DeleteStateEnum {
	/**使用中*/
	U,
	/** 真删除*/
	T,
	/** 假删除*/
	F
}
