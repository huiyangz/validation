package com.dcits.validation.step;

import com.dcits.validation.facade.bo.ST013InputBO;
import com.dcits.validation.facade.bo.ST013OutputBO;

/**
 * ST013 检查账户是否存在不允许销户的限制 - 步骤接口
 * <p>只读查询步骤，无本地数据库写入，对调用方无事务要求。</p>
 */
public interface IST013 {
    /** 检查账户是否存在不允许销户的限制 */
    ST013OutputBO execute(ST013InputBO input);
}
