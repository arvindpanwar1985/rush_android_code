package com.hoffmans.rush.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hoffmans.rush.R;
import com.hoffmans.rush.utils.Constants;

/**
 * Created by arvind on 22/5/17.
 */

public class FragmentAddComment extends BaseFragment implements View.OnClickListener{

    private Button mBtnCancel;
    private Button mBtnSubmit;
    private TextView mTxtComment;
    private String mStrComment;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View View=inflater.inflate(R.layout.fragment_add_comment,container,false);
        initViews(View);
        initListeners();
        return View;
    }

    public static FragmentAddComment newInstance(String comment){
        FragmentAddComment fragmentAddComment=new FragmentAddComment();
        Bundle args=new Bundle();
        args.putString(Constants.KEY_COMMENT,comment);
        fragmentAddComment.setArguments(args);
        return fragmentAddComment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            mStrComment=getArguments().getString(Constants.KEY_COMMENT);

        }
    }

    @Override
    protected void initViews(View view) {

        mBtnCancel=(Button)view.findViewById(R.id.btnCancel);
        mBtnSubmit=(Button)view.findViewById(R.id.btnSubmitComment);
        mTxtComment=(TextView)view.findViewById(R.id.text_add_comment);
        mTxtComment.setText(mStrComment.toString());


    }

    @Override
    protected void initListeners() {

        mBtnCancel.setOnClickListener(this);
        mBtnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.btnCancel){

            Intent intent=new Intent();
            intent.putExtra(Constants.KEY_COMMENT,"");
            getActivity().setResult(mActivity.RESULT_OK,intent);
            mActivity.finish();

        }
        if(view.getId()==R.id.btnSubmitComment){
            String msgComment=mTxtComment.getText().toString();
            if(mTxtComment.getText().length()==0){
                mTxtComment.setError(getResources().getString(R.string.error_empty_comment));
                mTxtComment.setFocusable(true);
            }else{
                Intent intent=new Intent();
                intent.putExtra(Constants.KEY_COMMENT,msgComment);
                getActivity().setResult(mActivity.RESULT_OK,intent);
                mActivity.finish();
            }
        }
    }
}
