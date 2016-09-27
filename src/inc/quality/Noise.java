/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inc.quality;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author maryan
 */
public class Noise {

    private final int[][] pixels;
    private static final int BLOCKSIZE = 32;
    private Map<Double, Double> map = new TreeMap<>();
    private int noiseMultiplier = 9;
    
    public Noise(int[][] pixels) {
        this.pixels = pixels;
    }
    
    /**
     * 
     * @param noiseMultiplier 
     */
    public void setNoiseMultiplier(int noiseMultiplier) {
        this.noiseMultiplier = noiseMultiplier;
    }
    
    /**
     * 
     * @param x
     * @param y
     * @return 
     */
    public double getMean(final int x, final int y) {
        int sum = 0;
        int count = 0;
        for (int i = x; i < x + BLOCKSIZE; i++) {
            for (int j = y; j < y + BLOCKSIZE; j++) {
                try {
                    sum += this.pixels[i][j];
                } catch (ArrayIndexOutOfBoundsException e) {
//                    count++; 
                    count += ((y + BLOCKSIZE) - j);
                    break;   
                }
            }
        }
        return sum / (double) ((BLOCKSIZE * BLOCKSIZE) - count);
    }
    
    /**
     * 
     * @param x
     * @param y
     * @param mean
     * @return 
     */
    public final double getVariance(final int x, final int y, final double mean) {
        double v = 0;
        int count = 0;
        for (int i = x; i < x + BLOCKSIZE; i++) {
            for (int j = y; j < y + BLOCKSIZE; j++) {
                try {
                    v += Math.pow(pixels[i][j] - mean, 2);
                } catch (ArrayIndexOutOfBoundsException e) {
//                    count++;
                    count += ((y + BLOCKSIZE) - j);
                    break;
                }
            }
        }
        return Math.sqrt(v / (double) ((BLOCKSIZE * BLOCKSIZE) - count));
    }
    
    /**
     * 
     * @return 
     */
    public double getNoise() {
        int h = pixels[0].length;
        int w = pixels.length;
        final int rangeAvarage = 10;
        int step = BLOCKSIZE / 2; //  overlapping square area
        for (int x = 0; x < w; x += step) {  
            for (int y = 0; y < h; y += step) { 
                double mean = getMean(x, y);
                map.put(mean, getVariance(x, y, mean));
            }
        }   
        
        List<Double> mins = new ArrayList<>();
        int b = map.size() / rangeAvarage;
        double min = 255;
        int i = 0;
        
        for (Double d : map.keySet()) {
            min = min > map.get(d) ?  map.get(d) : min;
            i++;
            if (i > b) {
                i = 0;
                mins.add(min);
                min = 255;
            }
        }
        mins.add(min);
        
        double sum = 0;
        for (Double d: mins) {
            sum += d;
        }

        return sum / (double) rangeAvarage;
    }
    
    
    /**
     * 
     * @return 
     */
    public int getNoiseThreshold() {
        return (int) (noiseMultiplier * getNoise());
    }
    
}
