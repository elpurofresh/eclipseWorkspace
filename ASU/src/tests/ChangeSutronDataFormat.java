package tests;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.StringTokenizer;


public class ChangeSutronDataFormat {

	static int		numSensors = 12;  //Total number of sensors 
	static String 	date;
	static String[] time = new String[numSensors];
	static String[] sensor = new String[numSensors];

	public ChangeSutronDataFormat(File inputFile, File outputFile) {

		StringTokenizer token;
		int counter = 0;

		try {
			BufferedReader input =  new BufferedReader(new FileReader(inputFile));
			Writer output = new BufferedWriter(new FileWriter(outputFile));

			String line = null;

			for (int i = 0; i < numSensors; i++) {
				if (i == 0) {					
					output.write("Date" + "\t");
				}

				output.append("Time" + i + "\t" + "Sensor" + i + "\t");

				if (i == (numSensors-1)) {
					output.append("\n");
				} 
			}

			/*output.write("Date" + "\t" 
					+ "Time0" + " " + "Sensor0" + "\t" + "Time1" + " " + "Sensor1" + "\t" + "Time2" + " " + "Sensor2"
					+ "\t" + "Time3" + " " + "Sensor3" + "\t" + "Time4" +" " + "Sensor4" + "\n");*/

			while (( line = input.readLine()) != null){

				token = new StringTokenizer(line, ",");
				
				date = token.nextToken();				
				time[counter] = token.nextToken();
				token.nextToken();
				sensor[counter] = token.nextToken();
				token.nextToken();
				token.nextToken();					

				if (counter == 0) {					
					output.write(date + "\t");
				}

				output.append(time[counter] + "\t" + sensor[counter] + "\t");

				if (counter == (numSensors-1)) {
					counter = 0;
					output.append("\n");
				} else {					
					counter++;
				}


				/*if (counter == 5) {
					counter = 0;

					output.write(date + "\t" 
							+ time[0] + " " + sensor[0] + "\t" + time[1] + " " + sensor[1] + "\t" + time[2] + " " + sensor[2] 
							+ "\t" + time[3] + " " + sensor[3] + "\t" + time[4] + " " + sensor[4]+ "\n");
				}*/


			}
			input.close();
			output.close();
		}
		catch (IOException ex){
			ex.printStackTrace();
		}
	}

	public static void main (String[] args){

		//File inputFile = new File("C:\\andres\\Code\\sutronCode\\data\\dataJan302012.txt");
		File inputFile = new File("C:\\andres\\Dropbox\\ASU\\SutronEverett\\Data\\calibrationDataSutron031512Short.txt");
		File outputFile = new File("C:\\andres\\Dropbox\\ASU\\SutronEverett\\Data\\formatedCalibrationDataSutron031512Short.txt");
		new ChangeSutronDataFormat(inputFile, outputFile);

	}
} 