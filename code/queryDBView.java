import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.util.*;
    
public class queryDBView implements ActionListener {
	private int maxSize;
	private JFrame win;
	private JPanel partyPanel;
	private JButton addPatron, newPatron, remPatron, finished,gameCount;
	private JList partyList, allBowlers;
	private Vector party, bowlerdb;

	private ControlDeskView controlDesk;

	private String selectedMember,selectedNick;

	public queryDBView(ControlDeskView controlDesk) {

		this.controlDesk = controlDesk;
		win = new JFrame("Queries");
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);

		JPanel colPanel = new JPanel();
		colPanel.setLayout(new GridLayout(1, 3));

		// Party Panel
	 partyPanel = new JPanel();
		partyPanel.setLayout(new FlowLayout());
		partyPanel.setBorder(new TitledBorder("Result"));

		party = new Vector();
		Vector empty = new Vector();
		empty.add("(Empty)");

		partyList = new JList(empty);
		partyList.setFixedCellWidth(120);
		partyList.setVisibleRowCount(5);
		JScrollPane partyPane = new JScrollPane(partyList);
		partyPanel.add(partyPane);

		// Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(5, 1));

		addPatron = new JButton("Top Player");
		JPanel addPatronPanel = new JPanel();
		addPatronPanel.setLayout(new FlowLayout());
		addPatron.addActionListener(this);
		addPatronPanel.add(addPatron);

		remPatron = new JButton("Highest Score");
		JPanel remPatronPanel = new JPanel();
		remPatronPanel.setLayout(new FlowLayout());
		remPatron.addActionListener(this);
		remPatronPanel.add(remPatron);

		newPatron = new JButton("Lowest Score");
		JPanel newPatronPanel = new JPanel();
		newPatronPanel.setLayout(new FlowLayout());
		newPatron.addActionListener(this);
		newPatronPanel.add(newPatron);

        gameCount = new JButton("Most frequent player");
		JPanel newCountPanel = new JPanel();
		newCountPanel.setLayout(new FlowLayout());
		gameCount.addActionListener(this);
		newCountPanel.add(gameCount);

		finished = new JButton("Finish");
		JPanel finishedPanel = new JPanel();
		finishedPanel.setLayout(new FlowLayout());
		finished.addActionListener(this);
		finishedPanel.add(finished);

		buttonPanel.add(addPatronPanel);
		buttonPanel.add(remPatronPanel);
		buttonPanel.add(newPatronPanel);
		//buttonPanel.add(newCountPanel);
		buttonPanel.add(finishedPanel);

		// Clean up main panel
		colPanel.add(partyPanel);
		// colPanel.add(bowlerPanel);
		colPanel.add(buttonPanel);

		win.getContentPane().add("Center", colPanel);

		win.pack();

		// Center Window on Screen
		Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
		win.setLocation(
				((screenSize.width) / 2) - ((win.getSize().width) / 2),
				((screenSize.height) / 2) - ((win.getSize().height) / 2));
		win.show();

	}

	public void actionPerformed(ActionEvent e) {
		query q=new query();
		String  str;
		JFrame f=new JFrame(); 
		if (e.getSource().equals(remPatron)) 
		{
		str=q.highestScore();
		JOptionPane.showMessageDialog(f,"Maximum score till now is"+str); 
		}
		if (e.getSource().equals(addPatron)) 
		{
		 str=q.topPlayer();  
		JOptionPane.showMessageDialog(f,"Best player till now is "+str); 
		}
		if (e.getSource().equals(newPatron)) 
		{
		 str=q.lowestScore();  
		JOptionPane.showMessageDialog(f,"Lowest score till now is "+str); 
		}
		if (e.getSource().equals(gameCount)) 
		{
		 str=q.mostFrequentPlayer();  
		JOptionPane.showMessageDialog(f,"Most freqeunt player till now is "+str); 
		}

		if (e.getSource().equals(finished)) {
			
			win.hide();
		}
	}

	


}

