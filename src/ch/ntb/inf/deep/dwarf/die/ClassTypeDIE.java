package ch.ntb.inf.deep.dwarf.die;

import ch.ntb.inf.deep.classItems.Class;
import ch.ntb.inf.deep.classItems.ConstField;
import ch.ntb.inf.deep.classItems.Field;
import ch.ntb.inf.deep.classItems.Method;
import ch.ntb.inf.deep.classItems.StdConstant;
import ch.ntb.inf.deep.classItems.StringLiteral;
import ch.ntb.inf.deep.classItems.Type;

public class ClassTypeDIE extends TypeDIE {

	private final String name;
	private byte byteSize;

	public ClassTypeDIE(Class clazz, DebugInformationEntry parent) {
		super(parent, DwTagType.DW_TAG_class_type);
		this.name = clazz.name.toString();
		clazz.dwarfDIE = new RefTypeDIE(clazz, parent, this);
	}

	public void InsertMembers(Class clazz) {
		System.out.println("Class: " + clazz.name);
		Field field = (Field) clazz.instFields;
		byteSize = 0;
		while (field != null && field != clazz.classFields) {
			// Instance Fields
			System.out.println("\tInstance Field: " + field.name + " offset: " + field.offset);
			new InstanceMemberDIE(field, this);
			byteSize += (byte)((Type)field.type).getTypeSize();
			field = (Field) field.next;
		}

		field = (Field) clazz.classFields;
		while (field != null && field != clazz.constFields) {
			// Static Fields
			System.out.println("\tStatic Field: " + field.name + " address: " + field.address);
			//TODO: Static Fields are not printed if i print a object. Not Member of Object!
			new ClassMemberDIE(field, this);
			field = (Field) field.next;
		}

		ConstField constant = (ConstField) clazz.constFields;
		while (constant != null) {
			System.out.println("\tConstant Field: " + field.name);
			if (constant.getConstantItem() instanceof StdConstant) {
				new ConstantDIE((StdConstant) constant.getConstantItem(), constant.name, this);
			} else if (constant.getConstantItem() instanceof StringLiteral) {
				new ClassMemberDIE(constant, constant.getConstantItem().address, this);
			}
			constant = (ConstField) constant.next;
		}
	}

	public void InsertMethods(Class clazz) {
		Method method = (Method) clazz.methods;
		while (method != null) {
			if (method.address != -1) {
				new SubProgramDIE(method, this);
			}
			method = (Method) method.next;
		}
	}

	@Override
	public void serializeDie(DWARF dwarf) {
		dwarf.add(DwAtType.DW_AT_name, name);
		dwarf.addByte(DwAtType.DW_AT_byte_size, DwFormType.DW_FORM_data1, byteSize);
	}
}
