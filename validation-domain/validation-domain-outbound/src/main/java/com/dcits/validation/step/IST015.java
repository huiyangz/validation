package com.dcits.validation.step;

import com.dcits.validation.facade.bo.ST015InputBO;
import com.dcits.validation.facade.bo.ST015OutputBO;

/**
 * ST015 检查是否存在属性限制 步骤接口。
 *
 * <p>根据账号查询对公存款账户限制表（RB_BUS_RESTRAINTS）中限制状态为 A-生效、
 * 限制级别为 NATURE-账户属性限制的账户限制信息：为空时属性限制标志返回"否"；
 * 非空时返回"是"，并按限制编号数值比较最小的一条填充限制编号、账户限制类型、限制状态、限制级别。</p>
 *
 * <p>本步骤仅查询本地数据，无本地数据库写入，无事务要求；
 * 无业务失败场景，失败仅由技术异常传播表达。</p>
 */
public interface IST015 {
	/**
	 * 执行 ST015 检查是否存在属性限制 步骤。
	 *
	 * @param input 步骤输入，baseAcctNo 必填
	 * @return 属性限制标志及限制编号等明细输出，succeed=true 表示正常完成
	 */
	ST015OutputBO execute(ST015InputBO input);
}
