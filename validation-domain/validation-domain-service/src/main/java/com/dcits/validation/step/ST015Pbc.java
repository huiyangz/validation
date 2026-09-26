package com.dcits.validation.step;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dcits.validation.enums.RestraintLevel;
import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.facade.bo.ST015InputBO;
import com.dcits.validation.facade.bo.ST015OutputBO;
import com.dcits.validation.facade.components.IRbBusRestraintsBcc;
import com.dcits.validation.facade.eo.RbBusRestraintsEO;

/**
 * ST015 检查是否存在属性限制 步骤实现。
 *
 * <p>依据 SPEC（docs/specs/ST015.md）：
 * 子步骤1 按账号查询对公存款账户限制表（RB_BUS_RESTRAINTS）中限制状态为 A-生效、
 * 限制级别为 NATURE-账户属性限制的账户限制信息；
 * 子步骤2 账户限制信息为空时属性限制标志返回"否"，非空时返回"是"，
 * 多条时取限制编号按数值比较最小的一条填充限制编号、账户限制类型、限制状态、限制级别。</p>
 *
 * <p>本步骤仅查询本地数据，无业务失败场景，失败仅由技术异常传播表达。</p>
 */
@Service
public class ST015Pbc implements IST015 {

	private final IRbBusRestraintsBcc rbBusRestraintsBcc;

	public ST015Pbc(IRbBusRestraintsBcc rbBusRestraintsBcc) {
		this.rbBusRestraintsBcc = rbBusRestraintsBcc;
	}

	@Override
	public ST015OutputBO execute(ST015InputBO input) {
		ST015OutputBO output = new ST015OutputBO();

		// 子步骤1：获取属性级限制——根据{账号}查询【对公存款账户限制表（RB_BUS_RESTRAINTS）】
		// 中$限制状态$等于"A-生效"且$限制级别$等于"NATURE-账户属性限制"的[账户限制信息]
		RbBusRestraintsEO queryEo = new RbBusRestraintsEO();
		queryEo.setBaseAcctNo(input.getBaseAcctNo());
		queryEo.setRestraintsStatus(RestraintsStatus.A);
		queryEo.setRestraintLevel(RestraintLevel.NATURE);
		List<RbBusRestraintsEO> restraints = rbBusRestraintsBcc.findByEo(queryEo);

		// 子步骤2：检查是否存在属性限制——账户限制信息等于空返回"否"，否则返回"是"
		// 并填充明细；多条时取$限制编号$按数值比较最小的一条
		if (restraints == null || restraints.isEmpty()) {
			output.setNatureRestraintFlag("否");
		} else {
			output.setNatureRestraintFlag("是");
			RbBusRestraintsEO min = selectMinByResSeqNo(restraints);
			output.setResSeqNo(min.getResSeqNo());
			output.setRestraintType(min.getRestraintType());
			output.setRestraintsStatus(min.getRestraintsStatus());
			output.setRestraintLevel(min.getRestraintLevel());
		}

		output.setSucceed(true);
		return output;
	}

	/**
	 * 子步骤2辅助：多条账户限制信息时，取$限制编号$按数值比较最小的一条。
	 *
	 * @param restraints 已过滤的账户限制信息，非空集合
	 * @return 限制编号数值最小的记录
	 */
	private RbBusRestraintsEO selectMinByResSeqNo(List<RbBusRestraintsEO> restraints) {
		RbBusRestraintsEO min = restraints.get(0);
		for (RbBusRestraintsEO eo : restraints) {
			if (new BigDecimal(eo.getResSeqNo()).compareTo(new BigDecimal(min.getResSeqNo())) < 0) {
				min = eo;
			}
		}
		return min;
	}
}
