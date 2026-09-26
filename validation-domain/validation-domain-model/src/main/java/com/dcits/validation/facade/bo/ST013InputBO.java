package com.dcits.validation.facade.bo;

import java.util.List;

import com.dcits.validation.enums.RestraintLevel;
import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.enums.RestraintType;

/** ST013 检查账户是否存在不允许销户的限制 - 步骤输入BO */
public class ST013InputBO {
    /** 账户限制信息集合（来源：对公存款账户限制表 RB_BUS_RESTRAINTS） */
    private List<RestraintInfoDTO> restraintList;

    public List<RestraintInfoDTO> getRestraintList() {
        return restraintList;
    }

    public void setRestraintList(List<RestraintInfoDTO> restraintList) {
        this.restraintList = restraintList;
    }

    /** 账户限制信息DTO */
    public static class RestraintInfoDTO {
        /** 限制编号 */
        private String resSeqNo;
        /** 账户限制类型 */
        private RestraintType restraintType;
        /** 限制状态 */
        private RestraintsStatus restraintsStatus;
        /** 限制级别 */
        private RestraintLevel restraintLevel;

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

        public RestraintLevel getRestraintLevel() {
            return restraintLevel;
        }

        public void setRestraintLevel(RestraintLevel restraintLevel) {
            this.restraintLevel = restraintLevel;
        }
    }
}
