package com.dcits.validation.step;

import com.dcits.validation.facade.bo.ST005InputBO;
import com.dcits.validation.facade.bo.ST005OutputBO;

/**
 * ST005 检查账户是否存在限制 步骤接口。
 * 本步骤为只读查询，不涉及本地数据库写入，无事务要求。
 */
public interface IST005 {

    /** 执行 ST005 检查账户是否存在限制步骤，返回待查账户的账户限制信息 */
    ST005OutputBO execute(ST005InputBO input);
}
