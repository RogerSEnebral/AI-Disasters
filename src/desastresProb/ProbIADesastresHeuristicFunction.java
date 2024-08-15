package desastresProb;

/**
 * Created by bejar on 17/01/17.
 */

import aima.search.framework.HeuristicFunction;

import java.util.ArrayList;
import java.util.Vector;

public class ProbIADesastresHeuristicFunction implements HeuristicFunction {
    private String heuristic;
    public ProbIADesastresHeuristicFunction(String heuristicF) {
        heuristic = heuristicF;
    }

    public double getHeuristicValue(Object n){
        ProbIADesastresBoard board = (ProbIADesastresBoard) n ;
        if (heuristic.equals("H1")) {
            return heuristica1(board);
        } else {
            return heuristica2(board);
        }
    }

    private double heuristica1 (ProbIADesastresBoard board) {
        int sumTime = 0;
        for(int heliID = 0; heliID < ProbIADesastresBoard.nHelicopteros; heliID++) {
            for(ArrayList<Short> viajesHeli : board.getRecogidas().get(heliID)) {

                //centro a primer grupo
                sumTime +=  ProbIADesastresBoard.tempCentrosGrupos[heliID/ ProbIADesastresBoard.getnHelicopterosCentro][viajesHeli.get(0)];

                //primer grupo a ultimo
                for(int destino = 0; destino < viajesHeli.size()-1; destino++)
                    sumTime += ProbIADesastresBoard.tempGrupos[viajesHeli.get(destino)][viajesHeli.get(destino+1)];

                //ultimo grupo a centro
                sumTime += ProbIADesastresBoard.tempGruposCentros[viajesHeli.get(viajesHeli.size()-1)][heliID/ ProbIADesastresBoard.getnHelicopterosCentro];
            }
            if(board.getRecogidas().get(heliID).size() > 0) sumTime = sumTime - 600000;
        }
        return sumTime;
    }

    private double heuristica2 (ProbIADesastresBoard board) {
        int sumTime = 0;
        int maxTimePrioritat = 0;

        for(int heliID = 0; heliID < ProbIADesastresBoard.nHelicopteros; heliID++) {
            int sumTimeH = 0;
            int maxTimePrioritatH = 0;
            for(ArrayList<Short> viajesHeli : board.getRecogidas().get(heliID)) {
                boolean hasPriorityGroup = false;

                //centro a primer grupo
                sumTimeH +=  ProbIADesastresBoard.tempCentrosGrupos[heliID/ ProbIADesastresBoard.getnHelicopterosCentro][viajesHeli.get(0)];

                //primer grupo a ultimo
                for(int destino = 0; destino < viajesHeli.size()-1; destino++) {
                    if(ProbIADesastresBoard.gruposPrioridad[viajesHeli.get(destino)] == 1) hasPriorityGroup = true;
                    sumTimeH += ProbIADesastresBoard.tempGrupos[viajesHeli.get(destino)][viajesHeli.get(destino+1)];
                }

                //ultimo grupo a centro (miramos si el ultimo grupo es de prioridad ya que no lo hemos mirado antes)
                if(ProbIADesastresBoard.gruposPrioridad[viajesHeli.get(viajesHeli.size()-1)] == 1) hasPriorityGroup = true;
                sumTimeH += ProbIADesastresBoard.tempGruposCentros[viajesHeli.get(viajesHeli.size()-1)][heliID/ ProbIADesastresBoard.getnHelicopterosCentro];

                if(hasPriorityGroup) maxTimePrioritatH = sumTimeH - 600000;
            }
            if(maxTimePrioritatH > maxTimePrioritat) maxTimePrioritat = maxTimePrioritatH;
            if(sumTimeH != 0) sumTime += sumTimeH - 600000;
        }
        //el peso de cada tiempo ha de ser modificado con experimentos para ver cual es el mas optimo
        return maxTimePrioritat + sumTime;
    }
}
