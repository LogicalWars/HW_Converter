package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class TextGraphicsConverterImpl implements TextGraphicsConverter {
    private final int[] STUB = new int[3];
    private TextColorSchema schema = new TextColorSchemaImpl();
    private double maxRatio;
    private int maxWidth;
    private int maxHeight;
    private int newWidth;
    private int newHeight;


    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));
        newWidth = img.getWidth();
        newHeight = img.getHeight();

        double ratio = (double) Math.max(newWidth, newHeight) / Math.min(newWidth, newHeight);
        if (ratio > maxRatio) {
            throw new BadImageSizeException(ratio, maxRatio);
        }

        if (newWidth > newHeight) {
            scaleWidth(newWidth, newHeight);
            scaleHeight(newWidth, newHeight);

        } else {
            scaleHeight(newWidth, newHeight);
            scaleWidth(newWidth, newHeight);
        }

        // Меняем размер картинки
        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        // Меняем цвета на черно-белое
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);

        // Включаем инструмент, чтобы пройтись по пикселям
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        WritableRaster bwRaster = bwImg.getRaster();

        StringBuilder imgText = new StringBuilder();
        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                for (int k = 0; k < 2; k++) {
                    imgText.append(schema.convert(bwRaster.getPixel(j, i, STUB)[0]));
                }
            }
            imgText.append("\n");
        }
        return imgText.toString();
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

    private void scaleWidth(int newWidth, int newHeight) {
        double scale;
        if (newWidth > maxWidth) {
            scale = (double) newWidth / maxWidth;
            this.newHeight = (int) (newHeight / scale);
            this.newWidth = maxWidth;
        }
    }

    private void scaleHeight(int newWidth, int newHeight) {
        double scale;
        if (newHeight > maxHeight) {
            scale = (double) newHeight / maxHeight;
            this.newWidth = (int) (newWidth / scale);
            this.newHeight = maxHeight;
        }
    }
}
