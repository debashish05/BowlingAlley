
/* $Id$
 *
 * Revisions:
 *   $Log: Lane.java,v $
 *   Revision 1.52  2003/02/20 20:27:45  ???
 *   Fouls disables.
 *
 *   Revision 1.51  2003/02/20 20:01:32  ???
 *   Added things.
 *
 *   Revision 1.50  2003/02/20 19:53:52  ???
 *   Added foul support.  Still need to update laneview and test this.
 *
 *   Revision 1.49  2003/02/20 11:18:22  ???
 *   Works beautifully.
 *
 *   Revision 1.48  2003/02/20 04:10:58  ???
 *   Score reporting code should be good.
 *
 *   Revision 1.47  2003/02/17 00:25:28  ???
 *   Added disbale controls for View objects.
 *
 *   Revision 1.46  2003/02/17 00:20:47  ???
 *   fix for event when game ends
 *
 *   Revision 1.43  2003/02/17 00:09:42  ???
 *   fix for event when game ends
 *
 *   Revision 1.42  2003/02/17 00:03:34  ???
 *   Bug fixed
 *
 *   Revision 1.41  2003/02/16 23:59:49  ???
 *   Reporting of sorts.
 *
 *   Revision 1.40  2003/02/16 23:44:33  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.39  2003/02/16 23:43:08  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.38  2003/02/16 23:41:05  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.37  2003/02/16 23:00:26  ???
 *   added mechnanical problem flag
 *
 *   Revision 1.36  2003/02/16 21:31:04  ???
 *   Score logging.
 *
 *   Revision 1.35  2003/02/09 21:38:00  ???
 *   Added lots of comments
 *
 *   Revision 1.34  2003/02/06 00:27:46  ???
 *   Fixed a race condition
 *
 *   Revision 1.33  2003/02/05 11:16:34  ???
 *   Boom-Shacka-Lacka!!!
 *
 *   Revision 1.32  2003/02/05 01:15:19  ???
 *   Real close now.  Honest.
 *
 *   Revision 1.31  2003/02/04 22:02:04  ???
 *   Still not quite working...
 *
 *   Revision 1.30  2003/02/04 13:33:04  ???
 *   Lane may very well work now.
 *
 *   Revision 1.29  2003/02/02 23:57:27  ???
 *   fix on pinsetter hack
 *
 *   Revision 1.28  2003/02/02 23:49:48  ???
 *   Pinsetter generates an event when all pins are reset
 *
 *   Revision 1.27  2003/02/02 23:26:32  ???
 *   ControlDesk now runs its own thread and polls for free lanes to assign queue members to
 *
 *   Revision 1.26  2003/02/02 23:11:42  ???
 *   parties can now play more than 1 game on a lane, and lanes are properly released after games
 *
 *   Revision 1.25  2003/02/02 22:52:19  ???
 *   Lane compiles
 *
 *   Revision 1.24  2003/02/02 22:50:10  ???
 *   Lane compiles
 *
 *   Revision 1.23  2003/02/02 22:47:34  ???
 *   More observering.
 *
 *   Revision 1.22  2003/02/02 22:15:40  ???
 *   Add accessor for pinsetter.
 *
 *   Revision 1.21  2003/02/02 21:59:20  ???
 *   added conditions for the party choosing to play another game
 *
 *   Revision 1.20  2003/02/02 21:51:54  ???
 *   LaneEvent may very well be observer method.
 *
 *   Revision 1.19  2003/02/02 20:28:59  ???
 *   fixed sleep thread bug in lane
 *
 *   Revision 1.18  2003/02/02 18:18:51  ???
 *   more changes. just need to fix scoring.
 *
 *   Revision 1.17  2003/02/02 17:47:02  ???
 *   Things are pretty close to working now...
 *
 *   Revision 1.16  2003/01/30 22:09:32  ???
 *   Worked on scoring.
 *
 *   Revision 1.15  2003/01/30 21:45:08  ???
 *   Fixed speling of received in Lane.
 *
 *   Revision 1.14  2003/01/30 21:29:30  ???
 *   Fixed some MVC stuff
 *
 *   Revision 1.13  2003/01/30 03:45:26  ???
 *   *** empty log message ***
 *
 *   Revision 1.12  2003/01/26 23:16:10  ???
 *   Improved thread handeling in lane/controldesk
 *
 *   Revision 1.11  2003/01/26 22:34:44  ???
 *   Total rewrite of lane and pinsetter for R2's observer model
 *   Added Lane/Pinsetter Observer
 *   Rewrite of scoring algorythm in lane
 *
 *   Revision 1.10  2003/01/26 20:44:05  ???
 *   small changes
 *
 * 
 */

import java.util.*;

public class Lane extends Thread implements PinsetterObserver {

	public LaneSubscriber laneSubscriber;
	private Party party;
	private Pinsetter setter;
	private HashMap scores;

	private boolean gameIsHalted;

	private boolean partyAssigned;
	private boolean gameFinished;
	private Iterator bowlerIterator;
	private int ball;
	private int bowlIndex;
	private int frameNumber;
	private boolean tenthFrameStrike;

	private int[] curScores;
	private ScoreCalculator currentCumulScores;
	private boolean canThrowAgain;

	private int second_highest;
	private int highest;

	private int[][] finalScores;
	private int gameNumber;

	private Bowler currentThrower; // = the thrower who just took a throw

	/**
	 * Lane()
	 * 
	 * Constructs a new lane and starts its thread
	 * 
	 * @pre none
	 * @post a new lane has been created and its thread is executing
	 */
	public Lane() {
		setter = new Pinsetter();
		scores = new HashMap<Bowler, int[]>();
		laneSubscriber = new LaneSubscriber();

		currentCumulScores = new ScoreCalculator(bowlIndex);
		gameIsHalted = false;
		partyAssigned = false;

		gameNumber = 0;

		setter.subscribe(this);

		this.start();
	}

	/**
	 * run()
	 * 
	 * entry point for execution of this lane
	 */

	public void tie() {
		System.out.println("Resolving Tie");
		if (party.getMembers().size() > 1) {

			Vector<Integer> vec = new Vector<Integer>();
			for (int i = 0; i < party.getMembers().size(); ++i) 
			{
				vec.add(currentCumulScores.cumulScores[i][9]);
			}
			Collections.sort(vec);
			int second_highest_score, highest_score;
			second_highest_score = vec.get(party.getMembers().size() - 2);
			highest_score = vec.get(party.getMembers().size() - 1);
			for (int i = 0; i < party.getMembers().size(); ++i) {
				if (second_highest_score == currentCumulScores.cumulScores[i][9])
					second_highest = i;
				if (highest_score == currentCumulScores.cumulScores[i][9])
					highest = i;
			}

			NewTieLane newLane = new NewTieLane();
			Vector<String> vec2 = new Vector();
			vec2.add(((Bowler) party.getMembers().get(highest)).getNickName());
			vec2.add(((Bowler) party.getMembers().get(second_highest)).getNickName());
			newLane.receiveLaneEvent(vec2);
			newLane.show();
			frameNumber++;
			canThrowAgain = true;
			tenthFrameStrike = false;
			ball = -1;
			int bowlerID = 1, frame_no = 0;
			boolean value = true;
			int[] score_val, cell_no;
			score_val = new int[2];
			boolean[] prev_strike, pins;
			pins = new boolean[10];
			prev_strike = new boolean[2];
			prev_strike[0] = false;
			prev_strike[1] = false;

			cell_no = new int[2];
			cell_no[0] = 1;
			cell_no[1] = 0;
			Random rnd = new Random();

			int i = 0;
			while (i < 10) {
				pins[i] = true;
				i = i + 1;
			}
			while (value && frame_no < 3) {
				int count = 0; // Initializing count
				double skill = rnd.nextDouble(); // skill? Another random variable

				ThrowWindow new_throw = new ThrowWindow();
				new_throw.getResult();
				new_throw.destroy();

				i = 0;
				while (i < 10) {
					if (pins[i]) { // If pins[i] is still not knocked down
						double pinluck = rnd.nextDouble();

						if (((skill + pinluck) / 2.0 * 1.2) > .5) { // Improve the calc here
							pins[i] = false; // Some random math to put the pin down
						}
						if (!pins[i]) {
							count++;
						}
					}
					i = i + 1;
				}

				System.out.format("Count:%d\n", count);
				if (ball == -1) {
					newLane.changeScore(bowlerID, second_highest_score + count, frame_no);
					newLane.changeBall(bowlerID, count, cell_no[1]++);
				} else {

					newLane.changeBall(bowlerID, count, cell_no[bowlerID]++);
				}
				score_val[bowlerID] += count;
				if (second_highest_score + count > highest_score && ball == -1) {
					score_val[bowlerID] = second_highest_score;
					score_val[bowlerID] += count;

					if (bowlerID == 1)
						bowlerID = 0;
					score_val[bowlerID] = highest_score;
					ball = 0;

				} else if (ball == -1)
					value = false;
				else {
					ball++;
					if (ball == 2) {
						i = 0;
						while (i < 10) {
							pins[i] = true;
							i = i + 1;
						}
						newLane.changeScore(bowlerID, score_val[bowlerID], frame_no);
						ball = 0;
						if (bowlerID == 1)
							frame_no++;
						bowlerID = 1 - bowlerID;

					}
				}

			}
			if (ball != -1) {
				currentCumulScores.cumulScores[highest][9] = score_val[0];
				currentCumulScores.cumulScores[second_highest][9] = score_val[1];
			}

		}
	}

	public void gameNotFinished() {
		while (gameIsHalted) {
			try {
				sleep(10);
			} catch (Exception e) {
			}
		}

		if (bowlerIterator.hasNext()) {
			currentThrower = (Bowler) bowlerIterator.next();

			canThrowAgain = true;
			tenthFrameStrike = false;
			ball = 0;
			while (canThrowAgain) {

				ThrowWindow new_throw = new ThrowWindow();
				int result = new_throw.getResult();
				new_throw.destroy();

				setter.ballThrown(); // simulate the thrower's ball hitting
				ball++;
			}

			if (frameNumber == 9) {
				finalScores[bowlIndex][gameNumber] = currentCumulScores.getFinalScore();
				try {
					Date date = new Date();
					String dateString = "" + date.getHours() + ":" + date.getMinutes() + " " + date.getMonth()
							+ "/" + date.getDay() + "/" + (date.getYear() + 1900);
					ScoreHistoryFile.addScore(currentThrower.getNick(), dateString,
							Integer.toString(currentCumulScores.getFinalScore()));
				} catch (Exception e) {
					System.err.println("Exception in addScore. " + e);
				}
			}

			setter.reset();
			bowlIndex++;
			currentCumulScores.setBowlIndex(bowlIndex);

		} else {
			frameNumber++;
			resetBowlerIterator();
			bowlIndex = 0;
			currentCumulScores.setBowlIndex(bowlIndex);
			if (frameNumber > 9) {
				tie();
				gameFinished = true;
				gameNumber++;
			}

		}
	}

	public void gameFinished() {
		EndGamePrompt egp = new EndGamePrompt(((Bowler) party.getMembers().get(0)).getNickName() + "'s Party");
		int result = egp.getResult();
		egp.distroy();

		System.out.println("result was: " + result);

		// TODO: send record of scores to control desk
		if (result == 1) { // yes, want to play again
			resetScores();
			currentCumulScores.reset(party.getMembers().size());
			resetBowlerIterator();

		} else if (result == 2) {// no, dont want to play another game
			Vector printVector;
			EndGameReport egr = new EndGameReport(
					((Bowler) party.getMembers().get(0)).getNickName() + "'s Party", party);
			printVector = egr.getResult();
			partyAssigned = false;
			Iterator scoreIt = party.getMembers().iterator();
			party = null;
			partyAssigned = false;
			laneSubscriber.publish(lanePublish());

			int myIndex = 0;
			while (scoreIt.hasNext()) {
				Bowler thisBowler = (Bowler) scoreIt.next();
				ScoreReport sr = new ScoreReport(thisBowler, finalScores[myIndex++], gameNumber);
				sr.sendEmail(thisBowler.getEmail());
				Iterator printIt = printVector.iterator();

				while (printIt.hasNext()) {
					if (thisBowler.getNick().equals(printIt.next())) {
						System.out.println("Printing " + thisBowler.getNick());
						sr.sendPrintout();
					}
				}

			}
		}
	}

	public void run() {

		while (true) {
			if (partyAssigned) {
				if (!gameFinished) { // we have a party on this lane,
					// so next bower can take a throw
					gameNotFinished();

				} else {
					gameFinished();
				}
			}
			try {
				sleep(10);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * recievePinsetterEvent()
	 * 
	 * recieves the thrown event from the pinsetter
	 *
	 * @pre none
	 * @post the event has been acted upon if desiered
	 * 
	 * @param pe The pinsetter event that has been received.
	 */

	public void receivePinsetterEvent(PinsetterEvent pe) {

		int throwNo = pe.getThrowNumber();
		int totalPinsDownOnThisThrow = pe.pinsDownOnThisThrow();

		if (totalPinsDownOnThisThrow >= 0) { // this is a real throw
			markScore(currentThrower, frameNumber + 1, throwNo, totalPinsDownOnThisThrow);

			// next logic handles the ?: what conditions don't allow them another throw?
			// handle the case of 10th frame first
			if (frameNumber == 9) {
				if (pe.totalPinsDown() == 10) {
					setter.resetPins();
					tenthFrameStrike = (throwNo == 1);
				}

				canThrowAgain = (throwNo != 3);
			} else { // it's not the 10th frame

				if (totalPinsDownOnThisThrow == 10 || throwNo == 2) { // threw a strike
					canThrowAgain = false;
				} else if (throwNo == 3)
					System.out.println("I'm here...");
			}
		}
	}

	/**
	 * resetBowlerIterator()
	 * 
	 * sets the current bower iterator back to the first bowler
	 * 
	 * @pre the party as been assigned
	 * @post the iterator points to the first bowler in the party
	 */
	private void resetBowlerIterator() {
		bowlerIterator = (party.getMembers()).iterator();
	}

	/**
	 * resetScores()
	 * 
	 * resets the scoring mechanism, must be called before scoring starts
	 * 
	 * @pre the party has been assigned
	 * @post scoring system is initialized
	 */
	private void resetScores() {
		Iterator bowlIt = (party.getMembers()).iterator();

		while (bowlIt.hasNext()) {
			int[] toPut = new int[25];
			for (int i = 0; i != 25; i++) {
				toPut[i] = -1;
			}
			scores.put(bowlIt.next(), toPut);
		}

		gameFinished = false;
		frameNumber = 0;
	}

	/**
	 * assignParty()
	 * 
	 * assigns a party to this lane
	 * 
	 * @pre none
	 * @post the party has been assigned to the lane
	 * 
	 * @param theParty Party to be assigned
	 */
	public void assignParty(Party theParty) {
		party = theParty;
		resetBowlerIterator();
		partyAssigned = true;

		curScores = new int[party.getMembers().size()];
		currentCumulScores.reset(party.getMembers().size());
		finalScores = new int[party.getMembers().size()][128]; // Hardcoding a max of 128 games, bite me.
		gameNumber = 0;

		resetScores();
	}

	/**
	 * markScore()
	 *
	 * Method that marks a bowlers score on the board.
	 * 
	 * @param Cur   The current bowler
	 * @param frame The frame that bowler is on
	 * @param ball  The ball the bowler is on
	 * @param score The bowler's score
	 */
	private void markScore(Bowler Cur, int frame, int ball, int score) {
		int[] curScore;
		int index = ((frame - 1) * 2 + ball);

		curScore = (int[]) scores.get(Cur);

		curScore[index - 1] = score;
		scores.put(Cur, curScore);
		currentCumulScores.getScore(frame, ball, (int[]) scores.get(Cur));
		laneSubscriber.publish(lanePublish());
	}

	/**
	 * lanePublish()
	 *
	 * Method that creates and returns a newly created laneEvent
	 * 
	 * @return The new lane event
	 */
	private LaneEvent lanePublish() {
		LaneEvent laneEvent = new LaneEvent(party, bowlIndex, currentThrower, ball, gameIsHalted);
		laneEvent.setScore(currentCumulScores.getCumulScores(), scores, frameNumber + 1, curScores);
		return laneEvent;
	}

	/**
	 * isPartyAssigned()
	 * 
	 * checks if a party is assigned to this lane
	 * 
	 * @return true if party assigned, false otherwise
	 */
	public boolean isPartyAssigned() {
		return partyAssigned;
	}

	/**
	 * Accessor to get this Lane's pinsetter
	 * 
	 * @return A reference to this lane's pinsetter
	 */

	public Pinsetter getPinsetter() {
		return setter;
	}

	/**
	 * Pause the execution of this game
	 */
	public void pauseGame() {
		gameIsHalted = true;
		laneSubscriber.publish(lanePublish());
	}

	/**
	 * Resume the execution of this game
	 */
	public void unPauseGame() {
		gameIsHalted = false;
		laneSubscriber.publish(lanePublish());
	}

}
