package com.chun.calc.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * 类说明
 * Created on 2014-01-13
 * 海南新境软件有限公司
 *
 * @author cjh
 */
public class Ajax {
    private static final String TAG = "Ajax";
    public final int ERROR_WHAT = -110;
    public final int DATA_RECEIVE = 200;
    public final int SHOW_MESSAGE = 201;
    public final int TRNS_DATA_NUM = 202;
    public final int READY_DOWNLOAD = 203;
    public final int FETCH_FILE_TOTAL_SIZE = 204;
    public final int CANCEL_DOWNLOAD = 205;
    public final int READY_UPLOAD = 207;

    /*create by Gavin on 2016/12/05 */
    public final int FETCH_FILE_NAME = 206;
    /*create by Gavin on 2016/12/05 */

    private static Ajax inst;

    public static Ajax getInst() {
        if (inst == null) {
            inst = new Ajax();
        }
        return inst;
    }

    /**
     * 同步调用
     */
    public String doPost(String url, PostParam params) throws DoPostException {
        HttpURLConnection conn = null;
        URL _url = null;
        OutputStream out = null;
        InputStream in = null;
        String result = "";
        try {
            _url = new URL(url);
            conn = (HttpURLConnection) _url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(60000);
            conn.setReadTimeout(60000);
            conn.setRequestMethod("POST");
            //conn.setRequestProperty("Connection", "Keep-Alive");
            //conn.setRequestProperty("Accept", "application/octet-stream, */*");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("UseZip", Boolean.toString(params.isZip()));
            conn.setRequestProperty("os-version", android.os.Build.VERSION.RELEASE);
            String mb_model = android.os.Build.MANUFACTURER + "," + android.os.Build.PRODUCT;
            conn.setRequestProperty("mb-model", mb_model);
            out = conn.getOutputStream();
            if (params != null)
                out.write(params.getParamBytes());
            out.flush();
            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                StringBuilder content = new StringBuilder();
                in = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String line = "";
                while ((line = br.readLine()) != null) {
                    content.append(line);
                }
                result = content.toString();
            } else {
                throw new DoPostException(conn.getResponseMessage() + " 错误代码：" + conn.getResponseCode());
            }
        } catch (OutOfMemoryError er) {
            throw new DoPostException("内在溢出");
        } catch (Error er) {
            er.printStackTrace();
            throw new DoPostException("出错了");
        } catch (SocketTimeoutException ex) {
            ex.printStackTrace();
            throw new DoPostException("连接服务器超时");
        } catch (SocketException ex) {
            ex.printStackTrace();
            throw new DoPostException("网络连接不可用");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new DoPostException("连接服务器异常，请检查网络");
        } catch (IOException e) {
            e.printStackTrace();
            throw new DoPostException("网络数据读写异常，请检查网络");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DoPostException("网络连接不可用");
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return result;
    }

    /**
     * 异步Post请求，返回json
     *
     * @param url      Post请求地址
     * @param listener listener回调接口
     */
    public void doPost(final String url, final PostParam params, final OnDoPostDataListener listener) {
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                try {
                    if (msg.what == ERROR_WHAT)
                        listener.onError(msg.getData().getString("errorMessage"));
                    else if (msg.what == SHOW_MESSAGE) {
                        String message = msg.getData().getString("message");
                        int value = msg.getData().getInt("value");
                        listener.onShowMessage(message, value);
                    } else if (msg.what == DATA_RECEIVE) {
                        String result = msg.getData().getString("result");
                        listener.onDataReceiver(result);
                    }
                } catch (Error er) {

                } catch (Exception ex) {

                }
            }
        };
        //AsyncTask<String, Integer, String> postThread = new AsyncTask<String, Integer, String>() {
        MessageExecutorService.getInstance().execute(new Runnable() {
            @Override
            //protected String doInBackground(String... pms) {
            public void run() {
                HttpURLConnection conn = null;
                URL _url = null;
                OutputStream out = null;
                InputStream in = null;
                String result = "";
                try {
                    showMessage(handler, "正在准备连接服务...", 2);
                    _url = new URL(url);
                    conn = (HttpURLConnection) _url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setConnectTimeout(30000);
                    conn.setReadTimeout(30000);
                    conn.setRequestMethod("POST");
                    //conn.setRequestProperty("Connection", "Keep-Alive");
                    //conn.setRequestProperty("Accept", "application/octet-stream, */*");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    if (params != null) {
                        if (params.containsKey("ContentType")) {
                            conn.setRequestProperty("Content-Type", (String) params.getParam("ContentType"));
                        }
                        if (params.containsKey("Accept")) {
                            conn.setRequestProperty("Accept-Type", (String) params.getParam("Accept"));
                        }
                    }
                    if (params != null)
                        conn.setRequestProperty("UseZip", Boolean.toString(params.isZip()));
                    conn.setRequestProperty("os-version", android.os.Build.VERSION.RELEASE);
                    String mb_model = android.os.Build.MANUFACTURER + "," + android.os.Build.PRODUCT;
                    conn.setRequestProperty("mb-model", mb_model);
                    conn.setUseCaches(false);
                    showMessage(handler, "正在连接服务...", 10);
                    out = conn.getOutputStream();
                    showMessage(handler, "正在发送请求参数...", 50);
                    if (params != null)
                        out.write(params.getParamBytes());
                    out.flush();
                    showMessage(handler, "正在接收数据...", 80);
                    int code = conn.getResponseCode();
                    if (code == HttpURLConnection.HTTP_OK) {
                        StringBuilder content = new StringBuilder();
                        in = conn.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                        String line = "";
                        while ((line = br.readLine()) != null) {
                            content.append(line);
                        }
                        result = content.toString();
                        //listener.onDataReceiver(result);
                        Message msg = new Message();
                        msg.what = DATA_RECEIVE;
                        Bundle data = new Bundle();
                        data.putString("result", result);
                        msg.setData(data);
                        sendMessage(handler, msg);
                        showMessage(handler, "接收数据完成", 100);
                    } else {
                        sendError(handler, conn.getResponseMessage() + " 错误代码：" + conn.getResponseCode(), false);
                        showMessage(handler, "", 100);
                    }
                } catch (OutOfMemoryError er) {
                    er.printStackTrace();
                    sendError(handler, "内存溢出", false);
                } catch (Error er) {
                    er.printStackTrace();
                    sendError(handler, "出错了", false);
                } catch (SocketTimeoutException ex) {
                    ex.printStackTrace();
                    sendError(handler, "连接服务器超时", true);
                } catch (SocketException ex) {
                    ex.printStackTrace();
                    sendError(handler, "网络连接不可用", true);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    sendError(handler, "连接服务器异常，请检查网络", true);
                } catch (IOException e) {
                    e.printStackTrace();
                    sendError(handler, "网络数据读写异常，请检查网络", true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    sendError(handler, "网络连接不可用", false);
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                        }
                    }

                    if (conn != null) {
                        conn.disconnect();
                    }
                }
                //return "doPost";
            }
        });
    }

    private void showMessage(Handler handler, String message, int value) {
        Message msg = new Message();
        msg.what = SHOW_MESSAGE;
        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        bundle.putInt("value", value);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    public void sendMessage(Handler handler, Message msg) {
        handler.sendMessage(msg);
    }

    public void get(String url, OnLoadDataListener listener) {
        get(url, "UTF-8", listener);
    }

    public void get(String url, boolean zip, OnLoadDataListener listener) {
        get(url, zip, "UTF-8", listener);
    }

    public void get(final String url, final String charset, final OnLoadDataListener listener) {
        get(url, false, charset, listener);
    }

    public void get(final String url, final boolean zip, final String charset, final OnLoadDataListener listener) {
        if (url == null)
            return;
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                try {
                    if (msg.what == DATA_RECEIVE)
                        listener.onDataReceiver(msg.getData().getString(
                                "dataContent"));
                    else if (msg.what == ERROR_WHAT)
                        listener.onError(msg.getData().getString("errorMessage"));
                } catch (Error er) {

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        //AsyncTask<String, Integer, String> postThread = new AsyncTask<String, Integer, String>() {
        //@Override
        //protected String doInBackground(String... params) {
        //Thread thread = new Thread(new Runnable() {
        MessageExecutorService.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext httpContext = new BasicHttpContext();
                //HttpPost httpPost = new HttpPost(url);
                HttpGet httpGet = new HttpGet(url);
                httpGet.addHeader("UseZip", Boolean.toString(zip));
                httpGet.addHeader("os-version", android.os.Build.VERSION.RELEASE);

                String mb_model = android.os.Build.MANUFACTURER + "," + android.os.Build.PRODUCT;
                httpGet.addHeader("mb-model", mb_model);
                try {
                    HttpResponse response = httpClient.execute(httpGet, httpContext);
                    int code = response.getStatusLine().getStatusCode();
                    if (code == HttpURLConnection.HTTP_OK) {
                        String serverResponse = EntityUtils.toString(response.getEntity(), charset);
                        Message msg = new Message();
                        msg.what = DATA_RECEIVE;
                        Bundle bundle = new Bundle();
                        bundle.putString("dataContent", serverResponse);
                        msg.setData(bundle);
                        sendMessage(handler, msg);
                    } else {
                        String message = "读取数据失败,错误代码：" + code;
                        Message msg = new Message();
                        msg.what = ERROR_WHAT;
                        Bundle bundle = new Bundle();
                        bundle.putString("errorMessage", message);
                        msg.setData(bundle);
                        sendMessage(handler, msg);
                    }
                } catch (OutOfMemoryError er) {
                    er.printStackTrace();
                    sendError(handler, "内存溢出", false);
                } catch (Error er) {
                    er.printStackTrace();
                    sendError(handler, "出错了", false);
                } catch (SocketTimeoutException ex) {
                    ex.printStackTrace();
                    sendError(handler, "连接服务器超时", true);
                } catch (SocketException ex) {
                    ex.printStackTrace();
                    sendError(handler, "网络连接不可用", true);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    sendError(handler, "连接服务器异常，请检查网络", true);
                } catch (IOException e) {
                    e.printStackTrace();
                    sendError(handler, "网络数据读写异常，请检查网络", true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    sendError(handler, "网络连接不可用", false);
                } finally {
                }
                //return "get";
            }
        });
        //postThread.execute();
        //thread.start();
        //return thread;
    }

    private void sendError(Handler handler, String message, boolean sendBroadcase) {
        System.out.println("sendError message:" + message);
        Message msg = new Message();
        msg.what = ERROR_WHAT;
        Bundle bundle = new Bundle();
        bundle.putString("errorMessage", message);
        msg.setData(bundle);
        handler.sendMessage(msg);
        if (sendBroadcase) {
            //sendBroadcast("Show.Error.Line");
        }
    }

    public void getEx(final String url, final OnLoadDataListener listener) {
        getEx(url, false, listener);
    }

    public void getEx(final String url, final boolean zip, final OnLoadDataListener listener) {
        if (url == null)
            return;
        MessageExecutorService.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext httpContext = new BasicHttpContext();
                HttpGet httpGet = new HttpGet(url);
                httpGet.addHeader("UseZip", Boolean.toString(zip));
                httpGet.addHeader("os-version", android.os.Build.VERSION.RELEASE);
                String mb_model = android.os.Build.MANUFACTURER + "," + android.os.Build.PRODUCT;
                httpGet.addHeader("mb-model", mb_model);
                try {
                    HttpResponse response = httpClient.execute(httpGet, httpContext);

                    int code = response.getStatusLine().getStatusCode();
                    if (code == HttpURLConnection.HTTP_OK) {
                        String serverResponse = EntityUtils.toString(response.getEntity());
                        listener.onDataReceiver(serverResponse);
                    } else {
                        String message = "读取数据失败,错误代码：" + code;
                        listener.onError(message);
                    }
                } catch (OutOfMemoryError er) {
                    er.printStackTrace();
                    listener.onError("内存溢出");
                } catch (Error er) {
                    er.printStackTrace();
                    listener.onError("出错了");
                } catch (SocketTimeoutException ex) {
                    ex.printStackTrace();
                    listener.onError("连接服务器超时");
                } catch (SocketException ex) {
                    ex.printStackTrace();
                    listener.onError("网络连接不可用");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    listener.onError("连接服务器异常，请检查网络");
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onError("网络数据读写异常，请检查网络");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    listener.onError("网络连接不可用");
                } finally {
                }
                //return "get";
            }
        });
        //postThread.execute();
        //thread.start();
        //return thread;
    }

    /**
     * 异步HTTP POST 线程内完成，解决在主线程引起卡界面状态
     */
    public void doPostInThread(final String url, final PostParam params, final OnDoPostDataListener listener) {
        //AsyncTask<String, Integer, String> postThread = new AsyncTask<String, Integer, String>() {
        //	@Override
        if (url == null)
            return;
        MessageExecutorService.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                //protected String doInBackground(String... pms) {
                HttpURLConnection conn = null;
                URL _url = null;
                OutputStream out = null;
                InputStream in = null;
                String result = "";
                try {
                    listener.onShowMessage("正在准备连接服务...", 2);
                    _url = new URL(url);
                    conn = (HttpURLConnection) _url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setConnectTimeout(60000);
                    conn.setReadTimeout(60000);
                    conn.setRequestMethod("POST");
                    //conn.setRequestProperty("Connection", "Keep-Alive");
                    //conn.setRequestProperty("Accept", "application/octet-stream, */*");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//					conn.setRequestProperty("Content-Type","application/json");
                    // conn.setRequestProperty("User-Agent", WebViewGlobalValue.userAgent);
                    if (params != null) {
                        conn.setRequestProperty("UseZip", Boolean.toString(params.isZip()));
                    }
                    conn.setUseCaches(false);
                    conn.setRequestProperty("os-version", android.os.Build.VERSION.RELEASE);
                    String mb_model = android.os.Build.MANUFACTURER + "," + android.os.Build.PRODUCT;
                    conn.setRequestProperty("mb-model", mb_model);
                    listener.onShowMessage("正在准备连接服务...", 2);
                    out = conn.getOutputStream();
                    listener.onShowMessage("正在发送请求参数...", 50);
                    if (params != null)
                        out.write(params.getParamBytes());
                    out.flush();
                    listener.onShowMessage("正在接收数据...", 80);
                    int code = conn.getResponseCode();
                    if (code == HttpURLConnection.HTTP_OK) {
                        StringBuilder content = new StringBuilder();
                        in = conn.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                        String line = "";
                        while ((line = br.readLine()) != null) {
                            content.append(line);
                        }
                        result = content.toString();
                        //listener.onDataReceiver(result);
                        listener.onDataReceiver(result);
                        listener.onShowMessage("接收数据完成", 100);
                    } else {
                        listener.onError(conn.getResponseMessage() + " 错误代码：" + conn.getResponseCode());
                        listener.onShowMessage("", 100);
                    }
                } catch (OutOfMemoryError er) {
                    er.printStackTrace();
                    listener.onError("内存溢出");
                } catch (Error er) {
                    er.printStackTrace();
                    listener.onError("内存溢出");
                } catch (SocketTimeoutException ex) {
                    ex.printStackTrace();
                    listener.onError("连接服务器超时");
                    //sendBroadcast("Show.Error.Line");
                } catch (SocketException ex) {
                    ex.printStackTrace();
                    listener.onError("网络连接不可用");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    listener.onError("连接服务器异常，请检查网络");
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onError("网络数据读写异常，请检查网络");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    listener.onError("网络连接不可用");
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                        }
                    }

                    if (conn != null) {
                        conn.disconnect();
                    }
                }
                //return "doPost";
            }
        });
    }

}

