package backend;

import main.Main;

public class DownloadTask implements Runnable{

	Main main = null;
	private String ftoWrite;
	public int numLines 		= 0;

	/*private int startTX     	= 8;
	private int stopTX      	= 9;
	private boolean txFlag		= false;*/
	private char startRead		= 26;
	private char stopRead	    = 27;
	private boolean rxFlag  	= false;

	private int numTries 		= 0;
	private boolean stopThread	= false;

	private boolean runThread 	= false;
	Thread downloadThread		= null;

	private char tempVal;
	private char[] rxdChar 		= new char[2];
	private int byteCounter 	= 0;

	private boolean sendStart	= false;
	
	private int counter = 0;

	public DownloadTask(Main main){
		this.main = main;
	}

	public void downloadTask(String fileToWrite){
		System.out.println("Downloading Task");
		
		ftoWrite = fileToWrite;
		main.fileReadWrite.FileToWriteTo(ftoWrite);
		
		main.serialPortManager.setRxFromEEPROM(false); //"reset" receiving buffer

		//start upload thread here
		downloadThread = new Thread(main.downloadTask, "DownloadTaskClass"); //this?
		//runThread = true;
		sendStart = true;
		stopThread = false;
		downloadThread.start();
	}

	@Override
	public void run() {
		while (true) {
			
			//counter++;
			
			if (stopThread) {
				System.out.println("Stopping Download Thread Normally");
				break;
			}
			
			if (sendStart) {
				sendStart = false;
				main.serialPortManager.sendChar(startRead);
				//System.out.println("numTries: " + numTries);
				//System.out.println("startRead1 Sent " + counter);

				/*try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}	*/				
			}
			
			if (main.serialPortManager.isRxFromEEPROM()) {
				
				tempVal = main.serialPortManager.getCharVal();
				main.serialPortManager.setRxFromEEPROM(false);
				System.out.println("rxd1: " + (int)tempVal /*+" " + counter*/);	
				
				if (tempVal != stopRead) {
					rxdChar[byteCounter++] = tempVal;
					
					if (byteCounter > 1) { //This ensures the size of the rxdChar buffer is not exceeded
						byteCounter = 0;
						
						System.out.println("1st Byte Char Rxd: " +(int)rxdChar[0]+ ", 2nd Byte Char Rxd: " +(int)rxdChar[1]);
						
						main.conversionTable.serialToFile(rxdChar);
						
						//main.gui.textAreaDialog.append("'"+ tempVal+ "'" + "\n");
						//main.conversionTable.serialToFile(rxdChar);
					}
					
				} else if (tempVal == stopRead) {
					System.out.println("rxd2: " + (int)tempVal /*+" " + counter*/);
					
					
					stopThread = true;
					
					main.fileReadWrite.CloseFile();
					System.out.println("Rxd all Data, Stopping Thread");
				}
				
			}			
		}
	}
	
	/*//if (!stopThread) {
	numTries++;

	if (sendStart) {
		main.serialPortManager.sendChar(startRead);
		System.out.println("numTries: " + numTries);
		System.out.println("startRead1 Sent " + counter);

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}					
	}

	tempVal = main.serialPortManager.getCharVal();

	if (!rxFlag &&  tempVal == startRead) {
		System.out.println("rxd1: " +(int)tempVal +" " + counter);
		rxFlag = true;
		sendStart = false;
		numTries = 0;

		main.serialPortManager.sendChar(startRead); //This will request the arduino to start sending data
		System.out.println("startRead2 Sent" +" " + counter);


	} else if (rxFlag && tempVal != stopRead) {

		// convert data to file writeable data main.fileReadWrite.ReadData(ftoWrite);
		// write data to file main.serialPortManager.sendChar(stopTX);
		System.out.println("rxd2: " + (int)tempVal +" " + counter);

		if (tempVal != startRead) {
			rxdChar[byteCounter++] = tempVal;

			if (byteCounter > 1) { //This ensures the size of the rxdChar buffer is not exceeded
				byteCounter = 0;

				System.out.println("1st Byte Char Rxd: " +(int)rxdChar[0]+ ", 2nd Byte Char Rxd: " +(int)rxdChar[1]);

				main.conversionTable.serialToFile(rxdChar);

				//main.gui.textAreaDialog.append("'"+ tempVal+ "'" + "\n");
				//main.conversionTable.serialToFile(rxdChar);
			} else {
				//do nothing						
			}

		}

	} else if (rxFlag && tempVal == stopRead) {
		System.out.println("rxd3: " + (int)tempVal+" " + counter);

		rxFlag = false;
		runThread = false;

		main.serialPortManager.sendChar(stopRead);
		main.fileReadWrite.CloseFile();
		System.out.println("Rxd all Data, Stopping Thread");
	}
	
	try {
		Thread.sleep(3000);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}

	if (numTries > 5) {
		main.fileReadWrite.CloseFile();
		stopThread = true;
		main.serialPortManager.sendChar(stopRead);
		System.out.println("Stopping Download Thread after 3 tries");
	}
//}
*/
	/*private boolean compare(String msg){
		try {
			if (main.serialPortManager.getPacket().substring(0, 6).compareTo(msg) == 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;

	}*/

}

