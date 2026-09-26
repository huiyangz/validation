package com.dcits.validation.step;

import com.dcits.validation.enums.DrCrCtlFlag;
import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.enums.Status;
import com.dcits.validation.facade.bo.ST004InputBO;
import com.dcits.validation.facade.bo.ST004OutputBO;
import com.dcits.validation.facade.components.IRbBusRestraintsBcc;
import com.dcits.validation.facade.components.IRbRestraintTypeBcc;
import com.dcits.validation.facade.eo.RbBusRestraintsEO;
import com.dcits.validation.facade.eo.RbRestraintTypeEO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ST004 检查转账止收限制
 */
@Service
public class ST004Pbc implements IST004 {

	/** 转账止收标志：是 */
	private static final String TRANSFER_STOP_FLAG_YES = "是";
	/** 转账止收标志：否 */
	private static final String TRANSFER_STOP_FLAG_NO = "否";
	/** 转账标志：N-不允许转账 */
	private static final String TRANSFER_FLAG_NOT_ALLOWED = "N";

	private final IRbBusRestraintsBcc rbBusRestraintsBcc;
	private final IRbRestraintTypeBcc rbRestraintTypeBcc;

	public ST004Pbc(IRbBusRestraintsBcc rbBusRestraintsBcc, IRbRestraintTypeBcc rbRestraintTypeBcc) {
		this.rbBusRestraintsBcc = rbBusRestraintsBcc;
		this.rbRestraintTypeBcc = rbRestraintTypeBcc;
	}

	@Override
	public ST004OutputBO execute(ST004InputBO input) {
		ST004OutputBO output = new ST004OutputBO();

		// 子步骤1 获取账户限制信息：按账号 + 限制状态“A-生效”查询，结果可为空集
		List<RbBusRestraintsEO> restraints = fetchRestraints(input.getBaseAcctNo());

		// 子步骤2 获取账户限制类型信息 + 子步骤3 检查转账止收限制：
		// 逐条查询限制类型，仅状态“A-生效”的记录参与命中判断；
		// 命中条件为借贷方控制标志“C-禁止贷方”且转账标志“N-不允许转账”，多条命中时取限制编号最小的一条
		RbBusRestraintsEO hitRestraint = null;
		RbRestraintTypeEO hitType = null;
		if (restraints != null) {
			for (RbBusRestraintsEO restraint : restraints) {
				RbRestraintTypeEO type = rbRestraintTypeBcc.findByRestraintType(restraint.getRestraintType());
				if (!isTransferStopHit(type)) {
					continue;
				}
				if (hitRestraint == null || restraint.getResSeqNo().compareTo(hitRestraint.getResSeqNo()) < 0) {
					hitRestraint = restraint;
					hitType = type;
				}
			}
		}

		if (hitRestraint != null) {
			// 存在命中记录：返回“是”，其余输出字段取命中记录及其限制类型对应信息
			output.setTransferStopFlag(TRANSFER_STOP_FLAG_YES);
			output.setResSeqNo(hitRestraint.getResSeqNo());
			output.setRestraintType(hitRestraint.getRestraintType());
			output.setRestraintsStatus(hitRestraint.getRestraintsStatus());
			output.setDrCrCtlFlag(hitType.getDrCrCtlFlag());
			output.setStatus(hitType.getStatus());
			output.setTransferFlag(hitType.getTransferFlag());
			output.setStopFlag(hitType.getStopFlag());
		} else {
			// 账户限制信息为空集或均不满足：返回“否”，其余输出字段置空
			output.setTransferStopFlag(TRANSFER_STOP_FLAG_NO);
		}

		output.setSucceed(true);
		return output;
	}

	/**
	 * 子步骤1 获取账户限制信息：按{账号}、限制状态“A-生效”查询【账户限制信息】
	 */
	private List<RbBusRestraintsEO> fetchRestraints(String baseAcctNo) {
		RbBusRestraintsEO condition = new RbBusRestraintsEO();
		condition.setBaseAcctNo(baseAcctNo);
		condition.setRestraintsStatus(RestraintsStatus.A);
		return rbBusRestraintsBcc.findByEo(condition);
	}

	/**
	 * 子步骤3 命中条件：限制类型状态为“A-生效”，且借贷方控制标志为“C-禁止贷方”、转账标志为“N-不允许转账”；
	 * 限制类型表无对应记录时不命中
	 */
	private boolean isTransferStopHit(RbRestraintTypeEO type) {
		return type != null
				&& Status.A == type.getStatus()
				&& DrCrCtlFlag.C == type.getDrCrCtlFlag()
				&& TRANSFER_FLAG_NOT_ALLOWED.equals(type.getTransferFlag());
	}
}
