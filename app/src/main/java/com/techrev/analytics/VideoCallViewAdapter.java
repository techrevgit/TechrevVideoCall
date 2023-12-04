package com.techrev.analytics;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class VideoCallViewAdapter extends BaseAdapter {

    private String[] call_views;
    private Activity mActivity = null;
    private int mPosition = 0;

    public VideoCallViewAdapter(Activity activity , String[] views) {
        this.call_views = views;
        this.mActivity = activity;
    }

    @Override
    public int getCount() {
        return call_views.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }


    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View mView = null;
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.spinner_item_view , viewGroup , false);
        //mView = super.getDropDownView(position, convertView, parent);
        TextView tv_call_view = mView.findViewById(R.id.tvViewType);
        tv_call_view.setText(call_views[i]);

        if (i == mPosition){
            tv_call_view.setTextColor(mActivity.getResources().getColor(R.color.color_primary));
        }
        return mView;
    }

    public void setSelection(int position){
        mPosition = position;
        notifyDataSetChanged();
    }

}
