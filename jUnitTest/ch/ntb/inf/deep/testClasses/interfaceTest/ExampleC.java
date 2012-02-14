package ch.ntb.inf.deep.testClasses.interfaceTest;

public class ExampleC {
	static int  rA = 191;
	
	static Ic3  refIc3; // C01, C02
	static Ie2  refIe2; // C01, C02
	static Ig3  refIg3; // C01, C02

	static Cr2 refCr2; // C01, C02

	static If2  refIf2; // C02
	static Ib2  refIb2; // C02

	static Id1  refId1; // C03
	static Ia1  refIa1; // C04

	private static void instanceAllClasses(){ 
		new Cx1(); // C04
		new Cy1(); // C04
		new Cz1(); // C03

		new Cr1(); // C01, C02
		refCr2 = new Cr2(); // C01, C02
		new Cr3( 11 ); // C01, C02
	}

	private static void refInstanceMeths(){
		refCr2.cmr11();
	}

	private static void refAllIntfMeths(){
		refIc3.ima11(); // C01, C02
		refIc3.imX1(); // C03
		refIc3.imc31(); // C03

		refIe2.imXY(); // C01, C02
//		refIe1.imX1();
		
		refIg3.imf21(); // C01, C02
		
		refIg3.img31(); // C02
		
		refIf2.imf21(); // C02
		
		refIb2.ima11(); // C02
//		refIb2.imX1(); // C02
		
		refIe2.imX1(); // C03
		refId1.imXY(); // C03

		refIa1.ima11(); // C03
}

	public static void main(String[] args) {
		refAllIntfMeths();
		refInstanceMeths();
		instanceAllClasses();
	}
}