package main;

import gui.Gui;
import backend.ConversionTable;
import backend.DownloadTask;
import backend.FileReadWrite;
import backend.SerialPortManager;
import backend.UploadTask;

public class Main {

	public Gui gui								= null;
	public SerialPortManager serialPortManager 	= null;
	public FileReadWrite fileReadWrite			= null;
	public ConversionTable conversionTable		= null;
	public UploadTask uploadTask				= null;
	public DownloadTask downloadTask			= null;

	public Main(){

		serialPortManager 	= new SerialPortManager(this);
		gui 				= new Gui(this);
		serialPortManager.searchForPorts();
		fileReadWrite 		= new FileReadWrite(this);
		conversionTable 	= new ConversionTable(this);
		uploadTask			= new UploadTask(this);
		downloadTask		= new DownloadTask(this);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Main();
	}

}
