package desastresProb;

import IA.Desastres.Centros;
import IA.Desastres.Grupos;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static java.lang.System.*;

public class Main {

    public static void main(String[] args) throws Exception {
        int ngrupos = Integer.parseInt(args[0]);
        int ncentros = Integer.parseInt(args[1]);
        int helicopteros = Integer.parseInt(args[2]);
        int seedGrupos = Integer.parseInt(args[3]);
        int seedCentros = Integer.parseInt(args[4]);
        String experiment = args[5];
        String initialSol = args[6];
        String heuristic = args[7];

        Grupos grupos = new Grupos(ngrupos, seedGrupos);
        Centros centros = new Centros(ncentros, helicopteros, seedCentros);

        ProbIADesastresBoard board = new ProbIADesastresBoard(centros, grupos, initialSol);

        if (experiment.equals("HC")) {
            experimentHC(board, heuristic);
        } else if (experiment.equals("SA")){
            int steps = Integer.parseInt(args[8]);
            int stiter = Integer.parseInt(args[11]);
            int k = Integer.parseInt(args[9]);
            double lamb = Double.parseDouble(args[10]);
            experimentSA(board, heuristic, steps, stiter, k, lamb);
        } else {
            out.println("Check your experiment type");
        }
    }
    private static void experimentSA(
            ProbIADesastresBoard board, String heuristic, int steps, int stiter, int k, double lamb
    ) throws Exception {
        // Create the Problem object
        Problem problem = new  Problem(
                board,
                new ProbIADesastresSuccesorFunctionSA(),
                new ProbIADesastresGoalTest(),
                new ProbIADesastresHeuristicFunction(heuristic)
        );

        Search algorithm = new SimulatedAnnealingSearch(steps, stiter, k,lamb);

        long startTime = currentTimeMillis();
        SearchAgent agent = new SearchAgent(problem, algorithm);
        long endTime = currentTimeMillis();

        long duration = (endTime - startTime);
        out.println("Search Time:");
        out.println(duration);

        out.println();
        printInstrumentation(agent.getInstrumentation());
        printSolutionSA(agent.getActions(), heuristic);
    }

    private static void experimentHC(ProbIADesastresBoard board, String heuristic) throws Exception {
        // Create the Problem object
        Problem problem = new  Problem(
                board,
                new ProbIADesastresSuccesorFunction(),
                new ProbIADesastresGoalTest(),
                new ProbIADesastresHeuristicFunction(heuristic)
        );

        Search algorithm = new HillClimbingSearch();

        long startTime = currentTimeMillis();
        SearchAgent agent = new SearchAgent(problem, algorithm);
        long endTime = currentTimeMillis();

        long duration = (endTime - startTime);
        out.println("Search Time:");
        out.println(duration);

        // We print the results of the search
        out.println();
        printActions(agent.getActions());
        printInstrumentation(agent.getInstrumentation());
        printSolution(agent.getActions(), board, heuristic);
    }

    private static void printInstrumentation(Properties properties) {
        for (Object o : properties.keySet()) {
            String key = (String) o;
            String property = properties.getProperty(key);
            out.println(key + " : " + property);
        }
    }
    
    private static void printActions(List actions) {
        for (Object o : actions) {
            String action = (String) o;
            out.println(action);
        }
    }

    private static void printSolution(List actions, ProbIADesastresBoard board, String heuristic) {
        for (Object o : actions){
            String action = String.valueOf(o);
            String[] parts = action.split(" ");
            if (parts[0].equals("MOVE")){
                int h1 = Integer.parseInt(parts[3]);
                int h2 = Integer.parseInt(parts[9]);
                int v = Integer.parseInt(parts[5]);
                int g = Integer.parseInt(parts[7]);
                int v2 = Integer.parseInt(parts[11]);
                board.moveDestinations(h1, h2, v, v2, g);
            }else if (parts[0].equals("SWAP")) {
                int h1 = Integer.parseInt(parts[3]);
                int h2 = Integer.parseInt(parts[5]);
                int t1 = Integer.parseInt(parts[7]);
                int t2 = Integer.parseInt(parts[9]);
                int g1 = Integer.parseInt(parts[11]);
                int g2 = Integer.parseInt(parts[13]);
                board.swapDestinations(h1, h2, t1, t2, g1, g2);
            }
        }
        for (ArrayList<ArrayList<Short>> h : board.getRecogidas()) {
            out.println(h);
        }
        ProbIADesastresHeuristicFunction heuristicFunction = new ProbIADesastresHeuristicFunction(heuristic);
        out.println("Solution heuristic time in ms:");
        out.println(String.format("%.0f",heuristicFunction.getHeuristicValue(board)));
    }

    private static void printSolutionSA(List actions, String heuristic) {
        ProbIADesastresBoard board = (ProbIADesastresBoard) actions.get(0);
        for (ArrayList<ArrayList<Short>> h : board.getRecogidas()) {
            out.println(h);
        }
        ProbIADesastresHeuristicFunction heuristicFunction = new ProbIADesastresHeuristicFunction(heuristic);
        out.println("Solution heuristic time in ms:");
        out.println(String.format("%.0f",heuristicFunction.getHeuristicValue(board)));
    }
}


