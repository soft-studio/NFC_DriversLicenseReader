/*****************************************************************************
 *
 * $Id: MonochromeInputRestrictedProfile.java 169 2012-01-15 18:33:24Z mroland $
 *
 * Copyright Eastman Kodak Company, 343 State Street, Rochester, NY 14650
 * $Date $
 *****************************************************************************/

package jj2000.icc;

import jj2000.icc.tags.ICCCurveType;

/**
 * This class is a 1 component RestrictedICCProfile
 * 
 * @version	1.0
 * @author	Bruce A Kern
 */
public class MonochromeInputRestrictedProfile extends RestrictedICCProfile {

    /**
     * Factory method which returns a 1 component RestrictedICCProfile
     *   @param c Gray TRC curve
     * @return the RestrictedICCProfile
     */
    public static RestrictedICCProfile createInstance (ICCCurveType c) {
        return new MonochromeInputRestrictedProfile(c);}

    /**
     * Construct a 1 component RestrictedICCProfile
     *   @param c Gray TRC curve
     */
    private MonochromeInputRestrictedProfile (ICCCurveType c) {
        super (c); }

    /**
     * Get the type of RestrictedICCProfile for this object
     * @return kMonochromeInput
     */
    public  int getType () {return kMonochromeInput;}

    /* end class MonochromeInputRestrictedProfile */ }








