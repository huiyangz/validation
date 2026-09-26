package com.dcits.validation.step;

import com.dcits.validation.facade.bo.ST008InputBO;
import com.dcits.validation.facade.bo.ST008OutputBO;

/** ST008 检查是否存在现金止收限制 步骤接口；仅做实体读取，无事务要求 */
public interface IST008 {
    /** 根据账号检查是否存在现金止收限制，返回现金止收标志及首条满足条件记录的限制与类型信息 */
    ST008OutputBO execute(ST008InputBO input);
}
