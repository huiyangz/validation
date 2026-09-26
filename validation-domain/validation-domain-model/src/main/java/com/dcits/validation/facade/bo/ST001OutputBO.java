package com.dcits.validation.facade.bo;

import com.dcits.common.step.StepResult;
import com.dcits.validation.enums.RestraintType;
import com.dcits.validation.enums.RestraintsStatus;
import java.util.List;

/**
 * ST001 检查客户是否存在限制 输出BO
 */
public class ST001OutputBO extends StepResult {
    /** 客户限制信息（限制状态等于"A-生效"的全部记录，可能多条） */
    private List<RestraintDTO> restraints;

    public List<RestraintDTO> getRestraints() {
        return restraints;
    }

    public void setRestraints(List<RestraintDTO> restraints) {
        this.restraints = restraints;
    }

    /**
     * 客户限制信息DTO
     */
    public static class RestraintDTO {
        /** 限制编号 */
        private String resSeqNo;
        /** 账户限制类型 */
        private RestraintType restraintType;
        /** 限制状态 */
        private RestraintsStatus restraintsStatus;

        public String getResSeqNo() {
            return resSeqNo;
        }

        public void setResSeqNo(String resSeqNo) {
            this.resSeqNo = resSeqNo;
        }

        public RestraintType getRestraintType() {
            return restraintType;
        }

        public void setRestraintType(RestraintType restraintType) {
            this.restraintType = restraintType;
        }

        public RestraintsStatus getRestraintsStatus() {
            return restraintsStatus;
        }

        public void setRestraintsStatus(RestraintsStatus restraintsStatus) {
            this.restraintsStatus = restraintsStatus;
        }
    }
}
