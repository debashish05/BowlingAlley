import java.util.*;
import java.awt.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;




public class Smiley {
	private JFrame win;
	private int SmileyId;
	public Smiley( int Id ) {
		SmileyId = Id;
		win = new JFrame("Smiley");
		
        win.getContentPane().setLayout(new BorderLayout());
		((JPanel) win.getContentPane()).setOpaque(false);
		win.setSize(512,512);
		
        if(SmileyId == 0) {
			win.add(new JLabel(new ImageIcon("happy.jpg")));
		} else {
			win.add(new JLabel(new ImageIcon("sad.png")));
		}
		win.show();
		try {
			Thread.sleep(500);				// pinsetter is where delay will be in a real game
		} catch (Exception e) {}
		win.hide();
	}

}