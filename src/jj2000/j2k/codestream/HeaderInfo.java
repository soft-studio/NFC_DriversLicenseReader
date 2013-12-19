/*
 * CVS identifier:
 *
 * $Id: HeaderInfo.java 169 2012-01-15 18:33:24Z mroland $
 *
 * Class:                   HeaderInfo
 *
 * Description:             Holds information found in main and tile-part
 *                          headers 
 *
 *
 *
 * COPYRIGHT:
 * 
 * This software module was originally developed by Raphal Grosbois and
 * Diego Santa Cruz (Swiss Federal Institute of Technology-EPFL); Joel
 * Askelf (Ericsson Radio Systems AB); and Bertrand Berthelot, David
 * Bouchard, Flix Henry, Gerard Mozelle and Patrice Onno (Canon Research
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
package jj2000.j2k.codestream;

import jj2000.j2k.wavelet.*;

import java.util.*;

/**
 * Classe that holds information found in the marker segments of the main and
 * tile-part headers. There is one inner-class per marker segment type found
 * in these headers.
 * */
public class HeaderInfo implements Markers,ProgressionType,FilterTypes,
                                   Cloneable {

    /** Internal class holding information found in the SIZ marker segment */
    public class SIZ implements Cloneable {
        public int lsiz;
        public int rsiz;
        public int xsiz;
        public int ysiz;
        public int x0siz;
        public int y0siz;
        public int xtsiz;
        public int ytsiz;
        public int xt0siz;
        public int yt0siz;
        public int csiz;
        public int[] ssiz;
        public int[] xrsiz;
        public int[] yrsiz;

        /** Component widths */
        private int[] compWidth = null;
        /** Maximum width among all components */
        private int maxCompWidth = -1;
        /** Component heights */
        private int[] compHeight = null;
        /** Maximum height among all components */
        private int maxCompHeight = -1;
        /** 
         * Width of the specified tile-component
         *
         * @param t Tile index
         *
         * @param c Component index
         * */
        public int getCompImgWidth(int c) {
            if (compWidth==null) {
                compWidth = new int[csiz];
                for(int cc=0; cc<csiz; cc++) {
                    compWidth[cc] = 
                        (int)(Math.ceil((xsiz)/(double)xrsiz[cc])
                              - Math.ceil(x0siz/(double)xrsiz[cc]));
                }
            } 
            return compWidth[c];
        }
        public int getMaxCompWidth() {
            if (compWidth==null) {
                compWidth = new int[csiz];
                for(int cc=0; cc<csiz; cc++) {
                    compWidth[cc] = 
                        (int)(Math.ceil((xsiz)/(double)xrsiz[cc])
                              - Math.ceil(x0siz/(double)xrsiz[cc]));
                }
            } 
            if (maxCompWidth==-1) {
                for(int c=0; c<csiz; c++) {
                    if(compWidth[c]>maxCompWidth) {
                        maxCompWidth = compWidth[c];
                    }
                }
            }
            return maxCompWidth;
        }
        public int getCompImgHeight(int c) {
            if (compHeight==null) {
                compHeight = new int[csiz];
                for(int cc=0; cc<csiz; cc++) {
                    compHeight[cc] = 
                        (int)(Math.ceil((ysiz)/(double)yrsiz[cc])
                              - Math.ceil(y0siz/(double)yrsiz[cc]));
                }
            } 
            return compHeight[c];
        }
        public int getMaxCompHeight() {
            if (compHeight==null) {
                compHeight = new int[csiz];
                for(int cc=0; cc<csiz; cc++) {
                    compHeight[cc] = 
                        (int)(Math.ceil((ysiz)/(double)yrsiz[cc])
                              - Math.ceil(y0siz/(double)yrsiz[cc]));
                }
            } 
            if (maxCompHeight==-1) {
                for(int c=0; c<csiz; c++) {
                    if(compHeight[c]!=maxCompHeight) {
                        maxCompHeight = compHeight[c];
                    }
                }
            }
            return maxCompHeight;
        }
        private int numTiles = -1;
        public int getNumTiles() {
            if(numTiles==-1) {
                numTiles = ((xsiz-xt0siz+xtsiz-1) / xtsiz) * 
                    ((ysiz-yt0siz+ytsiz-1) / ytsiz);
            }
            return numTiles;
        }
        private boolean[] origSigned = null;
        public boolean isOrigSigned(int c) {
            if(origSigned==null) {
                origSigned = new boolean[csiz];
                for(int cc=0; cc<csiz; cc++) {
                    origSigned[cc] = ((ssiz[cc]>>>SSIZ_DEPTH_BITS)==1);
                }
            }
            return origSigned[c];
        }
        private int[] origBitDepth = null;
        public int getOrigBitDepth(int c) {
            if(origBitDepth==null) {
                origBitDepth = new int[csiz];
                for (int cc=0; cc<csiz; cc++) {
                    origBitDepth[cc] = (ssiz[cc] & ((1<<SSIZ_DEPTH_BITS)-1))+1;
                }
            }
            return origBitDepth[c];
        }
        public SIZ getCopy() { 
            SIZ ms = null;
            try {
                ms = (SIZ)this.clone(); 
            } catch (CloneNotSupportedException e) {
                throw new Error("Cannot clone SIZ marker segment");
            }
            return ms;
        }
    }
    /** Returns a new instance of SIZ */
    public SIZ getNewSIZ() { return new SIZ(); }

    /** Internal class holding information found in the SOt marker segments */
    public class SOT {
        public int lsot;
        public int isot;
        public int psot;
        public int tpsot;
        public int tnsot;
    }
    /** Returns a new instance of SOT */
    public SOT getNewSOT() { return new SOT(); }

    /** Internal class holding information found in the COD marker segments */
    public class COD implements Cloneable {
        public int lcod;
        public int scod;
        public int sgcod_po; // Progression order
        public int sgcod_nl; // Number of layers
        public int sgcod_mct; // Multiple component transformation
        public int spcod_ndl; // Number of decomposition levels
        public int spcod_cw; // Code-blocks width
        public int spcod_ch; // Code-blocks height
        public int spcod_cs; // Code-blocks style
        public int[] spcod_t = new int[1]; // Transformation
        public int[] spcod_ps; // Precinct size

        public COD getCopy() { 
            COD ms = null;
            try {
                ms = (COD)this.clone(); 
            } catch (CloneNotSupportedException e) {
                throw new Error("Cannot clone SIZ marker segment");
            }
            return ms;
        }
    }
    /** Returns a new instance of COD */
    public COD getNewCOD() { return new COD(); }

    /** Internal class holding information found in the COC marker segments */
    public class COC {
        public int lcoc;
        public int ccoc;
        public int scoc;
        public int spcoc_ndl; // Number of decomposition levels
        public int spcoc_cw;
        public int spcoc_ch;
        public int spcoc_cs;
        public int[] spcoc_t = new int[1];
        public int[] spcoc_ps;
    }
    /** Returns a new instance of COC */
    public COC getNewCOC() { return new COC(); }

    /** Internal class holding information found in the RGN marker segments */
    public class RGN {
        public int lrgn;
        public int crgn;
        public int srgn;
        public int sprgn;
    }
    /** Returns a new instance of RGN */
    public RGN getNewRGN() { return new RGN(); }
    
    /** Internal class holding information found in the QCD marker segments */
    public class QCD {
        public int lqcd;
        public int sqcd;
        public int[][] spqcd;

        private int qType = -1;
        public int getQuantType() {
            if(qType==-1) {
                qType = sqcd & ~(SQCX_GB_MSK<<SQCX_GB_SHIFT);
            }
            return qType;
        }
        private int gb = -1;
        public int getNumGuardBits() {
            if(gb==-1) {
                gb = (sqcd>>SQCX_GB_SHIFT)&SQCX_GB_MSK;
            }
            return gb;
        }
    }
    /** Returns a new instance of QCD */
    public QCD getNewQCD() { return new QCD(); }

    /** Internal class holding information found in the QCC marker segments */
    public class QCC {
        public int lqcc;
        public int cqcc;
        public int sqcc;
        public int[][] spqcc;

        private int qType = -1;
        public int getQuantType() {
            if(qType==-1) {
                qType = sqcc & ~(SQCX_GB_MSK<<SQCX_GB_SHIFT);
            }
            return qType;
        }
        private int gb = -1;
        public int getNumGuardBits() {
            if(gb==-1) {
                gb = (sqcc>>SQCX_GB_SHIFT)&SQCX_GB_MSK;
            }
            return gb;
        }
    }
    /** Returns a new instance of QCC */
    public QCC getNewQCC() { return new QCC(); }

    /** Internal class holding information found in the POC marker segments */
    public class POC {
        public int lpoc;
        public int[] rspoc;
        public int[] cspoc;
        public int[] lyepoc;
        public int[] repoc;
        public int[] cepoc;
        public int[] ppoc;
    }
    /** Returns a new instance of POC */
    public POC getNewPOC() { return new POC(); }

    /** Internal class holding information found in the CRG marker segment */
    public class CRG {
        public int lcrg;
        public int[] xcrg;
        public int[] ycrg;
    }
    /** Returns a new instance of CRG */
    public CRG getNewCRG() { return new CRG(); }

    /** Internal class holding information found in the COM marker segments */
    public class COM {
        public int lcom;
        public int rcom;
        public byte[] ccom;
    }
    /** Returns a new instance of COM */
    public COM getNewCOM() { ncom++; return new COM(); }

    /** Returns the number of found COM marker segments */
    public int getNumCOM() { return ncom; }

    /** Reference to the SIZ marker segment found in main header */
    public SIZ siz;

    /** Reference to the SOT marker segments found in tile-part headers. The
     * kwy is given by "t"+tileIdx"_tp"+tilepartIndex. */
    public Hashtable sot = new Hashtable();

    /** Reference to the COD marker segments found in main and first tile-part
     * header. The key is either "main" or "t"+tileIdx.*/
    public Hashtable cod = new Hashtable();

    /** Reference to the COC marker segments found in main and first tile-part
     * header. The key is either "main_c"+componentIndex or
     * "t"+tileIdx+"_c"+component_index. */
    public Hashtable coc = new Hashtable();

    /** Reference to the RGN marker segments found in main and first tile-part
     * header. The key is either "main_c"+componentIndex or
     * "t"+tileIdx+"_c"+component_index. */
    public Hashtable rgn = new Hashtable();

    /** Reference to the QCD marker segments found in main and first tile-part
     * header. The key is either "main" or "t"+tileIdx. */
    public Hashtable qcd = new Hashtable();

    /** Reference to the QCC marker segments found in main and first tile-part
     * header. They key is either "main_c"+componentIndex or
     * "t"+tileIdx+"_c"+component_index. */
    public Hashtable qcc = new Hashtable();

    /** Reference to the POC marker segments found in main and first tile-part
     * header. They key is either "main" or "t"+tileIdx. */
    public Hashtable poc = new Hashtable();

    /** Reference to the CRG marker segment found in main header */
    public CRG crg;

    /** Reference to the COM marker segments found in main and tile-part
     * headers. The key is either "main_"+comIdx or "t"+tileIdx+"_"+comIdx. */
    public Hashtable com = new Hashtable();

    /** Number of found COM marker segment */
    private int ncom = 0;

    /** Display information found in the different marker segments of the main
     * header */
    public String toStringMainHeader() {
        int nc = siz.csiz;
        // SIZ
        String str = ""+siz;
        // COD
        if(cod.get("main")!=null) {
            str += ""+(COD)cod.get("main");
        }
        // COCs
        for(int c=0; c<nc; c++) {
            if(coc.get("main_c"+c)!=null) {
                str += ""+(COC)coc.get("main_c"+c);
            }
        }
        // QCD
        if(qcd.get("main")!=null) {
            str += ""+(QCD)qcd.get("main");
        }
        // QCCs
        for(int c=0; c<nc; c++) {
            if(qcc.get("main_c"+c)!=null) {
                str += ""+(QCC)qcc.get("main_c"+c);
            }
        }
        // RGN
        for(int c=0; c<nc; c++) {
            if(rgn.get("main_c"+c)!=null) {
                str += ""+(RGN)rgn.get("main_c"+c);
            }
        }
        // POC
        if(poc.get("main")!=null) {
            str += ""+(POC)poc.get("main");
        }
        // CRG
        if(crg!=null) {
            str += ""+crg;
        }
        // COM
        for(int i=0; i<ncom; i++) {
            if(com.get("main_"+i)!=null) {
                str += ""+(COM)com.get("main_"+i);
            }
        }
        return str;
    }

    /** 
     * Returns information found in the tile-part headers of a given tile.
     *
     * @param t index of the tile
     *
     * @param tp Number of tile-parts
     * */
    public String toStringTileHeader(int t, int ntp) {
        int nc = siz.csiz;
        String str = "";
        // SOT
        for(int i=0; i<ntp; i++) {
            str += "Tile-part "+i+", tile "+t+":\n";
            str += ""+(SOT)sot.get("t"+t+"_tp"+i);
        }
        // COD
        if(cod.get("t"+t)!=null) {
            str += ""+(COD)cod.get("t"+t);
        }
        // COCs
        for(int c=0; c<nc; c++) {
            if(coc.get("t"+t+"_c"+c)!=null) {
                str += ""+(COC)coc.get("t"+t+"_c"+c);
            }
        }
        // QCD
        if(qcd.get("t"+t)!=null) {
            str += ""+(QCD)qcd.get("t"+t);
        }
        // QCCs
        for(int c=0; c<nc; c++) {
            if(qcc.get("t"+t+"_c"+c)!=null) {
                str += ""+(QCC)qcc.get("t"+t+"_c"+c);
            }
        }
        // RGN
        for(int c=0; c<nc; c++) {
            if(rgn.get("t"+t+"_c"+c)!=null) {
                str += ""+(RGN)rgn.get("t"+t+"_c"+c);
            }
        }
        // POC
        if(poc.get("t"+t)!=null) {
            str += ""+(POC)poc.get("t"+t);
        }
        return str;
    }

    /** 
     * Returns information found in the tile-part headers of a given tile
     * exception the SOT marker segment.
     *
     * @param t index of the tile
     *
     * @param tp Number of tile-parts
     * */
    public String toStringThNoSOT(int t, int ntp) {
        int nc = siz.csiz;
        String str = "";
        // COD
        if(cod.get("t"+t)!=null) {
            str += ""+(COD)cod.get("t"+t);
        }
        // COCs
        for(int c=0; c<nc; c++) {
            if(coc.get("t"+t+"_c"+c)!=null) {
                str += ""+(COC)coc.get("t"+t+"_c"+c);
            }
        }
        // QCD
        if(qcd.get("t"+t)!=null) {
            str += ""+(QCD)qcd.get("t"+t);
        }
        // QCCs
        for(int c=0; c<nc; c++) {
            if(qcc.get("t"+t+"_c"+c)!=null) {
                str += ""+(QCC)qcc.get("t"+t+"_c"+c);
            }
        }
        // RGN
        for(int c=0; c<nc; c++) {
            if(rgn.get("t"+t+"_c"+c)!=null) {
                str += ""+(RGN)rgn.get("t"+t+"_c"+c);
            }
        }
        // POC
        if(poc.get("t"+t)!=null) {
            str += ""+(POC)poc.get("t"+t);
        }
        return str;
    }

    /** Returns a copy of this object */
    public HeaderInfo getCopy(int nt) {
        HeaderInfo nhi = null;
        // SIZ
        try {
            nhi = (HeaderInfo)clone();
        } catch (CloneNotSupportedException e) {
            throw new Error("Cannot clone HeaderInfo instance");
        }
        nhi.siz = siz.getCopy();
        // COD
        if(cod.get("main")!=null) {
            COD ms = (COD)cod.get("main");
            nhi.cod.put("main",ms.getCopy());
        }
        for (int t=0; t<nt; t++) {
            if(cod.get("t"+t)!=null) {
                COD ms = (COD)cod.get("t"+t);
                nhi.cod.put("t"+t,ms.getCopy());
            }
        }
        return nhi;
    }
}
