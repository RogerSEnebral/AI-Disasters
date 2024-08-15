package desastresProb;

import aima.search.framework.GoalTest;

/**
 * Created by bejar on 17/01/17.
 */
public class ProbIADesastresGoalTest implements GoalTest {

    public boolean isGoalState(Object state){

        return((ProbIADesastresBoard) state).is_goal();
    }
}
