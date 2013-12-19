/*****************************************************************************
 *
 * $Id: LookUpTableFP.java 166 2012-01-11 23:48:05Z mroland $
 *
 * Copyright Eastman Kodak Company, 343 State Street, Rochester, NY 14650
 * $Date $
 *****************************************************************************/

package jj2000.icc.lut;

import jj2000.icc.tags.ICCCurveType;

/**
 * Toplevel class for a float [] lut.
 * 
 * @version	1.0
 * @author	Bruce A. Kern
 */
public abstract class LookUpTableFP extends LookUpTable {     
    
    /** The lut values. */ public final float [] lut;

    /**
     * Factory method for getting a lut from a given curve.
     *   @param curve  the data
     *   @param dwNumInput the size of the lut 
     * @return the lookup table
     */

    public static LookUpTableFP createInstance (
                 ICCCurveType curve,   // Pointer to the curve data            
                 int dwNumInput        // Number of input values in created LUT
                ) {

        if (curve.nEntries == 1) return new LookUpTableFPGamma  (curve, dwNumInput);
        else                     return new LookUpTableFPInterp (curve, dwNumInput); }

    /**
      * Construct an empty lut
      *   @param dwNumInput the size of the lut t lut.
      *   @param dwMaxOutput max output value of the lut
      */
    protected LookUpTableFP (
                 ICCCurveType curve,   // Pointer to the curve data            
                 int dwNumInput       // Number of input values in created LUT
                 ) { 
        super (curve, dwNumInput);
        lut = new float [dwNumInput]; }
    
    /**
     * lut accessor
     *   @param index of the element
     * @return the lut [index]
     */
    public final float elementAt  ( int index ) {
        return lut [index]; }
    
    /* end class LookUpTableFP */ }
















