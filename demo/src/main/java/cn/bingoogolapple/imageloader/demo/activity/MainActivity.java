package cn.bingoogolapple.imageloader.demo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.imageloader.BGAImage;
import cn.bingoogolapple.imageloader.demo.App;
import cn.bingoogolapple.imageloader.demo.R;
import cn.bingoogolapple.imageloader.demo.engine.ApiEngine;
import cn.bingoogolapple.imageloader.demo.model.NormalModel;
import cn.bingoogolapple.imageloader.demo.util.UIUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mContentRv;
    private ContentAdapter mContentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContentRv = (RecyclerView) findViewById(R.id.rv_main_content);
        mContentRv.setLayoutManager(new LinearLayoutManager(this));
        mContentAdapter = new ContentAdapter(mContentRv);
        mContentRv.setAdapter(mContentAdapter);

        loadData();
    }

    private void loadData() {
        App.getInstance().getRetrofit().create(ApiEngine.class).getNormalModels().enqueue(new Callback<List<NormalModel>>() {
            @Override
            public void onResponse(Call<List<NormalModel>> call, Response<List<NormalModel>> response) {
                mContentAdapter.setData(response.body());
            }

            @Override
            public void onFailure(Call<List<NormalModel>> call, Throwable t) {
                Toast.makeText(App.getInstance(), "数据加载失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class ContentAdapter extends BGARecyclerViewAdapter<NormalModel> {

        public ContentAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.item_normal);
        }

        @Override
        public void fillData(BGAViewHolderHelper viewHolderHelper, int position, NormalModel model) {
            BGAImage.displayImage(MainActivity.this, viewHolderHelper.getImageView(R.id.iv_item_main_avatar), model.avatorPath, R.mipmap.holder, R.mipmap.holder, UIUtil.dp2px(50), UIUtil.dp2px(50), null);
            viewHolderHelper.setText(R.id.tv_item_main_title, model.title).setText(R.id.tv_item_main_detail, model.detail);
        }
    }
}