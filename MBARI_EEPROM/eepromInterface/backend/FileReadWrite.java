package backend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import main.Main;

public class FileReadWrite {
	Main main = null;

	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss"); //"dd-MM-yyyy_HH-mm-ss"
	Date date = new Date();

	//private String outputFileNameExperTwo 	= "C:\\andres\\Code\\eclipseWorkspace\\SimpleGUI\\guiMain\\data\\experTwoLog" + dateFormat.format(date) + ".txt";
	//private File outputFile = new File(outputFileNameExperTwo); 
	//private Writer output = null; 
	@SuppressWarnings("unused")
	private BufferedReader input = null;
	
	private FileWriter outputFile = null;
	private BufferedWriter output = null;

	boolean firstWrite = true;

	public FileReadWrite(Main main){
		this.main = main;
		//initialization();
	}

	public void initialization()
	{
		System.out.println("FileReadWrite Class initialized...!");
	}
	
	public void FileToWriteTo(String fileToWrite){
		try {
			outputFile = new FileWriter(fileToWrite, true);
			output = new BufferedWriter(outputFile);
			output.write(dateFormat.format(date) + "\n" + "DNA/RNA\tCONCENTRATION\tPRIMERS\n");
			output.flush();
			//output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*public void WriteHeader(){
		System.out.println("Logging data to: " + outputFile.toString());

		// This is done here so the header files are done once 
		//without checking if there was a previous version of this file.
		try {
			output = new BufferedWriter(new FileWriter(outputFile, true));
			output.write("BER Experimental Data\t" +dateFormat.format(date) + "\n");
			output.append("Received data\t\n");			

			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/

	public void WriteHeader(){
		
		System.out.println("Logging data to: " + outputFile.toString());

		// This is done here so the header files are done once 
		//without checking if there was a previous version of this file.
		try {
			output.write(dateFormat.format(date) + "\n" + "DNA/RNA\tCONCENTRATION\tPRIMERS\n");
			output.flush();
			//output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Writes the contents of packageData to the desired outputFile
	/*public void WriteData(Package packageData){

		try {

			if (firstWrite == true) {
				output = new BufferedWriter(new FileWriter(outputFile, true));
				output.write("BER Experimental Data\t" + dateFormat.format(date) + "\n");
				output.append("Received data\t\n");
				output.close();
				firstWrite = false;
			}

			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/

	// Writes the contents of packageData to the desired outputFile
	/*public void WriteData(byte[] data){

		String str;
		try {
			//output = new BufferedWriter(new FileWriter(outputFile));
			output = new BufferedWriter(new FileWriter(outputFile, true));
			for (int i = 0; i < main.serialPortManager.numBytesSent; i++) {
				str = new String(new byte[] { (byte) data[i] });
				output.append(str);
			}
			output.append('\n');
			//output.flush();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/

	// Writes EEPROM data to the desired outputFile
	public void WriteData(String text){

		try {
			output.append(text);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void WriteDataEOF(){
		try {
			output.append('\n');
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void CloseFile(){
		try {
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Returns the number of lines of DATA to be saved 
	public short ReadNumberLines(String fileToRead){

		short numberLines = 0;

		try {
			BufferedReader input = new BufferedReader(new FileReader(new File(fileToRead)));

			while (input.readLine() != null) {

				numberLines++;
			}

			input.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return (short) (numberLines-2); //Two lines of header	s
	}

	// Example of reading data line by line from a given input file 
	public void ReadData(String fileToRead){

		StringTokenizer token;
		String line = null;
		String date = null;
		String headerCol_1, headerCol_2, headerCol_3 = null;
		String val_1, val_2, val_3 = null;
		int counter = 0;

		try {
			BufferedReader input = new BufferedReader(new FileReader(new File(fileToRead)));

			while ((line = input.readLine()) != null) {

				token = new StringTokenizer(line, "\t");

				if (counter == 0) {
					date = token.nextToken();
					//System.out.println(date);

				} else if (counter == 1) {
					headerCol_1 = token.nextToken();
					headerCol_2 = token.nextToken();
					headerCol_3 = token.nextToken();
					//System.out.println(headerCol_1 + "\t" + headerCol_2 + "\t" + headerCol_3);

				} else if (counter > 1) {
					val_1 = token.nextToken();
					val_2 = token.nextToken();
					val_3 = token.nextToken();
					//System.out.println(val_1 + "\t" + val_2 + "\t" + val_3);
					main.conversionTable.fileToSerial(val_1, val_2, val_3);
				}

				counter++;
				try {
					Thread.sleep(100); //To give some time to EEPROM to finish write cycle
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			input.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
