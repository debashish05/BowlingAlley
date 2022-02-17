// nikita 

/* AddPartyView.java
 *
 *  Version:
 * 		 $Id$
 * 
 *  Revisions:
 * 		$Log: AddPartyView.java,v $
 * 		Revision 1.7  2003/02/20 02:05:53  ???
 * 		Fixed addPatron so that duplicates won't be created.
 * 		
 * 		Revision 1.6  2003/02/09 20:52:46  ???
 * 		Added comments.
 * 		
 * 		Revision 1.5  2003/02/02 17:42:09  ???
 * 		Made updates to migrate to observer model.
 * 		
 * 		Revision 1.4  2003/02/02 16:29:52  ???
 * 		Added ControlDeskEvent and ControlDeskObserver. Updated Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of ControlDesk.
 * 		
 * 
 */

/**
 * Class for GUI components need to add a party
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.util.*;

/**
 * Constructor for GUI used to Add Parties to the waiting party queue.
 * 
 */

public class AddPartyView implements ActionListener, ListSelectionListener {

	private int maxSize;

	private JFrame win;
	private JButton addPatron, newPatron, remPatron, finished;
	private JList partyList, allBowlers;
	private Vector party, bowlerdb;

	private ControlDeskView controlDesk;

	private String selectedNick, selectedMember;

	public AddPartyView(ControlDeskView controlDesk, int max) {

		this.controlDesk = controlDesk;
		maxSize = max;

		win = new JFrame("Add Party");
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);

		JPanel colPanel = new JPanel();
		colPanel.setLayout(new GridLayout(1, 3));

		// Party Panel
		JPanel partyPanel = new JPanel();
		partyPanel.setLayout(new FlowLayout());
		partyPanel.setBorder(new TitledBorder("Your Party"));

		party = new Vector();
		Vector empty = new Vector();
		empty.add("(Empty)");

		partyList = new JList(empty);
		partyList.setFixedCellWidth(120);
		partyList.setVisibleRowCount(5);
		partyList.addListSelectionListener(this);
		JScrollPane partyPane = new JScrollPane(partyList);
		partyPanel.add(partyPane);

		// Bowler Database
		JPanel bowlerPanel = new JPanel();
		bowlerPanel.setLayout(new FlowLayout());
		bowlerPanel.setBorder(new TitledBorder("Bowler Database"));

		try {
			bowlerdb = new Vector(BowlerFile.getBowlers());
		} catch (Exception e) {
			System.err.println("File Error");
			bowlerdb = new Vector();
		}
		allBowlers = new JList(bowlerdb);
		allBowlers.setVisibleRowCount(8);
		allBowlers.setFixedCellWidth(120);
		JScrollPane bowlerPane = new JScrollPane(allBowlers);
		bowlerPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		allBowlers.addListSelectionListener(this);
		bowlerPanel.add(bowlerPane);

		// Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4, 1));

		addPatron = new JButton("Add to Party");
		JPanel addPatronPanel = new JPanel();
		createObjects(addPatron, addPatronPanel, this, buttonPanel);

		remPatron = new JButton("Remove Member");
		JPanel remPatronPanel = new JPanel();
		createObjects(remPatron, remPatronPanel, this, buttonPanel);

		newPatron = new JButton("New Patron");
		JPanel newPatronPanel = new JPanel();
		createObjects(newPatron, newPatronPanel, this, buttonPanel);

		finished = new JButton("Finished");
		JPanel finishedPanel = new JPanel();
		createObjects(finished, finishedPanel, this, buttonPanel);

		// Clean up main panel
		colPanel.add(partyPanel);
		colPanel.add(bowlerPanel);
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

	private void createObjects(JButton button, JPanel panel, AddPartyView ob, JPanel buttonPanel) {
		panel.setLayout(new FlowLayout());
		button.addActionListener(ob);
		panel.add(button);
		buttonPanel.add(panel);
	}

	public void addParty(ActionEvent e) {

		if (party.contains(selectedNick)) {
			System.err.println("Member already in Party");
		} else {
			party.add(selectedNick);
			partyList.setListData(party);
		}

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(addPatron) && selectedNick != null && party.size() < maxSize)
			addParty(e);

		if (e.getSource().equals(remPatron) && selectedMember != null) {
			party.removeElement(selectedMember);
			partyList.setListData(party);
		}
		if (e.getSource().equals(newPatron)) {
			NewPatronView newPatron = new NewPatronView(this);
		}
		if (e.getSource().equals(finished)) {
			if (party != null && !party.isEmpty()) {
				controlDesk.updateAddParty(this);
			}
			win.hide();
		}
	}

	/**
	 * Handler for List actions
	 * 
	 * @param e the ListActionEvent that triggered the handler
	 */

	public void valueChanged(ListSelectionEvent e) {
		if (e.getSource().equals(allBowlers)) {
			selectedNick = ((String) ((JList) e.getSource()).getSelectedValue());
		}
		if (e.getSource().equals(partyList)) {
			selectedMember = ((String) ((JList) e.getSource()).getSelectedValue());
		}
	}

	/**
	 * Accessor for Party
	 */

	/**
	 * Called by NewPatronView to notify AddPartyView to update
	 * 
	 * @param newPatron the NewPatronView that called this method
	 */

	public void updateNewPatron(NewPatronView newPatron) {
		try {
			Bowler checkBowler = BowlerFile.getBowlerInfo(newPatron.getNick());
			if (checkBowler == null) {
				BowlerFile.putBowlerInfo(
						newPatron.getNick(),
						newPatron.getFull(),
						newPatron.getEmail());
				bowlerdb = new Vector(BowlerFile.getBowlers());
				allBowlers.setListData(bowlerdb);
				party.add(newPatron.getNick());
				partyList.setListData(party);
			} else {
				System.err.println("A Bowler with that name already exists.");
			}
		} catch (Exception e2) {
			System.err.println("File I/O Error");
		}
	}

	/**
	 * Accessor for Party
	 */

	public Vector getParty() {
		return party;
	}

}