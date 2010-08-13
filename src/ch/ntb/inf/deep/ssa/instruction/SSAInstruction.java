package ch.ntb.inf.deep.ssa.instruction;

import ch.ntb.inf.deep.ssa.SSAInstructionMnemonics;
import ch.ntb.inf.deep.ssa.SSAValue;

/**
 * @author  millischer
 */
public abstract class SSAInstruction implements SSAInstructionMnemonics {
	protected SSAValue[] operands;
	public SSAValue result;
	public int ssaOpcode;
	
	public abstract void setOperands(SSAValue[] operands);
	public abstract SSAValue[] getOperands();
	public abstract void print(int level);
	
}
