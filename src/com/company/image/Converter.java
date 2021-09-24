package com.company.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.URL;

public class Converter implements TextGraphicsConverter {
    TextColorSchema schema;
    private int maxWidth = 1_000_000;
    private int maxHeight = 1_000_000;
    private int finalWidth = 0;
    private int finalHeight = 0;
    private double maxRatio = 0;

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));
        double ratio = (img.getWidth() > img.getHeight()) ?  (double)img.getWidth() / img.getHeight() :
                (double)img.getHeight() / img.getWidth();
        // проверяем картинку на соотношение сторон
        // если соотношение сторон слишком большое, выбрасываем исключение
        if (ratio > this.maxRatio) {
            throw new BadImageSizeException(ratio, this.maxRatio);
        }

        setImageSize(img);
        Image scaledImage = img.getScaledInstance(finalWidth, finalHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(finalWidth, finalHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        ImageIO.write(bwImg, "png", new File("out.png"));
        WritableRaster bwRaster = bwImg.getRaster();
        return getResult(getCharArray(bwRaster));
    }

    private void setImageSize(BufferedImage img) {
        /*
        далее урезаем картинку, устанавливаем новые длину и ширину
         картинки, если требуется
        в cutCoefficient записываем коэффициент в зависимости от того, какая сторона
        сильнее не укладывается в заданный диапазон
         */
        double cutCoefficient = 0;
        if (img.getWidth() > this.maxWidth && img.getHeight() <= this.maxHeight) {
            cutCoefficient = (double) img.getWidth() / maxWidth;
        } else if (img.getWidth() > this.maxWidth && img.getHeight() > this.maxHeight) {
            cutCoefficient = (img.getWidth() / maxWidth > img.getHeight() / this.maxHeight) ?
                    (double) img.getWidth() / maxWidth : (double) img.getHeight() / this.maxHeight;
        } else if (img.getWidth() <= this.maxWidth && img.getHeight() > this.maxHeight) {
            cutCoefficient = (double) img.getHeight() / maxHeight;
        } else if (img.getWidth() < this.maxWidth && img.getHeight() < this.maxHeight) {
            finalWidth = img.getWidth();
            finalHeight = img.getHeight();
        }
        finalWidth = (int) Math.floor(img.getWidth() / cutCoefficient);
        finalHeight = (int) Math.floor(img.getHeight() / cutCoefficient);
    }

    private char[][] getCharArray(WritableRaster bwRaster) {
        char[][] arrayChar = new char[finalWidth][finalHeight];
        for (int i = 0; i < finalWidth; i++) {
            for (int j = 0; j < finalHeight; j++) {
                arrayChar[i][j] = schema.convert(bwRaster.getPixel(finalWidth - i - 1, j, new int[3])[0]);
            }
        }
        return arrayChar;
    }

    private String getResult(char[][] arrayChar) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arrayChar[0].length; i++) {
            for (int j = 0; j < arrayChar.length; j++) {
                sb.append(arrayChar[arrayChar.length - j - 1][i]);
                sb.append(arrayChar[arrayChar.length - j - 1][i]);
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
        this.schema = schema;
    }
}