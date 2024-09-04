package com.example.fakebook;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fakebook.databinding.ActivityMainBinding;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private NodeDataAdapter adapter;
    private List<NodeData> nodeList;

    static {
        System.loadLibrary("fakebook");
    }
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        webView = findViewById(R.id.webView);

        // Initialize the node list and adapter
        nodeList = new ArrayList<>();
        adapter = new NodeDataAdapter(nodeList);

        // Allows javaScript execution in the webView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Add a JavaScript Interface
        webView.addJavascriptInterface(new WebAppInterface(), "Android");

        // Load the login page
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.facebook.com/login");

        // Load the JavaScript after the page is loaded
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url){
                super.onPageFinished(view, url);
                injectJavaScript();
            }
        });
    }

    // Inject JavaScript to interact with HTML content
    private void injectJavaScript() {
        webView.loadUrl("javascript: (function() {" +
                "var nodes = document.getElementsByTagName('*');" +
                "for(var i = 0; i < nodes.length; i++) {" +
                "   var node = nodes[i];" +
                "   Android.showNode(node.tagName, node.innerText);" +
                "}" +
                "})()");
    }

    // JavaScript Interface class to interact with WebView
    private class WebAppInterface{
        @JavascriptInterface
        public void showNode(String tagName, String innerText) {
            // Create a new NodeData object and add it to the list
            NodeData nodeData = new NodeData(tagName, innerText);

            // This method is called from JavaScript with node information
            // You can handle the node data here
            runOnUiThread(() -> {
                // Example: Show each node in a Toast
                nodeList.add(nodeData);
                adapter.notifyItemInserted(nodeList.size() - 1);
                //Log.i("MainActivity", "Going to show something");
                Log.d("NodeData", "Tag: " + tagName + ", Text: " + innerText);
                //Toast.makeText(MainActivity.this, "Tag: " + tagName + "\nText: " + innerText, Toast.LENGTH_SHORT).show();
            });
        }
    }


    // To handle back button press within the WebView
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public native String stringFromJNI();
}