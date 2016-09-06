/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inc.quality;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author maryan
 */
public class UtilsNGTest {
    
    public UtilsNGTest() {
    }



    /**
     * Test of SAT method, of class Utils.
     */
    @Test
    public void testSAT() {
        System.out.println("SAT");
        int[][] pixels = {
            {2,1,0,0},
            {0,1,2,0},
            {1,2,1,0},
            {1,1,0,2},
                            };
        int[][] expResult = {
            {2,3,3,3},
            {2,4,6,6},
            {3,7,10,10},
            {4,9,12,14},
                            };
        int[][] result = Utils.SAT(pixels);
      
        assertEquals(result, expResult);

    }
    
    @Test
    public void testgetLocalAverageValue() {
        System.out.println("getLocalAverageValue");
        int[][] pixels = {
            {2,1,0,0},
            {0,1,2,0},
            {1,2,1,0},
            {1,1,0,2},
                            };
        int[][] expResult = {
            {2,3,3,3},
            {2,4,6,6},
            {3,7,10,10},
            {4,9,12,14},
                            };
        int[][] result = Utils.mapThresholdArray(pixels);
      
        assertEquals(result, expResult);

    }
    
}
