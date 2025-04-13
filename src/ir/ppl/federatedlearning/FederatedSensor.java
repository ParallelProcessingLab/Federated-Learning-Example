package ir.ppl.federatedlearning;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FederatedSensor {
    private int id;
    private double cpuPower; // MIPS
    private int localDataSize;
    private double communicationCost;
    private double localAccuracy;
    private Random rand;

    public FederatedSensor(int id, double cpuPower, int localDataSize, double communicationCost) {
        this.id = id;
        this.cpuPower = cpuPower;
        this.localDataSize = localDataSize;
        this.communicationCost = communicationCost;
        this.rand = new Random(id);
    }

    public ModelParameters respondToReward(double rewardRate) {

        int iterations = (int) Math.max(1, rewardRate * 10);

        return trainLocalModel(iterations);
    }

    private ModelParameters trainLocalModel(int iterations) {
        List<Double> weights = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            weights.add(rand.nextDouble());
        }


        double accuracy = 1 - (1.0 / (iterations + 1)) + rand.nextDouble() * 0.05;
        accuracy = Math.min(accuracy, 1.0);

        this.localAccuracy = accuracy;

        return new ModelParameters(weights, accuracy, localDataSize);
    }

    public double computeUtility(double rewardRate, int iterations) {
        double reward = rewardRate * iterations;
        double cost = cpuPower * iterations + communicationCost;
        return reward - cost;
    }

    public double getLocalAccuracy() {
        return localAccuracy;
    }

    public int getId() {
        return id;
    }
}
