package GUI;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.json.JSONException;

import ADT.Contractor;
import Algorithms.Search;
import Algorithms.Sort;
import GUI.ButtonListeners.DistButton;
import Read.DataReader;
import Read.DataReaderForInterface;
import Read.Reviews;

/**
 * Class that includes all button action listeners used by the GUI
 * @author Ninos Yomo
 * @version 1.0
 **/
public class ButtonListeners  {

	//ActionListener for the "Exit" button when the main
	//frame is enabled, else, we are closing the "find" buttons'
	//pop up window.
	public class CloseFrame implements ActionListener {
	    @Override
	    public void actionPerformed(ActionEvent e) {
	    	boolean confirmClose = confirmClose();
			if (confirmClose && MainMenu.frame.isEnabled()) {closeProgram();}
			if (confirmClose && !MainMenu.frame.isEnabled()) {closeFrame();}
	    }
	}
	
	//WindowListener for the "x" button on the top of the 
	//window panel.
	public class CloseWindow implements WindowListener {
		@Override
		public void windowClosing(WindowEvent e) {
	    	boolean confirmClose = confirmClose();
			if (confirmClose && MainMenu.frame.isEnabled()) {closeProgram();}
			if (confirmClose && !MainMenu.frame.isEnabled()) {closeFrame();}
		}
		@Override public void windowDeactivated(WindowEvent e) {} //Mandatory
		@Override public void windowDeiconified(WindowEvent e) {}
		@Override public void windowIconified(WindowEvent e) {}
		@Override public void windowOpened(WindowEvent e) {}
		@Override public void windowClosed(WindowEvent e) {}
		@Override public void windowActivated(WindowEvent e) {}
	}
	
	//Shows dialog box when a user clicks a button that is used for closing 
	//the program and confirms if the user would like to close the window.
	private static boolean confirmClose() {
		if (JOptionPane.showConfirmDialog(MainMenu.frame, 
				"Are you sure to close this window?", "Closing Confirmation", 
			    JOptionPane.YES_NO_OPTION,
			    JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){
			return true;
		}
		return false;
	}
	
	
	//Find Button which loads the new frame for finding a contractor.
		class FindButton implements ActionListener {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	
		    	MainMenu.frame.setEnabled(false);
		    	
		    	JComboBox<String> states = new JComboBox<>(DataReaderForInterface.readStates());
		    	
		    	states.addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent e) {
		            	MainMenu.newFramePane.remove(MainMenu.newFramePane.getComponentCount()-5);
		            	MainMenu.cities = new JComboBox<>(DataReaderForInterface.readCities(states.getSelectedItem().toString()));
		            	MainMenu.newFramePane.add(MainMenu.cities, MainMenu.newFramePane.getComponentCount()-4);
		            	MainMenu.newFrame.add(MainMenu.newFramePane);
		            	MainMenu.newFrame.setVisible(true);
		            }          
		         });
		    	
		    	JComboBox<String> speciality = new JComboBox<>(DataReaderForInterface.readSpecialities());
		    	
		    	JLabel stateMsg = new JLabel("Please select your state:");
		    	JLabel cityMsg = new JLabel("Please select your city:");
		    	JLabel specialityMsg = new JLabel("Please select your work:");
		    	
		    	stateMsg.setAlignmentX(250);
		    	cityMsg.setAlignmentX(250);
		    	specialityMsg.setAlignmentX(250);
		    	
		    	JButton cancel = new JButton("Cancel");
		        cancel.addActionListener(MainMenu.CloseFrame);
		        
		    	JButton find = new JButton("Search");
		    	find.addActionListener(new ActionListener() {
		            public void actionPerformed(ActionEvent e) { 
		            	ButtonListeners.closeFrame();
		            	ArrayList<Contractor> allCons = DataReader.readContractors();
		            	Sort.sort(allCons);
		            	Contractor ideal = new Contractor(MainMenu.cities.getSelectedItem().toString().trim(), states.getSelectedItem().toString().trim(), speciality.getSelectedItem().toString().trim());
		            	System.out.println(MainMenu.cities.getSelectedItem().toString().trim());
		            	System.out.println(states.getSelectedItem().toString().trim());
		            	System.out.println(speciality.getSelectedItem().toString().trim());

		            	try {
		            		ArrayList<Contractor> results = Search.search(allCons, ideal, "Reviews");
		            		MainMenu.model.setRowCount(0);
							for (int i = 0; i < results.size(); i++) {
			    	        	Contractor c = results.get(i);
			    	        	System.out.println(c.getContractorName());
			    	        	MainMenu.model.addRow(new Object[] {c.getContractorName(),c.getLicenseNumber(),c.getState(),
			    	        			c.getCity(),c.getAddress(),c.getNumber(), c.getSpecialty(),c.avgReview(MainMenu.map)});
			    	        }
						} catch (IOException e1) {
							e1.printStackTrace();
						}
		            }    
		         });
		    	
		    	MainMenu.newFrame.setSize(500,300);
		    	MainMenu.newFrame.setLocationRelativeTo(null);
		    	MainMenu.newFrame.setAlwaysOnTop(true);
		    	MainMenu.newFramePane.setLayout(new BoxLayout(MainMenu.newFramePane, BoxLayout.Y_AXIS));
		    	MainMenu.newFramePane.add(stateMsg);
		    	MainMenu.newFramePane.add(states);			
		    	MainMenu.newFramePane.add(cityMsg);
		    	MainMenu.newFramePane.add(MainMenu.cities);				
		        MainMenu.newFramePane.add(specialityMsg);
		        MainMenu.newFramePane.add(speciality);
		        MainMenu.newFramePane.add(find);
		        MainMenu.newFramePane.add(cancel);
		        MainMenu.newFrame.add(MainMenu.newFramePane);
		        MainMenu.newFrame.addWindowListener(MainMenu.CloseWindow);
		        MainMenu.newFrame.pack();
		        MainMenu.newFrame.setVisible(true);
		    }
		}
		
		class DistButton implements ActionListener {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	ButtonListeners buttonListeners = new ButtonListeners();
		    	MainMenu.frame.setEnabled(false);
		    	MainMenu.newFramePane.removeAll();
		    	MainMenu.newFrame.dispose();
		    	MainMenu.newFrame.setSize(500,300);
		    	MainMenu.newFrame.setLocationRelativeTo(null);
		    	MainMenu.newFrame.setAlwaysOnTop(true);
		    	
		    	MainMenu.newFramePane.setLayout(new BoxLayout(MainMenu.newFramePane, BoxLayout.Y_AXIS));
		    	JLabel msg = new JLabel("Please input address in this format: 17+Sussex+Crt+Hamilton,ON,CA");
		    	JTextArea address = new JTextArea("Enter your address here!");
		    	JTextArea license = new JTextArea("Enter contractor license here!");
		    	JScrollPane jScrollPane = new JScrollPane(address);
		    	JScrollPane jScrollPane2 =new JScrollPane(license);
		    	JButton submit = new JButton("Submit");
		    	jScrollPane.getViewport().setPreferredSize(new Dimension(50,5));
		    	jScrollPane2.getViewport().setPreferredSize(new Dimension(50,5));
		    	MainMenu.newFramePane.add(msg);
		    	MainMenu.newFramePane.add(jScrollPane);
		    	MainMenu.newFramePane.add(jScrollPane2);
		    	MainMenu.newFramePane.add(submit);
		        MainMenu.newFrame.add(MainMenu.newFramePane);
		        MainMenu.newFrame.addWindowListener(MainMenu.CloseWindow);
		        MainMenu.newFrame.pack();
		        MainMenu.newFrame.setVisible(true);
		        
		        submit.addActionListener(new ActionListener() {
				    @Override
				    public void actionPerformed(ActionEvent e) {
				    	
				    	MainMenu.newFrame.setEnabled(false);
				    	
				    	try {
				    		String from = address.getText();
							Contractor found = Search.searchByLicense(license.getText());
							if (found == null) {
								JOptionPane.showMessageDialog(null, "No Contractor Found!", "InfoBox: ", JOptionPane.INFORMATION_MESSAGE);
								MainMenu.newFrame.setEnabled(true);
							}
							if (found != null) {
								String to = found.getAddress() + " " + found.getCity() + "," + found.getState() + "," + "US";
								to = to.replace(" ", "+");
								String dist = Graphing.DistanceGet.getDriveDist(from, to);
								JOptionPane.showMessageDialog(null, "Distance to Contractor: " + dist, "InfoBox: ", JOptionPane.INFORMATION_MESSAGE);
								MainMenu.newFrame.setEnabled(true);
							}
						} catch (IOException | JSONException e1) {
							e1.printStackTrace();
						}
				    }
				});
		    }
	    }
		
	
	
	
	//When the user has selected a button to close the program
	// and has confirmed their choice this function will close
	//the program.
	private static void closeProgram() {
		try {Reviews.writeMapToFile(MainMenu.map, "Reviews.txt");} 
		catch (FileNotFoundException | UnsupportedEncodingException e1) {e1.printStackTrace();}
		System.exit(0);
	}
	
	//Closes the pop up frame when confirmed to do so.
	protected static void closeFrame() {
    	MainMenu.frame.setEnabled(true);
    	MainMenu.newFrame.setVisible(false);
    	MainMenu.newFramePane.removeAll();
    	MainMenu.newFrame.dispose();
	}
	
}
