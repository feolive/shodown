package xyz.shodown.common.consts;

/**
 * @ClassName: HttpConst
 * @Description: HTTP常量
 * @Author: wangxiang
 * @Date: 2021/2/4 14:44
 */
public interface HttpConst {
    /**
     * localhost地址
     */
    String LOCALHOST = "127.0.0.1";

    /**
     * http
     */
    String HTTP = "http";

    /**
     * https
     */
    String HTTPS = "https";

    /**
     * unknown
     */
    String UNKNOWN = "unknown";

    /**
     * HTTP HEADER常量
     */
    interface Header{

        /**
         * x-forwarded-for
         */
        String X_FORWARDED_FOR = "x-forwarded-for";

        /**
         * Proxy-Client-IP
         */
        String PROXY_CLIENT_IP = "Proxy-Client-IP";

        /**
         * WL-Proxy-Client-IP
         */
        String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";

        /**
         * 加密签名
         */
        String SIGN = "sign";

        /**
         * 登录token
         */
        String AUTH_TOKEN = "auth-token";

    }

    /**
     * 常用restful规范的请求类型
     */
    interface RequestMethod{

        /**
         * GET
         */
        String GET = "GET";

        /**
         * POST
         */
        String POST = "POST";

        /**
         * DELETE
         */
        String DELETE = "DELETE";

        /**
         * PUT
         */
        String PUT = "PUT";

        /**
         * PATCH
         */
        String PATCH = "PATCH";
    }

    /**
     * content-type
     */
    interface ContentType{

        /**
         * JSON
         */
        String APPLICATION_JSON = "application/json";

        /**
         * 标准表单编码，当action为get时候，浏览器用x-www-form-urlencoded的编码方式把form数据转换成一个字串（name1=value1&amp;name2=value2…）
         */
        String X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";

        /**
         * 文件上传编码，浏览器会把整个表单以控件为单位分割，并为每个部分加上Content-Disposition，并加上分割符(boundary)
         */
        String MULTIPART_FORM_DATA = "multipart/form-data";

        /**
         * text/html
         */
        String TEXT_HTML = "text/html";

        /**
         * text/plain
         */
        String TEXT_PLAIN = "text/plain";

        /**
         * text/xml
         */
        String TEXT_XML = "text/xml";

        /**
         * jpeg
         */
        String IMAGE_JPEG = "image/jpeg";

        /**
         * gif
         */
        String IMAGE_GIF = "image/gif";

        /**
         * png
         */
        String IMAGE_PNG = "image/png";

        /**
         * jp2
         */
        String IMAGE_JP2 = "image/jp2";

        /**
         * tiff
         */
        String TIF = "image/tiff";

        /**
         * ico
         */
        String IMAGE_ICON = "image/x-icon";

        /**
         * fax
         */
        String IMAGE_FAX = "image/fax";

        /**
         * net
         */
        String IMAGE_NET = "image/pnetvue";

        /**
         * rp
         */
        String IMAGE_RP = "image/vnd.rn-realpix";

        /**
         * wbmp
         */
        String IMAGE_WBMP = "image/vnd.wap.wbmp";

        /**
         * application/octet-stream
         */
        String STEAM = "application/octet-stream";

        /**
         * application/x-001
         */
        String X_001 = "application/x-001";

        /**
         * application/x-301
         */
        String X_301 = "application/x-301";

        /**
         * text/h323
         */
        String H323 = "text/h323";

        /**
         * application/x-906
         */
        String X_906 = "application/x-906";

        /**
         * drawing/907
         */
        String DRAWING_908 = "drawing/907";

        String DRAWING_SLK = "drawing/x-slk";

        String DRAWING_TOP = " drawing/x-top";

        /**
         * application/x-a11
         */
        String ALL = "application/x-a11";

        /**
         * application/postscript
         * PostScript（PS）是主要用于电子产业和桌面出版领域的一种页面描述语言和编程语言
         */
        String PS = "application/postscript";

        /**
         * application/x-anv
         */
        String ANV = "application/x-anv";

        /**
         * text/asa
         */
        String ASA = "text/asa";

        /**
         * ASP
         */
        String ASP = "text/asp";

        /**
         * application/vnd.adobe.workflow
         */
        String AWF = "application/vnd.adobe.workflow";

        /**
         * application/x-bmp
         */
        String BMP = "application/x-bmp";

        /**
         * application/x-bot
         */
        String BOT = "application/x-bot";

        /**
         * application/x-c4t
         */
        String C4T = "application/x-c4t";

        /**
         * application/x-c90
         */
        String C90 = "application/x-c90";

        /**
         * application/x-cals
         */
        String CAL = "application/x-cals";

        /**
         * application/vnd.ms-pki.seccat
         */
        String CAT = "application/vnd.ms-pki.seccat";

        /**
         * application/x-netcdf
         */
        String CDF = "application/x-netcdf";

        /**
         * application/x-cdr
         */
        String CDR = "application/x-cdr";

        /**
         * application/x-cel
         */
        String CEL = "application/x-cel";

        /**
         * application/x-x509-ca-cert
         */
        String CERT = "application/x-x509-ca-cert";

        /**
         * application/x-g4
         */
        String CG4 = "application/x-g4";

        /**
         * application/x-cgm
         */
        String CGM = "application/x-cgm";

        /**
         * application/x-cit
         */
        String CIT = "application/x-cit";

        /**
         * java
         */
        String JAVA = "java/*";

        /**
         * application/x-cmp
         */
        String CMP = "application/x-cmp";

        /**
         * application/x-cmx
         */
        String CMX = "application/x-cmx";

        /**
         * application/x-cot
         */
        String COT = "application/x-cot";

        /**
         * application/pkix-crl
         */
        String CRL = "application/pkix-crl";

        /**
         * application/x-csi
         */
        String CSI = "application/x-csi";

        /**
         * css
         */
        String CSS = "text/css";

        /**
         * csv
         */
        String CSV = "text/csv";

        /**
         * dtd
         */
        String DTD = "application/xml-dtd";

        /**
         * application/x-cut
         */
        String CUT = "application/x-cut";

        /**
         * application/x-dbf
         */
        String DBF = "application/x-dbf";

        /**
         * application/x-dbm
         */
        String DBM = "application/x-dbm";

        /**
         * DBX
         */
        String DBX = "application/x-dbx";

        /**
         * DCX
         */
        String DCX = "application/x-dcx";

        /**
         * DGN
         */
        String DGN = "application/x-dgn";

        /**
         * DIB
         */
        String DIB = "application/x-dib";

        /**
         * DRW
         */
        String DRW = "application/x-drw";

        /**
         * DWF
         */
        String DWF = "Model/vnd.dwf";

        /**
         * DWG
         */
        String DWG = "application/x-dwg";

        /**
         * DXB
         */
        String DXB = "application/x-dxb";

        /**
         * DXF
         */
        String DXF = "application/x-dxf";

        /**
         * application/vnd.adobe.edn
         */
        String EDN = "application/vnd.adobe.edn";

        /**
         * EMF
         */
        String EMF = "application/x-emf";

        /**
         * message/rfc822
         */
        String MESSAGE_RFC822 = "message/rfc822";

        /**
         * EPI
         */
        String EPI = "application/x-epi";

        /**
         * application/x-ps
         */
        String EPS = "application/x-ps";

        /**
         * application/x-ebx
         */
        String ETD = "application/x-ebx";

        /**
         * FDF
         */
        String FDF = "application/vnd.fdf";

        /**
         * application/fractals
         */
        String FIF = "application/fractals";

        /**
         * frm
         */
        String FRM = "application/x-frm";

        String G4 = "application/x-g4";

        String GBR = "application/x-gbr";

        String GL2 = "application/x-gl2";

        String GP4 = "application/x-gp4";

        String HGL = "application/x-hgl";

        String HMR = "application/x-hmr";

        String HPGL = "application/x-hpgl";

        String HPL = "application/x-hpl";

        String HGX = "application/mac-binhex40";

        String HRF = "application/x-hrf";

        String HTA = "application/hta";

        String HTC = "text/x-component";

        String SOAP = "application/soap+xml";

        String RSS = "application/rss+xml";

        String HTT = "text/webviewhtml";

        String ICB = "application/x-icb";

        String APPLICATION_ICO = "application/x-ico";

        String IFF = "application/x-iff";

        String IGS = "application/x-igs";

        String FONT_WOFF = "application/x-font-woff";

        /**
         * Intel IPhone Compatible
         */
        String III = "application/x-iphone";

        String IMG = "application/x-img";

        String ISP = "application/x-internet-signup";

        String APPLICATION_JPE = "application/x-jpe";

        String APPLICATION_JPG = "application/x-jpg";

        String JS = "application/x-javascript";

        String LAR = "application/x-laplayer-reg";

        String LATEX = "application/x-latex";

        String LBM = "application/x-lbm";

        String LTR = "application/x-ltr";

        String MAC = "application/x-mac";

        String MAN = "application/x-troff-man";

        String MDB = "application/x-mdb";

        String MFP = "application/x-shockwave-flash";

        String MI = "application/x-mi";

        String MIL = "application/x-mil";

        String AUDIO_ACP = "audio/x-mei-aac";

        String AUDIO_AU = "audio/basic";

        String AUDIO_AIF = "audio/aiff";

        String AUDIO_LAL = "audio/x-liquid-file";

        String AUDIO_LAVS = "audio/x-liquid-secure";

        String AUDIO_MID = "audio/mid";

        String AUDIO_MND = "audio/x-musicnet-download";

        String AUDIO_MNS = "audio/x-musicnet-stream";

        String AUDIO_LMS = "audio/x-la-lms";

        String AUDIO_MP1 = "audio/mp1";

        String AUDIO_MP2 = "audio/mp2";

        String AUDIO_MP3 = "audio/mp3";

        String AUDIO_MP4 = "audio/mp4";

        String AUDIO_M3U = "audio/mpegurl";

        String AUDIO_MPGA = "audio/rn-mpeg";

        String AUDIO_PLS = "audio/scpls";

        String AUDIO_RAM = "audio/x-pn-realaudio";

        String AUDIO_WAV = "audio/wav";

        String AUDIO_WAX = "audio/x-ms-wax";

        String AUDIO_WMA = "audio/x-ms-wma";

        String AUDIO_OGG = "application/ogg";

        String VIDEO_AVI = "video/avi";

        String VIDEO_IVF = "video/x-ivf";

        String VIDEO_MOVIE = "video/x-sgi-movie";

        String VIDEO_MPEG = "video/x-mpeg";

        String VIDEO_MP2V = "video/mpeg";

        String VIDEO_MP4 = "video/mpeg4";

        String VIDEO_MPG = "video/mpg";

        String VIDEO_MPA = "video/x-mpg";

        String VIDEO_ASF = "video/x-ms-asf";

        String VIDEO_RV = "video/vnd.rn-realvideo";

        String VIDEO_WM = "video/x-ms-wm";

        String VIDEO_WMV = "video/x-ms-wmv";

        String VIDEO_WMX = "video/x-ms-wmx";

        String VIDEO_WVX = "video/x-ms-wvx";

        String MS_PROJECT = "application/vnd.ms-project";

        String MS_DOWNLOAD = "application/x-msdownload";

        String MS_PPT = "application/vnd.ms-powerpoint";

        String MS_WORD = "application/msword";

        String MS_XLS = "application/vnd.ms-excel";

        String MS_VDX = "application/vnd.visio";

        String MS_WORKS = "application/vnd.ms-works";

        String PDF = "application/pdf";

        String IPA = "application/vnd.iphone";

        String APK = "application/vnd.android.package-archive";

        String XHTML = "application/xhtml+xml";

        String ZIP = "application/zip";

        String GZIP = "application/gzip";



    }
}
