/*****************************************************************************
 *
 * $Id: MatrixBasedRestrictedProfile.java 169 2012-01-15 18:33:24Z mroland $
 *
 * Copyright Eastman Kodak Company, 343 State Street, Rochester, NY 14650
 * $Date $
 *****************************************************************************/

package jj2000.icc;

import jj2000.icc.tags.ICCCurveType;
import jj2000.icc.tags.ICCXYZType;

/**
 * This class is a 3 component RestrictedICCProfile
 * 
 * @version	1.0
 * @author	Bruce A Kern
 */
public class MatrixBasedRestrictedProfile extends  RestrictedICCProfile { 

    /**
     * Factory method which returns a 3 component RestrictedICCProfile
     *   @param rcurve Red TRC curve
     *   @param gcurve Green TRC curve
     *   @param bcurve Blue TRC curve
     *   @param rcolorant Red colorant
     *   @param gcolorant Green colorant
     *   @param bcolorant Blue colorant
     * @return the RestrictedICCProfile
     */
    public static RestrictedICCProfile createInstance (ICCCurveType rcurve, ICCCurveType gcurve, ICCCurveType bcurve, 
                        ICCXYZType rcolorant, ICCXYZType gcolorant, ICCXYZType bcolorant) {
        return new MatrixBasedRestrictedProfile(rcurve,gcurve,bcurve,rcolorant,gcolorant,bcolorant); }
    
    /**
     * Construct a 3 component RestrictedICCProfile
     *   @param rcurve Red TRC curve
     *   @param gcurve Green TRC curve
     *   @param bcurve Blue TRC curve
     *   @param rcolorant Red colorant
     *   @param gcolorant Green colorant
     *   @param bcolorant Blue colorant
     */
    protected MatrixBasedRestrictedProfile (ICCCurveType rcurve, ICCCurveType gcurve, ICCCurveType bcurve, 
                        ICCXYZType rcolorant, ICCXYZType gcolorant, ICCXYZType bcolorant) {
        super (rcurve, gcurve, bcurve, rcolorant, gcolorant, bcolorant); }

    /**
     * Get the type of RestrictedICCProfile for this object
     * @return kThreeCompInput
     */
    public  int getType () {return kThreeCompInput;}

    /* end class MatrixBasedRestrictedProfile */ }



