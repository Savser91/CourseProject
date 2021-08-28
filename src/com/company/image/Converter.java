package com.company.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Converter extends ColorSchema implements TextGraphicsConverter {
    ColorSchema schema = new ColorSchema();
    private int maxWidth = 1_000_000;
    private int maxHeight = 1_000_000;
    private double maxRatio = 0;
    private char[][] arrayChar;
    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));
        int newWidth = 0;
        int newHeight = 0;
        double ratio;
        if (img.getWidth() > this.maxWidth && img.getHeight() <= this.maxHeight) {
            ratio = (double) img.getWidth() / maxWidth;
            newWidth = (int) Math.floor(img.getWidth() / ratio);
            newHeight = (int) Math.floor(img.getHeight() / ratio);
        } else if (img.getWidth() > this.maxWidth && img.getHeight() > this.maxHeight) {
            ratio = (img.getWidth() / maxWidth > img.getHeight() / this.maxHeight) ? (double) img.getWidth() / maxWidth : (double) img.getHeight() / this.maxHeight;
            newWidth = (int) Math.floor(img.getWidth() / ratio);
            newHeight = (int) Math.floor(img.getHeight() / ratio);
        } else if (img.getWidth() <= this.maxWidth && img.getHeight() > this.maxHeight) {
            ratio = (double) img.getHeight() / maxHeight;
            newWidth = (int) Math.floor(img.getWidth() / ratio);
            newHeight = (int) Math.floor(img.getHeight() / ratio);
        } else if (img.getWidth() < this.maxWidth && img.getHeight() < this.maxHeight) {
            newWidth = img.getWidth();
            newHeight = img.getHeight();
        }
        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        ImageIO.write(bwImg, "png", new File("out.png"));
        WritableRaster bwRaster = bwImg.getRaster();

        arrayChar = new char[newWidth][newHeight];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < newHeight; j++) {
                arrayChar[i][j] = schema.convert(bwRaster.getPixel(i, j, new int[3])[0]);
            }
        }

        for (int i = 0; i < arrayChar[0].length; i++) {
            for (int j = 0; j < arrayChar.length; j++) {
                sb.append(String.valueOf(arrayChar[arrayChar.length - j - 1][i]));
                sb.append(String.valueOf(arrayChar[arrayChar.length - j - 1][i]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        schema = this.schema;
    }
}