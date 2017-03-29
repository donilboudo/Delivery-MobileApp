package ca.beogotechnologies.deliverymanager_mobileapp.util;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Iterator;

/**
 * Created by fabrice on 2017-03-18.
 */

public class JsonUtil {

    public static String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext())
        {
            String key = itr.next();
            Object value = params.get(key);

            if (first)
            {
                first = false;
            }
            else
            {
                result.append("&");
            }

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}
