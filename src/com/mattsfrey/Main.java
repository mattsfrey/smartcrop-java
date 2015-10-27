package com.company;

public class Main {

    public static void main(String[] args) {
	// write your code here
        SmartCrop smartCrop = new SmartCrop();

        System.out.println("RUNNING CROP...");

        SmartCropOptions options = new SmartCropOptions();
        options.width = 1000;
        options.height = 1000;

        SmartCropResult result = smartCrop.crop("/Users/matt/crop-test.jpg", options);

        try {
            System.out.println("RESULT>>>>>");
            System.out.println("result = " + result.topCrop.score.total);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

    }


}
