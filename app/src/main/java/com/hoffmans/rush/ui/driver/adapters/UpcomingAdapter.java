package com.hoffmans.rush.ui.driver.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoffmans.rush.R;
import com.hoffmans.rush.model.DriverDetail;
import com.hoffmans.rush.model.Record;
import com.hoffmans.rush.model.ServiceData;
import com.hoffmans.rush.ui.driver.viewholders.CharacterViewHolder;
import com.hoffmans.rush.ui.driver.viewholders.HeaderViewHolder;
import com.karumi.headerrecyclerview.HeaderRecyclerViewAdapter;

/**
 * Created by devesh on 17/3/17.
 */

public class UpcomingAdapter extends HeaderRecyclerViewAdapter<RecyclerView.ViewHolder, ServiceData,Record, DriverDetail>  {

    private static final String LOG_TAG = UpcomingAdapter.class.getSimpleName();
    private Context mContext;
     public UpcomingAdapter(Context context){
        mContext=context;

    }
    @Override
    protected RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View headerView = inflater.inflate(R.layout.header_upcoming, parent, false);
        return new HeaderViewHolder(headerView);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View characterView = inflater.inflate(R.layout.row_driver_records, parent, false);
        return new CharacterViewHolder(characterView);
    }



    @Override protected void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        ServiceData currentOrderData=getHeader();
        HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
        headerViewHolder.render(currentOrderData);
    }

    @Override protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        Record record = getItem(position);
        CharacterViewHolder characterViewHolder = (CharacterViewHolder) holder;
        characterViewHolder.render(record);
    }


    private LayoutInflater getLayoutInflater(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext());
    }

    @Override protected void onHeaderViewRecycled(RecyclerView.ViewHolder holder) {
        Log.v(LOG_TAG, "onHeaderViewRecycled(RecyclerView.ViewHolder holder)");
    }

    @Override protected void onItemViewRecycled(RecyclerView.ViewHolder holder) {
        Log.v(LOG_TAG, "onItemViewRecycled(RecyclerView.ViewHolder holder)");
    }

    @Override protected void onFooterViewRecycled(RecyclerView.ViewHolder holder) {
        Log.v(LOG_TAG, "onFooterViewRecycled(RecyclerView.ViewHolder holder)");
    }
}
