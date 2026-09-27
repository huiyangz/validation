package com.dcits.validation.task.dto;

import jakarta.validation.constraints.NotNull;

/**
 * T1S1 检查客户限制 输入DTO
 */
public class T1S1InputDTO {
    /** 客户号 */
    @NotNull
    private String clientNo;

    public String getClientNo() {
        return clientNo;
    }

    public void setClientNo(String clientNo) {
        this.clientNo = clientNo;
    }
}
