package ir.ppl.federatedlearning;

import java.util.List;

public class FederatedController {
    private IncentiveFogDevice fogDevice;
    private int maxRounds;
    private double targetAccuracy;

    public FederatedController(IncentiveFogDevice fogDevice, int maxRounds, double targetAccuracy) {
        this.fogDevice = fogDevice;
        this.maxRounds = maxRounds;
        this.targetAccuracy = targetAccuracy;
    }

    public void startTraining() {
        System.out.println("=== Federated Learning Starts ===");

        for (int round = 1; round <= maxRounds; round++) {
            System.out.println("\n--- Round " + round + " ---");

            System.out.println("Reward Rate: " + fogDevice.getRewardRate());

            fogDevice.broadcastRewardAndCollectModels();

            fogDevice.aggregateModels();

            double acc = fogDevice.getGlobalAccuracy();
            System.out.printf("Global Accuracy after aggregation: %.4f%n", acc);

            if (acc >= targetAccuracy) {
                System.out.println("\n Target Accuracy Achieved! Training Complete.");
                break;
            }

            fogDevice.updateRewardRate();
        }

        System.out.println("=== Federated Learning Ends ===");
    }
}
