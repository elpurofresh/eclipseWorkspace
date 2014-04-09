package backend;

import main.Main;

public class UploadTask implements Runnable{
	
	Main main = null;
	private String ftoRead;
	public short numLines 		= 0;
	
	private char startWrite    	= '\b';//(char) 8;
	private char stopWrite     	= '\t';//(char) 9;
	
	private boolean runThread 	= false;
	Thread uploadThread			= null;
	
	private int numTries 		= 0;
	private boolean stopThread	= false;

	public UploadTask(Main main){
		this.main = main;
	}
	
	public void uploadTask(String fileToRead){
		System.out.println("Uploading Task");
		ftoRead = fileToRead;
		
		//start upload thread here
		uploadThread = new Thread(main.uploadTask, "UploadTaskClass"); //this?
		runThread = true;
		uploadThread.start();
	}
	
	@Override
	public void run() {
		while (true) {
			
			if (!runThread) {
				break;
			}
						
			if (!stopThread) {
				numTries++;
				numLines = main.fileReadWrite.ReadNumberLines(ftoRead);
				//numLines = 321;
				System.out.println("numLines1: " + numLines);
				
				main.serialPortManager.sendChar(startWrite);
				System.out.println("startTX: " + startWrite);
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (main.serialPortManager.getCharVal() == startWrite) {
					
					//main.serialPortManager.sendChar_Int(numLines);
					main.serialPortManager.sendShort(numLines);
					System.out.println("numLines2: " + numLines);

					main.fileReadWrite.ReadData(ftoRead);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					main.serialPortManager.sendChar(stopWrite);
					//System.out.println("stopTX: " + stopTX);

					stopThread = true;
					/*if (main.serialPortManager.getCharVal() == stopTX) {
						System.out.println("Stopping Upload Thread! 1");
						runThread = false;
					}*/

				} else if (main.serialPortManager.getCharVal() == stopWrite) {
					System.out.println("Stopping Upload Thread! 2");
					runThread = false;

				} else if (numTries > 2) {
					runThread = false;
					System.out.println("Stopping Upload Thread! 3");
				}
			}
		}
		
	}
	
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
