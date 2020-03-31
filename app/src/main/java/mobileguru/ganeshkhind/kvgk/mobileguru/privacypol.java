
package mobileguru.ganeshkhind.kvgk.mobileguru;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class privacypol extends AppCompatActivity {

    String ShowOrHideWebViewInitialUse = "show";
    private WebView webview ;
    private ProgressBar spinner;
    Snackbar snack;
    View om;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacypol);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        om= findViewById(R.id.webView);
        webview =(WebView)findViewById(R.id.webView);
        webview.setWebViewClient(new CustomWebViewClient());

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        webview.loadUrl("https://mobilegurukv.blogspot.com/2018/08/privacy-policy-for-mobileguru-effective.html");

    }

    // This allows for a splash screen
    // (and hide elements once the page loads)
    private class CustomWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView webview, String url, Bitmap favicon) {

            // only make it invisible the FIRST time the app is run
            if (ShowOrHideWebViewInitialUse.equals("show")) {
                snack = Snackbar.make(om, "Page loading, Please wait.", Snackbar.LENGTH_INDEFINITE); //todo put username
                snack.show();

            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            ShowOrHideWebViewInitialUse = "hide";

            snack = Snackbar.make(om, "Page loaded", Snackbar.LENGTH_SHORT); //todo put username
            snack.show();
            super.onPageFinished(view, url);

        }
    }

    @Override
    public void onBackPressed() {
        if(webview.canGoBack()) {
            webview.goBack();
        } else {
            Intent i=new Intent(privacypol.this,PocketSphinxActivity.class);
            startActivity(i);
        }
    }

}