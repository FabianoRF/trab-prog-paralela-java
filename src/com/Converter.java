package com;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Converter extends Thread{
    private BufferedImage image;
    private int initialHeight;
    private int initialWidth;
    private int maxHeight;
    private int maxWidth;

    public Converter(BufferedImage image, int initialHeight, int maxHeight, int initialWidth, int maxWidth) {
        this.image = image;
        this.initialHeight = initialHeight;
        this.initialWidth = initialWidth;
        this.maxHeight = maxHeight;
        this.maxWidth = maxWidth;
    }

    @Override
    public void run() {
        for (int x = this.initialWidth; x < this.maxWidth; x++) {

            for (int y = this.initialHeight; y < this.maxHeight; y++) {
                Color pixel = new Color(this.image.getRGB(x, y));

                int tom = (pixel.getRed() + pixel.getGreen() + pixel.getBlue()) / 3;

                this.image.setRGB(x, y, new Color(tom, tom, tom).getRGB());
            }
        }
    }
}
