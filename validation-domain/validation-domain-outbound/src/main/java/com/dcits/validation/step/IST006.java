package com.dcits.validation.step;

import com.dcits.validation.facade.bo.ST006InputBO;
import com.dcits.validation.facade.bo.ST006OutputBO;

/**
 * ST006 检查是否存在转账止付限制 步骤接口。
 *
 * <p>只读查询步骤，无本地数据库写操作，不要求调用方提供事务。
 */
public interface IST006 {

    /**
     * 根据账号查询生效的账户限制信息，逐条核对限制类型的借贷方控制标志与转账标志，
     * 判定是否存在转账止付限制。
     *
     * @param input 输入BO，baseAcctNo 必填
     * @return 转账止付标志（是/否）及第一条满足条件记录的查询输出；本步骤无业务失败场景
     */
    ST006OutputBO execute(ST006InputBO input);
}
