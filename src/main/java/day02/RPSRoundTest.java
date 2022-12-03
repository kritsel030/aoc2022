package day02;

public class RPSRoundTest {

    public static void main(String[] args) {
         // winning rounds
        test("A Y", 1, 6+2);
        test("B Z", 1, 6+3);
        test("C X", 1, 6+1);

        // draws
        test("A X", 1, 3+1);
        test("B Y", 1, 3+2);
        test("C Z", 1, 3+3);

        // loose
        test("A Z", 1, 3);
        test("B X", 1, 1);
        test("C Y", 1, 2);
    }

    public static void test(String line, int strategy, int expectedScore) {
        RPSRound round = new RPSRound(line, strategy);
        if (round.getScore() == expectedScore) {
            System.out.println("OK    | " + line + " | " + round.getScore());
        } else {
            System.err.println("ERROR | " + line + " | " + round.getScore() + ", expected " + expectedScore);
        }

    }
}
