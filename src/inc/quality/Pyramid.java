/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inc.quality;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author maryan
 */
class Layer {
    private final int[][] min;
    private final int[][] max;
    private final int[][] average;

    Layer(final int[][] pixels) { 
        int h = pixels[0].length;
        int w = pixels.length;
        int sH = h / 2;
        int sW = w / 2;
//        if (s_h%2 != 0) {
//            s_h = s_h+1; 
//        }
//        if (s_w%2 != 0) {
//            s_w = s_w +1 ;
//        }
        int[][] mint = new int[sW][sH];
        int[][] maxt = new int[sW][sH];
        int[][] averaget = new int[sW][sH];
        for (int i = 0; i < w; i += 2) {
            for (int j = 0; j < h; j += 2) {
                try {
                    mint[i / 2][j / 2] = getMinValue(i, j, pixels);
                    maxt[i / 2][j / 2] = getMaxValue(i, j, pixels);
                    averaget[i / 2][j / 2] = getAverageValue(i, j, pixels);
                } catch (ArrayIndexOutOfBoundsException e) {
                    break;
                }
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
    
    /**
     * 
     * @param x
     * @param y
     * @param pixels
     * @return 
     */
    private int getMinValue(final int x, final int y, final int[][] pixels) {
        int minValue = pixels[x][y];
        for (int i = x; i < x + 2; i++) {
            for (int j = y; j < y + 2; j++) {
                try {
                    if (minValue > pixels[i][j]) {
                        minValue = pixels[i][j];
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    break;
                }
            }
        }
        return minValue;
    }
    
    /**
     * 
     * @param x
     * @param y
     * @param pixels
     * @return 
     */
    private int getMaxValue(final int x, final int y, final int[][] pixels) {
        int maxValue = pixels[x][y];
        for (int i = x; i < x + 2; i++) {
            for (int j = y; j < y + 2; j++) {
                try {
                    if (maxValue < pixels[i][j]) {
                        maxValue = pixels[i][j];
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    break;
                }
            }
        }
        return maxValue;
    }

    /**
     * 
     * @param x
     * @param y
     * @param pixels
     * @return 
     */
    private int getAverageValue(final int x, final int y, final int[][] pixels) {
        int avg = 0;
        int count = 0;
        final int squareSize = 4;
        for (int i = x; i < x + 2; i++) {
            for (int j = y; j < y + 2; j++) {
                try {
                    avg += pixels[i][j];
                } catch (ArrayIndexOutOfBoundsException e) {
                    count++;
                }
            }
        }
        return avg / (squareSize - count);
    }
}


/**
 *
 * @author maryan
 */
public class Pyramid {
    public List<Layer> list = new ArrayList<>();
    public List<Integer> lenWidth = new ArrayList<>();
    public List<Integer> lenHight = new ArrayList<>();
    public final int[][] pixels;
    Pyramid(final int[][] pixels) {
        this.pixels = pixels.clone();
    }
    
    /**
     * 
     */
    public final void proccessPyramid() {
        Layer layer = new Layer(this.pixels);
        int len = layer.getAverage().length;
        lenWidth.add(this.pixels.length);
        lenHight.add(this.pixels[0].length);
        while (len > 2) {   
            list.add(layer);
            layer = new Layer(layer.getAverage());
            len = layer.getAverage().length > layer.getAverage()[0].length 
                    ? layer.getAverage()[0].length : layer.getAverage().length;
            lenWidth.add(layer.getAverage().length);
            lenHight.add(layer.getAverage()[0].length);
        }
    }
}
