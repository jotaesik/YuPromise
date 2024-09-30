package com.example.last;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {

    public WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_search);

        webview = findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.addJavascriptInterface(new BridgeInterface(), "Android");
        webview.setWebViewClient(new WebViewClient(){

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // SSL 에러가 발생해도 계속 진행
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url){
                webview.loadUrl("javascript:sample2_execDaumPostcode();");
            }
        });

        webview.setWebChromeClient(new WebChromeClient() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                request.grant(request.getResources());
            }
        });

        //최초실행
        webview.loadUrl("");
    }

    private class BridgeInterface{
        @JavascriptInterface
        public void processDATA(String data){
            Intent intent = new Intent();
            intent.putExtra("data", data);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}