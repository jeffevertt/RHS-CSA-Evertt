package framework;

import java.util.ArrayList;

import math.Vec2;

/* NoiseMapComposite
 *  This objects can contain multiple NoiseMaps (which can 
 * have different scales/parameters). It abstracts this for
 * you and allows you to treat the composite as a single NoiseMap. */
public class NoiseMapComposite implements NoiseMap {
    // Nested classes...
    private class NoiseMapLayer {
        public NoiseMap noiseMap;
        public double scaleXZ;
        public double mapWeight;

        public NoiseMapLayer(NoiseMap noiseMap, double scaleXZ, double mapWeight) {
            this.noiseMap = noiseMap;
            this.scaleXZ = scaleXZ;
            this.mapWeight = mapWeight;
        }
    }

    // Enums...
    public enum CompositeMethod {
        Add,
        Average
    }

    // Data...
    CompositeMethod compositeMethod = CompositeMethod.Add;
    ArrayList<NoiseMapLayer> noiseMaps = new ArrayList<NoiseMapLayer>();

    // Methods...
    public NoiseMapComposite(CompositeMethod compositeMethod) {
        this.compositeMethod = compositeMethod;
    }

    public void addNoiseMapLayer(NoiseMap noiseMap, double scaleXZ) {
        addNoiseMapLayer(noiseMap, scaleXZ, 1.0);
    }
    public void addNoiseMapLayer(NoiseMap noiseMap, double scaleXZ, double mapWeight) {
        noiseMaps.add( new NoiseMapLayer(noiseMap, scaleXZ, mapWeight) );
    }

    public void resetMapLayers() {
        noiseMaps.clear();
    }

    public double calcValueAtPoint(Vec2 pt) {
        double sum = 0.0, layerWeightSum = 0.0;
        for (NoiseMapLayer layer : noiseMaps) {
            Vec2 ptScaled = Vec2.multiply( pt, layer.scaleXZ );
            double value = layer.noiseMap.calcValueAtPoint(ptScaled);
            sum += value * layer.mapWeight;
            layerWeightSum += layer.mapWeight;
        }
        if (compositeMethod == CompositeMethod.Average) {
            sum = sum / Math.max(layerWeightSum, 0.00001);
        }
        return sum;
    }
}
