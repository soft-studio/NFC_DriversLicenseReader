/*****************************************************************************
 *
 * $Id: ICCXYZTypeReverse.java 169 2012-01-15 18:33:24Z mroland $
 *
 * Copyright Eastman Kodak Company, 343 State Street, Rochester, NY 14650
 * $Date $
 *****************************************************************************/

package jj2000.icc.tags;

import jj2000.icc.ICCProfile;

/**
 * A tag containing a triplet.
 * 
 * @see		jj2000.j2k.icc.tags.ICCXYZType
 * @see	    jj2000.j2k.icc.types.XYZNumber
 * @version	1.0
 * @author	Bruce A. Kern
 */
public class ICCXYZTypeReverse extends ICCXYZType {

    /** x component */ public final long x;
    /** y component */ public final long y;
    /** z component */ public final long z;

    /**
     * Construct this tag from its constituant parts
     *   @param signature tag id
     *   @param data array of bytes
     *   @param offset to data in the data array
     *   @param length of data in the data array
     */
    protected ICCXYZTypeReverse (int signature, byte [] data, int offset, int length) {
        super (signature, data, offset, length);
        z=ICCProfile.getInt (data, offset+2*ICCProfile.int_size);
        y=ICCProfile.getInt (data, offset+3*ICCProfile.int_size);
        x=ICCProfile.getInt (data, offset+4*ICCProfile.int_size); }


    /* end class ICCXYZTypeReverse */ }











