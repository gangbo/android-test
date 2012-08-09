package gangbo.contact;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.support.v4.app.DialogFragment;


public class WebViewActivity extends Activity {
	WebView mWebView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);

		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		mWebView.setWebViewClient(new HelloWebViewClient());
		mWebView.loadUrl("http://192.168.1.4/html5/test.php");
	}

	@Override
	// 设置回退
	// 覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		mWebView = (WebView) findViewById(R.id.webview);
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack(); // goBack()表示返回WebView的上一页面
			return true;
		}
		return false;
	}
	
	//Web视图 
    private class HelloWebViewClient extends WebViewClient {

		@Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) { 
            view.loadUrl(url); 
            Log.d("url====",url);
            return true; 
        } 

		public void onProgressChanged(WebView view, int progress) {
             Log.v("progress==", ">"+progress);
        }
        @Override
        public void onReceivedError(WebView view, int errorCode,
                String description, String failingUrl){
        	// The magic redirect  
            if( "http://HTML5app.com/app/".equals(failingUrl) ) {
                  // main.html is the place we are redirected to by the server if we are online
                  mWebView.loadUrl("http://HTML5app.com/app/main.html");
                  return;    
            }
            else if( "http://HTML5app.com/app/main.html".equals(failingUrl) ) {    
                  // The cache failed – We don't have an offline version to show
         
                  // This code removes the ugly android's "can't open page"
                  // and simply shows a dialog stating we have no network
                  view.loadData("", "text/html", "UTF-8");
                  Log.v("====","no network");
                  //showDialog(DIALOG_NONETWORK);
            }
        	
        }
    } 
   
}
