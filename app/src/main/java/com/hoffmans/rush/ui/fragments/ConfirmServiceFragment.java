package com.hoffmans.rush.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hoffmans.rush.R;
import com.hoffmans.rush.model.CardData;
import com.hoffmans.rush.model.Estimate;
import com.hoffmans.rush.model.PickDropAddress;
import com.hoffmans.rush.model.Service;
import com.hoffmans.rush.ui.activities.CardListActivity;
import com.hoffmans.rush.ui.activities.ConfirmServiceActivity;
import com.hoffmans.rush.ui.adapters.LoadAddressAdapter;
import com.hoffmans.rush.utils.Constants;

import java.util.ArrayList;
import java.util.List;


public class ConfirmServiceFragment extends BaseFragment implements View.OnClickListener {


    private RelativeLayout viewCardDetails;
    private static final int REQUEST_SELECT_CARD=108;
    private CardData mSelectedCard;
    private ImageView imgCardType;
    private TextView txtCardData,txtCurrency,txtAmount,txtEstimatedTime;

    private Estimate mesTimatedData;
    private CardData defaultCardData;
    private Service mServiceParams;
    private RecyclerView recyclerView;
    private LoadAddressAdapter addressAdapter;
    private List<PickDropAddress>listAddressData=new ArrayList<>();

    public ConfirmServiceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfirmServiceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfirmServiceFragment newInstance(Estimate param1, CardData param2, Service param3) {
        ConfirmServiceFragment fragment = new ConfirmServiceFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.KEY_ESTIMATE_DATA, param1);
        args.putParcelable(Constants.KEY_CARD_DATA, param2);
        args.putParcelable(Constants.KEY_PARAM_DATA, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            mesTimatedData =getArguments().getParcelable(Constants.KEY_ESTIMATE_DATA);
            defaultCardData=getArguments().getParcelable(Constants.KEY_CARD_DATA);
            mServiceParams =getArguments().getParcelable(Constants.KEY_PARAM_DATA);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_confirm, container, false);
        initViews(view);
        initListeners();
        return view;
    }



    private void onCardSelected(CardData selectedCard){
        mSelectedCard=selectedCard;
        if(selectedCard!=null){
            try{
                txtCardData.setText("***********"+selectedCard.getLast4());
                Glide.with(mActivity).load(selectedCard.getImageUrl()).into(imgCardType);
            }catch (NullPointerException e){

            }
        }

    }

    @Override
    protected void initViews(View view) {

        recyclerView            =(RecyclerView)view.findViewById(R.id.addressRecycler);
        LinearLayoutManager llm = new LinearLayoutManager(mActivity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        viewCardDetails=(RelativeLayout)view.findViewById(R.id.viewCardDetails);
        imgCardType=(ImageView)view.findViewById(R.id.imgCardType);
        txtCardData=(TextView)view.findViewById(R.id.txtCardNumber);
        txtCurrency=(TextView)view.findViewById(R.id.txtCurrency);
        txtAmount=(TextView)view.findViewById(R.id.txtAmount);
        txtEstimatedTime=(TextView)view.findViewById(R.id.txtEstimatedTime);
        try {
            setEstimatedPrice();
            setEstimatedTime();
            setDefaultCarData();
            setPickDropUi();
        }catch (NullPointerException e){

        }





    }

    @Override
    protected void initListeners() {

        viewCardDetails.setOnClickListener(this);
    }


    private void setEstimatedPrice() throws NullPointerException{
        if(mesTimatedData!=null){
            txtCurrency.setText(mesTimatedData.getSymbol());
            if(mesTimatedData.getApproxConvertedAmount()!=null){
                txtAmount.setText(mesTimatedData.getApproxConvertedAmount().toString());
            }
        }
    }

    private void setEstimatedTime() throws NullPointerException{
        if(mesTimatedData!=null){
            txtEstimatedTime.setText(mesTimatedData.getApproxTime());
        }
    }


    private void setDefaultCarData()throws NullPointerException{
        mSelectedCard=defaultCardData;
        if(mSelectedCard!=null){
            try{
                txtCardData.setText("***********"+mSelectedCard.getLast4());
                Glide.with(mActivity).load(mSelectedCard.getImageUrl()).into(imgCardType);
            }catch (NullPointerException e){

            }
        }
    }

    private void setPickDropUi()throws NullPointerException{
        listAddressData.clear();
        listAddressData.add(mServiceParams.getPick_address());
        listAddressData.addAll(mServiceParams.getDrop_addresses());
        if(mServiceParams!=null) {
            addressAdapter = new LoadAddressAdapter(mActivity, listAddressData, null);
            recyclerView.setAdapter(addressAdapter);
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.viewCardDetails:
                Intent cardListIntent=new Intent(mActivity, CardListActivity.class);
                cardListIntent.putExtra(Constants.KEY_IS_CARD_SELECTABLE,true);
                startActivityForResult(cardListIntent,REQUEST_SELECT_CARD);

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_SELECT_CARD && resultCode==mActivity.RESULT_OK){
            if(data!=null){
                CardData selectedCard=data.getParcelableExtra(ConfirmServiceActivity.KEY_CARD_DATA);
                onCardSelected(selectedCard);
            }
        }
    }
}
