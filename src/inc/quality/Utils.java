/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inc.quality;

/**
 *
 * @author maryan
 */
public class Utils {
    
    private static final int LOCALDIMENSION = 29;
    private static final int K = 60;
    
    /**
     * 
     * @param pixels
     * @return 
     */
    public static int[][] SAT(final int[][] pixels) {
        int h = pixels[0].length;
        int w = pixels.length;
        int[][] tmp = new int[w][h];
        
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (j > 0) {
                    tmp[i][j] = pixels[i][j] + tmp[i][j - 1];
                } else {
                    tmp[i][j] = pixels[i][j];
                }
            }
        }
         
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (j > 0) {
                    tmp[j][i] += tmp[j - 1][i];
                }
            }
        }
        
        return tmp;
    }
    
    /**
     * 
     * @param sat
     * @return 
     */
    public static int[][] mapThresholdArray(final int[][] sat) {
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
    
    /**
     * 
     * @param sat
     * @param i
     * @param j
     * @return 
     */
    public static int getLocalAverageValue(final int[][] sat, final int i, final int j) {
        int offset = LOCALDIMENSION / 2;
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
        if (res < 0) {
            res = 0;
        } else if (res > 255) {
            res = 255;
        }
        return res;
    }
    
    /**
     * 
     * @param map
     * @param pixels 
     * @param contrastLevel 
     * @param noise 
     */
    public static void contrastUp(final int[][] map, int[][] pixels,
            int contrastLevel, int noise) {
        int h = map[0].length;
        int w = map.length;
        
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {

                int t = map[i][j] - noise;

                int tmp = contrastLevel * (pixels[i][j] - t) + t;
                if (tmp < 0) {
                    tmp = 0;
                } else if (tmp > 255) {
                    tmp = 255;
                }
                pixels[i][j] = tmp;        
            }
        }
    }
    
    /**
     * Resizing input image's pixels 
     * @param w - current width
     * @param h - current height
     * @param pixels
     * @param w1 - new width
     * @param h1 - new height
     * @return pixels with new dimension
     */
    public static int[][] bilinearInterpolation(final int w, final int h,
                            final int[][] pixels, final int w1, final int h1) {
        int A, B, C, D;
        int[][] newPixels = new int[w1][h1];
        double tmp, diffW, diffH;
        int h2, w2;
        for (int j = 0; j < h1; j++) {
            tmp = j * ((double) (h - 1) / (h1 - 1));
            h2 = (int) tmp;
            if (h2 < 0) {
                h2 = 0;
            } else {
                if (h2 >= h - 1) {
                    h2 = h - 2; 
                }
            }
            for (int i = 0; i < w1; i++) {
                tmp = i * ((double)(w - 1) / (w1 - 1));
                w2 = (int) tmp;
                if (w2 < 0) {
                    w2 = 0;
                } else {
                    if (w2 >= w-1) {
                        w2 = w -2;
                    }
                }
                diffH = (j * ((double) (h - 1) / (h1 - 1))) - h2;
                diffW = tmp - w2;

                A = pixels[w2][h2];
                B = pixels[w2][h2 + 1];
                C = pixels[w2 + 1][h2];
                D = pixels[w2 + 1][h2 + 1];
                
                
                newPixels[i][j] = (int) (A * (1 - diffW) * (1 - diffH) 
                        + B * diffW * (1 - diffH) + C * (1 - diffW) * diffH 
                        + D * diffW * diffH);
            }
        }
        return newPixels;
    }
    
    /**
     * 
     * @param pixelsMin
     * @param pixelsMax
     * @return 
     */
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
    
    /**
     * 
     * @param pixels
     * @param noiseThreshold
     * @return 
     */
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
    
    public static void applyContrastUP(Pyramid pyramid, int[][] pixels,
            int noiseMultiplier, int layerStop, int contrastLevel){

        
        Noise noise = new Noise(pixels);
        noise.setNoiseMultiplier(noiseMultiplier);
        int n = noise.getNoiseThreshold();
        int[][] map = pyramid.layers.get(pyramid.layers.size() - 1).getAverage();
        for (int i = pyramid.layers.size() - 2; i >= 0; i--) {
            map = bilinearInterpolation(map.length, map[0].length, map, 
                    pyramid.lenWidth.get(i), pyramid.lenHight.get(i));
            int[][] tt = averageMinMax(pyramid.layers.get(i).getMin(), 
                    pyramid.layers.get(i).getMax());
            if (i >= layerStop) {
                for (int ii = 0; ii < tt.length; ii++) {
                    for (int j = 0; j < tt[0].length; j++) {
                        if (tt[ii][j] > n) {
                            map[ii][j] = (pyramid.layers.get(i).getMax()[ii][j]
                                    + pyramid.layers.get(i).getMin()[ii][j]) / 2;
    //                        map[ii][j] = pyramid.list.get(i).getAverage()[ii][j];
                        }
                    }
                }       
            }
        }

        contrastUp(map, pixels, contrastLevel, n);
    }
}
