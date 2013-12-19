/*****************************************************************************
 *
 * $Id: ICCXYZType.java 169 2012-01-15 18:33:24Z mroland $
 *
 * Copyright Eastman Kodak Company, 343 State Street, Rochester, NY 14650
 * $Date $
 *****************************************************************************/

package jj2000.icc.tags;

import java.io.IOException;
import java.io.RandomAccessFile;
import jj2000.icc.ICCProfile;

/**
 * A tag containing a triplet.
 * 
 * @see		jj2000.j2k.icc.tags.ICCXYZTypeReverse 
 * @see	    jj2000.j2k.icc.types.XYZNumber
 * @version	1.0
 * @author	Bruce A. Kern
 */
public class ICCXYZType extends ICCTag {

    /** x component */ public final long x;
    /** y component */ public final long y;
    /** z component */ public final long z;

    /** Normalization utility */
    public static long DoubleToXYZ ( double x ) {
        return (long) Math.floor(x * 65536.0 + 0.5); }

    /** Normalization utility */
    public static double XYZToDouble (long x) { 
        return x / 65536.0; }

    /**
     * Construct this tag from its constituant parts
     *   @param signature tag id
     *   @param data array of bytes
     *   @param offset to data in the data array
     *   @param length of data in the data array
     */
    protected ICCXYZType (int signature, byte [] data, int offset, int length) {
        super (signature, data, offset, length);
        x=ICCProfile.getInt (data, offset+2*ICCProfile.int_size);
        y=ICCProfile.getInt (data, offset+3*ICCProfile.int_size);
        z=ICCProfile.getInt (data, offset+4*ICCProfile.int_size); }

    
    /** Write to a file. */
    public void write (RandomAccessFile raf) throws IOException {
        byte [] xb = ICCProfile.setLong (x);
        byte [] yb = ICCProfile.setLong (y);
        byte [] zb = ICCProfile.setLong (z);

        raf.write (xb, ICCProfile.int_size, 0);
        raf.write (yb, ICCProfile.int_size, 0);
        raf.write (zb, ICCProfile.int_size, 0); }


    /* end class ICCXYZType */ }











