
/**
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LaneStatusView implements ActionListener, LaneObserver, PinsetterObserver {

	private JPanel jp;

	private JLabel curBowler, pinsDown;
	private JButton viewLane;
	private JButton viewPinSetter, maintenance;

	private PinSetterView psv;
	private LaneView lv;
	private Lane lane;
	int laneNum;

	boolean laneShowing;
	boolean psShowing;

	public LaneStatusView(Lane lane, int laneNum) {

		this.lane = lane;
		this.laneNum = laneNum;

		laneShowing = false;
		psShowing = false;

		psv = new PinSetterView(laneNum);
		Pinsetter ps = lane.getPinsetter();
		ps.subscribe(psv);

		lv = new LaneView(lane, laneNum);
		lane.laneSubscriber.subscribe(lv);

		jp = new JPanel();
		jp.setLayout(new FlowLayout());
		JLabel cLabel = new JLabel("Now Bowling: ");
		curBowler = new JLabel("(no one)");
		JLabel pdLabel = new JLabel("Pins Down: ");
		pinsDown = new JLabel("0");

		// Button Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		viewLane = new JButton("View Lane");
		JPanel viewLanePanel = new JPanel();
		viewLanePanel.setLayout(new FlowLayout());
		viewLane.addActionListener(this);
		viewLanePanel.add(viewLane);

		viewPinSetter = new JButton("Pinsetter");
		JPanel viewPinSetterPanel = new JPanel();
		viewPinSetterPanel.setLayout(new FlowLayout());
		viewPinSetter.addActionListener(this);
		viewPinSetterPanel.add(viewPinSetter);

		maintenance = new JButton("     ");
		maintenance.setBackground(Color.GREEN);
		JPanel maintenancePanel = new JPanel();
		maintenancePanel.setLayout(new FlowLayout());
		maintenance.addActionListener(this);
		maintenancePanel.add(maintenance);

		viewLane.setEnabled(false);
		viewPinSetter.setEnabled(false);

		buttonPanel.add(viewLanePanel);
		buttonPanel.add(viewPinSetterPanel);
		buttonPanel.add(maintenancePanel);

		jp.add(cLabel);
		jp.add(curBowler);
		jp.add(pdLabel);
		jp.add(pinsDown);

		jp.add(buttonPanel);

	}

	public JPanel showLane() {
		return jp;
	}

	public void actionPerformed(ActionEvent e) {
		
		if (lane.isPartyAssigned() && e.getSource().equals(viewPinSetter)) {
			
			if (psShowing) {
				psv.hide();
			} 
			else {
				psv.show();
			}
			psShowing=!psShowing;
		}
		if (e.getSource().equals(viewLane) && lane.isPartyAssigned()) {

			if (laneShowing) {
				lv.hide();
			} 
			else {
				lv.show();
			}
			laneShowing=!laneShowing;
		}
		if (e.getSource().equals(maintenance) && lane.isPartyAssigned()) {

			lane.unPauseGame();
			maintenance.setBackground(Color.GREEN);
		}
	}

	public void receiveLaneEvent(LaneEvent le) {
		curBowler.setText((le.getBowler()).getNickName());
		if (le.isMechanicalProblem()) {
			maintenance.setBackground(Color.RED);
		}
		if (lane.isPartyAssigned()) {
			viewLane.setEnabled(true);
			viewPinSetter.setEnabled(true);
		}
		else {
			viewLane.setEnabled(false);
			viewPinSetter.setEnabled(false);
		}
	}

	public void receivePinsetterEvent(PinsetterEvent pe) {
		pinsDown.setText((new Integer(pe.totalPinsDown())).toString());

	}

}
