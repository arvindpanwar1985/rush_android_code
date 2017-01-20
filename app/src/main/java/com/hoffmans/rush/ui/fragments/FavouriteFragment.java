package com.hoffmans.rush.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hoffmans.rush.R;
import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.bean.FavouriteBean;
import com.hoffmans.rush.http.request.FavouriteRequest;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.listners.OnitemClickListner;
import com.hoffmans.rush.model.PickDropAddress;
import com.hoffmans.rush.ui.adapters.FavouriteAdapter;
import com.hoffmans.rush.utils.Constants;
import com.hoffmans.rush.utils.Progress;

import java.util.ArrayList;


public class FavouriteFragment extends BaseFragment implements OnitemClickListner.OnFrequentAddressClicked {

    private static final String ARG_PARAM1 = "param1";

    private boolean showIcon;
    private RecyclerView recyclerView;
    private FavouriteAdapter favouriteAdapter;
    private TextView txtNOFav;
    private ArrayList<PickDropAddress> addressArrayList;




    public FavouriteFragment() {
        // Required empty public constructor
    }



    public static FavouriteFragment newInstance(boolean param1) {
        FavouriteFragment fragment = new FavouriteFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            showIcon = getArguments().getBoolean(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity.initToolBar("FAVOURITES",true);
        View view=inflater.inflate(R.layout.fragment_favourite, container, false);
        // Inflate the layout for this fragment
        initViews(view);
        initListeners();
        getMyFavourites();
        return view;
    }



    @Override
    protected void initViews(View view) {

        recyclerView            =(RecyclerView)view.findViewById(R.id.favRecycler);
        LinearLayoutManager llm = new LinearLayoutManager(mActivity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        txtNOFav=(TextView)view.findViewById(R.id.txtNo_favorite);

    }

    @Override
    protected void initListeners() {

    }


    private void getMyFavourites(){
        Progress.showprogress(mActivity,getString(R.string.progress_loading),false);
        //mActivity.showProgress();
        String token=appPreference.getUserDetails().getToken();
        FavouriteRequest favouriteRequest=new FavouriteRequest();
        favouriteRequest.getFavourites(token, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                Progress.dismissProgress();
               // mActivity.hideProgress();
                FavouriteBean favouriteBean=(FavouriteBean)body;
                if(favouriteBean.getAddresses().size()!=0) {
                    addressArrayList = favouriteBean.getAddresses();
                    setAdapter(addressArrayList);
                    txtNOFav.setVisibility(View.GONE);
                }else{
                    txtNOFav.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onRequestFailed(String message) {
                Progress.dismissProgress();
                //mActivity.hideProgress();
                mActivity.showSnackbar(message,0);
                if(message.equals(Constants.KEY_AUTH_ERROR)){
                    mActivity.logOutUser();
                }
            }
        });
    }



    private  void setAdapter(ArrayList<PickDropAddress> addressArrayList){

        favouriteAdapter=new FavouriteAdapter(mActivity,addressArrayList,this,showIcon);
        recyclerView.setAdapter(favouriteAdapter);
    }
    @Override
    public void onitemclicked(View view, int position) {
        if(addressArrayList!=null) {
            PickDropAddress favouritePickDropAddress = addressArrayList.get(position);
            Intent intent = new Intent();
            intent.putExtra("fav_data", favouritePickDropAddress);
            getActivity().setResult(Activity.RESULT_OK, intent);
            mActivity.finish();
        }
    }

    @Override
    public void onfrequentAddressclicked(View view, int position) {


    }

    @Override
    public void onFavoriteAddressclicked(View view, int position) {
        if(addressArrayList!=null){
            PickDropAddress pickDropAddress=addressArrayList.get(position);
            unmarkFavourite(String.valueOf(pickDropAddress.getId()));
        }


    }


    private  void unmarkFavourite(String address_id){
        Progress.showprogress(mActivity,getString(R.string.progress_loading),false);
        String token =appPreference.getUserDetails().getToken();
        FavouriteRequest favouriteRequest=new FavouriteRequest();
        favouriteRequest.markFavUnFavourite(token, address_id, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                Progress.dismissProgress();
                FavouriteBean favouriteBean=(FavouriteBean)body;
                if(favouriteBean.getAddresses().size()!=0) {
                    addressArrayList = favouriteBean.getAddresses();
                    setAdapter(addressArrayList);
                    txtNOFav.setVisibility(View.GONE);
                }else{
                    recyclerView.setVisibility(View.GONE);

                    txtNOFav.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onRequestFailed(String message) {
                Progress.dismissProgress();
                mActivity.showSnackbar(message,0);
                if(message.equals(Constants.KEY_AUTH_ERROR)){
                    mActivity.logOutUser();
                }
            }
        });
    }
}
