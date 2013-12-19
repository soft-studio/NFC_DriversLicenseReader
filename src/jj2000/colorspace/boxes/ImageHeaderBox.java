/*****************************************************************************
 *
 * $Id: ImageHeaderBox.java 169 2012-01-15 18:33:24Z mroland $
 *
 * Copyright Eastman Kodak Company, 343 State Street, Rochester, NY 14650
 * $Date $
 *****************************************************************************/

package jj2000.colorspace.boxes;

import jj2000.colorspace.ColorSpaceException;
import jj2000.j2k.io.RandomAccessIO;
import jj2000.icc.ICCProfile;

import java.io.IOException;

/**
 * This class models the Image Header box contained in a JP2
 * image.  It is a stub class here since for colormapping the
 * knowlege of the existance of the box in the image is sufficient.
 * 
 * @version	1.0
 * @author	Bruce A. Kern
 */
public final class ImageHeaderBox extends JP2Box
{
    static { type = 69686472; }

    long height;
    long width;
    int nc;
    short bpc;
    short c;
    boolean unk;
    boolean ipr;
    

    /**
     * Construct an ImageHeaderBox from an input image.
     *   @param in RandomAccessIO jp2 image
     *   @param boxStart offset to the start of the box in the image
     * @exception IOException, ColorSpaceException
     */
    public ImageHeaderBox (RandomAccessIO in, int boxStart)
        throws IOException, ColorSpaceException {
        super (in, boxStart);
        readBox(); }


    /** Analyze the box content. */
    void readBox() throws IOException {
        byte [] bfr = new byte [14];
        in.seek(dataStart);
        in.readFully (bfr,0,14);

        height             = ICCProfile.getInt(bfr,0);
        width              = ICCProfile.getInt(bfr,4);
        nc                 = ICCProfile.getShort(bfr,8);
        bpc                = (short) (bfr[10] & 0x00ff);
        c                  = (short) (bfr[11] & 0x00ff);
        unk                = bfr[12]==0?true:false;
        ipr                = bfr[13]==1?true:false; }

    /* end class ImageHeaderBox */ }











