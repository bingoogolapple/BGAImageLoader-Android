package cn.bingoogolapple.imageloader.demo.engine;

import java.util.List;

import cn.bingoogolapple.imageloader.demo.model.NormalModel;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/7/16 下午8:27
 * 描述:
 */
public interface ApiEngine {
    @GET("normalModels.json")
    Call<List<NormalModel>> getNormalModels();
}
