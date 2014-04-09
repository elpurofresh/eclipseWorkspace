package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import main.Main;

@SuppressWarnings("serial") //I really don't want this here, but also don't like annoying warnings
public class Gui extends JPanel {

	Main main = null;
	//EventSource event = null;


	private JFrame frame;

	public JLabel lblMainTitle = new JLabel("Experiment Instructions Upload/Download");
	public JLabel lblSelectComm = new JLabel("Select Serial Comm Port ->");
	public JComboBox cboxPorts = new JComboBox();
	public JButton btnConnect = new JButton("Connect");
	public JButton btnDisconnect = new JButton("Disconnect");
	public JButton btnBrowseUpload = new JButton("Browse/Upload");
	public JButton btnBrowseDownload = new JButton("Browse/Download");
	public JLabel lblLeftTitle = new JLabel("Upload to EEPROM");


	private final JLabel lblRightTitle = new JLabel("Upload to EEPROM");
	private final JLabel lblSelectSelectFile = new JLabel("Select file to UPLOAD to EEPROM");
	public		  JTextField tFUploadPath = new JTextField();
	private final JLabel lblSelectFileTo = new JLabel("Select file to DOWNLOAD to Computer");
	public		  JTextField tFDownloadPath = new JTextField();
	public		  JTextArea textAreaDialog = new JTextArea();
	private final JScrollPane scrollPaneDialog = new JScrollPane();
	private final JButton btnUpload = new JButton("UPLOAD");
	private final JButton btnDownload = new JButton("DOWNLOAD");

	private File  fileUpload = new File("");
	private File  fileDownload = new File("");
	private final FileSystemView fsv = FileSystemView.getFileSystemView();
	private final JFileChooser fc = new JFileChooser("C://andres//Code//eclipseWorkspace//MBARI_EEPROM//eepromInterface//testFiles", fsv);

	/**
	 * Create the application.
	 */
	public Gui(Main main) {
		this.main = main;
		initialize();
		toggleControls();
	}

	public void toggleControls(){
		if (main.serialPortManager.getConnectionStatus()) {

			cboxPorts.setEnabled(false);
			btnConnect.setEnabled(false);

			btnDisconnect.setEnabled(true);
			btnBrowseUpload.setEnabled(true);
			btnBrowseDownload.setEnabled(true);
			

			btnUpload.setEnabled(true);
			btnDownload.setEnabled(true);
		}
		else {
			cboxPorts.setEnabled(true);
			btnConnect.setEnabled(true);

			btnDisconnect.setEnabled(false);
			btnBrowseUpload.setEnabled(false);
			btnBrowseDownload.setEnabled(false);

			btnUpload.setEnabled(false);
			btnDownload.setEnabled(false);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setVisible(true);
		frame.getContentPane().setEnabled(false);
		frame.getContentPane().setMaximumSize(new Dimension(600, 600));
		frame.setTitle("Experiment Upload/Download Control");
		frame.setBounds(100, 100, 568, 411);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{120, 120, 120, 120, 0};
		gridBagLayout.rowHeights = new int[] {10, 5, 5, 15, 5, 5, 5, 5, 35, 35, 35};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};

		frame.getContentPane().setLayout(gridBagLayout);
		GridBagConstraints gbc_lblMainTitle = new GridBagConstraints();
		gbc_lblMainTitle.insets = new Insets(0, 0, 5, 5);
		gbc_lblMainTitle.gridwidth = 2;
		gbc_lblMainTitle.gridx = 1;
		gbc_lblMainTitle.gridy = 0;

		//Uncomment one of the following lines to try a different
		//file selection mode.  The first allows just directories
		//to be selected (and, at least in the Java look and feel,
		//shown).  The second allows both files and directories
		//to be selected.  If you leave these lines commented out,
		//then the default mode (FILES_ONLY) will be used.
		//
		//fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		frame.getContentPane().add(lblMainTitle, gbc_lblMainTitle);
		GridBagConstraints gbc_lblSelectComm = new GridBagConstraints();
		gbc_lblSelectComm.insets = new Insets(0, 0, 5, 5);
		gbc_lblSelectComm.gridwidth = 2;
		gbc_lblSelectComm.gridx = 0;
		gbc_lblSelectComm.gridy = 1;

		frame.getContentPane().add(lblSelectComm, gbc_lblSelectComm);
		GridBagConstraints gbc_cboxPorts = new GridBagConstraints();
		gbc_cboxPorts.fill = GridBagConstraints.HORIZONTAL;
		gbc_cboxPorts.insets = new Insets(0, 0, 5, 0);
		gbc_cboxPorts.gridwidth = 2;
		gbc_cboxPorts.gridx = 2;
		gbc_cboxPorts.gridy = 1;

		frame.getContentPane().add(cboxPorts, gbc_cboxPorts);
		GridBagConstraints gbc_btnConnect = new GridBagConstraints();
		gbc_btnConnect.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnConnect.insets = new Insets(0, 0, 5, 5);
		gbc_btnConnect.gridwidth = 2;
		gbc_btnConnect.gridx = 0;
		gbc_btnConnect.gridy = 2;

		frame.getContentPane().add(btnConnect, gbc_btnConnect);
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				main.serialPortManager.connect();
				if (main.serialPortManager.getConnectionStatus() == true)
				{
					if (main.serialPortManager.initIOStream() == true)
					{
						main.serialPortManager.initListener();
					}
				}
			}
		});
		GridBagConstraints gbc_btnDisconnect = new GridBagConstraints();
		gbc_btnDisconnect.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDisconnect.insets = new Insets(0, 0, 5, 0);
		gbc_btnDisconnect.gridwidth = 2;
		gbc_btnDisconnect.gridx = 2;
		gbc_btnDisconnect.gridy = 2;

		frame.getContentPane().add(btnDisconnect, gbc_btnDisconnect);
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				main.serialPortManager.disconnect();
				toggleControls();

			}
		});
		GridBagConstraints gbc_lblLeftTitle = new GridBagConstraints();
		gbc_lblLeftTitle.insets = new Insets(0, 0, 5, 5);
		gbc_lblLeftTitle.gridwidth = 2;
		gbc_lblLeftTitle.gridx = 0;
		gbc_lblLeftTitle.gridy = 4;

		frame.getContentPane().add(lblLeftTitle, gbc_lblLeftTitle);
		GridBagConstraints gbc_lblRightTitle = new GridBagConstraints();
		gbc_lblRightTitle.insets = new Insets(0, 0, 5, 0);
		gbc_lblRightTitle.gridwidth = 2;
		gbc_lblRightTitle.gridx = 2;
		gbc_lblRightTitle.gridy = 4;

		frame.getContentPane().add(lblRightTitle, gbc_lblRightTitle);
		GridBagConstraints gbc_lblSelectSelectFile = new GridBagConstraints();
		gbc_lblSelectSelectFile.anchor = GridBagConstraints.WEST;
		gbc_lblSelectSelectFile.insets = new Insets(0, 0, 5, 5);
		gbc_lblSelectSelectFile.gridwidth = 2;
		gbc_lblSelectSelectFile.gridx = 0;
		gbc_lblSelectSelectFile.gridy = 5;

		frame.getContentPane().add(lblSelectSelectFile, gbc_lblSelectSelectFile);
		GridBagConstraints gbc_lblSelectFileTo = new GridBagConstraints();
		gbc_lblSelectFileTo.anchor = GridBagConstraints.EAST;
		gbc_lblSelectFileTo.insets = new Insets(0, 0, 5, 0);
		gbc_lblSelectFileTo.gridwidth = 2;
		gbc_lblSelectFileTo.gridx = 2;
		gbc_lblSelectFileTo.gridy = 5;

		frame.getContentPane().add(lblSelectFileTo, gbc_lblSelectFileTo);
		tFUploadPath.setColumns(1);
		GridBagConstraints gbc_tFUploadPath = new GridBagConstraints();
		gbc_tFUploadPath.fill = GridBagConstraints.HORIZONTAL;
		gbc_tFUploadPath.insets = new Insets(0, 0, 5, 5);
		gbc_tFUploadPath.gridx = 0;
		gbc_tFUploadPath.gridy = 6;

		frame.getContentPane().add(tFUploadPath, gbc_tFUploadPath);
		btnBrowseUpload.setIcon(new ImageIcon(Gui.class.getResource("/figs/Open16.gif")));
		GridBagConstraints gbc_btnBrowseUpload = new GridBagConstraints();
		gbc_btnBrowseUpload.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnBrowseUpload.insets = new Insets(0, 0, 5, 5);
		gbc_btnBrowseUpload.gridx = 1;
		gbc_btnBrowseUpload.gridy = 6;

		frame.getContentPane().add(btnBrowseUpload, gbc_btnBrowseUpload);
		btnBrowseUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				int returnVal = fc.showOpenDialog(Gui.this);
				String logText = "";

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					fileUpload = fc.getSelectedFile();
					//This is where a real application would open the file.
					logText = "Opening: " + fileUpload.getName() + "." + "\n";
					tFUploadPath.setText(logText);
					textAreaDialog.setForeground(Color.black);
					textAreaDialog.append(logText + fileUpload.getAbsolutePath() + "\n");
				} else {
					tFUploadPath.setText("Open command cancelled by user." + "\n");
				}
				tFUploadPath.setCaretPosition(tFUploadPath.getDocument().getLength());
			}
		});		

		tFDownloadPath.setColumns(1);
		GridBagConstraints gbc_tFDownloadPath = new GridBagConstraints();
		gbc_tFDownloadPath.fill = GridBagConstraints.HORIZONTAL;
		gbc_tFDownloadPath.insets = new Insets(0, 0, 5, 5);
		gbc_tFDownloadPath.gridx = 2;
		gbc_tFDownloadPath.gridy = 6;

		frame.getContentPane().add(tFDownloadPath, gbc_tFDownloadPath);
		btnBrowseDownload.setIcon(new ImageIcon(Gui.class.getResource("/figs/Open16.gif")));
		GridBagConstraints gbc_btnBrowseDownload = new GridBagConstraints();
		gbc_btnBrowseDownload.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnBrowseDownload.insets = new Insets(0, 0, 5, 0);
		gbc_btnBrowseDownload.gridx = 3;
		gbc_btnBrowseDownload.gridy = 6;

		frame.getContentPane().add(btnBrowseDownload, gbc_btnBrowseDownload);
		btnBrowseDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int returnVal = fc.showSaveDialog(Gui.this);
				String logText = "";
				
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					fileDownload = fc.getSelectedFile();
					//This is where a real application would save the file.
					logText = "Saving: " + fileDownload.getName() + "." + "\n";
					tFDownloadPath.setText(logText);
					textAreaDialog.setText(logText + fileDownload.getAbsolutePath() + "\n");
				} else {
					tFDownloadPath.setText("Save command cancelled by user." + "\n");
				}
				tFDownloadPath.setCaretPosition(tFDownloadPath.getDocument().getLength());
			}
		});

		GridBagConstraints gbc_btnUpload = new GridBagConstraints();
		gbc_btnUpload.gridwidth = 2;
		gbc_btnUpload.insets = new Insets(0, 0, 5, 5);
		gbc_btnUpload.gridx = 0;
		gbc_btnUpload.gridy = 7;

		frame.getContentPane().add(btnUpload, gbc_btnUpload);
		GridBagConstraints gbc_btnDownload = new GridBagConstraints();
		gbc_btnDownload.gridwidth = 2;
		gbc_btnDownload.insets = new Insets(0, 0, 5, 0);
		gbc_btnDownload.gridx = 2;
		gbc_btnDownload.gridy = 7;
		
		btnUpload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//System.out.println("Data Lines: " + main.fileReadWrite.ReadNumberLines(fileUpload.toString()) );
				//main.fileReadWrite.ReadData(fileUpload.toString());
				main.uploadTask.uploadTask(fileUpload.toString());
			}
		});

		frame.getContentPane().add(btnDownload, gbc_btnDownload);
		GridBagConstraints gbc_scrollPaneDialog = new GridBagConstraints();
		gbc_scrollPaneDialog.gridheight = 3;
		gbc_scrollPaneDialog.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneDialog.gridwidth = 4;
		gbc_scrollPaneDialog.gridx = 0;
		gbc_scrollPaneDialog.gridy = 8;

		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				main.downloadTask.downloadTask(fileDownload.toString());
			}
		});

		frame.getContentPane().add(scrollPaneDialog, gbc_scrollPaneDialog);
		scrollPaneDialog.setViewportView(textAreaDialog);

		GridBagConstraints gbc_panelGraph = new GridBagConstraints();
		gbc_panelGraph.gridwidth = 4;
		gbc_panelGraph.fill = GridBagConstraints.BOTH;
		gbc_panelGraph.gridx = 3;
		gbc_panelGraph.gridy = 10;
	}
}
