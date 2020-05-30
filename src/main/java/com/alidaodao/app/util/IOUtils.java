package com.alidaodao.app.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 *
 * @desc io帮助类
 * @author Jack
 * @date 2020/2/23 22:23
 */
public class IOUtils {
    static int BUFFER_SIZE = 1024;

    public static String inputStreamToString(InputStream in, String encoding) throws Exception {
        byte[] bytes = inputStreamToByte(in);
        String result = null;
        if (bytes.length <= 0) {
            return result;
        }
        result = new String(bytes, encoding);
        return result;
    }

    public static String inputStreamToString(InputStream in) throws Exception {
        return inputStreamToString(in, "UTF-8");
    }

    public static byte[] inputStreamToByte(InputStream in) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);

        data = null;
        return outStream.toByteArray();
    }

    public static InputStream stringToInputStream(String str, String encoding) throws Exception {
        InputStream is = null;
        if (str == null) {
            return is;
        }
        is = byteToInputStream(str.getBytes(encoding));
        return is;
    }

    public static InputStream byteToInputStream(byte[] bytes) throws Exception {
        ByteArrayInputStream is = null;
        if (bytes == null || bytes.length <= 0) {
            return is;
        }
        is = new ByteArrayInputStream(bytes);
        return is;
    }
}
