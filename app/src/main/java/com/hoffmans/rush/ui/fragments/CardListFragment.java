package com.hoffmans.rush.ui.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.hoffmans.rush.R;
import com.hoffmans.rush.bean.BaseBean;
import com.hoffmans.rush.bean.CardListBean;
import com.hoffmans.rush.http.request.PaymentRequest;
import com.hoffmans.rush.listners.ApiCallback;
import com.hoffmans.rush.listners.OnCardClicked;
import com.hoffmans.rush.model.CardData;
import com.hoffmans.rush.ui.activities.AddCardActivity;
import com.hoffmans.rush.ui.activities.ConfirmServiceActivity;
import com.hoffmans.rush.ui.adapters.CardListAdapter;
import com.hoffmans.rush.utils.Constants;
import com.hoffmans.rush.utils.Progress;

import java.util.ArrayList;
import java.util.HashMap;

import static com.hoffmans.rush.ui.activities.AddCardActivity.REQUEST_ADD_CARD;


public class CardListFragment extends BaseFragment implements View.OnClickListener,OnCardClicked {

    private static final String ARG_CARD_SELECTABLE      ="is_card_selectable";
    private static final String KEY_PAYMENT_METHOD_TOKEN ="payment_method_token";
    private boolean isCardSelectable;
    private View view;
    private ArrayList<CardData> cardDataList;
    private RecyclerView recyclerCardList;
    private ImageButton btnAddCard;
    private CardListAdapter adapter;
    public CardListFragment() {
        // Required empty public constructor
    }

    public static CardListFragment newInstance(boolean isCardSelectable, String param2) {
        CardListFragment fragment = new CardListFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_CARD_SELECTABLE, isCardSelectable);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isCardSelectable = getArguments().getBoolean(ARG_CARD_SELECTABLE);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view==null) {
            mActivity.initToolBar("",true,false);
            view = inflater.inflate(R.layout.fragment_card_list, container, false);
            initViews(view);
            initListeners();
            getCarList();
        }
        return view;
    }



    @Override
    protected void initViews(View view) {

        recyclerCardList=(RecyclerView)view.findViewById(R.id.cardListRecycler);
        LinearLayoutManager llm = new LinearLayoutManager(mActivity);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerCardList.setLayoutManager(llm);
        btnAddCard=(ImageButton)view.findViewById(R.id.imgAddCard);
    }


    @Override
    protected void initListeners() {
        btnAddCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.imgAddCard:
                Intent addCardIntent=new Intent(mActivity, AddCardActivity.class);
                startActivityForResult(addCardIntent,AddCardActivity.REQUEST_ADD_CARD);
                break;
        }
    }


    /**
     * get the  saved card list
     */
    private void getCarList(){
        Progress.showprogress(mActivity,"Loading cards..",false);
        String authToken=appPreference.getUserDetails().getToken();
        PaymentRequest request=new PaymentRequest();
        request.getPaymentCardList(authToken, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                Progress.dismissProgress();
                CardListBean cardListBean=(CardListBean)body;
                if(cardListBean!=null && cardListBean.getCards().size()!=0) {
                    cardDataList =cardListBean.getCards();
                    setCardAdapter(cardDataList);

                }
            }
            @Override
            public void onRequestFailed(String message) {
                Progress.dismissProgress();
                mActivity.showSnackbar(message,0);
                if(message.equals(Constants.AUTH_ERROR)){
                    mActivity.logOutUser();
                }
            }
        });
    }

    private void setCardAdapter(ArrayList<CardData> cardDataList ){
        boolean showDelete=(isCardSelectable)?false:true;
        adapter=new CardListAdapter(mActivity,cardDataList,this,showDelete);
        recyclerCardList.setAdapter(adapter);
    }


    @Override
    public void onitemclicked(View view, int position) {
        if(cardDataList!=null &&!isCardSelectable){
            CardData cardData=cardDataList.get(position);
            showDialogDefaultCard(cardData.getToken());
        }
    }

    @Override
    public void onCardDelet(View view, int position) {

        if(cardDataList!=null &&!isCardSelectable){
            CardData cardData=cardDataList.get(position);
            showDialog(cardData.getToken());
        }
    }

    @Override
    public void oncardSelected(View view, int position) {
        if(cardDataList!=null && isCardSelectable){
            CardData selectCard=cardDataList.get(position);
            Intent intent=new Intent();
            intent.putExtra(ConfirmServiceActivity.KEY_CARD_DATA,selectCard);
            getActivity().setResult(Activity.RESULT_OK,intent);
            mActivity.finish();
        }
    }



    /**
     * popup for deleting a card
     */
    private void showDialog(final String payment_token){
        try {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
            builder.setTitle(R.string.app_name)
                    .setMessage(R.string.str_delete_Card)
                    .setCancelable(false)
                    .setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();

                        }
                    })
                    .setPositiveButton(getString(R.string.str_delete), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            deleteCard(payment_token);
                        }

                    }).create().show();
        }catch (Exception e){

        }
    }



    /**
     * popup for deleting a card
     */
    private void showDialogDefaultCard(final String token){
        try {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mActivity);
            builder.setTitle(R.string.app_name)
                    .setMessage(R.string.str_default_Card)
                    .setCancelable(false)
                    .setNegativeButton(getString(R.string.str_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();

                        }
                    })
                    .setPositiveButton(getString(R.string.str_mark_default), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            defaultCard(token);
                        }

                    }).create().show();
        }catch (Exception e){

        }
    }

    /**
     * delete the selected card
     * @param token token linked to card added previously
     */
    private void deleteCard(String token){
        HashMap<String ,String> params=new HashMap<>();
        params.put(KEY_PAYMENT_METHOD_TOKEN,token);
        Progress.showprogress(mActivity,getString(R.string.progress_loading),false);
        PaymentRequest deleteCardRequest=new PaymentRequest();
        deleteCardRequest.deleteCard(appPreference.getUserDetails().getToken(), params, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                Progress.dismissProgress();
                CardListBean cardListBean=(CardListBean)body;
                if(cardListBean.getCards().size()>0){
                    cardDataList.clear();
                    cardDataList.addAll(cardListBean.getCards());
                    if(adapter!=null){
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onRequestFailed(String message) {

                mActivity.showSnackbar(message,0);
                Progress.dismissProgress();
                if(message.equals(Constants.AUTH_ERROR)){
                    mActivity.logOutUser();
                }
            }
        });
    }


    /**
     * make card default
     * @param token
     */

    private void defaultCard(String token){
       Progress.showprogress(mActivity,getString(R.string.progress_loading),false);
        PaymentRequest deleteCardRequest=new PaymentRequest();
        deleteCardRequest.defaultCard(appPreference.getUserDetails().getToken(), token, new ApiCallback() {
            @Override
            public void onRequestSuccess(BaseBean body) {
                Progress.dismissProgress();
                CardListBean cardListBean=(CardListBean)body;
                if(cardListBean.getCards().size()>0){
                    cardDataList.clear();
                    cardDataList.addAll(cardListBean.getCards());
                    if(adapter!=null){
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onRequestFailed(String message) {

                mActivity.showSnackbar(message,0);
                Progress.dismissProgress();
                if(message.equals(Constants.AUTH_ERROR)){
                    mActivity.logOutUser();
                }
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_ADD_CARD && resultCode==mActivity.RESULT_OK){
           if(data!=null){
               cardDataList.clear();
               ArrayList<CardData> cards=data.getParcelableArrayListExtra(AddCardActivity.KEY_CARD_DATA);
               boolean showDelete=(isCardSelectable)?false:true;
               cardDataList.clear();
               cardDataList.addAll(cards);
               adapter=new CardListAdapter(mActivity,cardDataList,this,showDelete);
               recyclerCardList.setAdapter(adapter);
               mActivity.showSnackbar("Card added Successfully.",0);
           }
        }
    }
}
