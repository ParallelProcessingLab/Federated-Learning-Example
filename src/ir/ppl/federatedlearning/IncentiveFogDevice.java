package ir.ppl.federatedlearning;

import java.util.ArrayList;
import java.util.List;

public class IncentiveFogDevice {
    private double rewardRate;
    private List<FederatedSensor> sensors;
    private List<ModelParameters> collectedModels;
    private double globalAccuracy;

    public IncentiveFogDevice(double initialRewardRate) {
        this.rewardRate = initialRewardRate;
        this.sensors = new ArrayList<>();
        this.collectedModels = new ArrayList<>();
        this.globalAccuracy = 0.0;
    }

    public void registerSensor(FederatedSensor sensor) {
        sensors.add(sensor);
    }

    public void broadcastRewardAndCollectModels() {
        collectedModels.clear();
        for (FederatedSensor sensor : sensors) {
            ModelParameters model = sensor.respondToReward(rewardRate);
            collectedModels.add(model);
        }
    }

    public void aggregateModels() {
        if (collectedModels.isEmpty()) return;

        int modelSize = collectedModels.get(0).getWeights().size();
        List<Double> aggregatedWeights = new ArrayList<>();
        int totalDataSize = 0;

        for (int i = 0; i < modelSize; i++) {
            aggregatedWeights.add(0.0);
        }

        for (ModelParameters mp : collectedModels) {
            List<Double> weights = mp.getWeights();
            int dataSize = mp.getDataSize();
            totalDataSize += dataSize;

            for (int i = 0; i < modelSize; i++) {
                double w = aggregatedWeights.get(i);
                aggregatedWeights.set(i, w + weights.get(i) * dataSize);
            }
        }

        for (int i = 0; i < modelSize; i++) {
            aggregatedWeights.set(i, aggregatedWeights.get(i) / totalDataSize);
        }

        globalAccuracy = evaluateGlobalAccuracy(aggregatedWeights);
    }

    private double evaluateGlobalAccuracy(List<Double> aggregatedWeights) {

        double sum = 0.0;
        for (double w : aggregatedWeights) {
            sum += w;
        }
        return Math.min(1.0, 0.8 + 0.2 * (sum / aggregatedWeights.size()));
    }

    public void updateRewardRate() {

    	if (globalAccuracy < 0.85) {
            rewardRate += 0.05;
        } else {
            rewardRate = Math.max(0.1, rewardRate - 0.02);
        }
    }

    public double getGlobalAccuracy() {
        return globalAccuracy;
    }

    public double getRewardRate() {
        return rewardRate;
    }
}
