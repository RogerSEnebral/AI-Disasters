package desastresProb;

import IA.Desastres.Centro;
import IA.Desastres.Centros;
import IA.Desastres.Grupo;
import IA.Desastres.Grupos;

import java.util.ArrayList;



public class ProbIADesastresBoard {
    // Tiempos entre grupos incluyendo recogida
    public static int[][] tempGrupos;
    // Tiempos de centros a grupos y de grupos a centros
    public static int[][] tempCentrosGrupos;
    public static int[][] tempGruposCentros;
    public static int[] gruposPrioridad;
    // Numero de grupos
    public static int nGrupos;
    // Numero de helicopteros
    public static int nHelicopteros;
    public static int getnHelicopterosCentro;
    // Orden de las recogidas de cada helicoptero en cada trayecto
    private final ArrayList<ArrayList<ArrayList<Short>>> recogidas;
    // Gente en grupos
    public static int[] personasGrupos;

    /* Constructor */
    public ProbIADesastresBoard(Centros centros, Grupos grupos, String initialSol) {
        nGrupos = grupos.size();
        getnHelicopterosCentro = centros.get(0).getNHelicopteros();
        nHelicopteros = centros.size() * getnHelicopterosCentro;
        tempGrupos = new int[nGrupos][nGrupos];
        tempCentrosGrupos = new int[centros.size()][nGrupos];
        tempGruposCentros = new int[nGrupos][centros.size()];
        gruposPrioridad = new int[nGrupos];
        personasGrupos = new int[nGrupos];

        //creamos recogidas y inicializamos el Arraylist de viajes de todos los helicopteros
        recogidas = new ArrayList<> ();
        for(int i = 0; i < nHelicopteros; i++) {
            recogidas.add(new ArrayList<>());
        }

        for (int i = 0; i < nGrupos; i++) {
            personasGrupos[i] = grupos.get(i).getNPersonas();
        }

        calculTiempoGrupos(grupos);
        calculTiempoCentrosGrupos(grupos, centros);
        calculGruposPrioridad(grupos);

        if (initialSol.equals("S1")) {
            initialBadSolution();
        } else {
            initialBetterSolution();
        }
    }


    public ProbIADesastresBoard( ArrayList<ArrayList<ArrayList<Short>>> newRecogidas) {
        ArrayList<ArrayList<ArrayList<Short>>> aux = new ArrayList<>();
        for (int h1 = 0; h1 < nHelicopteros; h1++) {
            ArrayList<ArrayList<Short>> aux2 = new ArrayList<>();
            for (int t1 = 0; t1 < newRecogidas.get(h1).size(); t1++) {
                aux2.add((ArrayList<Short>) newRecogidas.get(h1).get(t1).clone());
            }
            aux.add(aux2);
        }
        recogidas = aux;
    }


    private void initialBadSolution () {
        for(short newGrupoID = 0; newGrupoID < nGrupos; newGrupoID++) {
            ArrayList<Short> viaje = new ArrayList<>();
            viaje.add(newGrupoID);
            recogidas.get(newGrupoID % nHelicopteros).add(viaje);
        }
    }


    private void initialBetterSolution () {
        //asignamos los primeros helicopteros para que luego helicoptero.lastElement() tenga sentido
        for( short i = 0; i < nHelicopteros; i++) {
            ArrayList<Short> viaje = new ArrayList<>();
            viaje.add(i);
            recogidas.get(i).add(viaje);
        }

        for(short newGrupoID = (short) nHelicopteros; newGrupoID < nGrupos; newGrupoID++) {
            //se le asigna el helicoptero correspondiente, se mira si cabe en el ultimo viaje, y si no cabe se crea otro viaje
            ArrayList<ArrayList<Short>> helicoptero = recogidas.get(newGrupoID % nHelicopteros);

            int numPasageros = 0;

            for (Short grupID : helicoptero.get(helicoptero.size()-1))
                numPasageros += personasGrupos[grupID];

            if (numPasageros + personasGrupos[newGrupoID] <= 15 && helicoptero.get(helicoptero.size()-1).size() < 3)
                helicoptero.get(helicoptero.size()-1).add(newGrupoID);
            else {
                ArrayList<Short> viaje = new ArrayList<>();
                viaje.add(newGrupoID);
                helicoptero.add(viaje);
            }
        }
    }


    private void calculTiempoGrupos(ArrayList<Grupo> grupos) {
        for(int i = 0; i < grupos.size(); i++) {
            for (int j = 0; j < grupos.size(); j++) {
                tempGrupos[i][j] = tempsGrup(grupos.get(j)) + tempsDistanciaEuclidiana(grupos.get(i).getCoordX(),
                        grupos.get(i).getCoordY(), grupos.get(j).getCoordX(), grupos.get(j).getCoordY());
            }
        }
    }


    private void calculTiempoCentrosGrupos(ArrayList<Grupo> grupos, ArrayList<Centro> centros) {
        for(int i = 0; i < centros.size(); i++) {
            for (int j = 0; j < grupos.size(); j++) {
                int tiempoViaje = tempsDistanciaEuclidiana(grupos.get(j).getCoordX(),
                        grupos.get(j).getCoordY(), centros.get(i).getCoordX(),centros.get(i).getCoordY());

                tempCentrosGrupos[i][j] = tempsGrup(grupos.get(j)) + tiempoViaje;

                tempGruposCentros[j][i] = 600000 + tiempoViaje;
            }
        }
    }


    private int tempsGrup(Grupo grupo) {
         return (grupo.getNPersonas() * 120000) / grupo.getPrioridad();
    }


    private int tempsDistanciaEuclidiana(int x, int y, int x2, int y2) {
        double solution = 0;
        solution += Math.pow((float) x - (float) x2, 2);
        solution += Math.pow((float) y - (float) y2, 2);
        solution = Math.sqrt(solution);
        // + CALCUL VELOCITAT
        solution = solution*36000; //((solution*3600) / 100) * 1000
        return (int) solution;
    }


    private void calculGruposPrioridad(ArrayList<Grupo> grupos) {
        for(int i = 0; i < nGrupos; i++) {
            if (grupos.get(i).getPrioridad() == 1) gruposPrioridad[i] = 1;
            else gruposPrioridad[i] = 2;
        }
    }

    public int getPassengersTravel(int h, int v) {
        int passengers = 0;
        for (int group : recogidas.get(h).get(v)) {
            passengers += personasGrupos[group];
        }
        return passengers;
    }

    public void swapDestinations (int h1, int h2, int v1, int v2, int d1, int d2) {
        Short g1 = recogidas.get(h1).get(v1).get(d1);
        Short g2 = recogidas.get(h2).get(v2).get(d2);
        recogidas.get(h1).get(v1).set(d1, g2);
        recogidas.get(h2).get(v2).set(d2, g1);
    }


    public void moveDestinations (int h1, int h2, int v1, int v2, int g) {
        Short destination = recogidas.get(h1).get(v1).get(g);
        recogidas.get(h1).get(v1).remove(g);
        recogidas.get(h2).get(v2).add(destination);
        if (recogidas.get(h1).get(v1).size() == 0) {
            recogidas.get(h1).remove(v1);
        }
    }

    public int getNTravels(int h){
        return recogidas.get(h).size();
    }

    public ArrayList<ArrayList<ArrayList<Short>>> getRecogidas(){
        return recogidas;
    }

    public int getNGrups(int h, int v){
        return recogidas.get(h).get(v).size();
    }

    public boolean is_goal () {
        return false;
    }
}
