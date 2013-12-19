/*****************************************************************************
 *
 * $Id: ICCMonochromeInputProfile.java 166 2012-01-11 23:48:05Z mroland $
 *
 * Copyright Eastman Kodak Company, 343 State Street, Rochester, NY 14650
 * $Date $
 *****************************************************************************/

package jj2000.icc;

import java.io.InputStream;
import java.io.File;
import java.io.IOException;
import jj2000.colorspace .ColorSpace;
import jj2000.colorspace .ColorSpaceException;
import jj2000.j2k.io.RandomAccessIO;

/**
 * The monochrome ICCProfile.
 * 
 * @version	1.0
 * @author	Bruce A. Kern
 */
public class ICCMonochromeInputProfile extends ICCProfile {
        
    /**
     * Return the ICCProfile embedded in the input image
     *   @param in jp2 image with embedded profile
     * @return ICCMonochromeInputProfile 
     * @exception ColorSpaceICCProfileInvalidExceptionException
     * @exception 
     */
    public static ICCMonochromeInputProfile createInstance (ColorSpace csm) 
        throws ColorSpaceException, ICCProfileInvalidException {
        return new ICCMonochromeInputProfile (csm); }
    
    /**
     * Construct a ICCMonochromeInputProfile corresponding to the profile file
     *   @param f disk based ICCMonochromeInputProfile
     * @return theICCMonochromeInputProfile
     * @exception ColorSpaceException
     * @exception ICCProfileInvalidException
     */
    protected ICCMonochromeInputProfile (ColorSpace csm) 
        throws ColorSpaceException, ICCProfileInvalidException {
            super (csm);  }
    
    /* end class ICCMonochromeInputProfile */ }
