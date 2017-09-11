package bowling;

import java.io.*;
import java.util.ArrayList;

/**
 * Bowling Score Calculator, coding example for EITC. This class includes some
 * extra design so that the class could be used as part of a larger project. I
 * was not sure how elaborate of a design was desired. The solution could
 * actually consist of a single method, with less error-handling, and printing
 * as the scores are calculated.
 *
 * @author edswing
 */
public class ScoreCalculator {
    /**
     * Calculate the scores by reading through the file, line-by-line. The
     * scores are returned in an ArrayList, so this method could be invoked by
     * an external class as well
     * 
     * @return the list of scores from the file
     */
    public ArrayList<Integer> parseScoreFile(File scoreFile) throws IOException {
        // do some preliminary error checking on the file
        if (!scoreFile.exists()) {
            throw new IOException("ScoreFile " + scoreFile + " does not exist");
        } else if (!scoreFile.canRead()) {
            throw new IOException("Cannot read ScoreFile: " + scoreFile);
        }
        ArrayList<Integer> scores = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(scoreFile));
        String line;
        while ((line = reader.readLine()) != null) {
            // calculate the score for each game. Trim to remove extra spaces
            int score = scoreGame(line.trim());
            scores.add(score);
        }
        reader.close();
        return scores;
    }


    /**
     * Simple method to get the score on an individual ball
     * 
     * @param ch the character indicating the ball result. X=strike (10), -=0,
     *            and a /=spare should not happen
     * @return the score for the individual ball
     */
    private int getBallScore(char ch) {
        if (ch == 'X') {
            return 10;
        } else if (ch == '-') {
            return 0;
        } else {
            return ch - '0';
        }
    }


    /**
     * Calculate the score for an individual game
     * 
     * @param scoreRecord the character string representing the ball-by-ball
     *            results
     * @return the score for the game
     */
    public int scoreGame(String scoreRecord) {
        int score = 0;
        int chInx = 0;
        for (int frameInx = 0; frameInx < 10; frameInx++) {
            char ball = scoreRecord.charAt(chInx);
            char second = scoreRecord.charAt(chInx + 1);
            if (ball == 'X') {
                char third = scoreRecord.charAt(chInx + 2);
                // special case for a strike followed by a spare
                if (third == '/') {
                    score += 20;
                } else {
                    score += 10 + getBallScore(second) + getBallScore(third);
                }
                chInx++;
            } else {
                if (second == '/') {
                    score += 10 + getBallScore(scoreRecord.charAt(chInx + 2));
                } else {
                    score += getBallScore(ball) + getBallScore(second);
                }
                chInx += 2;
            }
        }
        return score;
    }


    /**
     * Simple method to print the score list in a nice form to stdout.
     * 
     */
    public void printScores(ArrayList<Integer> scores) {
        int game = 1;
        for (int score : scores) {
            System.out.println("Game: " + game + "  score: " + score);
            game++;
        }
    }


    /**
     * Run the scoring calculator. The program expects a single argument, the
     * file containing the frame-by-frame scoring notation. Each game is assumed
     * to be on a different line.
     * 
     * @param args command line arguments. Single argument is the filename
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            ScoreCalculator calc = new ScoreCalculator();
            File scoreFile = new File(args[0]);
            try {
                ArrayList<Integer> scores = calc.parseScoreFile(scoreFile);
                calc.printScores(scores);
            } catch (IOException e) {
                System.err.println("Could not read the score file:"
                        + e.getMessage());
            }
        }
    }
}
