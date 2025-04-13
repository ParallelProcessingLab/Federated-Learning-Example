package ir.ppl.federatedlearning;

import java.util.Random;

public class MainSimulation {

    public static void main(String[] args) {
        double initialRewardRate = 0.2;

        IncentiveFogDevice bs = new IncentiveFogDevice(initialRewardRate);

        int numSensors = 5;
        Random rand = new Random(42);

        for (int i = 1; i <= numSensors; i++) {
            double cpuPower = 200 + rand.nextInt(300); // MIPS
            int localDataSize = 100 + rand.nextInt(300);
            double communicationCost = 5 + rand.nextDouble() * 10;

            FederatedSensor sensor = new FederatedSensor(i, cpuPower, localDataSize, communicationCost);
            bs.registerSensor(sensor);
        }

        int maxRounds = 20;
        double targetAccuracy = 0.85;

        FederatedController controller = new FederatedController(bs, maxRounds, targetAccuracy);

        controller.startTraining();
    }
}
