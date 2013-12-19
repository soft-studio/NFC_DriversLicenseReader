/*****************************************************************************
 *
 * $Id: ICCProfileException.java 166 2012-01-11 23:48:05Z mroland $
 *
 * Copyright Eastman Kodak Company, 343 State Street, Rochester, NY 14650
 * $Date $
 *****************************************************************************/
package jj2000.icc;

/**
 * This exception is thrown when the content of a profile
 * is incorrect.
 * 
 * @see		jj2000.j2k.icc.ICCProfile
 * @version	1.0
 * @author	Bruce A. Kern
 */
public class ICCProfileException extends Exception {

    /**
     *  Contruct with message
     *  @param msg returned by getMessage()
     */
    public ICCProfileException (String msg) {
        super (msg); 
    }


    /**
     * Empty constructor
     */
    public ICCProfileException () { }
}




