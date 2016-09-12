/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inc.quality;

import java.util.ArrayList;
import java.util.List;


class Layer {
    private final int[][] min;
    private final int[][] max;
    private final int[][] average;
    
    Layer(int[][] pixels) { 
        int h = pixels[0].length;
        int w = pixels.length;
        int s_h = h/2;
        int s_w = w/2;
        if (s_h%2 != 0) {
            s_h = s_h+1; 
        }
        if (s_w%2 != 0) {
            s_w = s_w +1 ;
        }
        int mint[][] = new int[s_w][s_h];
        int maxt[][] = new int[s_w][s_h];
        int averaget[][] = new int[s_w][s_h];
//        System.out.println(s_w + " " + s_h);
        for (int i = 0; i < w; i += 2) {
            for (int j = 0; j < h; j += 2) {
               mint[i/2][j/2] = getMinValue(i,j, pixels);
               maxt[i/2][j/2] = getMaxValue(i,j, pixels);
               averaget[i/2][j/2] = getAverageValue(i, j, pixels);
            }
        }

        this.min = mint;
        this.average = averaget;
        this.max = maxt;   
    }
    
    public int[][] getMin() {
        return this.min;
    }
    
    public int[][] getMax() {
        return this.max;
    }
    
    public int[][] getAverage() {
        return this.average;
    }
    
    private int getMinValue(int x, int y, int[][] pixels) {
        int min = pixels[x][y];
        for (int i = x; i < x + 2; i ++ ) {
            for (int j = y; j < y + 2; j ++) {
                try {
                    if (min > pixels[i][j]) {
                        min = pixels[i][j];
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    
                }
            }
        }
        return min;
    }
    
    private int getMaxValue(int x, int y, int[][] pixels) {
        int max = pixels[x][y];
        for (int i = x; i < x + 2; i ++ ) {
            for (int j = y; j < y + 2; j ++) {
                try {
                    if (max < pixels[i][j]) {
                        max = pixels[i][j];
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    
                }
            }
        }
        return max;
    }
    
    private int getAverageValue(int x, int y, int[][] pixels) {
        int avg = 0;
        int count = 0;
        for (int i = x; i < x + 2; i ++ ) {
            for (int j = y; j < y + 2; j ++) {
                try {
                    avg += pixels[i][j];
                } catch (ArrayIndexOutOfBoundsException e) {
                    count ++;
                }
            }
        }
        return avg/(4 - count);
    }
}

/**
 *
 * @author maryan
 */
public class Pyramid {
    public List<Layer> list = new ArrayList<>();
    public int[][] pixels;
    Pyramid(int[][] pixels) {
        this.pixels = pixels;
    }
    
    public void proccessPyramid() {
        Layer layer = new Layer(this.pixels);
        int len = layer.getAverage().length;
        list.add(layer);
        while (len > 2) {   
            layer = new Layer(layer.getAverage());
            list.add(layer);
            len = layer.getAverage().length > layer.getAverage()[0].length 
                    ? layer.getAverage()[0].length : layer.getAverage().length;

        }
    }
}
