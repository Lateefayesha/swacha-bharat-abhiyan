package com.appynitty.swachbharatabhiyanlibrary.utils;

import android.content.Context;
import android.webkit.WebView;


/**
 * Created by Ayan Dey on 26/10/18.
 */
public class WebviewInitialize{

    private Context mContext;
    private WebView webView;

    public WebviewInitialize(Context mContext, WebView webView) {
        this.mContext = mContext;
        this.webView = webView;
    }

    public void InitiateDefaultWebview(String url){

        webView.setWebViewClient(new InternalWebviewClient(mContext, true));
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);

        webView.loadUrl(url);
    }

    public boolean webviewGoBack() {

        if(webView.canGoBack()){
            webView.goBack();
            return true;
        }else
            return false;
    }
}


