package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by matt on 10/26/15.
 */
public class SmartCropCanvas extends Component {

    public Graphics2D graphics = null;
    public BufferedImage image = null;

    public int width  = 0;
    public int height = 0;

    public SmartCropCanvas(String imagePath) {
        BufferedImage img = null;

        System.out.println("Loading data for IMG: '" + imagePath + "'");

        try {
            img = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            System.out.println("Error loading image file.");
        }

        if (img == null) {
            System.out.println("Image object null.");
        }

        this.width = img.getWidth();
        this.height = img.getHeight();

        this.image = img;

        this.graphics = (Graphics2D)img.getGraphics();

    }

    public SmartCropCanvas(int width, int height) {
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        this.graphics = this.image.createGraphics();
        this.width = width;
        this.height= height;
    }

    public void drawImage(BufferedImage imageSource, int sourceX, int sourceY, int sourceWidth, int sourceHeight, int destX, int destY, int destWidth, int destHeight) {

        this.graphics.drawImage(imageSource, destX, destY, destWidth, destHeight, sourceX, sourceY, sourceWidth, sourceHeight, this);

    }

    public void drawImage(BufferedImage img) {
        this.graphics.drawImage(img, 0 , 0, this);
    }

    public SmartCropImageData getImageData() {
        SmartCropImageData imageData = new SmartCropImageData();

        int data[] = null;

        data = this.image.getRaster().getPixels(0, 0, this.image.getWidth(), this.image.getHeight(), data);

        imageData.data = data;
        imageData.width = this.image.getWidth();
        imageData.height = this.image.getHeight();

        return imageData;
    }

    public void setImageData(int[] imageData) {
        this.image.getRaster().setPixels(0, 0, this.image.getWidth(), this.image.getHeight(), imageData);
    }

    public void setImageData(SmartCropImageData imageData) {

        System.out.println("Setting data: width=" + imageData.width + " height="  + imageData.height + " data=" + imageData.data);
        System.out.println("Canvas width: " + this.image.getWidth() + " Canvas height: " + this.image.getHeight());

        this.image.getRaster().setPixels(0, 0, this.image.getWidth(), this.image.getHeight(), imageData.data);
    }

    //public void drawImageFromSource(BufferedImage )


}
