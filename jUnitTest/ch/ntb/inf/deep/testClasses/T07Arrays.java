package ch.ntb.inf.deep.testClasses;


public class T07Arrays {
	static short[] s1 = new short[3];
	byte[] b1 = new byte[12];
	
	public static void emptyIntArray(){
		int[] a = new int[5];
	}
	
	public static int intArray(int start){
		int[] b = new int[5];
		for (int i = 0; i < b.length; i++){
			b[i]= i + start; 
		}
		return b[2];
	}
	
	public static String stringArray(){
		String[] strings = new String[]{"This", "is", "a", "test", "string"};
		
		return strings[0];
	}
	
	public static void objectArray(){
		Object[] objs = new Object[3];
		objs[0] = new Object();
	}

	public static void multiArray(){
		int[][] x = new int[2][3];
		double [][] d = new double[][]{{2.4d,3.1d},{3.5d,8.3d}};
	}
	
	public static String multiObjectArray(String s){
		Object[][] obj = new Object[3][5][6];		
		String[][] str = new String[][]{{s,s},{s,s}};
		return str[0][1];
	}
	
	static {
		s1[1] = 22;
		int a = s1[1];
		T07Arrays obj = null;
		obj.b1[7] = 32;
		a = obj.b1[2];		
	}

}
