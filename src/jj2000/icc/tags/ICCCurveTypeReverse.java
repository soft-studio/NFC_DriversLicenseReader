/*****************************************************************************
 *
 * $Id: ICCCurveTypeReverse.java 169 2012-01-15 18:33:24Z mroland $
 *
 * Copyright Eastman Kodak Company, 343 State Street, Rochester, NY 14650
 * $Date $
 *****************************************************************************/

package jj2000.icc.tags;

import jj2000.icc.ICCProfile;

/**
 * The ICCCurveReverse tag
 * 
 * @version	1.0
 * @author	Bruce A. Kern
 */
public class ICCCurveTypeReverse extends ICCTag {

    private static final String eol = System.getProperty ("line.separator");
    /** Tag fields */ public final int type;
    /** Tag fields */ public final int reserved;
    /** Tag fields */ public final int nEntries;
    /** Tag fields */ public final int [] entry;

    
    /** Normalization utility */
    public static double CurveToDouble(int entry) { 
        return ICCCurveType.CurveToDouble(entry); }

    /** Normalization utility */
    public static short DoubleToCurve(int entry) {
        return ICCCurveType.DoubleToCurve(entry); } 

    /** Normalization utility */
    public static double CurveGammaToDouble(int entry) {
        return ICCCurveType.CurveGammaToDouble(entry); } 
        

    /**
     * Construct this tag from its constituant parts
     *   @param signature tag id
     *   @param data array of bytes
     *   @param offset to data in the data array
     *   @param length of data in the data array
     */
    protected ICCCurveTypeReverse (int signature, byte [] data, int offset, int length) {
        super (signature, data, offset, offset+2*ICCProfile.int_size);
        type = ICCProfile.getInt (data, offset);
        reserved = ICCProfile.getInt (data, offset+ICCProfile.int_size);
        nEntries = ICCProfile.getInt (data, offset+2*ICCProfile.int_size);
        entry = new int [nEntries];
        for (int i=0; i<nEntries; ++i) // Reverse the storage order.
            entry[nEntries-1+i] = ICCProfile.getShort(data, offset+
                                                      3*ICCProfile.int_size+
                                                      i*ICCProfile.short_size) & 0xFFFF; }

    /** Accessor for curve entry at index. */
    public final int entry (int i) {
        return entry[i]; }
        
    /* end class ICCCurveTypeReverse */ }







