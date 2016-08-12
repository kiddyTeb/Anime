package com.liangdekai.anime.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.liangdekai.anime.R;
import com.liangdekai.anime.bean.ChapterBean;
import com.liangdekai.anime.util.ViewHolder;

import java.util.List;

/**
 * Created by asus on 2016/8/9.
 */
public class ChapterAdapter extends BaseAdapter {
    private List<ChapterBean> mList ;
    private Context mContext ;

    public ChapterAdapter(Context context , List<ChapterBean> list){
        mList = list ;
        mContext = context ;
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
        ViewHolder viewHolder ;
        View view ;
        if (convertView == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_lv_detail, null);
            viewHolder.name = (TextView) view.findViewById(R.id.part_lv_detail);
            view.setTag(viewHolder);
        }else{
            view = convertView ;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.name.setText(mList.get(i).getName());
        return view;
    }
}
