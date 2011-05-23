package ch.ntb.inf.deep.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.runtime.Path;

import ch.ntb.inf.deep.classItems.ICclassFileConsts;
import ch.ntb.inf.deep.host.ErrorReporter;
import ch.ntb.inf.deep.host.StdStreams;
import ch.ntb.inf.deep.strings.HString;

public class Configuration implements ErrorCodes, IAttributes, ICclassFileConsts {
	private static Project project;
	private static SystemConstants sysConst = SystemConstants.getInstance();
	private static Consts consts = Consts.getInstance();
	private static MemoryMap memoryMap = MemoryMap.getInstance();
	private static RegisterMap registerMap = RegisterMap.getInstance();
	private static RegisterInit regInit;	
	private static TargetConfiguration targetConfig;
	private static TargetConfiguration activeTarConf;
	private static OperatingSystem os;
	private static String location;
	// private static Class heap;
	private static final int maxNumbersOfHeaps = 4;
	private static final int maxNumbersOfStacks = 4;
	private static final int defaultLength = 32;
	private static int nofHeapSegments = 0;
	private static int nofStackSegments = 0;
	private static Segment[] heaps = new Segment[maxNumbersOfHeaps];
	private static Segment[] stacks = new Segment[maxNumbersOfStacks];
	private static Segment[] segs;
	private static int segsCount = 0;

	/**
	 * Returns the first Segment which contains the code for the given
	 * classname. If no such segment exists the method returns null.
	 * 
	 * @param clazz
	 *            the name of the desired Class
	 * @return a Segment or null
	 */
	public static Segment getCodeSegmentOf(HString clazz) {
		return getSegmentOf(clazz, HString.getHString("code"));
	}

	/**
	 * Returns the first Segment which contains the constants for the given
	 * classname. If no such segment exists the method returns null.
	 * 
	 * @param clazz
	 *            the name of the desired Class
	 * @return a Segment or null
	 */
	public static Segment getConstSegmentOf(HString clazz) {
		return getSegmentOf(clazz, HString.getHString("const"));
	}

	/**
	 * Returns the first Segment which contains the variables for the given
	 * classname. If no such segment exists the method returns null.
	 * 
	 * @param clazz
	 *            the name of the desired Class
	 * @return a Segment or null
	 */
	public static Segment getVarSegmentOf(HString clazz) {
		return getSegmentOf(clazz, HString.getHString("var"));
	}

	public static Segment getDefaultConstSegment(){
		Segment seg;
		SegmentAssignment segAss = activeTarConf.getModuleByName(HString.getHString("default")).getSegmentAssignments();
		while (segAss != null) {
			if (segAss.contentAttribute.equals(HString.getHString("const"))) {
				String segDesignator = segAss.segmentDesignator.toString();
				int index = segDesignator.indexOf('.');
				// Determine Device name
				HString name = HString.getHString(segDesignator.substring(0,
						index));
				segDesignator = segDesignator.substring(index + 1);
				Device dev = memoryMap.getDeviceByName(name);
				if (dev == null) {
					ErrorReporter.reporter.error(errNoSuchDevice, "Device: "
							+ name.toString() + "with segment for "
							+ "const not found\n");
					return null;
				}
				index = segDesignator.indexOf('.');
				if (index == -1) {
					return dev.getSegementByName(HString
							.getHString(segDesignator));
				}
				name = HString.getHString(segDesignator.substring(0, index));
				segDesignator = segDesignator.substring(index + 1);
				seg = dev.getSegementByName(name);
				index = segDesignator.indexOf('.');
				while (index != -1) {
					name = HString
							.getHString(segDesignator.substring(0, index));
					segDesignator = segDesignator.substring(index + 1);
					seg = seg.getSubSegmentByName(name);
					index = segDesignator.indexOf('.');
				}
				return seg.getSubSegmentByName(HString
						.getHString(segDesignator));
			}
			segAss = segAss.next;
		}

		// default segment for const not set
		return null;
	}
	private static Segment getSegmentOf(HString clazz, HString contentAttribute) {
		Segment seg;
		SegmentAssignment segAss = null;

		// first check if clazz is a system class
		if (os.getKernel().name.equals(clazz)) {
			segAss = activeTarConf.getModuleByName(HString.getHString("kernel")).getSegmentAssignments();
		} else if (os.getHeap().name.equals(clazz)) {
			segAss = activeTarConf.getModuleByName(HString.getHString("heap")).getSegmentAssignments();
		} else if (os.getExceptionBaseClass().name.equals(clazz)) {
			segAss = activeTarConf.getModuleByName(	HString.getHString("exception")).getSegmentAssignments();
		} else {
			SystemClass tempCls = os.getExceptions();
			while (tempCls != null && (tempCls.attributes & (1 << dpfExcHnd)) != 0  && (tempCls != os.getExceptionBaseClass())) {
				if (tempCls.name.equals(clazz.toString())) {
					segAss = activeTarConf.getModuleByName(	HString.getHString("exception")).getSegmentAssignments();
					break;
				}
				tempCls = tempCls.next;
			}
			if (segAss == null) {
				// Class is not a system class

				Module mod = activeTarConf.getModuleByName(clazz);
				if (mod == null) {
					mod = memoryMap.getModuleByName(clazz);
				}
				if (mod != null) {
					segAss = mod.getSegmentAssignments();
				} else {
					// module for Class not found load default
					segAss = activeTarConf.getModuleByName(
							HString.getHString("default"))
							.getSegmentAssignments();
				}
			}
		}
		while (segAss != null) {
			if (segAss.contentAttribute.equals(contentAttribute)) {
				String segDesignator = segAss.segmentDesignator.toString();
				int index = segDesignator.indexOf('.');
				// Determine Device name
				HString name = HString.getHString(segDesignator.substring(0,
						index));
				segDesignator = segDesignator.substring(index + 1);
				Device dev = memoryMap.getDeviceByName(name);
				if (dev == null) {
					ErrorReporter.reporter.error(errNoSuchDevice, "Device: "
							+ name.toString() + "with segment for "
							+ contentAttribute.toString() + " not found");
					ErrorReporter.reporter.println();
					return null;
				}
				index = segDesignator.indexOf('.');
				if (index == -1) {
					return dev.getSegementByName(HString
							.getHString(segDesignator));
				}
				name = HString.getHString(segDesignator.substring(0, index));
				segDesignator = segDesignator.substring(index + 1);
				seg = dev.getSegementByName(name);
				index = segDesignator.indexOf('.');
				while (index != -1) {
					name = HString
							.getHString(segDesignator.substring(0, index));
					segDesignator = segDesignator.substring(index + 1);
					seg = seg.getSubSegmentByName(name);
					index = segDesignator.indexOf('.');
				}
				return seg.getSubSegmentByName(HString
						.getHString(segDesignator));
			}
			segAss = segAss.next;
		}

		// segment for contentattribute not set
		return null;
	}

	public static Device getFirstDevice() {
		return memoryMap.getDevices();
	}

	public static String[] getSearchPaths() {
		int count = 0;
		HString libPaths = project.getLibPaths();
		HString current = libPaths;
		// count
		while (current != null) {
			count++;
			current = current.next;
		}

		String[] paths = new String[count + 1];
		paths[0] = location + "bin/";
		for (int i = 1; i <= count; i++) {
			paths[i] = libPaths.toString() + "bin/";
			libPaths = libPaths.next;
		}
		return paths;
	}

	/**
	 * Sets the projectblock of this configuration. If a Project is already set
	 * it will be overwritten.
	 * 
	 * @param project
	 */
	public static void setProject(Project project) {
		Configuration.project = project;

	}

	/**
	 * @return the project which was set or null.
	 */
	public static Project getProject() {
		return Configuration.project;
	}

	/**
	 * @return the number of defined stacks.
	 */
	public static int getNumberOfStacks() {
		return nofStackSegments;
	}

	/**
	 * @return the number of defined heaps.
	 */
	public static int getNumberOfHeaps() {
		return nofHeapSegments;
	}

	public static HString getHeapClassname() {
		return HString.getHString(os.getHeap().name);
	}

	public static HString getKernelClassname() {
		if(os == null){
			return null;
		}
		return HString.getHString(os.getKernel().name);
	}

	public static HString getExceptionClassname() {
		return HString.getHString(os.getExceptionBaseClass().name);
	}

	// public static Class getReferenceToHeapClass() {
	// if (heap == null) {
	// HString str = os.getHeap();
	// int heapHash = str.hashCode();
	// Class current = Class.classList;
	// while (current != null) {
	// if (current.name.hashCode() == heapHash) {
	// if (current.name.equals(str)) {
	// heap = current;
	// break;
	// }
	// }
	// current = (Class) current.next;
	// }
	// }
	// return heap;
	// }

	public static void setOperatingSystem(OperatingSystem os) {
		Configuration.os = os;
	}

	public static OperatingSystem getOperatingSystem() {
		return os;
	}

	public static void addTargetConfiguration(TargetConfiguration targetConf) {
		if (Configuration.targetConfig == null) {
			targetConfig = targetConf;
			return;
		}
		TargetConfiguration current = targetConfig;
		TargetConfiguration prev = null;
		int nameHash = targetConf.name.hashCode();
		while (current != null) {
			if (current.name.hashCode() == nameHash) {
				if (current.name.equals(targetConf.name)) {
					targetConf.next = current.next;
					if (prev != null) {
						prev.next = targetConf;
					} else {
						targetConfig = targetConf;
					}
					return;
				}
			}
			prev = current;
			current = current.next;
		}
		// if no match prev shows the tail of the list
		prev.next = targetConf;
	}

	public static TargetConfiguration getTargetConfigurations() {
		return targetConfig;
	}

	public static void setRegInit(HString name, int initValue) {
		Register reg = registerMap.getRegister(name);
		if(regInit == null){
			regInit = new RegisterInit(reg, initValue);
			return;
		}
		//check if register initialization already exist
		RegisterInit current = regInit;
		RegisterInit prev = null;
		while(current != null){
			if(reg == current.register){
				current.initValue = initValue;
				return;
			}
			prev = current;
			current = current.next;
		}
		//if no initialization exist, so append new one
		// if no match prev shows the tail of the list
		prev.next = new RegisterInit(reg, initValue);
	}

	public static RegisterInit[] getInitializedRegisters() {
		RegisterInit[] reg = new RegisterInit[2];
		reg[0] = regInit; //Global scope
		reg[1] = activeTarConf.getRegInit(); //active target config scope
		return reg;
	}

	public static ValueAssignment getConstant() {
		if (sysConst == null) {
			return null;
		}
		return sysConst.getSysConst();
	}

	public static int getSysConstValue(HString name) {
		if (consts == null) {
			return Integer.MAX_VALUE;
		}
		return sysConst.getConstByName(name);
	}

	public static int getValueFor(HString constName) {
		int res = consts.getConstByName(constName);
		if (res == Integer.MAX_VALUE) {
			res = sysConst.getConstByName(constName);
		}
		if (res == Integer.MAX_VALUE) {
			ErrorReporter.reporter.error(errUndefinedConst, constName
					.toString()
					+ " is not defined\n");
			Parser.incrementErrors();
		}
		return res;
	}

	public static void print() {
		StdStreams.vrb.println("configuration {");
		if (project != null) {
			project.println(1);
		}
		sysConst.println(1);
		consts.println(1);
		TargetConfiguration tarConf = targetConfig;
		while (tarConf != null) {
			tarConf.print(1);
			tarConf = tarConf.next;
		}
		if (os != null) {
			os.println(1);
		}
		registerMap.println(1);
		if (regInit != null) {
			StdStreams.vrb.println("  reginit{");
			RegisterInit initReg = regInit;
			while (initReg != null) {
				for (int i = 2; i > 0; i--) {
					StdStreams.vrb.print("  ");
				}
				StdStreams.vrb.println(initReg.register.getName().toString() + String.format(" = 0x%X", initReg.initValue));
				initReg = initReg.next;
			}
			StdStreams.vrb.println("  }");
		}
		memoryMap.println(1);
	}

	public static void createInterfaceFile(HString fileToCreate) {
		int indexOf;
		String pack;
		String className;
		try {
			indexOf = fileToCreate.lastIndexOf(Path.SEPARATOR);
			if (indexOf != -1) {
				className = fileToCreate.substring(indexOf + 1).toString();
				pack = fileToCreate.substring(0, indexOf).toString();
			} else {
				className = fileToCreate.toString();
				pack = "";
			}
			// check if path exists
			File f = new File(fileToCreate.substring(0, indexOf).toString());
			if (!f.exists()) {
				f.mkdirs();
			}
			FileWriter fw = new FileWriter(fileToCreate.toString());
			indexOf = pack.lastIndexOf(Path.SEPARATOR);
			if (indexOf != -1) {
				pack = pack.substring(indexOf + 1) + ";";
			}
			pack = pack.replace(Path.SEPARATOR, '.');
			fw.write("package ch.ntb.inf.deep.runtime." + pack + "\n\n");
			indexOf = className.indexOf(".");
			fw.write("public interface " + className.substring(0, indexOf)
					+ "{\n");
			fw.write("\t//Systemconstants\n");
			ValueAssignment current = sysConst.getSysConst();
			while (current != null) {
				fw.write("\tpublic static final int "
						+ current.getName().toString() + " = 0x"
						+ Integer.toHexString(current.getValue()) + ";\n");
				current = current.next;
			}
			fw.write("\n\t//Registermap GPR\n");
			Register reg = registerMap.gpr;
			while (reg != null) {
				fw.write("\tpublic static final int " + reg.getName() + " = 0x"
						+ Integer.toHexString(reg.addr) + ";\n");
				reg = reg.next;
			}
			fw.write("\n\t//Registermap FPR\n");
			reg = registerMap.fpr;
			while (reg != null) {
				fw.write("\tpublic static final int " + reg.getName() + " = 0x"
						+ Integer.toHexString(reg.addr) + ";\n");
				reg = reg.next;
			}
			fw.write("\n\t//Registermap SPR\n");
			reg = registerMap.spr;
			while (reg != null) {
				fw.write("\tpublic static final int " + reg.getName() + " = 0x"
						+ Integer.toHexString(reg.addr) + ";\n");
				reg = reg.next;
			}
			fw.write("\n\t//Registermap IOR\n");
			reg = registerMap.ior;
			while (reg != null) {
				fw.write("\tpublic static final int " + reg.getName() + " = 0x"
						+ Integer.toHexString(reg.addr) + ";\n");
				reg = reg.next;
			}
			fw.write("}");
			fw.flush();
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setHeapSegmentRef(Segment heapSegment) {
		if (nofHeapSegments >= maxNumbersOfHeaps) {
			ErrorReporter.reporter.error(errMaxNofReached,
					"Max number of heap segments(" + maxNumbersOfHeaps
							+ ") is reached\n");
			Parser.incrementErrors();
			return;
		}
		heaps[nofHeapSegments] = heapSegment;
		nofHeapSegments++;
	}

	public static void setStackSegmentRef(Segment stackSegment) {
		if (nofStackSegments >= maxNumbersOfStacks) {
			ErrorReporter.reporter.error(errMaxNofReached,
					"Max number of stck segments(" + maxNumbersOfStacks
							+ ") is reached\n");
			Parser.incrementErrors();
			return;
		}
		stacks[nofStackSegments] = stackSegment;
		nofStackSegments++;
	}

	public static Segment[] getHeapSegments() {
		Segment[] res = new Segment[nofHeapSegments];
		for (int i = 0; i < nofHeapSegments; i++) {
			res[i] = heaps[i];
		}
		return res;
	}

	public static Segment[] getStackSegments() {
		Segment[] res = new Segment[nofStackSegments];
		for (int i = 0; i < nofStackSegments; i++) {
			res[i] = stacks[i];
		}
		return res;
	}

	public static Segment[] getSysTabSegments() {
		segsCount = 0;// reset if it was used befor
		segs = new Segment[defaultLength];
		
		Module sysMod = activeTarConf.getSystemModules();
		while(sysMod != null){
			SegmentAssignment segAss = null;
			if(sysMod.name.equals(HString.getHString("systemtable"))){
				segAss = sysMod.getSegmentAssignments();
			}
			//search all systab segments from active target config
			while (segAss != null) {
				if (segAss.contentAttribute.equals(HString.getHString("systab"))) {
					String segDesignator = segAss.segmentDesignator.toString();
					int index = segDesignator.indexOf('.');
					// Determine Device name
					HString name = HString.getHString(segDesignator.substring(0, index));
					segDesignator = segDesignator.substring(index + 1);
					Device dev = memoryMap.getDeviceByName(name);
					if (dev == null) {
						ErrorReporter.reporter.error(errNoSuchDevice, "Device: " + name.toString() + "with segment for systab not found");
						ErrorReporter.reporter.println();
						return null;
					}
					index = segDesignator.indexOf('.');
					Segment seg;
					if (index == -1) {
						seg = dev.getSegementByName(HString.getHString(segDesignator));
						if(seg != null){
							noticeSegment(seg);
						}else{
							ErrorReporter.reporter.error(errInvalideParameter, "Segment: " + segDesignator.toString() + "with attribute systab not found");
							ErrorReporter.reporter.println();
						}
						
					}else{
						name = HString.getHString(segDesignator.substring(0, index));
						segDesignator = segDesignator.substring(index + 1);
						seg = dev.getSegementByName(name);
						index = segDesignator.indexOf('.');
						while (index != -1) {
							name = HString.getHString(segDesignator.substring(0, index));
							segDesignator = segDesignator.substring(index + 1);
							seg = seg.getSubSegmentByName(name);
							index = segDesignator.indexOf('.');
						}
						if(seg != null){
							noticeSegment(seg);
						}else{
							ErrorReporter.reporter.error(errInvalideParameter, "Segment: " + segDesignator.toString() + "with attribute systab not found");
							ErrorReporter.reporter.println();
						}
					}
				}
				segAss = segAss.next;
			}
			sysMod = sysMod.next;
		}

		Segment[] sysTabSegs = new Segment[segsCount];
		for (int i = 0; i < segsCount; i++) {
			sysTabSegs[i] = segs[i];
		}
		return sysTabSegs;
	}
	
//	private static void collectSegmentsForAttributes(int attributes) {
//		segs = new Segment[defaultLength];
//		Device currDev = memoryMap.getDevices();
//		while (currDev != null) {
//			Segment currSeg = currDev.segments;
//			if (currSeg != null) {
//				findSegment(currSeg, attributes);
//			}
//			currDev = currDev.next;
//		}
//	}
//
//	private static void findSegment(Segment s, int attributes) {
//		// descend
//		if (s.subSegments != null)
//			findSegment(s.subSegments, attributes);
//		// traverse from left to right
//		if (s.next != null)
//			findSegment(s.next, attributes);
//		if ((s.getAttributes() & (1 << atrSysTab)) != 0) {
//			noticeSegment(s);
//		}
//	}

	private static void noticeSegment(Segment s) {
		if (s == null)
			return;
		if (segsCount >= segs.length) {
			Segment[] temp = new Segment[segs.length * 2];
			for (int i = 0; i < segs.length; i++) {
				temp[i] = segs[i];
			}
			segs = temp;
		}
		segs[segsCount++] = s;
	}

	public static RegisterMap getRegisterMap() {
		return registerMap;
	}
	
	public static int getSystemMethodIdOf(String name){
		int hash = name.hashCode();
		SystemClass clazz = os.getClassList();
		while(clazz != null){
			SystemMethod meth = clazz.methods;
			while(meth != null){
				if(hash == meth.name.hashCode()){
					if(name.equals(meth.name)){
						return 0xFFF & meth.attributes;
					}
				}
				meth = meth.next;
			}
			clazz = clazz.next;			
		}		
		return 0;
	}
	public static String getSystemMethodForID(int id){
		if((id & 0xFFFFF000) != 0){
			ErrorReporter.reporter.error(errInvalideParameter,
					"getSystemMethodForID parameter 0x" + Integer.toHexString(id) + " to large, only 12-bit numbers are allowed");
		}
		SystemClass clazz = os.getClassList();
		while(clazz != null){
			SystemMethod meth = clazz.methods;
			while(meth != null){
				if((meth.attributes & 0xFFF) == id){
					return meth.name;
				}
				meth = meth.next;
			}
			clazz = clazz.next;
		}
		return null;
	}

	public static String[] getRootClassNames() {
		int count = 0;
		HString classNamesRoot = project.getRootClasses();
		HString current = classNamesRoot;
		// count
		while (current != null) {
			count++;
			current = current.next;
		}
		if (count > 0) {
			String[] classNames = new String[count];
			for (int i = 0; i < count; i++) {
				classNames[i] = classNamesRoot.toString();
				classNamesRoot = classNamesRoot.next;
			}
			return classNames;
		}
		return null;

	}

	public static void clear() {
		Parser.clear();
		project = null;
		SystemConstants.clear();
		sysConst = SystemConstants.getInstance();
		Consts.clear();
		consts = Consts.getInstance();
		MemoryMap.clear();
		memoryMap = MemoryMap.getInstance();
		RegisterMap.clear();
		registerMap = RegisterMap.getInstance();
		regInit = null;
		targetConfig = null;
		activeTarConf = null;
		os = null;
		// heap = null;
		nofHeapSegments = 0;
		nofStackSegments = 0;
		heaps = new Segment[maxNumbersOfHeaps];
		stacks = new Segment[maxNumbersOfStacks];

	}

	public static SystemClass getSystemPrimitives() {
		return os.getClassList();
	}

	protected static void setActiveTargetConfig(HString targetConfigName) {
		// determine active configuration if it is not set
//		if (activeTarConf == null) {
			activeTarConf = targetConfig;
			while (activeTarConf != null) {
				if (activeTarConf.name.equals(targetConfigName)) {
					break;
				}
				activeTarConf = activeTarConf.next;
			}
			if (activeTarConf == null) {
				ErrorReporter.reporter.error(errInconsistentattributes,	"Targetconfiguration which is set is not found\n");
			}
//		}

	}

	public static void parseAndCreateConfig(String file, String targetConfigurationName) {
		int index = file.lastIndexOf('/');
		HString fileToRead = HString.getHString(file);
		location = file.substring(0, index + 1);
		Parser.loc = HString.getHString(location);
		Parser par = new Parser(fileToRead);
		// if (importedFiles.size() < 1 || par.hasChanged(file)) {
		clear();
		Parser.checksum.add(par.calculateChecksum(fileToRead));
		Parser.importedFiles.add(fileToRead.substring(index + 1));
		Parser.locForImportedFiles.add(Parser.loc);
		par.config();
		// }
		if(ErrorReporter.reporter.nofErrors <=0) setActiveTargetConfig(HString.getHString(targetConfigurationName));
	}

//	public static void main(String[] args) {
//		parseAndCreateConfig("D:/work/Crosssystem/deep/ExampleProject.deep",
//				"BootFromRam");
//		Configuration.print();
//		Dbg.vrb.println("Config read with " + Parser.nOfErrors + " error(s)");
//		// String[] names =Configuration.getRootClassNames();
//		// for(int i = 0; i < names.length; i++){
//		// StdStreams.out.println(names[i]);
//		// }
//
//		// Configuration.getCodeSegmentOf(HString.getHString("ch/ntb/inf/mpc555/kernel")).println(0);
//		// Configuration.getVarSegmentOf(HString.getHString("ch/ntb/inf/mpc555/kernel")).println(0);
//		// Configuration.getConstSegmentOf(HString.getHString("ch/ntb/inf/myProject/package2/z")).println(0);
//		Configuration.createInterfaceFile(HString.getHString("D:/work/Crosssystem/deep/src/ch/ntb/inf/deep/runtime/mpc555/ntbMpc555HB.java"));
//	}
}
