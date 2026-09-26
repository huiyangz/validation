package com.dcits.validation.step;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.lenient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dcits.validation.enums.RestraintLevel;
import com.dcits.validation.enums.RestraintsStatus;
import com.dcits.validation.enums.RestraintType;
import com.dcits.validation.facade.bo.ST015InputBO;
import com.dcits.validation.facade.bo.ST015OutputBO;
import com.dcits.validation.facade.components.IRbBusRestraintsBcc;
import com.dcits.validation.facade.eo.RbBusRestraintsEO;

/**
 * ST015 检查是否存在属性限制 单元测试。
 */
@ExtendWith(MockitoExtension.class)
public class ST015PbcTest {

	@Mock
	private IRbBusRestraintsBcc rbBusRestraintsBcc;

	@InjectMocks
	private ST015Pbc st015Pbc;

	/**
	 * 构造一条已过滤（限制状态 A-生效、限制级别 NATURE-账户属性限制）的账户限制信息。
	 */
	private RbBusRestraintsEO buildRestraint(String resSeqNo, RestraintType restraintType) {
		RbBusRestraintsEO eo = new RbBusRestraintsEO();
		eo.setBaseAcctNo("9000010001");
		eo.setResSeqNo(resSeqNo);
		eo.setRestraintType(restraintType);
		eo.setRestraintsStatus(RestraintsStatus.A);
		eo.setRestraintLevel(RestraintLevel.NATURE);
		return eo;
	}

	private ST015InputBO buildInput() {
		ST015InputBO input = new ST015InputBO();
		input.setBaseAcctNo("9000010001");
		return input;
	}

	// 场景：账户无生效的属性级限制，查询返回空集合；预期 succeed=true，属性限制标志为"否"，四个明细字段为 null
	@Test
	public void testST015T01() {
		lenient().when(rbBusRestraintsBcc.findByEo(any(RbBusRestraintsEO.class)))
				.thenReturn(Collections.emptyList());

		ST015OutputBO output = st015Pbc.execute(buildInput());

		assertTrue(output.isSucceed());
		assertNull(output.getErrorCode());
		assertNull(output.getErrorMessage());
		assertEquals("否", output.getNatureRestraintFlag());
		assertNull(output.getResSeqNo());
		assertNull(output.getRestraintType());
		assertNull(output.getRestraintsStatus());
		assertNull(output.getRestraintLevel());
	}

	// 场景：存在一条生效属性限制；预期 succeed=true，标志为"是"，明细字段等于该条记录，同时经 argThat 核对请求 baseAcctNo 映射
	@Test
	public void testST015T02() {
		RbBusRestraintsEO record = buildRestraint("100", RestraintType.VALUE_6);
		lenient().when(rbBusRestraintsBcc.findByEo(
						argThat(eo -> eo != null && "9000010001".equals(eo.getBaseAcctNo()))))
				.thenReturn(Collections.singletonList(record));

		ST015OutputBO output = st015Pbc.execute(buildInput());

		assertTrue(output.isSucceed());
		assertNull(output.getErrorCode());
		assertNull(output.getErrorMessage());
		assertEquals("是", output.getNatureRestraintFlag());
		assertEquals("100", output.getResSeqNo());
		assertEquals(RestraintType.VALUE_6, output.getRestraintType());
		assertEquals(RestraintsStatus.A, output.getRestraintsStatus());
		assertEquals(RestraintLevel.NATURE, output.getRestraintLevel());
	}

	// 场景：存在多条生效属性限制（限制编号"300"/"21"/"100"）；预期按数值比较取最小的"21"一条填充，而非字典序最小的"100"
	@Test
	public void testST015T03() {
		List<RbBusRestraintsEO> records = Arrays.asList(
				buildRestraint("300", RestraintType.VALUE_13),
				buildRestraint("21", RestraintType.VALUE_7),
				buildRestraint("100", RestraintType.VALUE_6));
		lenient().when(rbBusRestraintsBcc.findByEo(any(RbBusRestraintsEO.class)))
				.thenReturn(records);

		ST015OutputBO output = st015Pbc.execute(buildInput());

		assertTrue(output.isSucceed());
		assertNull(output.getErrorCode());
		assertNull(output.getErrorMessage());
		assertEquals("是", output.getNatureRestraintFlag());
		assertEquals("21", output.getResSeqNo());
		assertEquals(RestraintType.VALUE_7, output.getRestraintType());
		assertEquals(RestraintsStatus.A, output.getRestraintsStatus());
		assertEquals(RestraintLevel.NATURE, output.getRestraintLevel());
	}

	// 场景：查询返回 null（"账户限制信息等于空"涵盖的空值形态）；预期同空集合，标志为"否"，明细字段为 null
	@Test
	public void testST015T04() {
		lenient().when(rbBusRestraintsBcc.findByEo(any(RbBusRestraintsEO.class)))
				.thenReturn(null);

		ST015OutputBO output = st015Pbc.execute(buildInput());

		assertTrue(output.isSucceed());
		assertNull(output.getErrorCode());
		assertNull(output.getErrorMessage());
		assertEquals("否", output.getNatureRestraintFlag());
		assertNull(output.getResSeqNo());
		assertNull(output.getRestraintType());
		assertNull(output.getRestraintsStatus());
		assertNull(output.getRestraintLevel());
	}
}
