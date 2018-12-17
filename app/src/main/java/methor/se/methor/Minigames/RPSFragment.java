package methor.se.methor.Minigames;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;

import methor.se.methor.R;

public class RPSFragment extends Fragment {

    private enum Result{
        WIN, DRAW, LOSS
    }

    private enum Choice {
        ROCK(1, "ROCK"), PAPER(2, "PAPER"), SCISSORS(3, "SCISSORS");

        private int type;
        private String name;
        Choice(int type, String name){ this.type = type; this.name = name; }
        public int getType(){ return type; }
        public String getName(){ return name; }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rps, container, false);
        initializeComponents(view);
        return view;
    }

    private void initializeComponents(View view) {
        //Get UI elements
        for (int i = 1; i <= 10; i++){
            Choice myChoice = Choice.ROCK;
            Choice opponentChoice = getOpponentChoice();


            Log.d("Game " + i, "My choice: " + myChoice.getName() +
                    ", Opponents choice: " + opponentChoice.getName() + ", Result: " + getResult(myChoice, opponentChoice));
        }
    }

    //Rock, paper, scissors. Best of three.
    private Choice getOpponentChoice(){
        Random rand = new Random();
        int choice = rand.nextInt(3);
        return Choice.values()[choice];
    }

    private Result getResult(Choice myChoice, Choice opponentChoice){
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