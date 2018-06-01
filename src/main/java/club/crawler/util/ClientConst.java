package club.crawler.util;

import java.util.Arrays;
import java.util.HashSet;

public class ClientConst {

    // for Auth
    public static final Integer IZI_AUTH_EXPIRE_IN_SECONDS = 1800;  // 30min
    public static final HashSet<String> IZI_HEADER_TO_SIGN =
            new HashSet<String>(Arrays.asList("content-md5", "content-length", "content-type"));
    public static final String IZI_PREFIX = "credit-";


    // encoding
    public static final String DEFAULT_ENCODING = "UTF8";
    public static final String ENCODING_GBK = "GBK";


    public static final String LOG4J_CONF_PROPERTY = "aip.log4j.conf";
}
