package cn.bingoogolapple.imageloader.bga;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/7/16 下午9:12
 * 描述:
 */
public class BGAMemoryCache implements BGAImageCache {
    private LruCache<String, Bitmap> mLruCache;

    public BGAMemoryCache() {
        // 获取最大可用内存
        final long maxMemory = Runtime.getRuntime().maxMemory();
        // 取八分之一的最大可用内存作为缓存。超过该缓存,则开始回收
        final int cacheMemory = (int) (maxMemory / 8);
        mLruCache = new LruCache<String, Bitmap>(cacheMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    @Override
    public Bitmap get(String url) {
        return mLruCache.get(url);
    }

    @Override
    public void put(String url, Bitmap bitmap) {
        mLruCache.put(url, bitmap);
    }
}