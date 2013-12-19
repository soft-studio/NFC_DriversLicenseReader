/*
 * CVS identifier:
 *
 * $Id: Decoder.java 169 2012-01-15 18:33:24Z mroland $
 *
 * Class:                   Decoder
 *
 * Description:             The decoder object
 *
 *
 *
 * COPYRIGHT:
 * 
 * This software module was originally developed by Rapha�ｽl Grosbois and
 * Diego Santa Cruz (Swiss Federal Institute of Technology-EPFL); Joel
 * Askel�ｽf (Ericsson Radio Systems AB); and Bertrand Berthelot, David
 * Bouchard, F�ｽlix Henry, Gerard Mozelle and Patrice Onno (Canon Research
 * Centre France S.A) in the course of development of the JPEG2000
 * standard as specified by ISO/IEC 15444 (JPEG 2000 Standard). This
 * software module is an implementation of a part of the JPEG 2000
 * Standard. Swiss Federal Institute of Technology-EPFL, Ericsson Radio
 * Systems AB and Canon Research Centre France S.A (collectively JJ2000
 * Partners) agree not to assert against ISO/IEC and users of the JPEG
 * 2000 Standard (Users) any of their rights under the copyright, not
 * including other intellectual property rights, for this software module
 * with respect to the usage by ISO/IEC and Users of this software module
 * or modifications thereof for use in hardware or software products
 * claiming conformance to the JPEG 2000 Standard. Those intending to use
 * this software module in hardware or software products are advised that
 * their use may infringe existing patents. The original developers of
 * this software module, JJ2000 Partners and ISO/IEC assume no liability
 * for use of this software module or modifications thereof. No license
 * or right to this software module is granted for non JPEG 2000 Standard
 * conforming products. JJ2000 Partners have full right to use this
 * software module for his/her own purpose, assign or donate this
 * software module to any third party and to inhibit third parties from
 * using this software module for non JPEG 2000 Standard conforming
 * products. This copyright notice must be included in all copies or
 * derivative works of this software module.
 * 
 * Copyright (c) 1999/2000 JJ2000 Partners.
 * */
package jj2000.j2k.decoder;

import jj2000.j2k.quantization.dequantizer.*;
import jj2000.j2k.image.invcomptransf.*;
import jj2000.j2k.fileformat.reader.*;
import jj2000.j2k.codestream.reader.*;
import jj2000.j2k.wavelet.synthesis.*;
import jj2000.j2k.entropy.decoder.*;
import jj2000.j2k.image.output.*;
import jj2000.j2k.codestream.*;
import jj2000.j2k.image.*;
import jj2000.j2k.util.*;
import jj2000.j2k.roi.*;
import jj2000.j2k.io.*;
import jj2000.j2k.*;

import jj2000.colorspace.*;
import jj2000.icc.*;

import java.util.*;
import java.io.*;

import android.graphics.Bitmap;

/**
 * This class is the main class of JJ2000's decoder. It instantiates all
 * objects and performs the decoding operations. It then writes the image to
 * the output file or displays it.
 *
 * <p>First the decoder should be initialized with a ParameterList object
 * given through the constructor. The when the run() method is invoked and the
 * decoder executes. The exit code of the class can be obtained with the
 * getExitCode() method, after the constructor and after the run method. A
 * non-zero value indicates that an error has ocurred.</p>
 *
 * <p>The decoding chain corresponds to the following sequence of modules:</p>
 *
 * <ul>
 * <li>BitstreamReaderAgent</li>
 * <li>EntropyDecoder</li>
 * <li>ROIDeScaler</li>
 * <li>Dequantizer</li>
 * <li>InverseWT</li>
 * <li>ImgDataConverter</li>
 * <li>EnumratedColorSpaceMapper, SyccColorSpaceMapper or ICCProfiler</li>
 * <li>ComponentDemixer (if needed)</li>
 * <li>ImgDataAdapter (if ComponentDemixer is needed)</li>
 * <li>ImgWriter</li>
 * <li>BlkImgDataSrcImageProducer</li>
 * </ul>
 *
 * <p>The 2 last modules cannot be used at the same time and corresponds
 * respectively to the writing of decoded image into a file or the graphical
 * display of this same image.</p>
 *
 * <p>The behaviour of each module may be modified according to the current
 * tile-component. All the specifications are kept in modules extending
 * ModuleSpec and accessible through an instance of DecoderSpecs class.</p>
 *
 * @see BitstreamReaderAgent
 * @see EntropyDecoder
 * @see ROIDeScaler
 * @see Dequantizer
 * @see InverseWT
 * @see ImgDataConverter
 * @see InvCompTransf
 * @see ImgWriter
 * @see BlkImgDataSrcImageProducer
 * @see ModuleSpec
 * @see DecoderSpecs
 * */
public class Decoder {

	/** Parses the inputstream to analyze the box structure of the JP2
	 * file. */
	private ColorSpace csMap = null;

	/** The exit code of the run method */
	private int exitCode;

	/** The parameter list (arguments) */
	private ParameterList pl;

	/** Information contained in the codestream's headers */
	private HeaderInfo hi;

	/** The valid list of options prefixes */
	private final static char vprfxs[] = {BitstreamReaderAgent.OPT_PREFIX,
		EntropyDecoder.OPT_PREFIX,
		ROIDeScaler.OPT_PREFIX,
		Dequantizer.OPT_PREFIX,
		InvCompTransf.OPT_PREFIX,
		HeaderDecoder.OPT_PREFIX,
		ColorSpaceMapper.OPT_PREFIX
	};

	/** The parameter information for this class */
	private final static String[][] pinfo = {
						{ "res", "<resolution level index>",
							"", null},
            { "rate","<decoding rate in bpp>",
              "","-1"},
            { "nbytes","<decoding rate in bytes>",
              "","-1"},
            { "parsing", null,
              "","on"},
            { "ncb_quit","<max number of code blocks>",
              "","-1"},
            { "l_quit","<max number of layers>",
              "","-1"},
            { "m_quit","<max number of bit planes>",
              "","-1"},
            { "poc_quit",null,
              "","off"},
            { "comp_transf",null,
              "","on"},
            { "nocolorspace",null,
              "","off"}
	};

	/**
	 * Instantiates a decoder object, with the ParameterList object given as
	 * argument. It also retrieves the default ParameterList.
	 *
	 * @param pl The ParameterList for this decoder (contains also defaults
	 * values).
	 * */
	public Decoder(ParameterList pl) {
		this.pl = pl;
	}

	/**
	 * Returns the exit code of the class. This is only initialized after the
	 * constructor and when the run method returns.
	 *
	 * @return The exit code of the constructor and the run() method.
	 * */
	public int getExitCode() {
		return exitCode;
	}

	/**
	 * Returns the parameters that are used in this class. It returns a 2D
	 * String array. Each of the 1D arrays is for a different option, and they
	 * have 3 elements. The first element is the option name, the second one
	 * is the synopsis and the third one is a long description of what the
	 * parameter is. The synopsis or description may be 'null', in which case
	 * it is assumed that there is no synopsis or description of the option,
	 * respectively.
	 *
	 * @return the options name, their synopsis and their explanation.
	 * */
	public static String[][] getParameterInfo() {
		return pinfo;
	}

	/**
	 * Runs the decoder. After completion the exit code is set, a non-zero
	 * value indicates that an error ocurred.
	 *
	 * @see #getExitCode
	 * */
	public Bitmap run(byte[] input) {
    if (input == null) return null;
    
		int res; // resolution level to reconstruct
		RandomAccessIO in;
		FileFormatReader ff;
		BitstreamReaderAgent breader;
		HeaderDecoder hd;
		EntropyDecoder entdec;
		ROIDeScaler roids;
		Dequantizer deq;
		InverseWT invWT;
		InvCompTransf ictransf;
		ImgDataConverter converter;
		DecoderSpecs decSpec = null;
		BlkImgDataSrc palettized;
		BlkImgDataSrc channels;
		BlkImgDataSrc resampled;
		BlkImgDataSrc color;
		int i;
		int depth[];

		try {

			// **** Check parameters ****
			try {
				pl.checkList(vprfxs,pl.toNameArray(pinfo));
			} catch (IllegalArgumentException e) {
				error(e.getMessage(),2);
				return null;
			}

			// **** Open input stream **** 
			// Creates a BEBufferedRandomAccessFile of a ISRandomAccessIO
			// instance for reading the file format and codestream data
      InputStream is = new ByteArrayInputStream(input);
      in = new ISRandomAccessIO(is);

			// **** File Format ****
			// If the codestream is wrapped in the jp2 fileformat, Read the
			// file format wrapper
			ff = new FileFormatReader(in);
			ff.readFileFormat();
			if(ff.JP2FFUsed) {
				in.seek(ff.getFirstCodeStreamPos());
			}

			// +----------------------------+
			// | Instantiate decoding chain |
			// +----------------------------+

			// **** Header decoder ****
			// Instantiate header decoder and read main header 
			hi = new HeaderInfo();
			try {
				hd = new HeaderDecoder(in,pl,hi);
			} catch (EOFException e) {
				error("Codestream too short or bad header, "+
						"unable to decode.",2);
				return null;
			}

			int nCompCod = hd.getNumComps();
			int nTiles = hi.siz.getNumTiles();
			decSpec = hd.getDecoderSpecs();

			// Get demixed bitdepths
			depth = new int[nCompCod];
			for(i=0; i<nCompCod;i++) { depth[i] = hd.getOriginalBitDepth(i); }

			// **** Bit stream reader ****
			try {
				breader = BitstreamReaderAgent.
				createInstance(in,hd,pl,decSpec,false,hi);
			} catch (IOException e) {
				error("Error while reading bit stream header or parsing "+
						"packets"+((e.getMessage() != null) ?
								(":\n"+e.getMessage()) : ""),4);
				return null;
			} catch (IllegalArgumentException e) {
				error("Cannot instantiate bit stream reader"+
						((e.getMessage() != null) ?
								(":\n"+e.getMessage()) : ""),2);
				return null;
			}

			// **** Entropy decoder ****
			try {
				entdec = hd.createEntropyDecoder(breader,pl);
			} catch (IllegalArgumentException e) {
				error("Cannot instantiate entropy decoder"+
						((e.getMessage() != null) ?
								(":\n"+e.getMessage()) : ""),2);
				return null;
			}

			// **** ROI de-scaler ****
			try {
				roids = hd.createROIDeScaler(entdec,pl,decSpec);
			} catch (IllegalArgumentException e) {
				error("Cannot instantiate roi de-scaler."+
						((e.getMessage() != null) ?
								(":\n"+e.getMessage()) : ""),2);
				return null;
			}

			// **** Dequantizer ****
			try {
				deq = hd.createDequantizer(roids,depth,decSpec);
			} catch (IllegalArgumentException e) {
				error("Cannot instantiate dequantizer"+
						((e.getMessage() != null) ?
								(":\n"+e.getMessage()) : ""),2);
				return null;
			}

			// **** Inverse wavelet transform ***
			try {
				// full page inverse wavelet transform
				invWT = InverseWT.createInstance(deq,decSpec);
			} catch (IllegalArgumentException e) {
				error("Cannot instantiate inverse wavelet transform"+
						((e.getMessage() != null) ?
								(":\n"+e.getMessage()) : ""),2);
				return null;
			}

			res = breader.getImgRes();
			invWT.setImgResLevel(res);

			// **** Data converter **** (after inverse transform module)
			converter = new ImgDataConverter(invWT,0);

			// **** Inverse component transformation **** 
			ictransf = new InvCompTransf(converter,decSpec,depth,pl);

			// **** Color space mapping ****
			if(ff.JP2FFUsed && pl.getParameter("nocolorspace").equals("off")) {
				try {
					csMap = new ColorSpace(in,hd,pl);
					channels = hd.
					createChannelDefinitionMapper(ictransf,csMap);
					resampled  = hd.createResampler (channels,csMap);
					palettized = hd.
					createPalettizedColorSpaceMapper (resampled,csMap);
					color = hd.createColorSpaceMapper(palettized,csMap);
				} catch (IllegalArgumentException e) {
					error("Could not instantiate ICC profiler"+
							((e.getMessage() != null) ?
									(":\n"+e.getMessage()) : ""),1,e);
					return null; 
				} catch (ColorSpaceException e) {
					error("error processing jp2 colorspace information"+
							((e.getMessage() != null) ?
									(": "+e.getMessage()) : "    "),1,e);
					return null;
        }
			} else { // Skip colorspace mapping
				color = ictransf; 
			}

			// This is the last image in the decoding chain and should be
			// assigned by the last transformation:
			BlkImgDataSrc decodedImage = color;
			if(color==null) {
				decodedImage = ictransf;
			}
			int nCompImg = decodedImage.getNumComps();

			// **** Create image writers/image display ****
			// Write decoded image to specified output file

			// output as PPM
//      ImgWriterBitmap imwriter = new ImgWriterBitmapPPM(decodedImage,0,1,2);
      ImgWriterBitmap imwriter = new ImgWriterBitmapPPM(decodedImage,0,0,0);	//モノクロ対応 2012.08.06 TK

      try {
        imwriter.writeAll();
      } catch (IOException e) {
        error("I/O error while writing output file" +
            ((e.getMessage() != null) ?
                (":\n"+e.getMessage()) : ""),2);
        return null;
      }
      try {
        return imwriter.get();
      } catch (IOException e) {
        error("I/O error while closing output file (data may "+
            "be corrupted" + ((e.getMessage() != null) ?
                (":\n"+e.getMessage()) : ""),
                2);
        return null;
      }

		} catch (IllegalArgumentException e) {
			error(e.getMessage(),2);
			return null;
		} catch (Error e) {
			if(e.getMessage()!=null) {
				error(e.getMessage(),2);
			} else {
				error("An error has occured during decoding.",2);
			}
			return null;
		} catch (RuntimeException e) {
			if(e.getMessage()!=null) {
				error("An uncaught runtime exception has occurred:\n"+
						e.getMessage(),2);
			} else {
				error("An uncaught runtime exception has occurred.",2);
			}
			return null;
		} catch (Throwable e) {
			error("An uncaught exception has occurred.",2);
			return null;
		}
	}

	/**
	 * Sets the exitCode to 'code'. An exit code different than 0
	 * indicates that there where problems. 
	 *
	 * @param msg The error message
	 *
	 * @param code The exit code to set
	 * */
	private void error(String msg, int code) {
		exitCode = code;
	}

	/**
	 * Sets the exitCode to 'code'. An exit code
	 * different than 0 indicates that there where problems.
	 *
	 * @param msg The error message
	 *
	 * @param code The exit code to set
	 *
	 * @param ex The exception associated with the call
	 * */
	private void error(String msg, int code, Throwable ex) {
		exitCode = code;
	}

	/** 
	 * Returns all the parameters used in the decoding chain. It calls
	 * parameter from each module and store them in one array (one row per
	 * parameter and 4 columns).
	 *
	 * @return All decoding parameters
	 *
	 * @see #getParameterInfo 
	 * */
	public static String[][] getAllParameters() {
		Vector vec = new Vector();
		int i;

		String[][] str = BitstreamReaderAgent.getParameterInfo();
		if(str!=null) for(i=str.length-1; i>=0; i--) vec.addElement(str[i]);

		str = EntropyDecoder.getParameterInfo();
		if(str!=null) for(i=str.length-1; i>=0; i--) vec.addElement(str[i]);

		str = ROIDeScaler.getParameterInfo();
		if(str!=null) for(i=str.length-1; i>=0; i--) vec.addElement(str[i]);

		str = Dequantizer.getParameterInfo();
		if(str!=null) for(i=str.length-1; i>=0; i--) vec.addElement(str[i]);

		str = InvCompTransf.getParameterInfo();
		if(str!=null) for(i=str.length-1; i>=0; i--) vec.addElement(str[i]);

		str = HeaderDecoder.getParameterInfo();
		if(str!=null) for(i=str.length-1; i>=0; i--) vec.addElement(str[i]);

		str = ICCProfiler.getParameterInfo();
		if(str!=null) for(i=str.length-1; i>=0; i--) vec.addElement(str[i]);

		str = getParameterInfo();
		if(str!=null) for(i=str.length-1; i>=0; i--) vec.addElement(str[i]);

		str = new String[vec.size()][4];
		if(str!=null) for(i=str.length-1; i>=0; i--)
			str[i] = (String[])vec.elementAt(i);

		return str;
	}
}
