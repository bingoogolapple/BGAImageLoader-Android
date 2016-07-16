package cn.bingoogolapple.imageloader.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/7/16 下午9:42
 * 描述:
 */
public class BGAImageLoaderUtil {
    private BGAImageLoaderUtil() {
    }

    public static String md5(String... strs) {
        if (strs == null || strs.length == 0) {
            throw new RuntimeException("请输入需要加密的字符串!");
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            boolean isNeedThrowNotNullException = true;
            for (String str : strs) {
                if (!TextUtils.isEmpty(str)) {
                    isNeedThrowNotNullException = false;
                    md.update(str.getBytes());
                }
            }
            if (isNeedThrowNotNullException) {
                throw new RuntimeException("请输入需要加密的字符串!");
            }
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static void log(String msg) {
        Log.i(BGAImageLoaderUtil.class.getSimpleName(), "" + msg);
    }

    // Returns a cache size equal to approximately three screens worth of images.
    public static int getCacheSize(Context ctx) {
        final DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
        final int screenWidth = displayMetrics.widthPixels;
        final int screenHeight = displayMetrics.heightPixels;
        // 4 bytes per pixel
        final int screenBytes = screenWidth * screenHeight * 4;

        return screenBytes * 3;
    }
}