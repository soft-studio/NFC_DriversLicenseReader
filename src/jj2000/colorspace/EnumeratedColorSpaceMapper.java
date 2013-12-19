/*****************************************************************************
 *
 * $Id: EnumeratedColorSpaceMapper.java 169 2012-01-15 18:33:24Z mroland $
 *
 * Copyright Eastman Kodak Company, 343 State Street, Rochester, NY 14650
 * $Date $
 *****************************************************************************/

package jj2000.colorspace;

import jj2000.j2k.image.BlkImgDataSrc;
import jj2000.j2k.image.DataBlk;

/**
 * This class provides Enumerated ColorSpace API for the jj2000.j2k imaging chain
 * by implementing the BlkImgDataSrc interface, in particular the getCompData
 * and getInternCompData methods.
 * 
 * @see		jj2000.j2k.colorspace.ColorSpace
 * @version	1.0
 * @author	Bruce A. Kern
 */
public class EnumeratedColorSpaceMapper extends ColorSpaceMapper
{
    /**
     * Factory method for creating instances of this class.
     *   @param src -- source of image data
     *   @param csMap -- provides colorspace info
     * @return EnumeratedColorSpaceMapper instance
     */
    public static BlkImgDataSrc createInstance (BlkImgDataSrc src, ColorSpace csMap) 
        throws ColorSpaceException {
        return new EnumeratedColorSpaceMapper (src, csMap); }

    /**
     * Ctor which creates an ICCProfile for the image and initializes
     * all data objects (input, working, and output).
     *
     *   @param src -- Source of image data
     *   @param csm -- provides colorspace info
     */
    protected EnumeratedColorSpaceMapper (BlkImgDataSrc src, ColorSpace csMap)  
        throws ColorSpaceException {
        super (src, csMap);
        /* end EnumeratedColorSpaceMapper ctor */ }


    /**
     * Returns, in the blk argument, a block of image data containing the
     * specifed rectangular area, in the specified component. The data is
     * returned, as a copy of the internal data, therefore the returned data
     * can be modified "in place".
     *
     * <P>The rectangular area to return is specified by the 'ulx', 'uly', 'w'
     * and 'h' members of the 'blk' argument, relative to the current
     * tile. These members are not modified by this method. The 'offset' of
     * the returned data is 0, and the 'scanw' is the same as the block's
     * width. See the 'DataBlk' class.
     *
     * <P>If the data array in 'blk' is 'null', then a new one is created. If
     * the data array is not 'null' then it is reused, and it must be large
     * enough to contain the block's data. Otherwise an 'ArrayStoreException'
     * or an 'IndexOutOfBoundsException' is thrown by the Java system.
     *
     * <P>The returned data has its 'progressive' attribute set to that of the
     * input data.
     *
     * @param blk Its coordinates and dimensions specify the area to
     * return. If it contains a non-null data array, then it must have the
     * correct dimensions. If it contains a null data array a new one is
     * created. The fields in this object are modified to return the data.
     *
     * @param c The index of the component from which to get the data. Only 0
     * and 3 are valid.
     *
     * @return The requested DataBlk
     *
     * @see #getInternCompData
     **/
    public DataBlk getCompData (DataBlk out, int c) {
        return src.getCompData (out, c); }

    /**
     * Returns, in the blk argument, a block of image data containing the
     * specifed rectangular area, in the specified component. The data is
     * returned, as a reference to the internal data, if any, instead of as a
     * copy, therefore the returned data should not be modified.
     *
     * <P>The rectangular area to return is specified by the 'ulx', 'uly', 'w'
     * and 'h' members of the 'blk' argument, relative to the current
     * tile. These members are not modified by this method. The 'offset' and
     * 'scanw' of the returned data can be arbitrary. See the 'DataBlk' class.
     *
     * <P>This method, in general, is more efficient than the 'getCompData()'
     * method since it may not copy the data. However if the array of returned
     * data is to be modified by the caller then the other method is probably
     * preferable.
     *
     * <P>If possible, the data in the returned 'DataBlk' should be the
     * internal data itself, instead of a copy, in order to increase the data
     * transfer efficiency. However, this depends on the particular
     * implementation (it may be more convenient to just return a copy of the
     * data). This is the reason why the returned data should not be modified.
     *
     * <P>If the data array in <tt>blk</tt> is <tt>null</tt>, then a new one
     * is created if necessary. The implementation of this interface may
     * choose to return the same array or a new one, depending on what is more
     * efficient. Therefore, the data array in <tt>blk</tt> prior to the
     * method call should not be considered to contain the returned data, a
     * new array may have been created. Instead, get the array from
     * <tt>blk</tt> after the method has returned.
     *
     * <P>The returned data may have its 'progressive' attribute set. In this
     * case the returned data is only an approximation of the "final" data.
     *
     * @param blk Its coordinates and dimensions specify the area to return,
     * relative to the current tile. Some fields in this object are modified
     * to return the data.
     *
     * @param c The index of the component from which to get the data.
     *
     * @return The requested DataBlk
     * 
     * @see #getCompData
     **/
    public DataBlk getInternCompData (DataBlk out, int c) {
       return src.getInternCompData(out, c);}

    /* end class EnumeratedColorSpaceMapper */ }










