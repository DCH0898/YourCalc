package com.chun.calc.net;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 类说明
 * Created on 2014-5-21
 * 海南新境界软件有限公司
 *
 * @author cjh
 */
public class MessageExecutorService {
    private ExecutorService executorService;// 线程池

    private static MessageExecutorService instance;

    private MessageExecutorService() {
        executorService = Executors.newCachedThreadPool();
    }

    public static MessageExecutorService getInstance() {
        if (instance == null)
            instance = new MessageExecutorService();
        return instance;
    }

    public void execute(Runnable runner) {
        executorService.execute(runner);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
