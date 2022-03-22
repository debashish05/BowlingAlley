import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class NewTieLane  
{

	JPanel[] pins;
	JFrame frame;
	Container cpanel;
	JLabel[][] ballLabel,scoreLabel;
	JPanel[][] balls,scores,ballGrid;

	public NewTieLane() {

		frame = new JFrame("Lane Extend");
		System.out.println("Tie Braker");
		cpanel = frame.getContentPane();
		cpanel.setLayout(new BorderLayout());

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				frame.hide();
			}
		});
		cpanel.add(new JPanel());
	}


	private JPanel makeFrame(Vector<String> Bowlers) {
		JPanel panel = new JPanel();
		System.out.println("Make New Frame for tie braker");
		panel.setLayout(new GridLayout(0, 1));

		balls = new JPanel[2][23];
		ballGrid = new JPanel[2][10];
		pins = new JPanel[2];
		scores = new JPanel[2][10];
		scoreLabel = new JLabel[2][10];
		ballLabel = new JLabel[2][23];

		int i=0;
		while( i <=1)
		{
			int j=0;
			while(j<=6)
			{
				ballLabel[i][j] = new JLabel(" ");
				balls[i][j] = new JPanel();
				balls[i][j].setBorder(
					BorderFactory.createLineBorder(Color.BLACK));
				balls[i][j].add(ballLabel[i][j]);
				j=j+1;
			}
			i=i+1;
		}

		i=0;
		while( i <=1){
			int j = 0;
			while(j<=2){
				ballGrid[i][j] = new JPanel();
				ballGrid[i][j].setLayout(new GridLayout(0, 3));
				ballGrid[i][j].add(new JLabel("  "), BorderLayout.EAST);
				int k=1;
				while(k<=2){
					ballGrid[i][j].add(balls[i][2 * j+k], BorderLayout.EAST);
					k=k+1;
				}
				j=j+1;
			}
			j=0;
			ballGrid[i][j] = new JPanel();
			ballGrid[i][j].setLayout(new GridLayout(0, 3));
			int k=0;
			while(k<=2){
				ballGrid[i][j].add(balls[i][2 * j+k]);
				k=k+1;
			}
			i=i+1;
		}

		i=0;
		while(i<=1){
			pins[i] = new JPanel();
			pins[i].setBorder(BorderFactory.createTitledBorder(Bowlers.get(i)));
			pins[i].setLayout(new GridLayout(0, 3));
			
			int k=0;
			while(k<=2)
			{
				scores[i][k] = new JPanel();
				scoreLabel[i][k] = new JLabel("  ", SwingConstants.CENTER);
				scores[i][k].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				scores[i][k].setLayout(new GridLayout(0, 1));
				scores[i][k].add(ballGrid[i][k], BorderLayout.EAST);
				scores[i][k].add(scoreLabel[i][k], BorderLayout.SOUTH);
				pins[i].add(scores[i][k], BorderLayout.EAST);
				k=k+1;
			}
			panel.add(pins[i]);
			i=i+1;
		}
		System.out.println("Pannel created");
		return panel;
	}

	public int changeScore(int bowlerID,int score,int frameNumber) {
		scoreLabel[bowlerID][frameNumber].setText(String.valueOf(score));
		return 0;
	}

	public int show() {
		frame.show();
		return 0;
	}

	public int hide() {
		frame.hide();
		return 0;
	}

	public void receiveLaneEvent(Vector<String> Bowlers){

		cpanel.removeAll();
		cpanel.add(makeFrame(Bowlers), "Center");

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		Insets buttonMargin = new Insets(4, 4, 4, 4);
		frame.pack();
	}

	public void changeBall(int bowlerID,int score,int ballCell) {
		String symbol="";
		if(score == 10)symbol="X";
		else if(score == -4)symbol="/";
		else if(score == -2)symbol="F";
		else symbol=String.valueOf(score);
		ballLabel[bowlerID][ballCell].setText(symbol);
	}
}