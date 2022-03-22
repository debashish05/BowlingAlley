import javax.swing.JOptionPane;
public class drive {

	public static void main(String[] args) {

		int numLanes = 3;
		int maxPatronsPerParty=6;
		
		maxPatronsPerParty = Integer.parseInt(JOptionPane.showInputDialog("Please enter number of maxPatrons per party : "));

		
		ControlDesk controlDesk = new ControlDesk( numLanes ) ;

		ControlDeskView cdv = new ControlDeskView( controlDesk, maxPatronsPerParty);
		controlDesk.subscribe( cdv );
	}
}
