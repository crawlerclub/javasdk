package club.crawler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import club.crawler.http.Request;
import club.crawler.util.ClientConst;
import club.crawler.util.SignUtil;
import club.crawler.util.Util;

public class Auth {

    /**
     * @param request IziRequest Object
     * @param ak access key ID
     * @param sk secret access key
     * @param timestamp UTC timestamp
     * @return signed authorization header for auth
     */
    public static String sign(Request request, String ak, String sk, String timestamp) {
        HashMap<String, String> headers = request.getHeaders();
        HashMap<String, String> params = request.getParams();
        String httpMethod = request.getHttpMethod().toString();
        String path = request.getUri().getPath();
        // 1. 生成signingKey
        //  1.1 authString,格式为：credit-v1/{accessKeyId}/{timestamp}/{expirationPeriodInSeconds}
        String authStringPrefix = String.format("credit-v1/%s/%s/%d",
                ak, timestamp, ClientConst.IZI_AUTH_EXPIRE_IN_SECONDS);

        try {
            // 1.2.使用authStringPrefix加上SK，用SHA-256生成sign key
            String signingKey = SignUtil.hmacSha256(sk, authStringPrefix);

            // 2. 生成规范化uri
            String canonicalUri = getCanonicalUri(path);

            // 3. 生成规范化query string
            String canonicalQuery = getCanonicalQuery(params);

            // 4. 生成规范化headers
            String canonicalHeaders = getCanonicalHeaders(headers);

            ArrayList<String> canonicalRequest = new ArrayList<String>();
            canonicalRequest.add(httpMethod);
            canonicalRequest.add(canonicalUri);
            canonicalRequest.add(canonicalQuery);
            canonicalRequest.add(canonicalHeaders);

            String signature = SignUtil.hmacSha256(signingKey, Util.mkString(canonicalRequest.iterator(), '\n'));

            return String.format("%s/%s/%s", authStringPrefix, "", signature);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getCanonicalUri(String path) {
        if (!path.startsWith("/")) {
            path = String.format("/%s", path);
        }
        return Util.uriEncode(path, false);
    }

    private static String getCanonicalQuery(HashMap<String, String> params) {
        if (params.isEmpty()) {
            return "";
        }

        TreeSet<String> querySet = new TreeSet<String>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!entry.getKey().toLowerCase().equals("authorization")) {
                querySet.add(String.format("%s=%s",
                        Util.uriEncode(entry.getKey(), true),
                        Util.uriEncode(entry.getValue(), true)));
            }
        }

        return Util.mkString(querySet.iterator(), '&');

    }

    /*
     *   对部分header名称及参数进行编码，默认使用：
     *   1. content-md5
     *   2. content-length
     *   3. content-type
     *   4. 所有以credit-开头的header项
     */
    private static String getCanonicalHeaders(HashMap<String, String> headers) {
        if (headers.isEmpty()) {
            return "";
        }
        TreeSet<String> headerSet = new TreeSet<String>();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String key = entry.getKey().trim().toLowerCase();
            if (key.startsWith(ClientConst.IZI_PREFIX)
                    || ClientConst.IZI_HEADER_TO_SIGN.contains(key)) {
                headerSet.add(String.format("%s:%s", Util.uriEncode(key, true),
                        Util.uriEncode(entry.getValue().trim(), true)));
            }
        }

        return Util.mkString(headerSet.iterator(), '\n');
    }

}
