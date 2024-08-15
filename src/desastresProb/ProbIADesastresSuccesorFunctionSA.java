package desastresProb;


import aima.search.framework.SuccessorFunction;
import aima.search.framework.Successor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProbIADesastresSuccesorFunctionSA implements SuccessorFunction{
    public List getSuccessors(Object state){
        ArrayList retVal = new ArrayList<>();
        Random myRandom = new Random();
        ProbIADesastresBoard board = (ProbIADesastresBoard) state;
        ProbIADesastresBoard newBoard = new ProbIADesastresBoard(board.getRecogidas());

        // MOVE or SWAP
        if(myRandom.nextBoolean()) {
            int h1, h2, v1, v2, g;
            do {
                do {
                    h1 = myRandom.nextInt(ProbIADesastresBoard.nHelicopteros);
                } while (newBoard.getRecogidas().get(h1).size() == 0);

                v1 = myRandom.nextInt(newBoard.getRecogidas().get(h1).size());
                g = myRandom.nextInt(newBoard.getRecogidas().get(h1).get(v1).size());

                h2 = myRandom.nextInt(ProbIADesastresBoard.nHelicopteros);
                if (h1 == h2) {
                    do {
                        v2 = myRandom.nextInt(newBoard.getRecogidas().get(h2).size());
                    } while (v1 == v2);
                } else v2 = myRandom.nextInt(newBoard.getRecogidas().get(h2).size());
            } while (board.getRecogidas().get(h2).get(v2).size() == 3 ||
                     board.getPassengersTravel(h2, v2) + ProbIADesastresBoard.personasGrupos[board.getRecogidas().get(h1).get(v1).get(g)] >= 15);
            newBoard.moveDestinations(h1, h2, v1, v2, g);
            retVal.add(new Successor("", newBoard));
        } else {
            int h1, h2, v1, v2, g1, g2;
            do {
                do {
                    h1 = myRandom.nextInt(ProbIADesastresBoard.nHelicopteros);
                } while (newBoard.getRecogidas().get(h1).size() == 0);
                do {
                    h2 = myRandom.nextInt(ProbIADesastresBoard.nHelicopteros);
                } while (newBoard.getRecogidas().get(h2).size() == 0);

                v1 = myRandom.nextInt(newBoard.getRecogidas().get(h1).size());
                g1 = myRandom.nextInt(newBoard.getRecogidas().get(h1).get(v1).size());
                v2 = myRandom.nextInt(newBoard.getRecogidas().get(h2).size());
                g2 = myRandom.nextInt(newBoard.getRecogidas().get(h2).get(v2).size());
            } while(board.getPassengersTravel(h1, v1) - ProbIADesastresBoard.personasGrupos[g1] + ProbIADesastresBoard.personasGrupos[g2] > 15 ||
                    board.getPassengersTravel(h2, v2) - ProbIADesastresBoard.personasGrupos[g2] + ProbIADesastresBoard.personasGrupos[g1] > 15);
            newBoard.swapDestinations(h1, h2, v1, v2, g1, g2);
            retVal.add(new Successor("", newBoard));
        }
        return retVal;
    }
}
