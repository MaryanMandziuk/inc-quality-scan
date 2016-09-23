/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inc.quality;

import static inc.quality.Utils.applyContrastUP;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author maryan
 */
public class IncQuality2 {
    public static final int LAYER_STOP = 4;
    
    public static void main(String[] args) throws IOException {
        File im = new File("images/test8.jpg");
        BufferedImage image = ImageIO.read(im);
        
        int width = image.getWidth();
        int height = image.getHeight();
        
        int[][] pixels = new int[width][height];
        
         for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    int rgb = image.getRGB(i, j);
                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = (rgb & 0xFF);
                    int gray = (r+g+b)/3;
                    int gg = (gray << 16) + (gray << 8) + gray;
                   
                    pixels[i][j] =  gray;
                }
            }
         
        Pyramid pyramid = new Pyramid(pixels);

        pyramid.proccessPyramid();
        int noiseMultiplier = 3;
        int layerStop = 2;
        int contrastLayer =22;
        applyContrastUP(pyramid, pixels, noiseMultiplier,layerStop, contrastLayer);
//        int noise = new Noise(pixels).getNoiseThreshold();
//        int[][] map = pyramid.layers.get(pyramid.layers.size() - 1).getAverage();
//        for (int i = pyramid.layers.size() - 2; i >= 0; i--) {
//            map = bilinearInterpolation(map.length, map[0].length, map, 
//                    pyramid.lenWidth.get(i), pyramid.lenHight.get(i));
//            int[][] tt = averageMinMax(pyramid.layers.get(i).getMin(), 
//                    pyramid.layers.get(i).getMax());
//            if (i >= LAYER_STOP) {
//                for (int ii = 0; ii < tt.length; ii++) {
//                    for (int j = 0; j < tt[0].length; j++) {
//                        if (tt[ii][j] > noise) {
//                            map[ii][j] = (pyramid.layers.get(i).getMax()[ii][j]
//                                    + pyramid.layers.get(i).getMin()[ii][j]) / 2;
//    //                        map[ii][j] = pyramid.list.get(i).getAverage()[ii][j];
//                        }
//                    }
//                }       
//            }
//        }

//        contrastUp(map, pixels);
        BufferedImage pixelImage = new BufferedImage(pixels.length, pixels[0].length, BufferedImage.TYPE_INT_RGB); 
//        BufferedImage mapImage = new BufferedImage(map.length, map[0].length, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < pixels.length; i++) {
                for (int j = 0; j < pixels[0].length; j++) {
                    int c = pixels[i][j];
//                    int c1 = map[i][j];
                    
                    pixelImage.setRGB(i, j, c + (c << 8) + (c << 16));
//                    mapImage.setRGB(i, j, c1 + (c1 << 8) + (c1 << 16));
                }
            }         
//        ImageIO.write(mapImage, "png", new File("test_map.png"));
        ImageIO.write(pixelImage, "png", new File("test_contrast_up.png")); 
    }
}
