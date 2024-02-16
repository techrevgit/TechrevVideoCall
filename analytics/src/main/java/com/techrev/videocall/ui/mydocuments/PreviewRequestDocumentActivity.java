package com.techrev.videocall.ui.mydocuments;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.github.barteksc.pdfviewer.PDFView;
import com.techrev.videocall.network.NetworkInterface;
import com.techrev.videocall.R;
import com.techrev.videocall.network.RetrofitNetworkClass;

import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PreviewRequestDocumentActivity extends AppCompatActivity {

    private static final String TAG = "PreviewRequestDocument";
    private ImageView iv_back;
    private PDFView pdf_viewer;
    private String docID, authToken;
    //public static String BASE_URL_VAL = "https://apias.digitalnotarize.com/api/";
    public static String BASE_URL_VAL = "https://custtestmobileapi.digitalnotarize.com/";
    private String pdfurl = BASE_URL_VAL + "downloadDewFile?DocId=";
    private WebView pdfWebview = null;
    static RetrofitNetworkClass networkClass = new RetrofitNetworkClass();
    static Retrofit retrofitLocal = networkClass.callingURL();
    static NetworkInterface serviceLocal = retrofitLocal.create(NetworkInterface.class);

    LocalBroadcastManager mLocalBroadcastManager;
    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("com.enotaryoncall.customer.action.close")){
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Added by Rupesh to close activity from service*/
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("com.enotaryoncall.customer.action.close");
        mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, mIntentFilter);
        /*Added by Rupesh to close activity from service*/
        /*Added By Rupesh*/
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        //getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        /*Added By Rupesh*/
        setContentView(R.layout.activity_preview_request_document);
        if (getIntent() != null) {
            authToken = getIntent().getStringExtra("AUTH_TOKEN");
        }
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        pdf_viewer = findViewById(R.id.idPDFView);
        if (getIntent() != null) {
            docID = getIntent().getStringExtra("DOC_ID");
            pdfurl = pdfurl+docID;
            //pdfurl = "https://custtestmobileapi.digitalnotarize.com/api/downloadDewFile?DocId=xCSkdAXSPNC9HY1zn8x8WCY91U5dCOAe"; // For testing
            Log.d(TAG , "PREVIEW PDF URL: "+pdfurl);
        }
        //new RetrievePDFfromUrl().execute(pdfurl);
        pdfWebview = findViewById(R.id.pdfWebview);
        WebSettings webSettings = pdfWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        pdfWebview.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.e("PDF WebView Error: ", error.getDescription().toString());
            }
        });
        try {
            downloadAndPreviewPdf();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void downloadAndPreviewPdf() throws JSONException {

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        dialog.show();

        Call<ResponseBody> responseBodyCall = serviceLocal.downloadPdf(docID);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(dialog!=null){
                    dialog.dismiss();
                }
                Log.d(TAG, "onResponse: Response From Server: "+response.body());
                if(response.isSuccessful() && response.body() != null){
                    try {
                        byte[] downloadedData = response.body().bytes();
                        Log.d(TAG, "onResponse: downloadAndPreviewPdf: PDF Data Length: "+ downloadedData.length);
                        //pdfWebview.loadData(downloadedData, "application/pdf", "UTF-8");
                        String baseUrl = "about:blank"; // or any other suitable base URL
                        //pdfWebview.loadDataWithBaseURL(baseUrl, new String(downloadedData), "application/pdf", "UTF-8", null);
                        pdf_viewer.fromBytes(downloadedData)
                                .defaultPage(0)
                                .enableSwipe(true)
                                .swipeHorizontal(false)
                                .enableAnnotationRendering(true)
                                .load();
                        //displayPDF(downloadedData);
                        //new WebViewLoaderTask(PreviewRequestDocumentActivity.this, pdfWebview).execute(downloadedData);
                    } catch (IOException e) {
                        Log.d(TAG, "onResponse: downloadAndPreviewPdf: Error while displaying the PDF!");
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(dialog!=null){
                    dialog.dismiss();
                }
                Log.d("====onResponse", "Failure" + t.toString());
            }
        });

    }

    private void displayPDF(byte[] pdfByteArray) {
        // Convert byte array to Base64
        String base64 = Base64.encodeToString(pdfByteArray, Base64.DEFAULT);
        // Create a data URI
        String dataUri = "data:application/pdf;base64," + base64;
        Log.d(TAG , "Data URI: "+dataUri);
        // Optional: Set a WebViewClient to handle redirects within the WebView
        pdfWebview.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.e("PDF WebView Error", error.getDescription().toString());
            }
        });
        // Load the data URI in WebView
        pdfWebview.loadData(dataUri, "application/pdf", "base64");

    }

    public class WebViewLoaderTask extends AsyncTask<byte[], Void, File> {
        private static final int REQUEST_EXTERNAL_STORAGE = 1;
        private Context mContext;
        private WebView mWebView;

        public WebViewLoaderTask(Context context, WebView webView) {
            mContext = context;
            mWebView = webView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Check for external storage permission before executing the task
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted, request it
                ActivityCompat.requestPermissions((Activity) mContext,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_EXTERNAL_STORAGE);
            }
        }

        @Override
        protected File doInBackground(byte[]... params) {
            byte[] byteArray = params[0];
            InputStream inputStream = new ByteArrayInputStream(byteArray);
            File file = null;
            FileOutputStream outputStream = null;

            try {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    /*File directory = new File(mContext.getExternalFilesDir(null), "Notarization_Docs"); // Specify the folder name
                    if (!directory.exists()) {
                        if (!directory.mkdirs()) {
                            throw new IOException("Failed to create directory: " + directory.getAbsolutePath());
                        }
                    }*/
                    File fileNew = new File(Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                            + "/Notarization_Doc.pdf"); // Create a file object
                    outputStream = new FileOutputStream(fileNew);

                    // Write InputStream data to the file
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, length);
                    }
                    file = fileNew; // Assign the created file to the result
                } else {
                    // Permission not granted, return null file
                    file = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return file;
        }

        @Override
        protected void onPostExecute(File file) {
            if (file != null) {
                // Load the file into WebView
                String fileUrl = "file://" + file.getAbsolutePath();
                Log.d("WebViewLoaderTask", "Loading file: " + fileUrl);
                pdfWebview.loadUrl("https://docs.google.com/gview?embedded=true&url="+fileUrl);
            } else {
                // Handle permission denied
                Log.d(TAG , "Error while rendering the pdf");
            }
        }
    }

}