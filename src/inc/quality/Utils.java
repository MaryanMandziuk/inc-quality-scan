/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inc.quality;

import java.awt.Color;

/**
 *
 * @author maryan
 */
public class Utils {
    
    public static int LOCALDIMENSION = 29;
    private static final int K = 60;
    public static int[][] SAT(int[][] pixels) {
        int h = pixels[0].length;
        int w = pixels.length;
        int[][] tmp = new int[w][h];
        
        
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (j > 0) {
                    tmp[i][j] = pixels[i][j] + tmp[i][j-1];
                } else {
                    tmp[i][j] = pixels[i][j];
                }

            }
        }
         
        
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (j > 0)
                    tmp[j][i] += tmp[j-1][i];
            }
        }
        
        
        return tmp;
    }
    
    public static int[][] mapThresholdArray(int[][] sat) {
        int h = sat[0].length;
        int w = sat.length;
        int[][] tmp = new int[w][h];
         for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {
                    tmp[i][j] = getLocalAverageValue(sat, i, j);
            }
        }
         
        return tmp;
    }
    
    
    public static int getLocalAverageValue(int[][] sat, int i, int j) {
        int offset = LOCALDIMENSION / 2 ;
        int A,B,C,D;
        try {
            A = sat[i - offset - 1][j - offset - 1];
        } catch (ArrayIndexOutOfBoundsException e) {
            A = 0;
        }
        try { 
            B = sat[i + offset ][j - offset - 1];
        } catch (ArrayIndexOutOfBoundsException e) {
            B = 0;
        }
        try {
            C = sat[i - offset - 1][j + offset];
        } catch (ArrayIndexOutOfBoundsException e) {
            C = 0;
        }
        try { 
            D = sat[i + offset][j + offset];
        } catch (ArrayIndexOutOfBoundsException e) {
            D = 0;
        }
        int res = (D + A - B - C) / (LOCALDIMENSION * LOCALDIMENSION);
        if (res < 0 ) {
            res = 0;
        } else if (res > 255) {
            res = 255;
        }
        return res;
    }
    
    public static void contrastUp(int[][] map, int[][] pixels) {
        int h = map[0].length;
        int w = map.length;
        Noise n = new Noise(pixels);
        
        int constant = n.getNoiseThreshold();
//        System.out.println(constant);
         for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {

                    int t = map[i][j] - constant;
                   
                    int tmp = K*(pixels[i][j] - t)  + t;
                    if (tmp < 0) {
                        tmp = 0;
                    } else if (tmp > 255) {
                        tmp = 255;
                    }

                    pixels[i][j] = tmp;
                    
            }
        }
    }
    
    public static int[][] bilinearInterpolation(int w, int h, int[][] pixels) {
        int new_w = w * 2;
        int new_h = h * 2;
        int A, B, C, D;
        int[][] newPixels = new int[new_w][new_h];
        double tmp, diff_w, diff_h;
        int h2, w2;
        for (int j = 0; j < new_h; j++) {
            tmp = j * ((double) (h-1) / (new_h-1));
            h2 = (int) tmp;
            if (h2 < 0 ) {
                h2 = 0;
            } else {
                if (h2 >= h - 1) {
                    h2 = h - 2; 
                }
            }
            for (int i = 0; i < new_w; i++) {
                tmp = i * ((double)(w-1) / (new_w-1));
                w2 = (int) tmp;
                if (w2 < 0) {
                    w2 = 0;
                } else {
                    if (w2 >= w-1) {
                        w2 = w -2;
                    }
                }
                diff_h = (j * ((double) (h-1) / (new_h-1))) - h2;
                diff_w = tmp - w2;
//                System.out.println("h=" + diff_h + " w=" + diff_w);
                A = pixels[w2][h2];
                B = pixels[w2][h2 + 1];
                C = pixels[w2 + 1][h2];
                D = pixels[w2 + 1][h2 + 1];
                
                
                newPixels[i][j] = (int) (A *(1-diff_w)*(1-diff_h) + B*diff_w*(1-diff_h) +
                        C*(1-diff_w)*diff_h + D*diff_w*diff_h);
            }
        }

        
        return newPixels;
    }
    
    public static int[][] averageMinMax(int[][] pixelsMin, int[][] pixelsMax) {
        int h = pixelsMin[0].length;
        int w = pixelsMin.length;
        int[][] tmp = new int[w][h];
         for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {
                    tmp[i][j] = pixelsMax[i][j] - pixelsMin[i][j];
            }
        }
         
        return tmp;
    }
    
    public static boolean checkNoiseThreshold(int[][] pixels, int noiseThreshold) {
        int h = pixels[0].length;
        int w = pixels.length;
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (pixels[i][j] > noiseThreshold) {
                    return true;
                }
            }
        }
         
        return false;
    } 
}
