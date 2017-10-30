package com.reader.readingManagement.network;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.webkit.URLUtil;

import com.reader.readingManagement.R;

import org.xmlpull.v1.XmlPullParser;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 */

//TODO : 서버 api factory
public class UrlHelper {
    private static final Pattern API_TEMPLATE_PATTERN = Pattern.compile("[\\?|\\&]+([^=]+)\\=\\{[0-9a-zA-Z]+\\}|([\\?|\\&])+([^=]+\\=[^&]+)");
    private static final char PATH_SEPARATOR = '/';
    private static final String TAG_URL = "url";
    private static final String TAG_URL_SET = "url-set";
    private static final String TAG_ITEM = "item";
    private static final String SCHEME_HTTP = "http://";
    public static final String BASE_API_URL = SCHEME_HTTP + "damp-atoll-30477.herokuapp.com/api"; //"jangdock.cafe24.com:3011/api";
    private static UrlHelper instance;
    private SparseArray<String> urlList;

    private UrlHelper(Context context) {
        loadUrlsResources(context);
    }

    public static void initialize(Context context) {
        instance = new UrlHelper(context);
    }

    private void loadUrlsResources(Context context) {
        urlList = new SparseArray<>();
        loadUrlsFromXml(context, R.xml.urls_api);
    }

    private void loadUrlsFromXml(Context context, int xmlRes) {
        XmlResourceParser xml = context.getResources().getXml(xmlRes);
        try {
            xml.next();
            int eventType = xml.getEventType();
            String text = null;
            int id = -1;
            String startTag = null;

            UrlSet urlSet = null;
            boolean defaultValue = false;
            String key = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        startTag = xml.getName();
                        if (TAG_URL.equals(startTag)) {
                            id = xml.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "id", -1);
                            urlSet = null;
                        }
                        if (TAG_URL_SET.equals(startTag)) {
                            id = xml.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "id", -1);
                            urlSet = new UrlSet();
                        }
                        if (TAG_ITEM.equals(startTag)) {
                            key = xml.getAttributeValue(null, "key");
                            defaultValue = xml.getAttributeBooleanValue(null, "default", false);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = xml.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = xml.getName();
                        if (TextUtils.equals(endTag, TAG_URL) && id > -1 && text != null) {
                            Log.d("parsed api : %s, ", text);
                            urlList.put(id, text);
                            id = -1;
                        }
                        if (TextUtils.equals(endTag, TAG_ITEM) && text != null && key != null && urlSet != null) {
                            urlSet.putUrl(key, text, defaultValue);
                        }
//                        if (TextUtils.equals(endTag, TAG_URL_SET) && urlSet != null && id > -1) {
//                            urlGroupSet.put(id, urlSet);
//                            id = -1;
//                        }
                        text = null;
                        startTag = null;
                        key = null;
                        defaultValue = false;
                        break;
                }
                eventType = xml.next();
            }
        } catch (Exception e) {
            Log.e("URL HELPER", "PARSE", e);
        }
    }


    public static String getApiUrl(int id, Object... paramValues) {
        return buildUrl(BASE_API_URL, id, paramValues);
    }

    /**
     * gw를 통한 api가 아닌 외부 url을 호출하기 위한 메서드. http 로 시작하는 full path를 받는다.
     *
     * @param id
     * @param paramValues
     * @return
     */
    public static String getUrl(int id, Object... paramValues) {
        return buildUrl(null, id, paramValues);
    }

    private static String buildUrl(String baseUrl, int id, Object... paramValues) {
        String apiPath = instance.urlList.get(id);
        if (apiPath == null) {
            return "";
        }
        if (baseUrl == null) {
            return injectParams(apiPath, paramValues).trim();
        } else {
            return (baseUrl + injectParams(apiPath, paramValues)).trim();
        }
    }

    private static String injectParams(String urlTemplate, Object... paramValues) {
        StringBuffer sb = new StringBuffer();
        Matcher matcher = API_TEMPLATE_PATTERN.matcher(urlTemplate);
        int i = 0;
        boolean paramStart = false;
        while (matcher.find()) {
            String param = null;
            if (matcher.group(1) == null) {
                param = matcher.group(3);
            } else {
                String paramName = matcher.group(1);
                if (i >= paramValues.length) {
                    continue;
                }
                Object paramValue = paramValues[i];
                i++;
                if (paramValue != null) {
                    param = paramName + "=" + normalizeParam(paramValue);
                }
            }

            if (param == null) {
                param = "";
            }
            if (!TextUtils.isEmpty(param)) {
                if (paramStart) {
                    param = "&" + param;
                } else {
                    param = "?" + param;
                    paramStart = true;
                }
            }
            matcher.appendReplacement(sb, param);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String normalizeParam(Object value) {
        if (value == null) {
            return "";
        }
        try {
            return URLEncoder.encode(String.valueOf(value), "utf-8");
        } catch (UnsupportedEncodingException e) {
            Log.e("error", "", e);
            return String.valueOf(value);
        }
    }

    static class UrlSet extends HashMap<String, String> {
        private String defaultUrl;

        public void putUrl(String key, String url, boolean defaultValue) {
            super.put(key, url);
            if (defaultValue) {
                defaultUrl = url;
            }

        }
    }

    public static String getFileBaseName(String url) {
        if (url == null || TextUtils.isEmpty(url)) {
            return null;
        }
        String fileName = URLUtil.guessFileName(url, null, null);
        return fileName;
    }

}
