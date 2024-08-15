package desastresProb;

import aima.search.framework.SuccessorFunction;
import aima.search.framework.Successor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bejar on 17/01/17
 */
public class ProbIADesastresSuccesorFunction implements SuccessorFunction{

    public List getSuccessors(Object state){
        ArrayList retVal = new ArrayList();
        ProbIADesastresBoard board = (ProbIADesastresBoard) state;

        for (int h1 = 0; h1 < ProbIADesastresBoard.nHelicopteros; h1++) {
            for (int t1 = 0; t1 < board.getNTravels(h1); t1++) {
                for (int g1 = 0; g1 < board.getNGrups(h1, t1); g1++) {
                    //separamos los bucles entre el helicoptero propio y el resto de helicopteros para no repetir viajes entre el propio helicoptero
                    for (int t2 = t1; t2 < board.getNTravels(h1); t2++) {
                        if (t1 == t2) {
                            if (board.getRecogidas().get(h1).get(t1).size() == 3) {
                                ProbIADesastresBoard newBoard = new ProbIADesastresBoard(board.getRecogidas());
                                newBoard.swapDestinations(h1, h1, t1, t1, 0, 1);
                                String S =
                                        "SWAP -> h1: " + h1 + " h2: " + h1 + " t1: "
                                                + t1 + " t2: " + t1 + " g1: " + 0 + " g2: " + 1;
                                retVal.add(new Successor(S, newBoard));
                                ProbIADesastresBoard newBoard2 = new ProbIADesastresBoard(board.getRecogidas());
                                newBoard2.swapDestinations(h1, h1, t1, t1, 1, 2);
                                S =
                                        "SWAP -> h1: " + h1 + " h2: " + h1 + " t1: "
                                                + t1 + " t2: " + t1 + " g1: " + 1 + " g2: " + 2;
                                retVal.add(new Successor(S, newBoard2));
                            }
                        } else {
                            for (int g2 = 0; g2 < board.getNGrups(h1, t2); g2++) {
                                Short d1 = board.getRecogidas().get(h1).get(t1).get(g1);
                                Short d2 = board.getRecogidas().get(h1).get(t2).get(g2);
                                if (board.getPassengersTravel(h1, t1) - ProbIADesastresBoard.personasGrupos[d1] + ProbIADesastresBoard.personasGrupos[d2] <= 15 &&
                                        board.getPassengersTravel(h1, t2) - ProbIADesastresBoard.personasGrupos[d2] + ProbIADesastresBoard.personasGrupos[d1] <= 15) {

                                    ProbIADesastresBoard newBoard = new ProbIADesastresBoard(board.getRecogidas());
                                    newBoard.swapDestinations(h1, h1, t1, t2, g1, g2);
                                    String S =
                                            "SWAP -> h1: " + h1 + " h2: " + h1 + " t1: "
                                                    + t1 + " t2: " + t2 + " g1: " + g1 + " g2: " + g2;
                                    retVal.add(new Successor(S, newBoard));

                                }
                            }
                        }
                    }
                    for (int h2 = h1 + 1; h2 < ProbIADesastresBoard.nHelicopteros; h2++) {
                        for (int t2 = 0; t2 < board.getNTravels(h2); t2++) {
                            for (int g2 = 0; g2 < board.getNGrups(h2, t2); g2++) {
                                Short d1 = board.getRecogidas().get(h1).get(t1).get(g1);
                                Short d2 = board.getRecogidas().get(h2).get(t2).get(g2);
                                if (board.getPassengersTravel(h1, t1) - ProbIADesastresBoard.personasGrupos[d1] + ProbIADesastresBoard.personasGrupos[d2] <= 15 &&
                                    board.getPassengersTravel(h2, t2) - ProbIADesastresBoard.personasGrupos[d2] + ProbIADesastresBoard.personasGrupos[d1] <= 15) {

                                    ProbIADesastresBoard newBoard = new ProbIADesastresBoard(board.getRecogidas());
                                    newBoard.swapDestinations(h1, h2, t1, t2, g1, g2);
                                    String S =
                                            "SWAP -> h1: " + h1 + " h2: " + h2 + " t1: "
                                                    + t1 + " t2: " + t2 + " g1: " + g1 + " g2: " + g2;
                                    retVal.add(new Successor(S, newBoard));
                                }
                            }
                        }
                    }
                }
            }
        }

       for (int h1 = 0; h1 < ProbIADesastresBoard.nHelicopteros; h1++) {
            for (int v1 = 0; v1 < board.getNTravels(h1); v1++) {
                for(int g = 0; g < board.getNGrups(h1, v1); g++){
                    for (int h2 = 0; h2 < ProbIADesastresBoard.nHelicopteros; h2++) {
                        for (int v2 = 0; v2 < board.getNTravels(h2); v2++) {
                            if ((h1 != h2 || v1 != v2) && board.getRecogidas().get(h2).get(v2).size() < 3 &&
                                board.getPassengersTravel(h2, v2) + ProbIADesastresBoard.personasGrupos[board.getRecogidas().get(h1).get(v1).get(g)] <= 15) {
                                ProbIADesastresBoard newBoard = new ProbIADesastresBoard(board.getRecogidas());
                                newBoard.moveDestinations(h1, h2, v1, v2, g);
                                String S =
                                        "MOVE -> h1: " + h1 + " v1: " + v1 + " g: "
                                                + g + " h2: " + h2 + " v2: " + v2;
                                retVal.add(new Successor(S, newBoard));
                            }
                        }
                    }
                }
            }
        }

        return retVal;
    }
}
