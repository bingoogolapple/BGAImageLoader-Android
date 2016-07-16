package cn.bingoogolapple.imageloader.bga;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import cn.bingoogolapple.imageloader.util.BGAImageLoaderUtil;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/7/16 下午9:40
 * 描述:
 */
public class BGADiskCache implements BGAImageCache {
    private String mCacheDirPath;

    public void init(Context context) {
        if (context != null && mCacheDirPath == null) {
            File cacheDir;
            if (BGAImageLoaderUtil.isExternalStorageWritable()) {
                cacheDir = new File(context.getExternalCacheDir(), BGAImageCache.class.getSimpleName());
                BGAImageLoaderUtil.log("缓存图片到本地外部存储地址：" + cacheDir.getAbsolutePath());
            } else {
                cacheDir = new File(context.getCacheDir(), BGAImageCache.class.getSimpleName());
                BGAImageLoaderUtil.log("缓存图片到本地内部存储地址：" + cacheDir.getAbsolutePath());
            }
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            mCacheDirPath = cacheDir.getAbsolutePath() + File.separator;
        }
    }

    @Override
    public Bitmap get(String url) {
        if (mCacheDirPath == null) {
            return null;
        }

        try {
            String fileName = BGAImageLoaderUtil.md5(url);
            File file = new File(mCacheDirPath + fileName);
            if (file.exists()) {
                return BitmapFactory.decodeFile(file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void put(String url, Bitmap bitmap) {
        if (mCacheDirPath == null) {
            return;
        }

        OutputStream os = null;
        try {
            String fileName = BGAImageLoaderUtil.md5(url);
            os = new FileOutputStream(mCacheDirPath + fileName);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            BGAImageLoaderUtil.log("缓存图片到本地");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BGAImageLoaderUtil.closeQuietly(os);
        }
    }
}
