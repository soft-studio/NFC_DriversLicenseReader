/*****************************************************************************
 *
 * $Id: ICCCurveType.java 169 2012-01-15 18:33:24Z mroland $
 *
 * Copyright Eastman Kodak Company, 343 State Street, Rochester, NY 14650
 * $Date $
 *****************************************************************************/

package jj2000.icc.tags;

import jj2000.icc.ICCProfile;

/**
 * The ICCCurve tag
 * 
 * @version	1.0
 * @author	Bruce A. Kern
 */
public class ICCCurveType extends ICCTag {

    private static final String eol = System.getProperty ("line.separator");
    /** Tag fields */ public final int type;
    /** Tag fields */ public final int reserved;
    /** Tag fields */ public final int nEntries;
    /** Tag fields */ public final int [] entry;

        
    /** Normalization utility */
    public static double CurveToDouble(int entry)
        { return (double) entry / 65535.0; }

    /** Normalization utility */
    public static short DoubleToCurve(double entry)
        { return (short) Math.floor(entry * 65535.0 + 0.5); }
        
    /** Normalization utility */
    public static double CurveGammaToDouble(int entry)
        {	return (double)entry / 256.0; }
        

    /**
     * Construct this tag from its constituant parts
     *   @param signature tag id
     *   @param data array of bytes
     *   @param offset to data in the data array
     *   @param length of data in the data array
     */
    protected ICCCurveType (int signature, byte [] data, int offset, int length) {
        super (signature, data, offset, offset+2*ICCProfile.int_size);
        type = ICCProfile.getInt (data, offset);
        reserved = ICCProfile.getInt (data, offset+ICCProfile.int_size);
        nEntries = ICCProfile.getInt (data, offset+2*ICCProfile.int_size);
        entry = new int [nEntries];
        for (int i=0; i<nEntries; ++i) 
            entry[i] = ICCProfile.getShort(data, offset+
                                           3*ICCProfile.int_size+
                                           i*ICCProfile.short_size)&0xFFFF; }


    /** Accessor for curve entry at index. */
    public final int entry (int i) {
        return entry[i]; }
        
    /* end class ICCCurveType */ }







