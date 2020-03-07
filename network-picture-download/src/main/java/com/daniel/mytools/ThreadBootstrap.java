package com.daniel.mytools;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author fengyu
 * @date 2020-03-01
 */
public class ThreadBootstrap {
    /**
     * 线程数量
     */
    private static final Integer THREAD_NUM = 8;
    private static final int POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;

    public static void main(String[] args) {
//        ExecutorService executor = new ThreadPoolExecutor(POOL_SIZE, POOL_SIZE, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(512), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        AtomicInteger dirNum = new AtomicInteger(5103);
        String refererUrl = "https://www.mm131.net";
        String targetDir = "E:/images/mm131";
        String picUrlPrefix = "https://img1.mmmw.net/pic/";
        Map<String, String> headers = new HashMap<>(10);
        headers.put("Referer", refererUrl);
        for (int i = 0; i < THREAD_NUM; i++) {
            Thread thread = new Thread(new PictureCrawler(headers, dirNum, targetDir, picUrlPrefix));
            thread.start();
        }
//        for (int i = 0; i < THREAD_NUM; i++) {
//            executor.submit(new PictureCrawler(headers, dirNum, targetDir, picUrlPrefix));
//        }
//        executor.shutdownNow();
    }
}



