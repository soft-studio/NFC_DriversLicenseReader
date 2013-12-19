/*****************************************************************************
 *
 * $Id: LookUpTable.java 166 2012-01-11 23:48:05Z mroland $
 *
 * Copyright Eastman Kodak Company, 343 State Street, Rochester, NY 14650
 * $Date $
 *****************************************************************************/

package jj2000.icc.lut;

import jj2000.icc.tags.ICCCurveType;


/**
 * Toplevel class for a lut.  All lookup tables must
 * extend this class.
 * 
 * @version	1.0
 * @author	Bruce A. Kern
 */
public abstract class LookUpTable {

    /** End of line string.             */ protected static final String eol = System.getProperty ("line.separator");
    /** The curve data                  */ protected ICCCurveType curve = null;
    /** Number of values in created lut */ protected int          dwNumInput  = 0;
                

    /**
     * For subclass usage.
     *   @param curve The curve data  
     *   @param dwNumInput Number of values in created lut
     */
    protected LookUpTable (
                 ICCCurveType curve,
                 int dwNumInput
                 ) {
        this.curve       = curve;
        this.dwNumInput  = dwNumInput; }

    /* end class LookUpTable */ }
















