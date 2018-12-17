package methor.se.methor.Models;

import java.util.Random;

public class RPS {
    public enum Result{
        WIN, DRAW, LOSS
    }

    public enum Choice {
        ROCK(1, "ROCK"), PAPER(2, "PAPER"), SCISSORS(3, "SCISSORS");

        private int type;
        private String name;
        Choice(int type, String name){ this.type = type; this.name = name; }
        public int getType(){ return type; }
        public String getName(){ return name; }
        @Override
        public String toString(){
            return name;
        }
    }

    public static class GameInfo{
        public final Result result;
        public final Choice myChoice;
        public final Choice opponentChoice;

        public GameInfo(Result result, Choice myChoice, Choice opponentChoice){
            this.result = result;
            this.myChoice = myChoice;
            this.opponentChoice = opponentChoice;
        }

        @Override
        public String toString(){
            return "Your choice: " + myChoice + ", Opponent choice: " + opponentChoice + ", Result: " + result;
        }
    }

    public static GameInfo simulateGame(Choice myChoice){
        Choice opponentChoice = getOpponentChoice();
        return new GameInfo(getResult(myChoice, opponentChoice), myChoice, opponentChoice);
    }

    //Rock, paper, scissors. Best of three.
    private static Choice getOpponentChoice(){
        Random rand = new Random();
        int choice = rand.nextInt(3);
        return Choice.values()[choice];
    }

    private static Result getResult(Choice myChoice, Choice opponentChoice){
        switch (myChoice) {
            case ROCK:
                if(opponentChoice == Choice.ROCK)
                    return Result.DRAW;
                else if(opponentChoice == Choice.PAPER)
                    return Result.LOSS;
                else if(opponentChoice == Choice.SCISSORS)
                    return Result.WIN;
                break;
            case PAPER:
                if(opponentChoice == Choice.ROCK)
                    return Result.WIN;
                else if(opponentChoice == Choice.PAPER)
                    return Result.DRAW;
                else if(opponentChoice == Choice.SCISSORS)
                    return Result.LOSS;
                break;
            case SCISSORS:
                if(opponentChoice == Choice.ROCK)
                    return Result.LOSS;
                else if(opponentChoice == Choice.PAPER)
                    return Result.WIN;
                else if(opponentChoice == Choice.SCISSORS)
                    return Result.DRAW;
                break;
        }
        return null;
    }
}
