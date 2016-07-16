package cn.bingoogolapple.imageloader.bga;

import android.graphics.Bitmap;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/7/16 下午9:00
 * 描述:
 */
public interface BGAImageCache {
    Bitmap get(String url);

    void put(String url, Bitmap bitmap);
}