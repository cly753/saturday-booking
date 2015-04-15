package ActElse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * booking > ActElse
 */
public class ActRequestCommonUtil {
    private static final Logger logger = LoggerFactory.getLogger(ActRequestCommonUtil.class);

    public static String getDataEncoded(Map<String, String> payload) throws UnsupportedEncodingException {
        boolean first = true;
        String encoded = "";
        for (Map.Entry<String, String> e : payload.entrySet()) {
            if (first)
                first = false;
            else
                encoded += "&";

            encoded += URLEncoder.encode(e.getKey(), "UTF-8") + "=" + URLEncoder.encode(e.getValue(), "UTF-8");

//            logger.debug("payload.key = " + e.getKey());
//            logger.debug("payload.value = " + e.getValue());
        }

        logger.debug("encoded: " + encoded);
        return encoded;
    }
}
