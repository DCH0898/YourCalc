package com.chun.calc.net;

/**
 * 类说明
 * Created on 2014-01-13
 * 海南新境软件有限公司
 *
 * @author cjh
 */
public interface OnDoPostDataListener {
    public void onDataReceiver(String result);

    public void onError(String errorMessage);

    public void onShowMessage(String message, int value);
}
