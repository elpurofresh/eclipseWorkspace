package main;

public class ExperimentFour implements Runnable{

	Main main = null;
	private boolean runThread 	= false;
	private long timeInterval 	= 4000; //in milliseconds
	private int numRobots 		= 3;    //number of B robots

	//private String cmdC1 = "<SENDC1>";
	private String reqCX = "<REQTCX>";
	private String rpyC4 = "<C424!>";
	
	private String[] robotCmd 	= {"<SENDC1>", "<SENDC2>", "<SENDC3>"};//new String[numRobots];	
	private String[] robotId	= {"C1", "C2", "C3"};
	
	private boolean waitForABit = false;

	public ExperimentFour(Main main){
		this.main = main;
	}


	@Override
	public void run() {

		while(true){
			if(!runThread){ //To stop/exit this thread
				main.gui.setExpThreeSelected(false);
				break;
			}
			
			

			
			/*main.serialPortManager.sendData("N");
			
			if (main.serialPortManager.isRxdPackage() && main.serialPortManager.getTestVal().equals("A")) {
//				waitForABit = true;
				try {
					Thread.sleep(20000);
				} catch (InterruptedException e) { //sleep for 20secs
					e.printStackTrace();
				}
//				waitForABit = false;				
				//main.serialPortManager.sendData("N");
			} else {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) { //sleep for 20secs
					e.printStackTrace();
				}
				//main.serialPortManager.sendData("N");
			}*/
			
			
			
			/*for (int i = 0; i < numRobots; i++) { //definitely works with two nodes.
				
				main.serialPortManager.sendData(robotCmd[i]); //"<REQTCX>"
				waitForPacketFirstTime();
				if (checkPacket(main.serialPortManager.getPacket(), robotCmd[i])){
					System.out.println(robotId[i] +" Alive: " + main.serialPortManager.getPacket());

					waitForPacketFirstTime();
					if (checkPacket(main.serialPortManager.getPacket(), reqCX)) {
						//do nothing
						System.out.println(robotId[i] +" Checked for child: " + main.serialPortManager.getPacket());
					}

					waitForPacketFirstTime();
					if (checkPacket(main.serialPortManager.getPacket(), rpyC4)){//(checkPacket(main.serialPortManager.getNetText(), "C424!", 12)) {//response from c1: <sendc1datacXdata>
						//parsedata_C1
						//parsedata_C2
						//main.gui.animation.updateSensorbotPic(robotNameX, alive);
						System.out.println(robotId[i] +" has Children: " +new String(main.serialPortManager.getPacket()));
					} else {
						//main.gui.animation.updateSensorbotPic(robotNameX, dead);
						System.out.println(robotId[i] +" has no Children: " +new String(main.serialPortManager.getPacket()));
					}
				} else {
					//main.gui.animation.updateSensorbotPic(robotName1, dead);
					System.out.println(robotId[i] +" is dead: " +new String(main.serialPortManager.getPacket()));
				}
				
				try {
					Thread.sleep(timeInterval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}*/
			
			/*main.serialPortManager.sendData(cmdC1); //"<REQTCX>" //kinda works
			waitForPacket();
			if (checkPacket(main.serialPortManager.getPacket(), cmdC1)){
				System.out.println("C1 Alive: " + main.serialPortManager.getPacket());

				waitForPacket();
				if (checkPacket(main.serialPortManager.getPacket(), reqCX)) {
					//do nothing
					System.out.println("C1 Checked for child: " + main.serialPortManager.getPacket());
				}

				waitForPacket();
				if (checkPacket(main.serialPortManager.getPacket(), rpyC4)){//(checkPacket(main.serialPortManager.getNetText(), "C424!", 12)) {//response from c1: <sendc1datacXdata>
					//parsedata_C1
					//parsedata_C2
					//main.gui.animation.updateSensorbotPic(robotNameX, alive);
					System.out.println("C1 has Children: " +new String(main.serialPortManager.getPacket()));
				} else {
					//main.gui.animation.updateSensorbotPic(robotNameX, dead);
					System.out.println("C1 has no Children: " +new String(main.serialPortManager.getPacket()));
				}
			} else {
				//main.gui.animation.updateSensorbotPic(robotName1, dead);
				System.out.println("C1 is dead: " +new String(main.serialPortManager.getPacket()));
			}

			try {
				Thread.sleep(timeInterval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
	}

	/*private boolean checkPacket(byte[] packet, String str, int length){

		System.out.println("packet: " + new String(packet));

		for (int i = 0; i < packet.length; i++) {
			if (((byte) str.charAt(i)) != packet[i]) {
				return false;
			}
		}		
		return true;
	}*/

	private boolean checkPacket(String packet, String str2){
		System.out.println("packet: " + packet);

		if (packet.compareTo(str2) == 0) {
			System.out.println("oh yesss");
			return true;
		}

		System.out.println("oh noooo");
		return false;

	}

	private void waitForPacket(){
		while (true) {
			//System.out.println("waiting...");
			if (main.serialPortManager.isRxdPackage()) {
				main.serialPortManager.setRxdPackage(false);
				System.out.print('\n');
				break;
			}
			System.out.print(".");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//just wait for a new packet
		}
	}

	private void waitForPacketFirstTime(){
		int counter = 0;
		while (true) {
			//System.out.println("waiting...");
			if (main.serialPortManager.isRxdPackage()) {
				main.serialPortManager.setRxdPackage(false);
				System.out.print('\n');
				break;
			}
			System.out.print(".");
			counter++;
			
			if (counter == 20) {
				//main.serialPortManager.setRxdPackage(false);
				break;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//just wait for a new packet
		}
	}

	public boolean isRunThread() {
		return runThread;
	}


	public void setRunThread(boolean runThread) {
		this.runThread = runThread;
	}


	public boolean isWaitForABit() {
		return waitForABit;
	}


	public void setWaitForABit(boolean waitForABit) {
		this.waitForABit = waitForABit;
	}


	/*try {
	Thread.sleep(timeInterval); //wait for response
} catch (InterruptedException e) {
	e.printStackTrace();
}
if (checkPacket(main.serialPortManager.getPacket(), cmdC1)){ //(checkPacket(main.serialPortManager.getNetText(), "SENDC1", 6) ) {//response correct from c1: <sendC1>
	//main.gui.animation.updateSensorbotPic(robotName1, alive);
	System.out.println("C1 Alive: " + main.serialPortManager.getPacket());

	if (checkPacket(main.serialPortManager.getPacket(), reqCX)) {
		//do nothing
		System.out.println("C1 Checked for child: " + main.serialPortManager.getPacket());
	}
	try {
		Thread.sleep(timeInterval*4); //wait for response
	} catch (InterruptedException e) {
		e.printStackTrace();
	}

	if (checkPacket(main.serialPortManager.getPacket(), rpyC4)){//(checkPacket(main.serialPortManager.getNetText(), "C424!", 12)) {/response from c1: <sendc1datacXdata>
		//parsedata_C1
		//parsedata_C2
		//main.gui.animation.updateSensorbotPic(robotNameX, alive);
		System.out.println("C1 has Children: " +new String(main.serialPortManager.getPacket()));
	} else {
		//main.gui.animation.updateSensorbotPic(robotNameX, dead);
		System.out.println("C1 has no Children: " +new String(main.serialPortManager.getPacket()));
	}

} else {
	//main.gui.animation.updateSensorbotPic(robotName1, dead);
	System.out.println("C1 is dead: " +new String(main.serialPortManager.getPacket()));
}*/


	/*Robot 2
main.serialPortManager.sendData("<sendC2>");
try {
	Thread.sleep(timeInterval); //wait for response
} catch (InterruptedException e) {
	e.printStackTrace();
}
if (checkPacket(main.serialPortManager.getNetText(), "sendC2", 6) ) {response correct from c1: <sendC1>
	//main.gui.animation.updateSensorbotPic(robotName1, alive);
	System.out.println(main.serialPortManager.getNetText());
	try {
		Thread.sleep(timeInterval); //wait for response
	} catch (InterruptedException e) {
		e.printStackTrace();
	}

	if (checkPacket(main.serialPortManager.getNetText(), "sendC233C424", 12)) {response from c1: <sendc1datacXdata>
		//parsedata_C1
		//parsedata_C2
		//main.gui.animation.updateSensorbotPic(robotNameX, alive);
		System.out.println(main.serialPortManager.getNetText());
	} else {
		//main.gui.animation.updateSensorbotPic(robotNameX, dead);
		System.out.println(main.serialPortManager.getNetText());
	}

} else {
	//main.gui.animation.updateSensorbotPic(robotName1, dead);
	System.out.println(main.serialPortManager.getNetText());
}

Robot 3
main.serialPortManager.sendData("<sendC3>");
try {
	Thread.sleep(timeInterval); //wait for response
} catch (InterruptedException e) {
	e.printStackTrace();
}
if (checkPacket(main.serialPortManager.getNetText(), "sendC3", 6) ) {response correct from c1: <sendC1>
	//main.gui.animation.updateSensorbotPic(robotName1, alive);
	System.out.println(main.serialPortManager.getNetText());
	try {
		Thread.sleep(timeInterval); //wait for response
	} catch (InterruptedException e) {
		e.printStackTrace();
	}

	if (checkPacket(main.serialPortManager.getNetText(), "sendC344C424", 12)) {response from c1: <sendc1datacXdata>
		//parsedata_C1
		//parsedata_C2
		//main.gui.animation.updateSensorbotPic(robotNameX, alive);
		System.out.println(main.serialPortManager.getNetText());
	} else {
		//main.gui.animation.updateSensorbotPic(robotNameX, dead);
		System.out.println(main.serialPortManager.getNetText());
	}

} else {
	//main.gui.animation.updateSensorbotPic(robotName1, dead);
	System.out.println(main.serialPortManager.getNetText());
}*/	

}
