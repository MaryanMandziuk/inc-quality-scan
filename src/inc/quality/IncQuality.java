/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inc.quality;

import static inc.quality.Utils.SAT;
import static inc.quality.Utils.contrastUp;
import static inc.quality.Utils.mapThresholdArray;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author maryan
 */
public class IncQuality {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        File im = new File("images/test3.jpg");
        BufferedImage image = ImageIO.read(im);
        
        int width = image.getWidth();
        int  height = image.getHeight();
        BufferedImage pixelImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
        int[][] pixels = new int[width][height];
        int[][] sat = new int[width][height];
        int[][] map = new int[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb & 0xFF);

                pixels[i][j] = (r + g + b) / 3;
            }
        }
         
        sat = SAT(pixels);
        map = mapThresholdArray(sat);
        Noise noise = new Noise(pixels);
        int n = noise.getNoiseThreshold();
        contrastUp(map, pixels, 23, n);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int c = pixels[i][j];
                pixelImage.setRGB(i, j, c + (c << 8) + (c << 16));
            }
        }         
        ImageIO.write(pixelImage, "png", new File("test.png"));
    }  
}
