package backend;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.TooManyListenersException;

import main.Main;

/**
 * @author andres
 *
 */
public class SerialPortManager implements SerialPortEventListener{

	//passed from main GUI
	Main main = null;

	//for containing the ports that will be found
	@SuppressWarnings("rawtypes")
	private Enumeration ports = null;
	//map the port names to CommPortIdentifiers
	private HashMap<String, CommPortIdentifier> portMap = new HashMap<String, CommPortIdentifier>();

	//this is the object that contains the opened port
	private CommPortIdentifier selectedPortIdentifier = null;
	private SerialPort serialPort = null;

	//input and output streams for sending and receiving data
	private InputStream input = null;
	private OutputStream output = null;

	//just a boolean flag that i use for enabling
	//and disabling buttons depending on whether the program
	//is connected to a serial port or not
	private boolean bConnected = false;

	//the timeout value for connecting with the port
	final static int TIMEOUT = 2000;

	//Some ascii values for for certain things
	final static int SPACE_ASCII = 32;
	final static int DASH_ASCII = 45;
	final static int NEW_LINE_ASCII = 10;
	final static int START_BYTE = 60; 	//'<' 91; // '['
	final static int STOP_BYTE = 62; 	//'>' 93; // ']'
	final static int ASCII_VALUE_OF_ZERO = 48;

	//a string for recording what goes on in the program
	//this string is written to the GUI
	String logText = "";

	boolean dataPacketFlag = false;
	private int bufferSize = 200;
	//private int byteCounter = 0;
	byte netText[] = new byte[bufferSize];

	public int numRxD = 0;
	public int numTxD = 0;

	public boolean flagTx		= false;
	public boolean flagACKTx 	= false;
	public boolean flagEndData	= false;
	public boolean flagACKEnd	= false;

	public final String msgTx		= "[";
	public final String msgACKTx	= "<";
	public final String msgEndData 	= "]";
	public final String msgACKEnd	= ">";
	public final String msgACKFinal	= "?";

	public final byte byteTx		= singleStringToBytesASCII(msgTx);
	public final byte byteACKTx		= singleStringToBytesASCII(msgACKTx);
	public final byte byteEndData	= singleStringToBytesASCII(msgEndData);
	public final byte byteACKEnd 	= singleStringToBytesASCII(msgACKEnd);
	public final byte byteACKFinal	= singleStringToBytesASCII(msgACKFinal);

	private boolean sendingData = false;
	//private long startTime = 0;

	public final float numBytesSent = 36;

	private int lengthRxPacket = 0;
	
	private boolean rxdPackage = false;
	
	private String testVal = "";
	
	//private char[] rxdChar = new char[2];
	
	private char charVal;
	private boolean rxFromEEPROM = false;
	

	/*private int codeCnter = 6;
	private final String keyCode = "[SEND]";*/

	public SerialPortManager(Main main){
		this.main = main;
	}

	public void searchForPorts(){
		ports = CommPortIdentifier.getPortIdentifiers();


		while (ports.hasMoreElements()) {
			CommPortIdentifier currentPort = (CommPortIdentifier) ports.nextElement();
			if (currentPort.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				main.gui.cboxPorts.addItem(currentPort.getName());
				portMap.put(currentPort.getName(), currentPort);
			} 
		}
		if (!ports.hasMoreElements()) {
			main.gui.cboxPorts.addItem("No Serial Ports");
		}
	}

	public void connect(){
		String selectedPort = (String)main.gui.cboxPorts.getSelectedItem();
		selectedPortIdentifier = (CommPortIdentifier)portMap.get(selectedPort);

		CommPort commPort = null;

		try {
			commPort = selectedPortIdentifier.open("UWSN_ControlPanel", TIMEOUT);
			serialPort = (SerialPort)commPort;
			serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			setConnected(true);

			logText = selectedPort + " opened successfully!";
			main.gui.textAreaDialog.setForeground(Color.black);
			main.gui.textAreaDialog.append(logText + "\n");

			main.gui.toggleControls();

		} catch (PortInUseException e) {
			logText = selectedPort + " is in use. (" +e.toString() + ")";

			main.gui.textAreaDialog.setForeground(Color.red);
			main.gui.textAreaDialog.append(logText + "\n");
			e.printStackTrace();
			
		} catch (Exception e) {
			logText = "Failed to open " + selectedPort + "(" + e.toString() + ")";
			main.gui.textAreaDialog.setForeground(Color.red);
			main.gui.textAreaDialog.append(logText + "\n");
			//e.printStackTrace();
		}
	}

	public boolean initIOStream(){

		boolean successful = false;

		try {
			input 	= serialPort.getInputStream();
			output 	= serialPort.getOutputStream();

			successful = true;
			return successful;

		} catch (IOException e) {
			logText = "I/O Streams failed to open. (" + e.toString() + ")";
			main.gui.textAreaDialog.setForeground(Color.red);
			main.gui.textAreaDialog.append(logText + "\n");
			return successful;
		}
	}

	public void initListener(){

		try {
			serialPort.addEventListener(this);
			//serialPort.addEventListener(new SerialReader(input));
			serialPort.notifyOnDataAvailable(true);

		} catch (TooManyListenersException e) {
			logText = "Too many listeners. (" + e.toString() + ")";
			main.gui.textAreaDialog.setForeground(Color.red);
			main.gui.textAreaDialog.append(logText + "\n");
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void disconnect(){

		try {
			sendingData = true;
			serialPort.removeEventListener();
			serialPort.close();
			input.close();
			output.close();

			setConnected(false);
			logText = "Disconnected.";
			main.gui.textAreaDialog.setForeground(Color.black);
			main.gui.textAreaDialog.append(logText + "\n");

		} catch (Exception e) {
			logText = "Failed to close " + serialPort.getName() + "(" + e.toString() + ")";
			main.gui.textAreaDialog.setForeground(Color.red);
			main.gui.textAreaDialog.append(logText + "\n");
			//e.printStackTrace();
		}
	}

	
	public void serialEvent(SerialPortEvent spe) {

		if (spe.getEventType() == SerialPortEvent.DATA_AVAILABLE) {//If one byte of data came in
			try {
							
				 charVal = (char) input.read();

				if (charVal != -1 ) {
					System.out.println("Rxd: " + (int)charVal);
					setRxFromEEPROM(true);
					
					/*if (isRxFromEEPROM()) {
						//write data to file
						rxdChar[byteCounter++] = charVal;
						
						if (byteCounter > 1) {
							byteCounter = 0;
							
							main.conversionTable.serialToFile(rxdChar);
							
							main.gui.textAreaDialog.append("'"+ charVal+ "'" + "\n");
							main.conversionTable.serialToFile(rxdChar);
							System.out.println("1st Byte Char Rxd: " +rxdChar[0]+ ", 2nd Byte Char Rxd: " +rxdChar[1]);
						} 
					}*/						
				}
				
			} catch (Exception e) {
				//logText = "Failed to read data." + "(" + e.toString() + ")";
				System.err.println(e.toString());
			}
		}
	}
	
	
	/*private char charReceived(InputStream is){
		byte[] readBuffer 	= new byte[3];
		int numBytes		= 0;
		String data 		= "";
		char charVal		= 0;
		
		try {
			numBytes = is.read(readBuffer); //<-this is a tricky method
			String t = new String(readBuffer); 
			data = t.substring(0, numBytes); //<- readBuffer may have 2 or 3 chars, with numBytes I can dynamically cut and parse correctly
			charVal = (char) Integer.parseInt(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("No. chars: " +numBytes+ ", charVals: " +data+ ", charRxd: " + charVal);
		return charVal;
	}*/

	public static byte[] stringToBytesASCII(String str) {
		byte[] b = new byte[str.length()];
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) str.charAt(i);
		}
		return b;
	}

	public static byte singleStringToBytesASCII(String str){
		return (byte) str.charAt(0);
	}
	
	public static byte charToByteASCII(char charToSend){
		return (byte) charToSend;
	}
	
	public void sendChar(char charToSend){
		byte[] data = new byte[1];
		data[0] = charToByteASCII(charToSend);
		
		try {
			output.write(data);
			output.flush();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendChar_Int(int intToSend){
		try {
			output.write(intToSend);
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendShort(short shortToSend){
		short value = shortToSend;
		char LSB	= 0;
		char MSB	= 0;
		
		LSB = (char) (value & 0xFF);
		MSB = (char) ((value >> 8) & 0xFF);

		try {
			output.write(MSB);
			output.write(LSB);
			output.flush();			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendData(String dataToSend){

		sendingData = true;
		byte[] dataBytesOut = stringToBytesASCII(dataToSend);

		try {

			//window.textOutputArea.append("TxD-");

			for (int i = 0; i < dataBytesOut.length; i++) {
				output.write(dataBytesOut[i]);
			}

			output.flush();
			//numTxD++;

			

		} catch (Exception e) {
			logText = "Failed to write data. (" + e.toString() + ")";
			//main.gui.textOutputArea.setForeground(Color.red);
			//main.gui.textOutputArea.append(logText + "\n");
		}

		//startTime = System.currentTimeMillis();
		sendingData = false;
	}

	
	final public boolean getConnectionStatus()
	{
		return bConnected;
	}

	public void setConnected(boolean bConnected)
	{
		this.bConnected = bConnected;
	}

	public int getNumRxD() {
		return numRxD;
	}

	public int getNumTxD() {
		return numTxD;
	}

	public boolean isSendingData() {
		return sendingData;
	}

	public void setSendingData(boolean sendingData) {
		this.sendingData = sendingData;
	}

	public static class SerialReader implements SerialPortEventListener 
	{
		private InputStream in;
		private byte[] buffer = new byte[1024];

		public SerialReader ( InputStream in )
		{
			this.in = in;
			System.out.println("I was created!");
		}

		public void serialEvent(SerialPortEvent arg0) {
			System.out.println("I got data");
			int data;

			try
			{
				int len = 0;
				while ( ( data = in.read()) > -1 )
				{
					if ( data == '\n' ) {
						break;
					}
					buffer[len++] = (byte) data;
				}
				System.out.print(new String(buffer,0,len));
			}
			catch ( IOException e )
			{
				e.printStackTrace();
				System.exit(-1);
			}             
		}

	}

	public byte[] getNetText() {
		return netText;
	}
	
	public String getPacket(){
		
		byte [] buff = new byte [lengthRxPacket];
		for (int i = 0; i < lengthRxPacket; i++) {
			buff[i] = netText[i];
		}
		String str = new String(buff);
		System.out.println("SerialToExp4: -" + str+ "-");
		return str;
	}
	
	public void setRxdPackage(boolean rxdPackage) {
		this.rxdPackage = rxdPackage;
	}

	public boolean isRxdPackage() {
		return rxdPackage;
	}

	public String getTestVal() {
		return testVal;
	}

	public void setTestVal(String testVal) {
		this.testVal = testVal;
	}

	public char getCharVal() {
		return charVal;
	}
	
	public byte getCharVal_byte() {
		System.out.println("byte charVal" +(byte)charVal);
		return (byte) charVal;
	}

	public boolean isRxFromEEPROM() {
		return rxFromEEPROM;
	}

	public void setRxFromEEPROM(boolean rxFromEEPROM) {
		this.rxFromEEPROM = rxFromEEPROM;
	}

}
