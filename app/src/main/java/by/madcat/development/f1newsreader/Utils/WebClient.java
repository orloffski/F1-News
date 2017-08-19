package by.madcat.development.f1newsreader.Utils;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url)
    {
        view.loadUrl("javascript:document.body.innerHTML = document.body.innerHTML.replace(/<a.*href=/gi,'<a href=\"#\" _url=');");
    }
}