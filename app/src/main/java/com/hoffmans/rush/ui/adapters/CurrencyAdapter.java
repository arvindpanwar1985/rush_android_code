package com.hoffmans.rush.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hoffmans.rush.R;
import com.hoffmans.rush.model.Currency;

import java.util.List;

/**
 * Created by devesh on 6/1/17.
 */

public class  CurrencyAdapter extends ArrayAdapter<Currency> {

    private List<Currency>mList;
    private Context mContext;
    public CurrencyAdapter(Context context, int resource ,List<Currency> currencyList){
        super(context, resource, currencyList);
        mList=currencyList;
        mContext=context;

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {



        LayoutInflater inflater = LayoutInflater.from(mContext);
        View row = inflater.inflate(R.layout.texview_spinner, parent,
                false);
        TextView txt = (TextView) row.findViewById(R.id.txtSpinner);
        final  Currency currency=mList.get(position);
        txt.setText(currency.getCurrencyName());
        return row;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            v = inflater.inflate(R.layout.texview_spinner, parent,false);
        }
        TextView lbl = (TextView) v.findViewById(R.id.txtSpinner);
        final  Currency currency=mList.get(position);
        lbl.setText(currency.getCurrencyName());
        return convertView;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Nullable
    @Override
    public Currency getItem(int position) {
        return mList.get(position);
    }
}
