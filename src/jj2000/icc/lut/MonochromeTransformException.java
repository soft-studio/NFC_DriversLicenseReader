/*****************************************************************************
 *
 * $Id: MonochromeTransformException.java 166 2012-01-11 23:48:05Z mroland $
 *
 * Copyright Eastman Kodak Company, 343 State Street, Rochester, NY 14650
 * $Date $
 *****************************************************************************/

package jj2000.icc.lut;

/**
 * Exception thrown by MonochromeTransformTosRGB.
 * 
 * @see		jj2000.j2k.icc.lut.MonochromeTransformTosRGB
 * @version	1.0
 * @author	Bruce A. Kern
 */

public class MonochromeTransformException extends Exception {

    /**
     * Contruct with message
     *   @param msg returned by getMessage()
     */
    MonochromeTransformException (String msg) {
        super (msg); }

    /**
     * Empty constructor
     */
    MonochromeTransformException () {
    }
    
    /* end class MonochromeTransformException */ }




