package org.albacete.simd.hcf;


import consensusBN.HierarchicalAgglomerativeClustererBNs;
import edu.cmu.tetrad.graph.Dag;

import java.util.ArrayList;
import java.util.HashMap;

public class HierarchicalClusteringFusion {

    private HierarchicalAgglomerativeClustererBNs clustererBNs;
    private ArrayList<Dag> initalDags;
    private int maxSize;
    private double maxComplexity;
    private ArrayList<ArrayList<Dag>> hTree;
    private ArrayList<Dag> candidates;
    private HashMap<DagPair, Integer> distances;

    public HierarchicalClusteringFusion(ArrayList<Dag> dags, int maxSize){
        this.initalDags = dags;
        this.maxSize = maxSize;
        this.maxComplexity = -1;
        this.hTree = new ArrayList<>(dags.size());
        this.candidates = new ArrayList<>(dags.size());
        this.distances = new HashMap<>(dags.size());
    }

    public HierarchicalClusteringFusion(ArrayList<Dag> dags, double maxComplexity){
        this(dags, -1);
        this.maxComplexity = maxComplexity;
    }

    public void cluster(){

        // Add initial candidates
        candidates.addAll(initalDags);

        // Add first level to the hierarchical tree
        hTree.add(initalDags);

        // Starting Hierarchical clustering
        while(candidates.size() > 1){
            DagPair bestPair = null;
            int bestScore = Integer.MAX_VALUE;

            // 1. Get best candidate
            int bestIndex1 = -1;
            int bestIndex2 = -1;
            for(int i = 0; i < candidates.size(); i++){
                Dag dag1 = candidates.get(i);
                for(int j = i+1; j < initalDags.size(); j++ ){
                    Dag dag2 = initalDags.get(j);
                    DagPair pair = new DagPair(dag1, dag2);
                    int score;
                    if(distances.containsKey(pair)){
                        score = distances.get(pair);
                    }
                    else {
                        score = pair.calculateScore();
                        distances.put(pair, score);
                    }
                    // Checking Improvement
                    if (score < bestScore) {
                        bestIndex1 = i;
                        bestIndex2 = j;
                        bestScore = score;
                        bestPair = pair;
                    }
                }
            }
            // Checking for errors
            if(bestIndex1 == -1 || bestIndex2 == -1 || bestPair == null){
                System.out.println("Nothing found...");
                break;
            }

            // 2. Remove best candidate from the  candidate array
            candidates.remove(bestIndex1);
            candidates.remove(bestIndex2);


            // 3. Add the new fusion dag to the candidates and hTree
            Dag fusedDag = bestPair.getFusedDag();
            candidates.add(fusedDag);
            hTree.add(candidates);
        }

    }

    @Override
    public String toString() {

        StringBuilder result = new StringBuilder();
        int l = 1;
        for(ArrayList<Dag> level : hTree){
            result.append("---------------");
            result.append("Level: ").append(l);
            result.append("\n");
            for(Dag dag : level){
                result.append(dag);
            }
        }
        return result.toString();


    }
}
