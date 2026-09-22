package com.whitesr09.charecterbuild;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build;
import android.view.Window;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final int FILE_CHOOSER_REQUEST = 2401;
    private WebView webView;
    private ValueCallback<Uri[]> fileChooserCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.setStatusBarColor(Color.rgb(5, 6, 10));
        window.setNavigationBarColor(Color.rgb(5, 6, 10));

        webView = new WebView(this);
        webView.setBackgroundColor(Color.rgb(5, 6, 10));
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        setContentView(webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setLoadWithOverviewMode(false);
        settings.setUseWideViewPort(false);
        float fontScale = getResources().getConfiguration().fontScale;
        int textZoom = Math.max(90, Math.min(135, Math.round(fontScale * 100f)));
        settings.setTextZoom(textZoom);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setMediaPlaybackRequiresUserGesture(true);

        webView.addJavascriptInterface(new NativeBridge(), "Android");
        webView.setWebViewClient(new WebViewClient());

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(
                    WebView webView,
                    ValueCallback<Uri[]> filePathCallback,
                    FileChooserParams fileChooserParams
            ) {
                if (fileChooserCallback != null) {
                    fileChooserCallback.onReceiveValue(null);
                }

                fileChooserCallback = filePathCallback;

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");

                try {
                    startActivityForResult(intent, FILE_CHOOSER_REQUEST);
                    return true;
                } catch (Exception e) {
                    fileChooserCallback = null;
                    Toast.makeText(MainActivity.this, "Unable to open image picker", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });

        webView.loadUrl("file:///android_asset/index.html");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_CHOOSER_REQUEST) {
            if (fileChooserCallback != null) {
                Uri[] results = null;
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    results = new Uri[]{data.getData()};
                }
                fileChooserCallback.onReceiveValue(results);
                fileChooserCallback = null;
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (webView != null) {
            webView.evaluateJavascript(
                    "(function(){return (window.handleNativeBack && window.handleNativeBack()) ? 'handled' : 'not-handled';})()",
                    value -> {
                        if (!"\"handled\"".equals(value)) {
                            if (webView.canGoBack()) {
                                webView.goBack();
                            } else {
                                finish();
                            }
                        }
                    }
            );
        } else {
            super.onBackPressed();
        }
    }

    public class NativeBridge {
        @JavascriptInterface
        public void copy(String text) {
            ClipboardManager clipboard =
                    (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(ClipData.newPlainText("Character prompt", text));
            runOnUiThread(() ->
                    Toast.makeText(MainActivity.this, "Prompt copied", Toast.LENGTH_SHORT).show()
            );
        }

        @JavascriptInterface
        public void share(String text) {
            runOnUiThread(() -> {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(intent, "Share character prompt"));
            });
        }

        @JavascriptInterface
        public void toast(String text) {
            runOnUiThread(() ->
                    Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show()
            );
        }

        @JavascriptInterface
        public String appVersion() {
            return "1.7.0";
        }

        @JavascriptInterface
        public void haptic(int kind) {
            runOnUiThread(() -> {
                if (webView == null) return;
                int feedback = HapticFeedbackConstants.KEYBOARD_TAP;
                if (kind == 1) feedback = HapticFeedbackConstants.VIRTUAL_KEY;
                if (kind >= 2) feedback = HapticFeedbackConstants.LONG_PRESS;
                webView.performHapticFeedback(feedback);
            });
        }

        @JavascriptInterface
        public void openUrl(String url) {
            runOnUiThread(() -> {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Unable to open link", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @JavascriptInterface
        public void setSystemBars(boolean lightMode) {
            runOnUiThread(() -> {
                Window window = getWindow();
                if (lightMode) {
                    window.setStatusBarColor(Color.rgb(248, 248, 250));
                    window.setNavigationBarColor(Color.rgb(248, 248, 250));
                    int flags = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
                    }
                    window.getDecorView().setSystemUiVisibility(flags);
                    if (webView != null) webView.setBackgroundColor(Color.rgb(248, 248, 250));
                } else {
                    window.setStatusBarColor(Color.rgb(5, 6, 10));
                    window.setNavigationBarColor(Color.rgb(5, 6, 10));
                    window.getDecorView().setSystemUiVisibility(0);
                    if (webView != null) webView.setBackgroundColor(Color.rgb(5, 6, 10));
                }
            });
        }
    }
}
