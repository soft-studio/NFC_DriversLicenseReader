/*****************************************************************************
 *
 * $Id: LookUpTable32Gamma.java 166 2012-01-11 23:48:05Z mroland $
 *
 * Copyright Eastman Kodak Company, 343 State Street, Rochester, NY 14650
 * $Date $
 *****************************************************************************/

package jj2000.icc.lut;

import jj2000.icc.tags.ICCCurveType;

/**
 * A Gamma based 32 bit lut.
 * 
 * @see		jj2000.j2k.icc.tags.ICCCurveType
 * @version	1.0
 * @author	Bruce A. Kern
 */

public class LookUpTable32Gamma extends LookUpTable32 {     
    

    /* Construct the lut    
     *   @param curve data 
     *   @param dwNumInput size of lut  
     *   @param dwMaxOutput max value of lut   
     */
    public LookUpTable32Gamma (
                 ICCCurveType curve,   // Pointer to the curve data            
                 int dwNumInput,       // Number of input values in created LUT
                 int dwMaxOutput       // Maximum output value of the LUT
                 ) {
        super (curve, dwNumInput, dwMaxOutput);
        double dfE = ICCCurveType.CurveGammaToDouble(curve.entry(0)); // Gamma exponent for inverse transformation
        for (int i = 0; i < dwNumInput; i++)
            lut[i] = (int) Math.floor(Math.pow((double)i / (dwNumInput - 1), dfE) * dwMaxOutput + 0.5); }

    /* end class LookUpTable32Gamma */ }
















