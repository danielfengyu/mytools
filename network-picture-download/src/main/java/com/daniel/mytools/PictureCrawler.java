package com.daniel.mytools;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class PictureCrawler implements Runnable {
    /**
     * 起始文件夹
     */
    private AtomicInteger start;

    /**
     * 文件下载后存储的目录
     */
    private String targetDir;

    /**
     * 图片地址的固定前缀
     */
    private String picUrlPrefix;

    /**
     * 下载图片的头信息
     */
    private Map<String, String> headers;

    public PictureCrawler(Map<String, String> header, AtomicInteger start, String targetDir, String picUrlPrefix) {
        this.headers = header;
        this.start = start;
        this.targetDir = targetDir;
        this.picUrlPrefix = picUrlPrefix;
    }

    @Override
    public void run() {
        String imgUrl;
        while (true) {
            int index = start.getAndIncrement();
            System.out.println(Thread.currentThread().getName() + ":" + index);
            for (int j = 1; j < 500; j++) {
                imgUrl = picUrlPrefix + index + "/" + j + ".jpg";
                try {
                    getPicture2(imgUrl, targetDir, headers, index, j);
                } catch (Exception e) {
                    if (e instanceof FileNotFoundException) {
                        break;
                    }
                }
            }
        }
    }

    public static void getPicture2(String urlHttp, String path, Map<String, String> headers, int i, int j) throws Exception {
        FileOutputStream out = null;
        BufferedInputStream in = null;
        HttpURLConnection connection = null;
        byte[] buf = new byte[1024 * 500];
        int len = 0;
        try {
            URL url = new URL(urlHttp);
            connection = (HttpURLConnection) url.openConnection();
            if (headers != null && !headers.isEmpty()) {
                //设置头信息
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            connection.connect();
            connection.setConnectTimeout(10000);
            in = new BufferedInputStream(connection.getInputStream());
            String realPath = path + "/" + i;
            File targetDir = new File(realPath);
            //如果文件夹不存在
            if (!targetDir.exists()) {
                //创建文件夹
                targetDir.mkdir();
            }
            out = new FileOutputStream(realPath + "/" + j + ".jpg");
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e instanceof FileNotFoundException) {
                throw e;
            }
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    System.out.println("close in error!");
                }
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    System.out.println("close out error!");
                }
            }
            if (null != connection) {
                try {
                    connection.disconnect();
                } catch (Exception e) {
                    System.out.println("close connection error!");
                }
            }
        }
    }
}
