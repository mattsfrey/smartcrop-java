package com.company;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by matt on 10/26/15.
 * object returned from the 'analyze' method
 */
public class SmartCropResult {

    public List<SmartCropCrop> crops = new ArrayList<SmartCropCrop>();

    public SmartCropCrop topCrop = null;

}
