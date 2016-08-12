package com.liangdekai.anime.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.liangdekai.anime.R;
import com.liangdekai.anime.adapter.ChapterAdapter;
import com.liangdekai.anime.bean.ChapterBean;
import com.liangdekai.anime.db.CartoonDB;
import com.liangdekai.anime.util.HandleSaveResponse;
import com.liangdekai.anime.util.TransformString;
import com.liangdekai.anime.util.VolleyHelper;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private String mName ;
    private ListView mListView ;
    private ChapterAdapter mChapterAdapter;
    private List<ChapterBean> mChapterList;
    private CartoonDB mCartoonDB ;
    private ProgressDialog mProgressDialog ;

    /**
     * 启动该活动
     * @param context
     * @param name
     */
    public static void startActivity(Context context , String name ){
        Intent intent = new Intent(context , DetailActivity.class);
        intent.putExtra("name" ,name);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        init();
    }

    /**
     * 获取跳转活动数据，初始化控件
     */
    private void init(){
        Intent i = getIntent() ;
        mName = i.getStringExtra("name");
        mCartoonDB = CartoonDB.getInstance(DetailActivity.this);
        mListView = (ListView) findViewById(R.id.activity_lv_detail);
        mChapterList = new ArrayList<>();
        mListView.setOnItemClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null){
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListView.setSelection(0);
                }
            });
        }

        CollapsingToolbarLayout layout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        if (layout != null){
            layout.setTitle(mName);
            layout.setExpandedTitleColor(Color.RED);
            layout.setCollapsedTitleTextColor(Color.WHITE);
            layout.setBackground(getResources().getDrawable(R.mipmap.toolbar));
        }
        loadData(mName);
    }

    /**
     * 加载章节列表数据
     * @param name
     */
    private void loadData(String name){
        mChapterList = mCartoonDB.loadChapterInfo(name);
        if (mChapterList.size() == 0 ){
            showDialog();
            sendRequest(name);
        }else{
            mChapterAdapter = new ChapterAdapter(DetailActivity.this , mChapterList);
            mListView.setAdapter(mChapterAdapter);
            mChapterAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 根据URL发送网络请求
     * @param name
     */
    private void sendRequest(final String name){
        String comicName = TransformString.transform(name);
        String url = "http://japi.juhe.cn/comic/chapter?comicName="+comicName+"&skip=&key=eff3d8473c455edbc5e47e2b0b2d21ea" ;
        VolleyHelper.sendByVolley(url, new VolleyHelper.VolleyListener() {
            @Override
            public void onSucceed(String jsonString) {
                closeDialog();
                HandleSaveResponse.handleSaveChapter(mCartoonDB ,jsonString);
                if (mChapterList != null){
                    mChapterList.clear();
                }
                mChapterList = mCartoonDB.loadChapterInfo(name) ;
                mChapterAdapter = new ChapterAdapter(DetailActivity.this , mChapterList);
                mListView.setAdapter(mChapterAdapter);
                mChapterAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed() {
                closeDialog();
                Toast.makeText(DetailActivity.this , "请检查网络设置" , Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(DetailActivity.this , "此功能尚未开发" , Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CartoonActivity.startActivity(DetailActivity.this , mChapterList.get(i).getId() , mName);
    }

    /**
     *创建展示对话框
     */
    private void showDialog(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("请耐心等待");
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    /**
     * 关闭对话框
     */
    private void closeDialog(){
        if (mProgressDialog != null){
            mProgressDialog.dismiss();
        }
    }
}
