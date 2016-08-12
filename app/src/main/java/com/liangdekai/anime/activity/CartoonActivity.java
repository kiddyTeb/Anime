package com.liangdekai.anime.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.liangdekai.anime.MyApplication;
import com.liangdekai.anime.R;
import com.liangdekai.anime.db.CartoonDB;
import com.liangdekai.anime.util.BitmapCache;
import com.liangdekai.anime.util.HandleSaveResponse;
import com.liangdekai.anime.util.ImageLoader;
import com.liangdekai.anime.util.TransformString;
import com.liangdekai.anime.util.VolleyHelper;

import java.util.ArrayList;
import java.util.List;

public class CartoonActivity extends Activity implements ViewPager.OnPageChangeListener{
    private ViewPager mViewPager ;
    private List<String> mImageList ;
    private CartoonDB mCartoon ;
    private ImageLoader mImageLoader ;
    private TextView mTextView ;
    private DetailAdapter mDetailAdapter ;
    private String mName ;
    private BitmapCache mBitmapCache ;

    /**
     * 启动该活动
     * @param context
     * @param id
     * @param name
     */
    public static void startActivity(Context context , String id , String name){
        Intent intent = new Intent(context , CartoonActivity.class);
        intent.putExtra("id" , id);
        intent.putExtra("name" , name);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE) ;
        setContentView(R.layout.activity_vp_cartoon);
        init();
    }

    /**
     * 获取跳转活动的数据，初始化控件
     */
    private void init(){
        mName = getIntent().getStringExtra("name");
        mCartoon = CartoonDB.getInstance(CartoonActivity.this);
        mImageLoader = ImageLoader.getInstance();
        mBitmapCache = MyApplication.getBitmapCache() ;
        mImageList = new ArrayList<>();
        mDetailAdapter = new DetailAdapter();
        mViewPager = (ViewPager) findViewById(R.id.activity_vp_detail) ;
        mTextView = (TextView) findViewById(R.id.activity_tv_record);
        mViewPager.addOnPageChangeListener(this);
        setIndex(0);
        sendRequest();
    }

    /**
     * 设置当前图片位置，以及图片总数
     * @param position
     */
    private void setIndex(int position){
        int temp = position + 1 ;
        String current = ""+temp;
        String sum = ""+mImageList.size();
        String result = current+"/"+sum;
        mTextView.setText(result);
    }

    /**
     * 发送网络请求
     */
    private void sendRequest(){
        String id = getIntent().getStringExtra("id");
        String comicName = TransformString.transform(mName);
        String url = "http://japi.juhe.cn/comic/chapterContent?comicName="+comicName+"&id="+id+"&key=eff3d8473c455edbc5e47e2b0b2d21ea";
        VolleyHelper.sendByVolley(url, new VolleyHelper.VolleyListener() {
            @Override
            public void onSucceed(String jsonString) {
                mImageList = HandleSaveResponse.handleSaveImage(jsonString);
                mViewPager.setAdapter(mDetailAdapter);
                mDetailAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed() {
                Toast.makeText(CartoonActivity.this , "请检查网络设置" , Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setIndex(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class DetailAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(CartoonActivity.this).inflate(R.layout.item_vp_message, null);
            NetworkImageView imageView = (NetworkImageView) view.findViewById(R.id.part_vp_cartoon);
            //mViewArray.put(position , imageView);
            imageView.setImageResource(R.mipmap.empty);
            //mImageLoader.loadImage(mImageList.get(position) , imageView);
            Bitmap bitmap = mBitmapCache.getBitmap(mImageList.get(position));
            if (bitmap == null){
                VolleyHelper.downloadBitmap(mImageList.get(position) , imageView);
            }else {
                imageView.setImageBitmap(bitmap);
            }

            //VolleyHelper.download(mImageList.get(position) , imageView);
            /*Bitmap bitmap = mImageLoader.getImageByCache(mImageList.get(position));
            if (bitmap == null){
                mImageLoader.loadImage(mImageList.get(position) , imageView);
            }else {
                imageView.setImageBitmap(bitmap);
            }*/
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

        @Override
        public int getCount() {
            return mImageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
