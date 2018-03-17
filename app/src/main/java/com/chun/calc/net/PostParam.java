package com.chun.calc.net;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.http.params.HttpParams;

/**
 * 类说明
 * Created on 2014-01-13
 * 海南新境软件有限公司
 *
 * @author cjh
 */
public class PostParam implements Serializable {
    HashMap<String, Object> param;
    private boolean zip;

    public PostParam(boolean zip) {
        this();
        this.zip = zip;
    }

    public PostParam() {
        param = new HashMap<String, Object>();
    }

    public PostParam(String body) {
        this();
        addParam("ContentType", "application/json");
        addParam("Accept", "application/json");
        addParam("_ReqBody", body);
    }

    public void addParam(String name, String value) {
        if (value == null)
            param.put(name, "");
        else
            param.put(name, value);
    }

    public void addParam(String name, int value) {
        param.put(name, value);
    }

    public void addParam(String name, long value) {
        param.put(name, value);
    }

    public Object getParam(String name) {
        return param.get(name);
    }

    public byte[] getParamBytes() {
        return getParamBytes("UTF-8");
    }

    public void addToHttpParams(HttpParams pm) {
        try {
            Iterator iter = param.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, String> entry = (Entry<String, String>) iter.next();
                String key = entry.getKey();
                Object _value = entry.getValue();
                if (zip && _value instanceof String) { //加密和压缩处理
                    String value = _value == null ? "" : (String) _value;
//                    value = StringTools.getCompactString(value);//压缩
                    pm.setParameter(key, value);
                } else {
                    if (_value instanceof String) {
                        String value = _value == null ? "" : (String) _value;
                        pm.setParameter(key, URLEncoder.encode(value, "UTF-8"));
                    } else if (_value instanceof Integer) {
                        pm.setParameter(key, _value);
                    } else if (_value instanceof Long) {
                        pm.setParameter(key, _value);
                    }
                }
            }
            pm.setParameter("rand", System.currentTimeMillis());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean containsKey(String name) {
        return param.containsKey(name);
    }

    public byte[] getParamBytes(String charset) {
        byte[] bytes = null;
        try {
            if (param.containsKey("_ReqBody")) {
                bytes = param.get("_ReqBody").toString().getBytes(charset);
                return bytes;
            }

            Iterator iter = param.entrySet().iterator();
            StringBuffer buf = new StringBuffer();
            while (iter.hasNext()) {
                Entry<String, String> entry = (Entry<String, String>) iter.next();
                String key = entry.getKey();
                Object _value = entry.getValue();
                if (zip && _value instanceof String) { //加密和压缩处理
                    String value = _value == null ? "" : (String) _value;
//                    value = StringTools.getCompactString(value);//压缩
                    buf.append(key);
                    buf.append("=");
                    buf.append(value);
                    buf.append("&");
                } else {
                    if (_value instanceof String) {
                        String value = _value == null ? "" : (String) _value;
                        buf.append(URLEncoder.encode(key, charset));
                        buf.append("=");
                        buf.append(URLEncoder.encode(value, charset));
                        buf.append("&");
                    } else if (_value instanceof Integer) {
                        buf.append(URLEncoder.encode(key, charset));
                        buf.append("=");
                        buf.append(_value);
                        buf.append("&");

                    } else if (_value instanceof Long) {
                        buf.append(URLEncoder.encode(key, charset));
                        buf.append("=");
                        buf.append(_value);
                        buf.append("&");
                    }
                }
            }
            //buf.append("rand="+System.currentTimeMillis());
            String url = buf.toString();
            bytes = url.getBytes(charset);
        } catch (Exception ex) {
            ex.printStackTrace();
            bytes = new byte[0];
        }
        return bytes;
    }

    public boolean isZip() {
        return zip;
    }
}
