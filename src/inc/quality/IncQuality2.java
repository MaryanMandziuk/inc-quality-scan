/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inc.quality;

import static inc.quality.Utils.averageMinMax;
import static inc.quality.Utils.bilinearInterpolation;
import static inc.quality.Utils.checkNoiseThreshold;
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
        
        int[][] pixels = new int[width][height];

        System.out.println(width + " " + height);
        
        
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


        int noise = new Noise(pixels).getNoiseThreshold();
        int[][] map = pyramid.list.get(pyramid.list.size()-1).getAverage();
        
        for(int i = pyramid.list.size() - 2; i >= 0; i --) {
            map = bilinearInterpolation(map.length, map[0].length, map);
            int[][] tt = averageMinMax(pyramid.list.get(i).getMin(), 
                    pyramid.list.get(i).getMax());
            if (i > 0)
            for (int ii = 0; ii < tt.length; ii++) {
                for (int j = 0; j < tt[0].length; j++) {
                    if(tt[ii][j] > noise) {
                        map[ii][j] = (pyramid.list.get(i).getMax()[ii][j]
                                +pyramid.list.get(i).getMin()[ii][j])/2;
                    }
//                     System.out.println(tt[ii][j]);
                }
            }      
            
//            if ( checkNoiseThreshold(averageMinMax(pyramid.list.get(i).getMax(), 
//                    pyramid.list.get(i).getMin()), noise ) ) {
//                map = pyramid.list.get(i).getAverage();
//            }
        }
        System.out.println(map.length + " " + map[0].length);
        BufferedImage pixelImage = new BufferedImage(map.length, map[0].length, BufferedImage.TYPE_INT_RGB); 
        for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    int c = map[i][j];
                    if (c > 255 || c < 0) {
                        System.err.println(c);
                    }
                    
                    pixelImage.setRGB(i, j, c + (c << 8 ) + (c <<16));

                  
                }
            }         
        ImageIO.write(pixelImage, "png", new File( "test_map.png"));


    }
}
