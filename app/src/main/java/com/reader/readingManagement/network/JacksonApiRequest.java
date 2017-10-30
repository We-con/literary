package com.reader.readingManagement.network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.firebase.crash.FirebaseCrash;
import com.navercorp.volleyextensions.request.AbstractConverterRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by naver on 2017. 2. 9..
 */

public class JacksonApiRequest<T> extends AbstractConverterRequest<T> {

    public static final int DEFAULT_TIMEOUT_MS = 5000;

    private static final Response.ErrorListener EMPTY_ERROR_LISTENER = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("asD", error.getMessage(), error);
        }
    };
    protected ObjectMapper objectMapper;

    public JacksonApiRequest(String url, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(Method.GET, url, clazz, listener, errorListener);
        objectMapper = getObjectMapper();
    }

    public JacksonApiRequest(String url, Class<T> clazz, Response.Listener<T> listener) {
        this(url, clazz, listener, EMPTY_ERROR_LISTENER);
        objectMapper = getObjectMapper();
    }

    public JacksonApiRequest(int method, String url, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, clazz, listener, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        setShouldCache(false);
        objectMapper = getObjectMapper();
    }

    protected ObjectMapper getObjectMapper() {
        return ObjectMapperHolder.getObjectMapper();
    }

    @Override
    public String getUrl() {
        String url = super.getUrl();

        if (getMethod() == Method.GET) {
            return appendDefaultQueryString(url);
        }
        return "";
    }

    private String appendDefaultQueryString(String sourceUrl) {
        StringBuilder sb = new StringBuilder(sourceUrl);

        try {
            Map<String, String> params = getParams();
            for (Map.Entry<String, String> eachParam : params.entrySet()) {
                if (sb.indexOf("?") > -1) {
                    sb.append("&");
                } else {
                    sb.append("?");
                }
                sb.append(eachParam.getKey());
                sb.append("=");
                sb.append(eachParam.getValue());
            }
        } catch (AuthFailureError authFailureError) {
            FirebaseCrash.log(authFailureError.getMessage());
        }
        return sb.toString();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        appendParams(params);
        return params;
    }

    /**
     * 파라미터 추가하는 곳
     *
     * @param params
     */
    public void appendParams(Map<String, String> params) {
    }

    @Override
    public Priority getPriority() {
        return Priority.NORMAL;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String charset = HttpHeaderParser.parseCharset(response.headers);
        Reader reader = null;
        try {
            reader = new InputStreamReader(new ByteArrayInputStream(response.data), charset);
            return dispatchResponse(response, reader);
        } catch (JsonParseException e) {
            return Response.error(new ParseError(e));
        } catch (JsonMappingException e) {
            return Response.error(new ParseError(e));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (IOException e) {
            return Response.error(new VolleyError(e));
        } catch (Exception e) {
            return Response.error(new VolleyError(e));
        } finally {
            com.navercorp.volleyextensions.util.IoUtils.closeQuietly(reader);
        }
    }

    protected Response dispatchResponse(NetworkResponse response, Reader reader) throws IOException {
        JavaType type = objectMapper.getTypeFactory().constructParametricType(ResponseMessage.class, getResultType(objectMapper.getTypeFactory()));
        ResponseMessage<T> message = objectMapper.readValue(reader, type);
        if (message.getResult() == null) {
            return Response.error(new VolleyError("response empty"));
        }
        return Response.success(message.getResult(), HttpHeaderParser.parseCacheHeaders(response));
    }

    protected JavaType getResultType(TypeFactory typeFactory) {
        return typeFactory.constructType(getTargetClass());
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        VolleyError error = super.parseNetworkError(volleyError);

        return error;
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();

        headers.put("Accept-Language", "en");
        headers.put("User-Agent", "VOOK_ANDROID");
        return headers;
    }

    @Override
    public String getCacheKey() {
        return super.getCacheKey();
    }


    static class ObjectMapperHolder {
        private final static ObjectMapper objectMapper;

        static {
            objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        }

        private static ObjectMapper getObjectMapper() {
            return objectMapper;
        }
    }
}
