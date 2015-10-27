package com.company;

import javax.security.auth.callback.Callback;
import java.awt.Image;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by matt on 10/26/15.
 */
public class SmartCrop {

    public SmartCropOptions options = null;
    
    //constructor - instantiate default options objec if we haven't been passed an options object
    public SmartCrop(SmartCropOptions options) {
        this.options = options;
    }

    public SmartCrop() {
        this.options = new SmartCropOptions();
    }

    public SmartCropResult crop(String imagePath, SmartCropOptions options) {
        if(options.aspect != 0){
            options.width = options.aspect;
            options.height = 1;
        }

        //load image from path to canvas object
        SmartCropCanvas imageCanvas = new SmartCropCanvas(imagePath);

        double scale = 1;
        double prescale = 1;

        if(options.width != 0 && options.height != 0) {
            scale = Math.min(imageCanvas.width / options.width, imageCanvas.height / options.height);
            options.cropWidth = this.safeFloor(options.width * scale);
            options.cropHeight = this.safeFloor(options.height * scale);
            // img = 100x100, width = 95x95, scale = 100/95, 1/scale > min
            // don't set minscale smaller than 1/scale
            // -> don't pick crops that need upscaling
            options.minScale = Math.min(options.maxScale, Math.max(1 / scale, options.minScale));
        }

        SmartCrop smartCrop = new SmartCrop(options);

        if(options.width != 0 && options.height != 0) {
            if(options.prescale != false){
                prescale = 1/scale/options.minScale;
                if(prescale < 1) {
                    SmartCropCanvas prescaledCanvas = new SmartCropCanvas((int)(imageCanvas.width*prescale), (int)(imageCanvas.height*prescale));
                    prescaledCanvas.drawImage(imageCanvas.image, 0, 0, imageCanvas.width, imageCanvas.height, 0, 0, prescaledCanvas.width, prescaledCanvas.height);


                    imageCanvas = prescaledCanvas;
                    smartCrop.options.cropWidth = this.safeFloor(options.cropWidth * prescale);
                    smartCrop.options.cropHeight = this.safeFloor(options.cropHeight * prescale);
                }
                else {
                    prescale = 1;
                }
            }
        }

        SmartCropResult result = smartCrop.analyze(imageCanvas);

        int resultSize = result.crops.size();

        for(int i = 0; i < resultSize; i++) {
            SmartCropCrop crop = result.crops.get(i);
            crop.x = this.safeFloor(crop.x / prescale);
            crop.y = this.safeFloor(crop.y/prescale);
            crop.width = this.safeFloor(crop.width/prescale);
            crop.height = this.safeFloor(crop.height/prescale);
        }

        return result;
    }

    public void edgeDetect(SmartCropImageData input, SmartCropImageData output) {
        int[] inputData = input.data, outputData = output.data;
        int width = input.width, height = input.height;

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int p = (y*width+x)*4;
                double lightness;

                if(x == 0 || x >= width-1 || y == 0 || y >= height-1){
                    lightness = sample(inputData, p);
                }
                else {
                    lightness = sample(inputData, p)*4 - sample(inputData, p-width*4) - sample(inputData, p-4) - sample(inputData, p+4) - sample(inputData, p+width*4);
                }
                outputData[p+1] = (int)(lightness);
            }
        }

        output.data = outputData;

    }

    public void skinDetect(SmartCropImageData input, SmartCropImageData output) {
        int[] inputData = input.data, outputData = output.data;
        int width = input.width, height = input.height;

        SmartCropOptions options = this.options;

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int p = (y*width+x)*4;
                
                double lightness = cie(inputData[p], inputData[p+1], inputData[p+2])/255;
                double skin = this.skinColor(inputData[p], inputData[p+1], inputData[p+2]);
                
                if(skin > options.skinThreshold && lightness >= options.skinBrightnessMin && lightness <= options.skinBrightnessMax){
                    outputData[p] = (int)((skin-options.skinThreshold)*(255/(1-options.skinThreshold)));
                }
                else {
                    outputData[p] = 0;
                }
            }
        }
        output.data = outputData;
    }
    
    
    public void saturationDetect(SmartCropImageData input, SmartCropImageData output){
        int[] inputData = input.data, outputData = output.data;
        int width = input.width, height = input.height;

        SmartCropOptions options = this.options;
        
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                int p = (y*width+x)*4;
                double lightness = this.cie(inputData[p], inputData[p+1], inputData[p+2])/255;
                double sat = this.saturation(inputData[p], inputData[p+1], inputData[p+2]);
                if(sat > options.saturationThreshold && lightness >= options.saturationBrightnessMin && lightness <= options.saturationBrightnessMax){
                    outputData[p+2] = (int)((sat-options.saturationThreshold)*(255/(1-options.saturationThreshold)));
                }
                else {
                    outputData[p+2] = 0;
                }
            }
        }
    }
    public List<SmartCropCrop> crops (SmartCropCanvas image) {
        List<SmartCropCrop> crops = new ArrayList<SmartCropCrop>();

        SmartCropOptions options = this.options;

        int width = image.width,
            height = image.height,
            minDimension = Math.min(width, height),
            cropWidth = (int)(options.cropWidth != 0 ? options.cropWidth : minDimension),
            cropHeight = (int)(options.cropHeight != 0 ? options.cropHeight : minDimension);


        for(double scale = options.maxScale; scale >= options.minScale; scale -= options.scaleStep){
            for(int y = 0; y+cropHeight*scale <= height; y+=options.step) {
                for(int x = 0; x+cropWidth*scale <= width; x+=options.step) {
                    SmartCropCrop crop = new SmartCropCrop();

                    crop.x = x;
                    crop.y = y;
                    crop.width =  (int)(cropWidth*scale);
                    crop.height = (int)(cropHeight*scale);

                    crops.add(crop);
                }
            }
        }
        return crops;
    }


    public SmartCropScore score(SmartCropImageData output, SmartCropCrop crop) {
        SmartCropScore score = new SmartCropScore();
        SmartCropOptions options = this.options;

        int[] outputData = output.data;
        double downSample = options.scoreDownSample;
        double invDownSample = 1/downSample;
        double outputHeightDownSample = output.height*downSample;
        double outputWidthDownSample = output.width*downSample;
        double outputWidth = output.width;

        for(int y = 0; y < outputHeightDownSample; y+=downSample) {
            for(int x = 0; x < outputWidthDownSample; x+=downSample) {
                int p = (int)((this.safeFloor(y*invDownSample)*outputWidth+this.safeFloor(x*invDownSample))*4);
                double importance = this.importance(crop, x, y),
                        detail = outputData[p+1]/255;
                score.skin += outputData[p]/255*(detail+options.skinBias)*importance;
                score.detail += detail*importance;
                score.saturation += outputData[p+2]/255*(detail+options.saturationBias)*importance;
            }

        }

        //precaution for DIV0 errors
        double cropWidth = (crop.width == 0 ? .001 : crop.width);
        double cropHeight = (crop.height == 0 ? .001 : crop.height);

        score.total = (score.detail*options.detailWeight + score.skin*options.skinWeight + score.saturation*options.saturationWeight)/cropWidth/cropHeight;
        System.out.println("score.total=" + score.total);

        return score;
    }

    public double importance(SmartCropCrop crop, int x, int y) {
        SmartCropOptions options = this.options;

        if (crop.x > x || x >= crop.x+crop.width || crop.y > y || y >= crop.y+crop.height) return options.outsideImportance;
        x = (x-crop.x)/crop.width;
        y = (y-crop.y)/crop.height;

        double px = Math.abs(0.5-x)*2,
                py = Math.abs(0.5-y)*2,
                // distance from edge
                dx = Math.max(px-1.0+options.edgeRadius, 0),
                dy = Math.max(py-1.0+options.edgeRadius, 0),
                d = (dx*dx+dy*dy)*options.edgeWeight;

        double s = 1.41-Math.sqrt(px * px + py * py);

        if(options.ruleOfThirds){
            s += (Math.max(0, s+d+0.5)*1.2)*(this.thirds(px)+this.thirds(py));
        }

        return s+d;
    }

    public double skinColor(int r, int g, int b){
        SmartCropOptions options = this.options;
        double mag = Math.sqrt(r * r + g * g + b * b),
                rd = (r/mag-options.skinColor[0]),
                gd = (g/mag-options.skinColor[1]),
                bd = (b/mag-options.skinColor[2]),
                d = Math.sqrt(rd * rd + gd * gd + bd * bd);

        return 1-d;
    }

    public SmartCropResult analyze(SmartCropCanvas image){
        SmartCropResult result = new SmartCropResult();
        SmartCropOptions options = this.options;

        SmartCropCanvas canvas = new SmartCropCanvas(image.width, image.height);

        canvas.drawImage(image.image);

        SmartCropImageData input = canvas.getImageData();
        SmartCropImageData output = canvas.getImageData();

        this.edgeDetect(input, output);

        this.skinDetect(input, output);
        this.saturationDetect(input, output);

        SmartCropCanvas scoreCanvas = new SmartCropCanvas((int)(Math.ceil(image.width/options.scoreDownSample)), (int)(Math.ceil(image.height/options.scoreDownSample)));

        canvas.setImageData(output);

        scoreCanvas.drawImage(canvas.image, 0, 0, canvas.image.getWidth(), canvas.image.getHeight(), 0, 0, scoreCanvas.width, scoreCanvas.height);

        SmartCropImageData scoreOutput = scoreCanvas.getImageData();

        double topScore = Double.NEGATIVE_INFINITY;
        SmartCropCrop topCrop = null;

        List<SmartCropCrop> crops = this.crops(image);

        for(int i = 0; i < crops.size(); i++) {
            SmartCropCrop crop = crops.get(i);

            crop.score = this.score(scoreOutput, crop);

            if(crop.score.total > topScore){
                topCrop = crop;
                topScore = crop.score.total;
            }

        }

        result.crops = crops;
        result.topCrop = topCrop;

        //TODO: clean this shit up or delete it
        /*

        if(options.debug && topCrop != null){

            ctx.fillStyle = 'rgba(255, 0, 0, 0.1)';
            ctx.fillRect(topCrop.x, topCrop.y, topCrop.width, topCrop.height);
            for (var y = 0; y < output.height; y++) {
                for (var x = 0; x < output.width; x++) {
                    var p = (y * output.width + x) * 4;
                    var importance = this.importance(topCrop, x, y);
                    if (importance > 0) {
                        output.data[p + 1] += importance * 32;
                    }

                    if (importance < 0) {
                        output.data[p] += importance * -64;
                    }
                    output.data[p + 3] = 255;
                }
            }
            ctx.putImageData(output, 0, 0);
            ctx.strokeStyle = 'rgba(255, 0, 0, 0.8)';
            ctx.strokeRect(topCrop.x, topCrop.y, topCrop.width, topCrop.height);
            result.debugCanvas = canvas;
        }

        */

        return result;
    }

    // gets value in the range of [0, 1] where 0 is the center of the pictures
    // returns weight of rule of thirds [0, 1]
    public double thirds(double x){
        x = ((x-(1/3)+1.0)%2.0*0.5-0.5)*16;
        return Math.max(1.0-x*x, 0.0);
    }

    public double cie(double r, double g, double b){
        return 0.5126*b + 0.7152*g + 0.0722*r;
    }

    public double sample(int[] inputData, int p) {
        return cie(inputData[p], inputData[p+1], inputData[p+2]);
    }

    public int safeFloor(double number) {
        if (number < 0) {
            double nonNegativeNumber = -number;
            double nonNegativeFloor = Math.floor(nonNegativeNumber);
            int negativeReturnValue = (int)(-nonNegativeFloor);
            return negativeReturnValue;
        } else {
            return (int)(Math.floor(number));
        }
    }

    public double saturation(double r, double g, double b){
        double maximum = Math.max(r / 255, Math.max(g / 255, b / 255));
        double minumum = Math.min(r/255, Math.min(g/255, b/255));

        if(maximum == minumum){
            return 0;
        }

        double l = (maximum + minumum) / 2;
        double d = maximum-minumum;

        return l > 0.5 ? d/(2-maximum-minumum) : d/(maximum+minumum);
    }


} //end of class
