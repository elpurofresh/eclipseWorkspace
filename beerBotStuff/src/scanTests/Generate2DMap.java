package scanTests;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.StringTokenizer;


public class Generate2DMap {
	
	static private double theta = 0.0;
	static private double dist = 0.0;
	static private double x_val = 0.0;
	static private double y_val = 0.0;

	public Generate2DMap(File inputFile, File outputFile) {

		StringTokenizer token;

		try {
			BufferedReader input =  new BufferedReader(new FileReader(inputFile));
			Writer output = new BufferedWriter(new FileWriter(outputFile));

			String line = null;

			while (( line = input.readLine()) != null){

				token = new StringTokenizer(line, "\t");
				
				theta = Double.parseDouble(token.nextToken());				
				dist = Double.parseDouble(token.nextToken());
				
				x_val = dist * Math.cos(theta);
				y_val = dist * Math.sin(theta);
				
				output.append(x_val + "\t" + y_val + "\n");

			}
			input.close();
			output.close();
		}
		catch (IOException ex){
			ex.printStackTrace();
		}
	}

	public static void main (String[] args){

		File inputFile = new File("C:\\andres\\Code\\teensy\\tests\\simpleScanTest\\dataTests\\scanTestPolar_0.txt");
		File outputFile = new File("C:\\andres\\Code\\teensy\\tests\\simpleScanTest\\dataTests\\scanTestQuad_0.txt");
		new Generate2DMap(inputFile, outputFile);

	}
} 