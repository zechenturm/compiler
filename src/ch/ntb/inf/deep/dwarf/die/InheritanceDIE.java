package ch.ntb.inf.deep.dwarf.die;

import ch.ntb.inf.deep.classItems.Type;

public class InheritanceDIE extends DebugInformationEntry {

	private final Type baseType;

	protected InheritanceDIE(Type type, DebugInformationEntry parent) {
		super(parent, DwTagType.DW_TAG_inheritance);
		baseType = type;
	}

	private ClassTypeDIE getBaseClassDIE() {
		RefTypeDIE baseClassRefTypeDIE = (RefTypeDIE) baseType.dwarfDIE;
		return (ClassTypeDIE) baseClassRefTypeDIE.getBaseType();
	}

	@Override
	protected void serializeDie(DWARF dwarf) {
		dwarf.add(getBaseClassDIE());
	}

}
