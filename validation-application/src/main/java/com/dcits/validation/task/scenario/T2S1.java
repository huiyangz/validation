package com.dcits.validation.task.scenario;

import com.dcits.common.task.RespHeader;
import com.dcits.validation.enums.OthTranType;
import com.dcits.validation.enums.RestraintType;
import com.dcits.validation.enums.SourceType;
import com.dcits.validation.facade.bo.ST002InputBO;
import com.dcits.validation.facade.bo.ST002OutputBO;
import com.dcits.validation.facade.bo.ST003InputBO;
import com.dcits.validation.facade.bo.ST003OutputBO;
import com.dcits.validation.facade.bo.ST004InputBO;
import com.dcits.validation.facade.bo.ST004OutputBO;
import com.dcits.validation.facade.bo.ST005InputBO;
import com.dcits.validation.facade.bo.ST005OutputBO;
import com.dcits.validation.facade.bo.ST006InputBO;
import com.dcits.validation.facade.bo.ST006OutputBO;
import com.dcits.validation.facade.bo.ST007InputBO;
import com.dcits.validation.facade.bo.ST007OutputBO;
import com.dcits.validation.facade.bo.ST008InputBO;
import com.dcits.validation.facade.bo.ST008OutputBO;
import com.dcits.validation.facade.bo.ST009InputBO;
import com.dcits.validation.facade.bo.ST009OutputBO;
import com.dcits.validation.facade.bo.ST010InputBO;
import com.dcits.validation.facade.bo.ST010OutputBO;
import com.dcits.validation.facade.bo.ST011InputBO;
import com.dcits.validation.facade.bo.ST011OutputBO;
import com.dcits.validation.facade.bo.ST012InputBO;
import com.dcits.validation.facade.bo.ST012OutputBO;
import com.dcits.validation.facade.bo.ST013InputBO;
import com.dcits.validation.facade.bo.ST013OutputBO;
import com.dcits.validation.facade.bo.ST014InputBO;
import com.dcits.validation.facade.bo.ST014OutputBO;
import com.dcits.validation.facade.bo.ST015InputBO;
import com.dcits.validation.facade.bo.ST015OutputBO;
import com.dcits.validation.step.IST002;
import com.dcits.validation.step.IST003;
import com.dcits.validation.step.IST004;
import com.dcits.validation.step.IST005;
import com.dcits.validation.step.IST006;
import com.dcits.validation.step.IST007;
import com.dcits.validation.step.IST008;
import com.dcits.validation.step.IST009;
import com.dcits.validation.step.IST010;
import com.dcits.validation.step.IST011;
import com.dcits.validation.step.IST012;
import com.dcits.validation.step.IST013;
import com.dcits.validation.step.IST014;
import com.dcits.validation.step.IST015;
import com.dcits.validation.task.dto.T2S1InputDTO;
import com.dcits.validation.task.dto.T2S1OutputDTO;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

/**
 * T2S1 检查账户限制
 * <p>按执行步骤表顺序调用 ST002–ST015 共 14 个检查步骤（无条件顺序执行，
 * ST002/ST011 的 checkResult 不用于跳转），全部成功后映射场景输出并置成功头。</p>
 * <p>全部步骤均为本地只读查询，无数据库写入，无事务要求；
 * 本场景及各步骤均无业务失败场景，失败仅由技术异常传播表达。</p>
 */
@Component
public class T2S1 {

    private final IST002 ist002;
    private final IST003 ist003;
    private final IST004 ist004;
    private final IST005 ist005;
    private final IST006 ist006;
    private final IST007 ist007;
    private final IST008 ist008;
    private final IST009 ist009;
    private final IST010 ist010;
    private final IST011 ist011;
    private final IST012 ist012;
    private final IST013 ist013;
    private final IST014 ist014;
    private final IST015 ist015;

    public T2S1(IST002 ist002, IST003 ist003, IST004 ist004, IST005 ist005, IST006 ist006,
            IST007 ist007, IST008 ist008, IST009 ist009, IST010 ist010, IST011 ist011,
            IST012 ist012, IST013 ist013, IST014 ist014, IST015 ist015) {
        this.ist002 = ist002;
        this.ist003 = ist003;
        this.ist004 = ist004;
        this.ist005 = ist005;
        this.ist006 = ist006;
        this.ist007 = ist007;
        this.ist008 = ist008;
        this.ist009 = ist009;
        this.ist010 = ist010;
        this.ist011 = ist011;
        this.ist012 = ist012;
        this.ist013 = ist013;
        this.ist014 = ist014;
        this.ist015 = ist015;
    }

    /**
     * 执行 T2S1 检查账户限制
     *
     * @param header 响应头，成功时显式置成功并清理旧错误，失败时置失败头
     * @param input 输入DTO，baseAcctNo/tranType/sourceType/restraintType/narrativeCode/prodType 必填
     * @return 输出DTO，各检查判定标志与命中限制记录信息；无来源字段（客户限制表三项、冻结级别两项）未赋值
     */
    public T2S1OutputDTO execute(RespHeader header, T2S1InputDTO input) {
        T2S1OutputDTO output = new T2S1OutputDTO();

        ST002InputBO st002Input = new ST002InputBO();
        st002Input.setSourceType(SourceType.byValue(input.getSourceType()));
        st002Input.setRestraintType(RestraintType.byValue(input.getRestraintType()));
        st002Input.setTranType(OthTranType.byValue(input.getTranType()));
        st002Input.setNarrativeCode(input.getNarrativeCode());
        st002Input.setProdType(input.getProdType());
        ST002OutputBO st002Result = ist002.execute(st002Input);
        if (!st002Result.isSucceed()) {
            handleError(header, st002Result.getErrorCode(), st002Result.getErrorMessage());
            return output;
        }

        ST003InputBO st003Input = new ST003InputBO();
        st003Input.setBaseAcctNo(input.getBaseAcctNo());
        ST003OutputBO st003Result = ist003.execute(st003Input);
        if (!st003Result.isSucceed()) {
            handleError(header, st003Result.getErrorCode(), st003Result.getErrorMessage());
            return output;
        }

        ST004InputBO st004Input = new ST004InputBO();
        st004Input.setBaseAcctNo(input.getBaseAcctNo());
        ST004OutputBO st004Result = ist004.execute(st004Input);
        if (!st004Result.isSucceed()) {
            handleError(header, st004Result.getErrorCode(), st004Result.getErrorMessage());
            return output;
        }

        ST005InputBO st005Input = new ST005InputBO();
        st005Input.setBaseAcctNo(input.getBaseAcctNo());
        ST005OutputBO st005Result = ist005.execute(st005Input);
        if (!st005Result.isSucceed()) {
            handleError(header, st005Result.getErrorCode(), st005Result.getErrorMessage());
            return output;
        }

        ST006InputBO st006Input = new ST006InputBO();
        st006Input.setBaseAcctNo(input.getBaseAcctNo());
        ST006OutputBO st006Result = ist006.execute(st006Input);
        if (!st006Result.isSucceed()) {
            handleError(header, st006Result.getErrorCode(), st006Result.getErrorMessage());
            return output;
        }

        ST007InputBO st007Input = new ST007InputBO();
        st007Input.setBaseAcctNo(input.getBaseAcctNo());
        ST007OutputBO st007Result = ist007.execute(st007Input);
        if (!st007Result.isSucceed()) {
            handleError(header, st007Result.getErrorCode(), st007Result.getErrorMessage());
            return output;
        }

        ST008InputBO st008Input = new ST008InputBO();
        st008Input.setBaseAcctNo(input.getBaseAcctNo());
        ST008OutputBO st008Result = ist008.execute(st008Input);
        if (!st008Result.isSucceed()) {
            handleError(header, st008Result.getErrorCode(), st008Result.getErrorMessage());
            return output;
        }

        ST009InputBO st009Input = new ST009InputBO();
        st009Input.setBaseAcctNo(input.getBaseAcctNo());
        ST009OutputBO st009Result = ist009.execute(st009Input);
        if (!st009Result.isSucceed()) {
            handleError(header, st009Result.getErrorCode(), st009Result.getErrorMessage());
            return output;
        }

        ST010InputBO st010Input = new ST010InputBO();
        st010Input.setBaseAcctNo(input.getBaseAcctNo());
        ST010OutputBO st010Result = ist010.execute(st010Input);
        if (!st010Result.isSucceed()) {
            handleError(header, st010Result.getErrorCode(), st010Result.getErrorMessage());
            return output;
        }

        ST011InputBO st011Input = new ST011InputBO();
        st011Input.setTranType(OthTranType.byValue(input.getTranType()));
        st011Input.setRestraintType(RestraintType.byValue(input.getRestraintType()));
        ST011OutputBO st011Result = ist011.execute(st011Input);
        if (!st011Result.isSucceed()) {
            handleError(header, st011Result.getErrorCode(), st011Result.getErrorMessage());
            return output;
        }

        ST012InputBO st012Input = new ST012InputBO();
        st012Input.setBaseAcctNo(input.getBaseAcctNo());
        ST012OutputBO st012Result = ist012.execute(st012Input);
        if (!st012Result.isSucceed()) {
            handleError(header, st012Result.getErrorCode(), st012Result.getErrorMessage());
            return output;
        }

        // ST013 输入 restraintList 的赋值来源未确认（已接受结论：场景输入无集合字段，
        // 前序步骤输出均为单值且无 restraintLevel）；按 ST013 已定义的空集合遍历语义传入空集合，
        // 待来源确认后补充映射。
        ST013InputBO st013Input = new ST013InputBO();
        st013Input.setRestraintList(new ArrayList<>());
        ST013OutputBO st013Result = ist013.execute(st013Input);
        if (!st013Result.isSucceed()) {
            handleError(header, st013Result.getErrorCode(), st013Result.getErrorMessage());
            return output;
        }

        ST014InputBO st014Input = new ST014InputBO();
        st014Input.setBaseAcctNo(input.getBaseAcctNo());
        ST014OutputBO st014Result = ist014.execute(st014Input);
        if (!st014Result.isSucceed()) {
            handleError(header, st014Result.getErrorCode(), st014Result.getErrorMessage());
            return output;
        }

        ST015InputBO st015Input = new ST015InputBO();
        st015Input.setBaseAcctNo(input.getBaseAcctNo());
        ST015OutputBO st015Result = ist015.execute(st015Input);
        if (!st015Result.isSucceed()) {
            handleError(header, st015Result.getErrorCode(), st015Result.getErrorMessage());
            return output;
        }

        output.setNatureRestraintFlag(st015Result.getNatureRestraintFlag());
        output.setStopFlag(st009Result.getStopFlag());
        output.setNoDebitNoCreditFlag(st012Result.getNoDebitNoCreditFlag());
        output.setCashNonRcvPayFlag(st014Result.getCashNonRcvPayFlag());
        output.setTransferNoRecvNoPayFlag(st010Result.getTransferNoRecvNoPayFlag());
        output.setChannelCounterFlag(st002Result.getChannelCounterFlag());
        output.setBaseAcctNo(st005Result.getBaseAcctNo());
        output.setLeadAcctFlag(st005Result.getLeadAcctFlag());
        output.setResSeqNo(st015Result.getResSeqNo());
        output.setRestraintType(st015Result.getRestraintType() == null ? null : st015Result.getRestraintType().getValue());
        output.setRestraintsStatus(st015Result.getRestraintsStatus() == null ? null : st015Result.getRestraintsStatus().getValue());
        output.setRestraintLevel(st015Result.getRestraintLevel() == null ? null : st015Result.getRestraintLevel().getValue());
        output.setDrCrCtlFlag(st014Result.getDrCrCtlFlag() == null ? null : st014Result.getDrCrCtlFlag().getValue());
        output.setStatus(st014Result.getStatus() == null ? null : st014Result.getStatus().getValue());
        output.setTransferFlag(st010Result.getTransferFlag());
        output.setRestraintTypeStopFlag(st010Result.getStopFlag());
        output.setRestraintTypeDef(st008Result.getRestraintTypeDef() == null ? null : st008Result.getRestraintTypeDef().getValue());
        output.setCashFlag(st014Result.getCashFlag());
        output.setPledgedFlag(st007Result.getPledgedFlag());
        output.setDetailStatus(st002Result.getStatus() == null ? null : st002Result.getStatus().getValue());
        output.setProdNo(st002Result.getProdNo());
        output.setTranTypeLink(st002Result.getTranTypeLink());
        output.setChannelMuster(st002Result.getChannelMuster());
        output.setNarrativeCode(st002Result.getNarrativeCode());
        output.setResBranchRange(st002Result.getResBranchRange() == null ? null : st002Result.getResBranchRange().getValue());
        output.setDetailCounterFlag(st002Result.getDetailCounterFlag());

        header.setSucceed(true);
        header.setErrorCode(null);
        header.setErrorMessage(null);
        return output;
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
