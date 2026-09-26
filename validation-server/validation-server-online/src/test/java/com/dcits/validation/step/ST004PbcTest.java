package com.dcits.validation.step;

import com.dcits.validation.enums.DrCrCtlFlag;
import com.dcits.validation.enums.RestraintType;
import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.enums.Status;
import com.dcits.validation.facade.bo.ST004InputBO;
import com.dcits.validation.facade.bo.ST004OutputBO;
import com.dcits.validation.facade.components.IRbBusRestraintsBcc;
import com.dcits.validation.facade.components.IRbRestraintTypeBcc;
import com.dcits.validation.facade.eo.RbBusRestraintsEO;
import com.dcits.validation.facade.eo.RbRestraintTypeEO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.lenient;

/**
 * ST004 检查转账止收限制 单元测试
 */
@ExtendWith(MockitoExtension.class)
class ST004PbcTest {

	private static final String BASE_ACCT_NO = "1100100012345678";

	@Mock
	private IRbBusRestraintsBcc rbBusRestraintsBcc;

	@Mock
	private IRbRestraintTypeBcc rbRestraintTypeBcc;

	@InjectMocks
	private ST004Pbc st004Pbc;

	// 场景：账户存在一条生效限制记录，其限制类型的借贷方控制标志为 C-禁止贷方且转账标志为 N-不允许转账，命中转账止收限制，返回“是”并回显命中记录及限制类型信息（子步骤1→2→3 全路径）
	@Test
	void testST004T01() {
		List<RbBusRestraintsEO> restraints = Collections.singletonList(
				buildRestraint("2026092200000001", RestraintType.VALUE_92));
		final RbBusRestraintsEO[] capturedRequest = new RbBusRestraintsEO[1];
		lenient().when(rbBusRestraintsBcc.findByEo(argThat(eo -> eo != null
						&& BASE_ACCT_NO.equals(eo.getBaseAcctNo())
						&& RestraintsStatus.A == eo.getRestraintsStatus())))
				.thenAnswer(invocation -> {
					capturedRequest[0] = invocation.getArgument(0);
					return restraints;
				});
		lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_92))
				.thenReturn(buildRestraintType(RestraintType.VALUE_92, Status.A, DrCrCtlFlag.C, "N", "Y"));

		ST004OutputBO output = st004Pbc.execute(buildInput());

		assertTrue(output.isSucceed());
		assertNull(output.getErrorCode());
		assertNull(output.getErrorMessage());
		assertEquals("是", output.getTransferStopFlag());
		assertEquals("2026092200000001", output.getResSeqNo());
		assertEquals(RestraintType.VALUE_92, output.getRestraintType());
		assertEquals(RestraintsStatus.A, output.getRestraintsStatus());
		assertEquals(DrCrCtlFlag.C, output.getDrCrCtlFlag());
		assertEquals(Status.A, output.getStatus());
		assertEquals("N", output.getTransferFlag());
		assertEquals("Y", output.getStopFlag());
		// 核对子步骤1 查询条件映射：请求 EO 携带账号与限制状态“A-生效”
		assertNotNull(capturedRequest[0]);
		assertEquals(BASE_ACCT_NO, capturedRequest[0].getBaseAcctNo());
		assertEquals(RestraintsStatus.A, capturedRequest[0].getRestraintsStatus());
	}

	// 场景：账户存在两条生效限制记录，仅一条的限制类型满足 C-禁止贷方且 N-不允许转账，按“存在任一记录命中”返回“是”，输出取命中记录
	@Test
	void testST004T02() {
		stubFindByEo(Arrays.asList(
				buildRestraint("2026092200000001", RestraintType.VALUE_91),
				buildRestraint("2026092200000002", RestraintType.VALUE_92)));
		lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_91))
				.thenReturn(buildRestraintType(RestraintType.VALUE_91, Status.A, DrCrCtlFlag.D, "Y", "N"));
		lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_92))
				.thenReturn(buildRestraintType(RestraintType.VALUE_92, Status.A, DrCrCtlFlag.C, "N", "Y"));

		ST004OutputBO output = st004Pbc.execute(buildInput());

		assertTrue(output.isSucceed());
		assertNull(output.getErrorCode());
		assertNull(output.getErrorMessage());
		assertEquals("是", output.getTransferStopFlag());
		assertEquals("2026092200000002", output.getResSeqNo());
		assertEquals(RestraintType.VALUE_92, output.getRestraintType());
		assertEquals(RestraintsStatus.A, output.getRestraintsStatus());
		assertEquals(DrCrCtlFlag.C, output.getDrCrCtlFlag());
		assertEquals(Status.A, output.getStatus());
		assertEquals("N", output.getTransferFlag());
		assertEquals("Y", output.getStopFlag());
	}

	// 场景：账户两条生效限制记录均命中（限制编号 2026092200000002 与 2026092200000001，返回列表中大号在前），按“多条命中取限制编号最小”输出 2026092200000001 记录
	@Test
	void testST004T03() {
		stubFindByEo(Arrays.asList(
				buildRestraint("2026092200000002", RestraintType.VALUE_53),
				buildRestraint("2026092200000001", RestraintType.VALUE_92)));
		lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_53))
				.thenReturn(buildRestraintType(RestraintType.VALUE_53, Status.A, DrCrCtlFlag.C, "N", "Y"));
		lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_92))
				.thenReturn(buildRestraintType(RestraintType.VALUE_92, Status.A, DrCrCtlFlag.C, "N", "N"));

		ST004OutputBO output = st004Pbc.execute(buildInput());

		assertTrue(output.isSucceed());
		assertNull(output.getErrorCode());
		assertNull(output.getErrorMessage());
		assertEquals("是", output.getTransferStopFlag());
		assertEquals("2026092200000001", output.getResSeqNo());
		assertEquals(RestraintType.VALUE_92, output.getRestraintType());
		assertEquals(RestraintsStatus.A, output.getRestraintsStatus());
		assertEquals(DrCrCtlFlag.C, output.getDrCrCtlFlag());
		assertEquals(Status.A, output.getStatus());
		assertEquals("N", output.getTransferFlag());
		assertEquals("N", output.getStopFlag());
	}

	// 场景：账户限制信息查询返回空集合（账号下无生效限制记录），返回“否”且其余输出字段全部为空
	@Test
	void testST004T04() {
		stubFindByEo(Collections.emptyList());

		ST004OutputBO output = st004Pbc.execute(buildInput());

		assertTransferStopNotHit(output);
	}

	// 场景：限制类型的借贷方控制标志为 D-禁止借方（不等于 C-禁止贷方），即使转账标志为 N 也不命中，返回“否”且其余输出字段置空
	@Test
	void testST004T05() {
		stubFindByEo(Collections.singletonList(
				buildRestraint("2026092200000003", RestraintType.VALUE_91)));
		lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_91))
				.thenReturn(buildRestraintType(RestraintType.VALUE_91, Status.A, DrCrCtlFlag.D, "N", "N"));

		ST004OutputBO output = st004Pbc.execute(buildInput());

		assertTransferStopNotHit(output);
	}

	// 场景：限制类型的转账标志为 Y（不等于 N-不允许转账），即使借贷方控制标志为 C 也不命中，返回“否”且其余输出字段置空
	@Test
	void testST004T06() {
		stubFindByEo(Collections.singletonList(
				buildRestraint("2026092200000004", RestraintType.VALUE_92)));
		lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_92))
				.thenReturn(buildRestraintType(RestraintType.VALUE_92, Status.A, DrCrCtlFlag.C, "Y", "N"));

		ST004OutputBO output = st004Pbc.execute(buildInput());

		assertTransferStopNotHit(output);
	}

	// 场景：限制类型表记录状态为 F-无效（非 A-生效），其借贷方控制标志、转账标志不参与命中判断，返回“否”且其余输出字段置空
	@Test
	void testST004T07() {
		stubFindByEo(Collections.singletonList(
				buildRestraint("2026092200000005", RestraintType.VALUE_24)));
		lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_24))
				.thenReturn(buildRestraintType(RestraintType.VALUE_24, Status.F, DrCrCtlFlag.C, "N", "N"));

		ST004OutputBO output = st004Pbc.execute(buildInput());

		assertTransferStopNotHit(output);
	}

	// 场景：账户限制记录对应的限制类型在限制类型表中无记录（findByRestraintType 返回 null），该记录不命中，返回“否”且其余输出字段置空
	@Test
	void testST004T08() {
		stubFindByEo(Collections.singletonList(
				buildRestraint("2026092200000006", RestraintType.VALUE_35)));
		lenient().when(rbRestraintTypeBcc.findByRestraintType(RestraintType.VALUE_35))
				.thenReturn(null);

		ST004OutputBO output = st004Pbc.execute(buildInput());

		assertTransferStopNotHit(output);
	}

	/** 构造步骤唯一输入：账号 */
	private ST004InputBO buildInput() {
		ST004InputBO input = new ST004InputBO();
		input.setBaseAcctNo(BASE_ACCT_NO);
		return input;
	}

	/** 构造账户限制表记录：限制编号、账户限制类型，限制状态固定“A-生效”（与子步骤1 查询口径一致） */
	private RbBusRestraintsEO buildRestraint(String resSeqNo, RestraintType restraintType) {
		RbBusRestraintsEO eo = new RbBusRestraintsEO();
		eo.setResSeqNo(resSeqNo);
		eo.setRestraintType(restraintType);
		eo.setRestraintsStatus(RestraintsStatus.A);
		return eo;
	}

	/** 构造存款限制类型表记录 */
	private RbRestraintTypeEO buildRestraintType(RestraintType restraintType, Status status,
			DrCrCtlFlag drCrCtlFlag, String transferFlag, String stopFlag) {
		RbRestraintTypeEO eo = new RbRestraintTypeEO();
		eo.setRestraintType(restraintType);
		eo.setStatus(status);
		eo.setDrCrCtlFlag(drCrCtlFlag);
		eo.setTransferFlag(transferFlag);
		eo.setStopFlag(stopFlag);
		return eo;
	}

	/** 设桩子步骤1：按账号 + 限制状态“A-生效”查询账户限制信息 */
	private void stubFindByEo(List<RbBusRestraintsEO> result) {
		lenient().when(rbBusRestraintsBcc.findByEo(argThat(eo -> eo != null
						&& BASE_ACCT_NO.equals(eo.getBaseAcctNo())
						&& RestraintsStatus.A == eo.getRestraintsStatus())))
				.thenReturn(result);
	}

	/** 未命中断言：正常成功返回“否”，七个业务输出字段全部为 null */
	private void assertTransferStopNotHit(ST004OutputBO output) {
		assertTrue(output.isSucceed());
		assertNull(output.getErrorCode());
		assertNull(output.getErrorMessage());
		assertEquals("否", output.getTransferStopFlag());
		assertNull(output.getResSeqNo());
		assertNull(output.getRestraintType());
		assertNull(output.getRestraintsStatus());
		assertNull(output.getDrCrCtlFlag());
		assertNull(output.getStatus());
		assertNull(output.getTransferFlag());
		assertNull(output.getStopFlag());
	}
}
