package backend;

import main.Main;

public class ConversionTable {

	Main main = null;
	int[] leftByteValue 	= {0,0,1,0,1,0,0,0};
	//int[] rightByteValue	= {};


	public ConversionTable(Main main){
		this.main = main;
	}

	public void fileToSerial(String dRna, String concentration, String primer){

		int intValue = 0, i = 0, pow = 0;
		char charLeftByteValue = 'x';
		char charRghtByteValue = 'y';

		//System.out.println("dRna: " +dRna+ ", concentration: " +concentration+ ", primer: " +primer);
		
		if (dRna.equals("D")) {
			leftByteValue[7] = 1;
		} else {
			leftByteValue[7] = 0;
		}
		
		if (concentration.equals("H")) {
			leftByteValue[6] = 1;
		} else {
			leftByteValue[6] = 0;
		}

		for (i = 0, pow = leftByteValue.length-1; i < leftByteValue.length; i++, pow--) {
			intValue += leftByteValue[i]*Math.pow(2, pow);
		}

		charLeftByteValue = (char)intValue;
		
		charRghtByteValue = (char) Integer.parseInt(primer);
		
		/*byte[] b = new byte[1];
		b[0] = (byte) charLeftByteValue;*/

		//System.out.println("Int Value: " +intValue+ ", Char Value: " + charLeftByteValue);
		//System.out.println("String Primer: " +primer+ ", Char Primer: " +charRghtByteValue);
		System.out.println("1st Byte Char Sent: " +charLeftByteValue+ ", 2nd Byte Char Sent: " +charRghtByteValue);
		
		//main.serialPortManager.sendChar_Int(intValue);
		//main.serialPortManager.sendChar_Int(Integer.parseInt(primer));
		
		main.serialPortManager.sendChar(charLeftByteValue);
		main.serialPortManager.sendChar(charRghtByteValue);
		
		//main.serialPortManager.sendData(String.valueOf(charLeftByteValue));
		//main.serialPortManager.sendData(String.valueOf(charRghtByteValue));
	}
	
	public void serialToFile(char [] charsToConvert){
		//inverse of the method above
		//save to file
		int bufferSize 	= 2;
		String trueString = "1";
		String str = new String(charsToConvert, 0, bufferSize);
		int leftData = (int) charsToConvert[0];//Integer.parseInt(str.substring(0, bufferSize-1));
		int rghtData = (int) charsToConvert[1];//Integer.parseInt(str.substring(bufferSize-1, bufferSize));
			
		String strF = Integer.toBinaryString(leftData);
		int lenBuff = strF.length();
		//System.out.println("lenBuff: " + lenBuff+ ", substr1: " + strF.substring(lenBuff-1, lenBuff));
		
		if (trueString.compareTo(strF.substring(lenBuff-1, lenBuff)) == 0) {
			main.fileReadWrite.WriteData("D\t");
			System.out.println("First char is DNA");
		} else {
			main.fileReadWrite.WriteData("R\t");
			System.out.println("First char is RNA");
		}
		
		//System.out.println("substr2: " + strF.substring(lenBuff-2, lenBuff-1));
		if (trueString.compareTo(strF.substring(lenBuff-2, lenBuff-1)) == 0) {
			main.fileReadWrite.WriteData("H\t");
			System.out.println("Scnd char is H");
		} else {
			main.fileReadWrite.WriteData("L\t");
			System.out.println("Scnd char is L");
		}
		
		//main.fileReadWrite.WriteData(String.valueOf(charsToConvert[1]) + '\n');
		main.fileReadWrite.WriteData(Integer.toString(charsToConvert[1]) + '\n');
		System.out.println("Primer is: " + rghtData);
	}

}
