package com.dcits.validation.task.scenario;

import com.dcits.common.task.RespHeader;
import com.dcits.validation.facade.bo.ST001InputBO;
import com.dcits.validation.facade.bo.ST001OutputBO;
import com.dcits.validation.step.IST001;
import com.dcits.validation.task.dto.T1S1InputDTO;
import com.dcits.validation.task.dto.T1S1OutputDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * T1S1 检查客户限制
 * <p>根据客户号调用 ST001 查询【客户限制表】中限制状态等于"A-生效"的全部客户限制信息并返回。</p>
 * <p>ST001 为本地只读查询，无数据库写入，无独立事务要求；
 * 本场景及 ST001 均无业务失败场景，失败仅由技术异常传播表达。</p>
 */
@Component
public class T1S1 {

    private final IST001 ist001;

    public T1S1(IST001 ist001) {
        this.ist001 = ist001;
    }

    /**
     * 执行 T1S1 检查客户限制
     *
     * @param header 响应头，成功时显式置成功并清理旧错误，失败时置失败头
     * @param input 输入DTO，clientNo（客户号）必填
     * @return 输出DTO，restraints 为限制状态"A-生效"的全部客户限制信息，无生效限制时为空集合
     */
    public T1S1OutputDTO execute(RespHeader header, T1S1InputDTO input) {
        T1S1OutputDTO output = new T1S1OutputDTO();

        ST001InputBO st001Input = new ST001InputBO();
        st001Input.setClientNo(input.getClientNo());
        ST001OutputBO st001Result = ist001.execute(st001Input);
        if (!st001Result.isSucceed()) {
            handleError(header, st001Result.getErrorCode(), st001Result.getErrorMessage());
            return output;
        }

        List<T1S1OutputDTO.RestraintDTO> restraints = new ArrayList<>();
        for (ST001OutputBO.RestraintDTO source : st001Result.getRestraints()) {
            restraints.add(convertRestraint(source));
        }
        output.setRestraints(restraints);

        header.setSucceed(true);
        header.setErrorCode(null);
        header.setErrorMessage(null);
        return output;
    }

    /**
     * 逐字段转换客户限制信息，枚举按业务值转换为 String；字段非必填，null 原样保留
     */
    private T1S1OutputDTO.RestraintDTO convertRestraint(ST001OutputBO.RestraintDTO source) {
        T1S1OutputDTO.RestraintDTO target = new T1S1OutputDTO.RestraintDTO();
        target.setResSeqNo(source.getResSeqNo());
        target.setRestraintType(source.getRestraintType() == null ? null : source.getRestraintType().getValue());
        target.setRestraintsStatus(source.getRestraintsStatus() == null ? null : source.getRestraintsStatus().getValue());
        return target;
    }

    /**
     * 设置失败响应头；本场景无已定义业务错误码，原样透传步骤自身的错误码与错误信息
     */
    private void handleError(RespHeader header, String errorCode, String errorMessage) {
        header.setSucceed(false);
        header.setErrorCode(errorCode);
        header.setErrorMessage(errorMessage);
    }
}
