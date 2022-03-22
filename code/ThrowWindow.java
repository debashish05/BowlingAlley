import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.text.*;

public class ThrowWindow implements ActionListener {

	private JFrame win;
	private int emoticonId;
	private JButton yesButton;
	private int result;

	public ThrowWindow( ) {

		result =0;
		win = new JFrame("Start");//Emoticon
		win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);
		win.setSize(512,512);
		
		JPanel colPanel = new JPanel();
		colPanel.setLayout(new GridLayout( 2, 1 ));
		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new FlowLayout());
		
		JLabel message = new JLabel( " Press To Start Bowling" );
		labelPanel.add( message );
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2));

		yesButton = new JButton("Yes");
		JPanel yesButtonPanel = new JPanel();
		yesButtonPanel.setLayout(new FlowLayout());
		yesButton.addActionListener(this);
		yesButtonPanel.add(yesButton);
		buttonPanel.add(yesButton);
		colPanel.add(labelPanel);
		colPanel.add(buttonPanel);
		win.getContentPane().add("Center", colPanel);
		win.pack();

		Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
		win.setLocation(
			((screenSize.width) / 2) - ((win.getSize().width) / 2),
			((screenSize.height) / 2) - ((win.getSize().height) / 2));
		win.show();

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(yesButton)) {		
			result=1;
		}
	}

	public int getResult() {
		while ( result == 0 ) {
			try {
				Thread.sleep(10);
			} catch ( InterruptedException e ) {
				System.err.println( "Interrupted" );
			}
		}
		return result;	
	}

	public void destroy() {
		win.hide();
	}

}
