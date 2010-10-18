package ch.ntb.inf.deep.cgPPC;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.*;

import ch.ntb.inf.deep.classItems.Class;
import ch.ntb.inf.deep.classItems.IClassFileConsts;
import ch.ntb.inf.deep.classItems.Type;

public class cgPPC07 extends TestCgPPC {

	@BeforeClass
	public static void setUp() {
		String[] rootClassNames = new String[] { "ch/ntb/inf/deep/testClasses/T07Arrays" };
		try {
			Class.buildSystem(rootClassNames, (1 << IClassFileConsts.atxCode)
					| (1 << IClassFileConsts.atxLocalVariableTable)
					| (1 << IClassFileConsts.atxLineNumberTable)
					| (1 << IClassFileConsts.atxExceptions));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (Type.nofRootClasses > 0) {
			createCgPPC(Type.rootClasses[0]);
		}
	}

//	@Ignore
	@Test
	public void emptyIntArray() {
		int[] code = getCode(1);
		int i = 0;
		assertEquals("wrong instruction", InstructionDecoder.getCode("stwu  r1, -32(r1)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("mfspr  r0, LR"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("stw  r0, 32(r1)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("stmw  r31, 20(r1)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("li  r2, 5"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("twi  ifless, r2, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lis  r3, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("addi  r3, r3, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("mtspr  LR, r3"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("li  r3, 10"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("bclr  always, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lr  r31, r2"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lmw  r31, 20(r1)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lwz  r0, 32(r1)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("mtspr  LR, r0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("addi  r1, r1, 32"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("bclr  always, 0"), code[i++]);
	}
	
//	@Ignore
	@Test
	public void intArray() {
		int[] code = getCode(2);
		int i = 0;
		assertEquals("wrong instruction", InstructionDecoder.getCode("stwu  r1, -32(r1)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("mfspr  r0, LR"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("stw  r0, 32(r1)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("stmw  r29, 20(r1)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lr  r31, r2"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("li  r2, 5"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("twi  ifless, r2, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lis  r3, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("addi  r3, r3, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("mtspr  LR, r3"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("li  r3, 10"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("bclr  always, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lr  r30, r2"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("li  r29, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("b  36"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("add  r2, r29, r31"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("twi  ifequal, r30, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lha  r3, 8(r30)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("tw  ifgeU, r29, r3"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("rlwinm  r3, r29, 2, 0, 29"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("addi  r4, r30, 12"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("stwx  r2, r3, r4"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("addi  r29, r29, 1"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("twi  ifequal, r30, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lha  r2, 8(r30)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("cmp  CRF0, 0, r29, r2"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("bc  iftrue, CRF0[LT], -44"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("li  r2, 2"), code[i++]);	
		assertEquals("wrong instruction", InstructionDecoder.getCode("twi  ifequal, r30, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lha  r3, 8(r30)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("tw  ifgeU, r2, r3"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("rlwinm  r3, r2, 2, 0, 29"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("addi  r4, r30, 12"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lwzx  r2, r3, r4"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lmw  r29, 20(r1)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lwz  r0, 32(r1)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("mtspr  LR, r0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("addi  r1, r1, 32"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("bclr  always, 0"), code[i++]);
	}
	
//	@Ignore
	@Test
	public void stringArray() {
		int[] code = getCode(3);
		int i = 0;
		assertEquals("wrong instruction", InstructionDecoder.getCode("stwu  r1, -32(r1)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("mfspr  r0, LR"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("stw  r0, 32(r1)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("stmw  r31, 20(r1)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("li  r2, 5"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("twi  ifless, r2, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lis  r3, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("addi  r3, r3, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("mtspr  LR, r3"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lis  r3, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("addi  r3, r3, 6"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("bclr  always, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lr  r31, r2"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("li  r2, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lis  r3, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("addi  r3, r3, 9"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("twi  ifequal, r31, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lha  r4, 8(r31)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("tw  ifgeU, r2, r4"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("rlwinm  r4, r2, 2, 0, 29"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("addi  r5, r31, 12"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("stwx  r3, r4, r5"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("li  r2, 1"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lis  r3, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("addi  r3, r3, 14"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("twi  ifequal, r31, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lha  r4, 8(r31)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("tw  ifgeU, r2, r4"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("rlwinm  r4, r2, 2, 0, 29"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("addi  r5, r31, 12"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("stwx  r3, r4, r5"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("li  r2, 2"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lis  r3, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("addi  r3, r3, 23"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("twi  ifequal, r31, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lha  r4, 8(r31)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("tw  ifgeU, r2, r4"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("rlwinm  r4, r2, 2, 0, 29"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("addi  r5, r31, 12"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("stwx  r3, r4, r5"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("li  r2, 3"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lis  r3, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("addi  r3, r3, 32"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("twi  ifequal, r31, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lha  r4, 8(r31)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("tw  ifgeU, r2, r4"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("rlwinm  r4, r2, 2, 0, 29"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("addi  r5, r31, 12"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("stwx  r3, r4, r5"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("li  r2, 4"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lis  r3, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("addi  r3, r3, 41"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("twi  ifequal, r31, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lha  r4, 8(r31)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("tw  ifgeU, r2, r4"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("rlwinm  r4, r2, 2, 0, 29"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("addi  r5, r31, 12"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("stwx  r3, r4, r5"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("li  r2, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("twi  ifequal, r31, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lha  r3, 8(r31)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("tw  ifgeU, r2, r3"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("rlwinm  r3, r2, 2, 0, 29"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("addi  r4, r31, 12"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lwzx  r2, r3, r4"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lmw  r31, 20(r1)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lwz  r0, 32(r1)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("mtspr  LR, r0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("addi  r1, r1, 32"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("bclr  always, 0"), code[i++]);
	}
	
	@Test
	public void objectArray() {
		int[] code = getCode(4);
		int i = 0;
		assertEquals("wrong instruction", InstructionDecoder.getCode("stwu  r1, -32(r1)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("mfspr  r0, LR"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("stw  r0, 32(r1)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("stmw  r31, 20(r1)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("li  r2, 3"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("twi  ifless, r2, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lis  r3, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("addi  r3, r3, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("mtspr  LR, r3"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lis  r3, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("addi  r3, r3, 6"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("bclr  always, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lr  r31, r2"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("li  r2, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lis  r2, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("addi  r2, r2, 9"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("mtspr  LR, r2"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lis  r2, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("addi  r2, r2, 14"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("bclr  always, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lr  r3, r2"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("twi  ifequal, r3, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lwz  R4, -4(r3)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lwz  R4, -24(r4)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("mtspr LR, r4"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lr  r2, r3"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("bclr  always, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("twi  ifequal, r31, 0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lha  r4, 8(r31)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("tw  ifgeU, r2, r4"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("rlwinm  r4, r2, 2, 0, 29"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("addi  r5, r31, 12"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("stwx  r3, r4, r5"), code[i++]);	
		assertEquals("wrong instruction", InstructionDecoder.getCode("lmw  r31, 20(r1)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("lwz  r0, 32(r1)"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("mtspr  LR, r0"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("addi  r1, r1, 32"), code[i++]);
		assertEquals("wrong instruction", InstructionDecoder.getCode("bclr  always, 0"), code[i++]);
	}

}