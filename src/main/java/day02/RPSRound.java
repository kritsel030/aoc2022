package day02;

public class RPSRound {
    Character me;
    Character opponent;

    Character result;
    long score;

    public RPSRound(String inputLine, int strategy) {
        String[] values = inputLine.split(" ");
        this.opponent = values[0].charAt(0);
        if (strategy == 1) {
            this.me = values[1].charAt(0);
        } else {
            this.result = values[1].charAt(0);
            determineMe();
            calculateScore();
        }
        calculateScore();
    }

    public long getScore() {
        return score;
    }

    private void calculateScore(){
        //Rock defeats Scissors,
        // //Scissors defeats Paper,
        // and Paper defeats Rock.
        // If both players choose the same shape, the round instead ends in a draw.

        // opponent shapes
        // A rock (1)
        // B paper (2)
        // C scissors (3)

        // me shapes
        // X rock (1)
        // Y paper (2)
        // Z scissors (3)

        // outcome of the round: 0 if you lost, 3 if the round was a draw, and 6 if you won

        int myValue = 0;
        switch (me) {
            case 'X':
                myValue = 1;
                break;
            case 'Y':
                myValue = 2;
                break;
            case 'Z':
                myValue = 3;
                break;
        }

        int opponentValue = 0;
        switch (opponent) {
            case 'A':
                opponentValue = 1;
                break;
            case 'B':
                opponentValue = 2;
                break;
            case 'C':
                opponentValue = 3;
                break;
        }

        // win
        if (myValue == (opponentValue+1) || myValue == (opponentValue+1)% 3) {
            score = 6 + myValue;
        // draw
        } else if (myValue == opponentValue) {
            score = 3 + myValue;
        // lose
        } else {
            score = myValue;
        }
    }

    private void determineMe() {
        int opponentValue = 0;
        switch (opponent) {
            case 'A':
                opponentValue = 1;
                break;
            case 'B':
                opponentValue = 2;
                break;
            case 'C':
                opponentValue = 3;
                break;
        }

        int meValue = 0;
        switch (result) {
            case 'X': // loose
                meValue = opponentValue-1 < 1 ? 3 : opponentValue-1;
                break;
            case 'Y': // draw
                meValue = opponentValue;
                break;
            case 'Z': // win
                meValue = opponentValue+1 > 3 ? 1 : opponentValue+1;
                break;
        }
        switch (meValue) {
            case 1:
                me = 'X';
                break;
            case 2:
                me = 'Y';
                break;
            case 3:
                me = 'Z';
                break;
        }
    }


}
