/*****************************************************************************
 *
 * $Id: LookUpTableFPGamma.java 169 2012-01-15 18:33:24Z mroland $
 *
 * Copyright Eastman Kodak Company, 343 State Street, Rochester, NY 14650
 * $Date $
 *****************************************************************************/

package jj2000.icc.lut;

import jj2000.icc.tags.ICCCurveType;

/**
 * Class Description
 * 
 * @version	1.0
 * @author	Bruce A. Kern
 */

public class LookUpTableFPGamma extends LookUpTableFP {     

    double dfE = -1;
    private static final String eol = System.getProperty("line.separator");

    public LookUpTableFPGamma (
                 ICCCurveType curve,   // Pointer to the curve data            
                 int dwNumInput       // Number of input values in created LUT
                 ) {
        super (curve, dwNumInput); 

        // Gamma exponent for inverse transformation
        dfE = ICCCurveType.CurveGammaToDouble(curve.entry(0));
        for (int i = 0; i < dwNumInput; i++)
            lut[i] = (float) Math.pow((double)i / (dwNumInput - 1), dfE); }


    /* end class LookUpTableFPGamma */ }
















