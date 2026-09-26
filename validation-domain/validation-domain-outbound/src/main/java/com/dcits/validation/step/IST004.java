package com.dcits.validation.step;

import com.dcits.validation.facade.bo.ST004InputBO;
import com.dcits.validation.facade.bo.ST004OutputBO;

/**
 * ST004 检查转账止收限制 步骤接口
 */
public interface IST004 {

	/**
	 * 根据账号查询全部生效账户限制及其限制类型，判断是否存在借贷方控制标志为
	 * “C-禁止贷方”且转账标志为“N-不允许转账”的转账止收限制。
	 * 本步骤仅查询本地表 RB_BUS_RESTRAINTS、RB_RESTRAINT_TYPE，无数据库写入，无事务要求。
	 * 无业务失败场景，失败仅由技术异常传播表达。
	 *
	 * @param input 输入BO，baseAcctNo 必填
	 * @return 输出BO，transferStopFlag 取值“是”（命中，其余输出字段取限制编号最小的命中记录及其限制类型信息）
	 *         或“否”（账户限制信息为空集或均不满足，其余输出字段置空）
	 */
	ST004OutputBO execute(ST004InputBO input);
}
