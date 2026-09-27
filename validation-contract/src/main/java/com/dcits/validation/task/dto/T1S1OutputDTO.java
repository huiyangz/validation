package com.dcits.validation.task.dto;

import java.util.List;

/**
 * T1S1 检查客户限制 输出DTO
 */
public class T1S1OutputDTO {
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
        private String restraintType;
        /** 限制状态 */
        private String restraintsStatus;

        public String getResSeqNo() {
            return resSeqNo;
        }

        public void setResSeqNo(String resSeqNo) {
            this.resSeqNo = resSeqNo;
        }

        public String getRestraintType() {
            return restraintType;
        }

        public void setRestraintType(String restraintType) {
            this.restraintType = restraintType;
        }

        public String getRestraintsStatus() {
            return restraintsStatus;
        }

        public void setRestraintsStatus(String restraintsStatus) {
            this.restraintsStatus = restraintsStatus;
        }
    }
}
