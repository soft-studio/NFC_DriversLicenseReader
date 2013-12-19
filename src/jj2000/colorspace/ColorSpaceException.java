/*****************************************************************************
 *
 * $Id: ColorSpaceException.java 166 2012-01-11 23:48:05Z mroland $
 *
 * Copyright Eastman Kodak Company, 343 State Street, Rochester, NY 14650
 * $Date $
 *****************************************************************************/

package jj2000.colorspace;

/**
 * This exception is thrown when the content of an
 * image contains an incorrect colorspace box
 * 
 * @see		jj2000.j2k.colorspace.ColorSpaceMapper
 * @version	1.0
 * @author	Bruce A. Kern
 */

public class ColorSpaceException extends Exception {

    /**
     * Contruct with message
     *   @param msg returned by getMessage()
     */
    public ColorSpaceException (String msg) {
        super (msg); }


    /**
     * Empty constructor
     */
    public ColorSpaceException () {
    }
    
    /* end class ColorSpaceException */ }




