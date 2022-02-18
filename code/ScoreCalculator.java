public class ScoreCalculator {

    private int[][] cumulScores;
    private int bowlIndex;

    public ScoreCalculator(int bowlVal) {
        bowlIndex = bowlVal;
    }

    public void setBowlIndex(int val) {
        bowlIndex = val;
    }

    public void reset(int partySize) {
        cumulScores = new int[partySize][10];
        for (int i = 0; i < partySize; i++) {
            for (int j = 0; j < 10; j++) {
                cumulScores[i][j] = 0;
            }
        }
    }

    public int getFinalScore() {
        return cumulScores[bowlIndex][9];
    }

    public int[][] getCumulScores() {
        return cumulScores;
    }

    public int calculateMaxSum(int cumulScores[][], int cell, int bowlIndex) {

        int maxSum = 0;
        int prev = cumulScores[bowlIndex][0];
        int curr = 0, diff = 0;

        for (int j = 1; j <= cell; j++) {
            curr = cumulScores[bowlIndex][j];
            diff = curr - prev;

            maxSum = Math.max(maxSum, diff);

            prev = curr;
        }
        return maxSum;
    }

    public void calculateCummulativeScore(int i, int curScore[], int current) {

        if (i == 1 && i < current - 1) {
            cumulScores[bowlIndex][i / 2] -= curScore[i + 1] / 2;
        } else {
            int maxSum = calculateMaxSum(cumulScores, (i / 2) - 1, bowlIndex);
            cumulScores[bowlIndex][i / 2] -= maxSum / 2;
        }
    }

    public boolean checkStrike(int i, int curScore[]) {
        if (curScore[i + 2] != -1 && (curScore[i + 3] != -1 || curScore[i + 4] != -1)) {
            strike(curScore, i);
            return false;
        }
        return true;
    }

    public boolean stopCondition(int i, int current, int curScore[]) {
        return (i < current && i % 2 == 0 && curScore[i] == 10 && i < 18 && checkStrike(i, curScore));
    }

    public boolean scoreHelper(int i, int current, int[] curScore) {

        int now_score = curScore[i];

        if (i % 2 == 1 && i < 19) {
            if (now_score == -2 && curScore[i - 1] == -2) {
                calculateCummulativeScore(i, curScore, current);
            } else if (now_score + curScore[i - 1] == 10 && i < current - 1) {

                // This ball was a the second of a spare.
                // Also, we're not on the current ball.
                // Add the next ball to the ith one in cumul.
                cumulScores[bowlIndex][i / 2] += curScore[i + 1] + now_score;
            }
        } else if (stopCondition(i, current, curScore)) {
            // This ball is the first ball, and was a strike.
            // If we can get 2 balls after it, good add them to cumul.
            return true;
        } else {
            // We're dealing with a normal throw, add it and be on our way.
            normal(curScore, i);
        }
        return false;
    }

    public int getScore(int frame, int ball, int[] curScore) { // remove return type,remove total score
        int totalScore = 0;
        for (int i = 0; i < 10; i++) {
            cumulScores[bowlIndex][i] = 0;
        }

        int current = 2 * (frame - 1) + ball - 1;
        // Iterate through each ball until the current one.

        for (int i = 0; i < current + 2; i++) {
            if (scoreHelper(i, current, curScore))
                break;
        }

        return totalScore;
    }

    private void normal(int[] curScore, int i) {
        int add = (curScore[i] != -2) ? curScore[i] : 0;

        if (i < 18) {
            if (i % 2 == 0 && i != 0) {
                // add his last frame's cumul to this ball, make it this frame's cumul.
                add += cumulScores[bowlIndex][i / 2 - 1];
            } else if (curScore[i] != -1) {
                cumulScores[bowlIndex][i / 2] += add;
            }
            cumulScores[bowlIndex][i / 2] += add;
        } else if (i < 22) {

            cumulScores[bowlIndex][9] += add;
            if (i == 18) {
                cumulScores[bowlIndex][i/2] += cumulScores[bowlIndex][i/2-1];
            }
        }
    }

    private void strike(int[] curScore, int i) {
        // Add up the strike.
        // Add the next two balls to the current cumulscore.
        cumulScores[bowlIndex][i / 2] += 10;
        int add=0;
        if (curScore[i + 1] != -1) {
            add = (curScore[i + 1] != -2) ? curScore[i + 1] : 0;
            cumulScores[bowlIndex][i / 2] += add + cumulScores[bowlIndex][(i / 2) - 1];
            add=0;
            if (curScore[i + 2] != -1 && curScore[i + 2] != -2) {
                add = (curScore[i + 2] != -2) ? curScore[i + 2] : 0;
            } else if (curScore[i + 3] != -2) {
                add = (curScore[i + 3] != -2) ? curScore[i + 3] : 0;
            }
            cumulScores[bowlIndex][(i / 2)] += add;
        } else {
            add = (i / 2 > 0) ? cumulScores[bowlIndex][(i / 2) - 1] : 0;
            int add1 = (curScore[i + 2] != -2) ? curScore[i + 2] : 0;
            cumulScores[bowlIndex][i / 2] += add1; 
            cumulScores[bowlIndex][i / 2] += add;

            int id = 4;
            if (curScore[i + 3] != -1 && curScore[i + 3] != -2) {
                id = 3;
            }
            cumulScores[bowlIndex][(i / 2)] += curScore[i + id];
        }
    }
}
