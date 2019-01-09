package ch.ntb.inf.deep.dwarf.die;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

import ch.ntb.inf.deep.dwarf.Utils;
import ch.ntb.inf.deep.dwarf.location.DwarfExpression;

public class DWARF {

	public final ByteBuffer debug_info;
	public final ByteBuffer debug_abbrev;
	public final ByteBuffer debug_line;
	public final ByteBuffer debug_loc;

	// Holds Reference Pointers to updated later the Address after all DIE's are
	// serialized
	private Map<Integer, DebugInformationEntry> references;

	public DWARF(ByteOrder byteOrder, CompilationUnitDIE compilationUnit) {
		debug_info = ByteBuffer.allocate(0xFFFF);
		debug_info.order(byteOrder);
		debug_abbrev = ByteBuffer.allocate(0xFFFF);
		debug_abbrev.order(byteOrder);
		debug_line = ByteBuffer.allocate(0xFFFF);
		debug_line.order(byteOrder);
		debug_loc = ByteBuffer.allocate(0xFFFF);
		debug_loc.order(byteOrder);

		references = new HashMap<>();

		compilationUnit.serialize(this);
		updateMissingReferences();
	}

	public void addByte(DwAtType type, DwFormType form, byte value) {
		Utils.writeUnsignedLeb128(debug_abbrev, type.value());
		Utils.writeUnsignedLeb128(debug_abbrev, form.value());
		debug_info.put(value);
	}

	public void addInt(DwAtType type, DwFormType form, int value) {
		Utils.writeUnsignedLeb128(debug_abbrev, type.value());
		Utils.writeUnsignedLeb128(debug_abbrev, form.value());
		debug_info.putInt(value);
	}

	public void addLong(DwAtType type, DwFormType form, long value) {
		Utils.writeUnsignedLeb128(debug_abbrev, type.value());
		Utils.writeUnsignedLeb128(debug_abbrev, form.value());
		debug_info.putLong(value);
	}

	public void addShort(DwAtType type, DwFormType form, short value) {
		Utils.writeUnsignedLeb128(debug_abbrev, type.value());
		Utils.writeUnsignedLeb128(debug_abbrev, form.value());
		debug_info.putShort(value);
	}

	public void add(DwAtType type, String str) {
		Utils.writeUnsignedLeb128(debug_abbrev, type.value());
		Utils.writeUnsignedLeb128(debug_abbrev, DwFormType.DW_FORM_string.value());
		debug_info.put(Utils.serialize(str));
	}

	public void add(DwAtType type, DwarfExpression expr) {
		Utils.writeUnsignedLeb128(debug_abbrev, type.value());
		Utils.writeUnsignedLeb128(debug_abbrev, DwFormType.DW_FORM_exprloc.value());
		expr.serialize(debug_info);
	}

	public void addFlag(DwAtType type) {
		Utils.writeUnsignedLeb128(debug_abbrev, type.value());
		Utils.writeUnsignedLeb128(debug_abbrev, DwFormType.DW_FORM_flag.value());
		debug_info.put((byte) 1);
	}

	public void addReference(DwAtType type, DebugInformationEntry die) {
		if (die != null) {
			references.put(debug_info.position(), die);
			addInt(type, DwFormType.DW_FORM_ref4, -1); // Write a Dummy Value at this Index!
		}
	}

	public void add(TypeDIE die) {
		addReference(DwAtType.DW_AT_type, die);
	}

	public void updateMissingReferences() {
		for (Map.Entry<Integer, DebugInformationEntry> ref : references.entrySet()) {
			DebugInformationEntry die = ref.getValue();
			int position = ref.getKey();
			debug_info.putInt(position, die.baseAddress - die.getRoot().baseAddress);
		}
	}

	public void addDieEnd() {
		Utils.writeUnsignedLeb128(debug_abbrev, 0);
		Utils.writeUnsignedLeb128(debug_abbrev, 0);
	}
}
