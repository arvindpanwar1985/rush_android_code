package com.hoffmans.rush.ui.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.hoffmans.rush.R;
import com.hoffmans.rush.ui.fragments.RegisterFragment;

public class TermsPolicyActivity extends BaseActivity {
    private WebView mWebview;
    private ProgressBar progressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_terms_policy, getParentView());
        initToolBar(getString(R.string.str_receipt),true);
        initViews();
        initListeners();
        boolean isTerms=getIntent().getBooleanExtra(RegisterFragment.KEY_TERMS,false);
        if(isTerms){
            initToolBar(getString(R.string.terms_condition),true);
        }else{
            initToolBar(getString(R.string.privacy_policy),true);
        }
        String termsPolicyUrl=getIntent().getStringExtra(RegisterFragment.KEY_URL);
        if(termsPolicyUrl!=null && !TextUtils.isEmpty(termsPolicyUrl)){
            loadWebview(termsPolicyUrl);
        }
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black);


    }

    @Override
    protected void initViews() {
        mWebview=(WebView)findViewById(R.id.webview);
        progressBar=(ProgressBar)findViewById(R.id.progress_bar);
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initManagers() {

    }

    private void loadWebview(String termsPolicyUrl){
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.setWebViewClient(new AppWebViewClients(progressBar));
        mWebview.loadUrl(termsPolicyUrl);
    }

    @Override
    public void onBackPressed() {
       this.finish();
    }



    public class AppWebViewClients extends WebViewClient {
        private ProgressBar progressBar;

        public AppWebViewClients(ProgressBar progressBar) {
            this.progressBar=progressBar;
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        public boolean shouldOverrideUrlLoading (WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }
}
