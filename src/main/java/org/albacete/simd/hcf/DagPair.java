package org.albacete.simd.hcf;

import consensusBN.ConsensusBES;
import consensusBN.HierarchicalAgglomerativeClustererBNs;
import edu.cmu.tetrad.graph.Dag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class DagPair{ //implements Comparable{

    private Dag dag1;
    private Dag dag2;
    private Dag fusedDag;
    private int score = Integer.MAX_VALUE;
    public static int maxSize = Integer.MAX_VALUE;

    public DagPair(Dag dag1, Dag dag2){
        this.dag1 = dag1;
        this.dag2 = dag2;
    }

    public int calculateScore(){
        //Implementing score method
        ArrayList<Dag> dags = new ArrayList<>(2);
        dags.add(dag1);
        dags.add(dag2);
        //HierarchicalAgglomerativeClustererBNs clustererBNs = new HierarchicalAgglomerativeClustererBNs(dags, maxSize);
        //clustererBNs.cluster();
        //clustererBNs.computeConsensusDag();

        ConsensusBES fusion = new ConsensusBES(dags);
        fusion.fusion();
        fusedDag = fusion.getFusion();
        score = fusion.getNumberOfInsertedEdges();
        return score;

    }

    /*
    @Override
    public int compareTo(Object o) {

        if (o instanceof DagPair){
            DagPair pair2 = (DagPair) o;
            return Integer.compare(this.score, pair2.score);
        }
        else{
            throw new IllegalArgumentException("The object you are trying to compare must be another DagPair object");
        }
    }
    */
    public Dag getFusedDag(){
        return fusedDag;
    }

    public int getScore(){
        return score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dag1, dag2);
    }
}
