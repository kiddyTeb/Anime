package com.liangdekai.anime.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.liangdekai.anime.MyApplication;
import com.liangdekai.anime.R;
import com.liangdekai.anime.bean.CartoonBean;
import com.liangdekai.anime.util.BitmapCache;
import com.liangdekai.anime.util.ViewHolder;
import com.liangdekai.anime.util.VolleyHelper;

import java.util.List;

public class CatalogueAdapter extends BaseAdapter{
    private List<CartoonBean> mList ;
    private Context mContext ;
    private BitmapCache mCache ;

    public CatalogueAdapter(Context context , List<CartoonBean> list){
        mList = list ;
        mContext = context ;
        mCache = MyApplication.getBitmapCache();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view ;
        ViewHolder viewHolder ;
        if (convertView == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.item_lv_message, null) ;
            viewHolder = new ViewHolder() ;
            viewHolder.imageView = (NetworkImageView) view.findViewById(R.id.activity_main_image) ;
            viewHolder.name = (TextView) view.findViewById(R.id.activity_main_title) ;
            viewHolder.area = (TextView) view.findViewById(R.id.activity_main_area) ;
            viewHolder.finish = (TextView) view.findViewById(R.id.activity_main_finish) ;
            viewHolder.introduction  = (TextView) view.findViewById(R.id.activity_main_introduction) ;
            view.setTag(viewHolder);
        }else{
            view = convertView ;
            viewHolder = (ViewHolder) view.getTag() ;
        }
        viewHolder.imageView.setTag(mList.get(i).getCoverImg());
        viewHolder.name.setText(mList.get(i).getName());
        viewHolder.area.setText("地区:" + mList.get(i).getArea());
        viewHolder.finish.setText("状态:" + mList.get(i).getFinish());
        viewHolder.introduction.setText("简介:"+mList.get(i).getIntroduction());
        Bitmap bitmap = mCache.getBitmap(mList.get(i).getCoverImg() );
        if (bitmap == null){
            VolleyHelper.downloadBitmap(mList.get(i).getCoverImg() , viewHolder.imageView);
        }else {
            viewHolder.imageView.setImageBitmap(bitmap);
        }
        return view;
    }
}
