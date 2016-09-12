/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inc.quality;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author maryan
 */
public class IncQuality2 {
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        File im = new File("images/test.jpg");
        BufferedImage image = ImageIO.read(im);
        
        int width = image.getWidth();
        int  height = image.getHeight();
        BufferedImage pixelImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
        int[][] pixels = new int[width][height];

        
        
        
         for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int rgb = image.getRGB(i, j);
//                    int alpha = (rgb >> 24) & 0xff;
                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = (rgb & 0xFF);

                    pixels[i][j] = (r + g + b) / 3;
                }
            }
         
        Pyramid pyramid = new Pyramid(pixels);

        pyramid.proccessPyramid();
//        System.out.println(pyramid.list.size());
//        for(Layer i: pyramid.list) {
//            System.out.println(i.getAverage()[0].length);
//        }


    }
}
