package com.company;

/**
 * Created by matt on 10/26/15.
 */
public class SmartCropOptions {

    //default values
    public double width = 0;
    public double height =  0;
    public double aspect =  0;
    public double cropWidth =  0;
    public double cropHeight =  0;
    public double detailWeight =  0.2;
    public double skinColor[] = {0.78, 0.57, 0.44};
    public double skinBias =  0.01;
    public double skinBrightnessMin =  0.2;
    public double skinBrightnessMax =  1.0;
    public double skinThreshold =  0.8;
    public double skinWeight =  1.8;
    public double saturationBrightnessMin =  0.05;
    public double saturationBrightnessMax =  0.9;
    public double saturationThreshold =  0.4;
    public double saturationBias =  0.2;
    public double saturationWeight =  0.3;
    // step * minscale rounded down to the next power of two should be good
    public double scoreDownSample =  8;
    public double step =  8;
    public double scaleStep =  0.1;
    public double minScale =  0.9;
    public double maxScale =  1.0;
    public double edgeRadius =  0.4;
    public double edgeWeight =  -20.0;
    public double outsideImportance =  -0.5;
    public boolean ruleOfThirds =  true;
    public boolean prescale =  true;
    public String canvasFactory = null;
    public boolean debug =  false;


    /* Getters and Setters */

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getAspect() {
        return aspect;
    }

    public void setAspect(double aspect) {
        this.aspect = aspect;
    }

    public double getCropWidth() {
        return cropWidth;
    }

    public void setCropWidth(double cropWidth) {
        this.cropWidth = cropWidth;
    }

    public double getCropHeight() {
        return cropHeight;
    }

    public void setCropHeight(double cropHeight) {
        this.cropHeight = cropHeight;
    }

    public double getDetailWeight() {
        return detailWeight;
    }

    public void setDetailWeight(double detailWeight) {
        this.detailWeight = detailWeight;
    }

    public double[] getSkinColor() {
        return skinColor;
    }

    public void setSkinColor(double[] skinColor) {
        this.skinColor = skinColor;
    }

    public double getSkinBias() {
        return skinBias;
    }

    public void setSkinBias(double skinBias) {
        this.skinBias = skinBias;
    }

    public double getSkinBrightnessMin() {
        return skinBrightnessMin;
    }

    public void setSkinBrightnessMin(double skinBrightnessMin) {
        this.skinBrightnessMin = skinBrightnessMin;
    }

    public double getSkinBrightnessMax() {
        return skinBrightnessMax;
    }

    public void setSkinBrightnessMax(double skinBrightnessMax) {
        this.skinBrightnessMax = skinBrightnessMax;
    }

    public double getSkinThreshold() {
        return skinThreshold;
    }

    public void setSkinThreshold(double skinThreshold) {
        this.skinThreshold = skinThreshold;
    }

    public double getSkinWeight() {
        return skinWeight;
    }

    public void setSkinWeight(double skinWeight) {
        this.skinWeight = skinWeight;
    }

    public double getSaturationBrightnessMin() {
        return saturationBrightnessMin;
    }

    public void setSaturationBrightnessMin(double saturationBrightnessMin) {
        this.saturationBrightnessMin = saturationBrightnessMin;
    }

    public double getSaturationBrightnessMax() {
        return saturationBrightnessMax;
    }

    public void setSaturationBrightnessMax(double saturationBrightnessMax) {
        this.saturationBrightnessMax = saturationBrightnessMax;
    }

    public double getSaturationThreshold() {
        return saturationThreshold;
    }

    public void setSaturationThreshold(double saturationThreshold) {
        this.saturationThreshold = saturationThreshold;
    }

    public double getSaturationBias() {
        return saturationBias;
    }

    public void setSaturationBias(double saturationBias) {
        this.saturationBias = saturationBias;
    }

    public double getSaturationWeight() {
        return saturationWeight;
    }

    public void setSaturationWeight(double saturationWeight) {
        this.saturationWeight = saturationWeight;
    }

    public double getScoreDownSample() {
        return scoreDownSample;
    }

    public void setScoreDownSample(double scoreDownSample) {
        this.scoreDownSample = scoreDownSample;
    }

    public double getStep() {
        return step;
    }

    public void setStep(double step) {
        this.step = step;
    }

    public double getScaleStep() {
        return scaleStep;
    }

    public void setScaleStep(double scaleStep) {
        this.scaleStep = scaleStep;
    }

    public double getMinScale() {
        return minScale;
    }

    public void setMinScale(double minScale) {
        this.minScale = minScale;
    }

    public double getMaxScale() {
        return maxScale;
    }

    public void setMaxScale(double maxScale) {
        this.maxScale = maxScale;
    }

    public double getEdgeRadius() {
        return edgeRadius;
    }

    public void setEdgeRadius(double edgeRadius) {
        this.edgeRadius = edgeRadius;
    }

    public double getEdgeWeight() {
        return edgeWeight;
    }

    public void setEdgeWeight(double edgeWeight) {
        this.edgeWeight = edgeWeight;
    }

    public double getOutsideImportance() {
        return outsideImportance;
    }

    public void setOutsideImportance(double outsideImportance) {
        this.outsideImportance = outsideImportance;
    }

    public boolean isRuleOfThirds() {
        return ruleOfThirds;
    }

    public void setRuleOfThirds(boolean ruleOfThirds) {
        this.ruleOfThirds = ruleOfThirds;
    }

    public boolean isPrescale() {
        return prescale;
    }

    public void setPrescale(boolean prescale) {
        this.prescale = prescale;
    }

    public String getCanvasFactory() {
        return canvasFactory;
    }

    public void setCanvasFactory(String canvasFactory) {
        this.canvasFactory = canvasFactory;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }



}
