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
    public static int BLOCKSIZE = 32;
    public Map<Double, Double> map = new TreeMap<Double, Double>();
    private static final int NOISE_CONSTANT = 5;
    
    public Noise(int[][] pixels) {
        this.pixels = pixels;
    }
    
    public double getMean(int x, int y) {
        int sum = 0;
        int count = 0;
        for (int i = x; i < x + BLOCKSIZE; i ++ ) {
            for (int j = y; j < y + BLOCKSIZE; j ++) {
                try {
                    sum += this.pixels[i][j];
                } catch ( ArrayIndexOutOfBoundsException e) {
                    count++; break;
                    
                }
            }
        }
        return sum / ((BLOCKSIZE * BLOCKSIZE) - count);
    }
    
    public double getVariance(int x, int y, double mean) {
       double v = 0;
       int count = 0;
        for (int i = x; i < x + BLOCKSIZE; i ++ ) {
            for (int j = y; j < y + BLOCKSIZE; j ++) {
                try {
                    v += Math.pow(pixels[i][j] - mean, 2);
                } catch (ArrayIndexOutOfBoundsException e) {
                    count++; break;
                }
            }
        }
       return Math.sqrt(v / ((BLOCKSIZE * BLOCKSIZE) - count));
    }
    
    public double getNoise() {
        int h = pixels[0].length;
        int w = pixels.length;
        int rangeAvarage = 10;
        for (int x = 0; x < w; x += BLOCKSIZE ) {
            for (int y = 0; y < h; y += BLOCKSIZE) {
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
            }
        }
        double sum =0;
        for (Double d: mins) {
            sum += d;
        }
        
        return sum / rangeAvarage;
    }
    
    
    public int getNoiseThreshold() {
        return (int)(NOISE_CONSTANT * getNoise());
    }
    
}
