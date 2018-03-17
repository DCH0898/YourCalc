package com.chun.calc.net;

/**
 * 类说明
 * Created on 2014-01-13
 * 海南新境软件有限公司
 *
 * @author cjh
 */
public interface OnLoadDataListener {
    public void onDataReceiver(String dataContent);

    public void onError(String errorMessage);
}
