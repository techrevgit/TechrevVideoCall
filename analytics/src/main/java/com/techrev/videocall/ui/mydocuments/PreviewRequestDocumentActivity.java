package com.techrev.videocall.ui.mydocuments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.pdfview.PDFView;
import com.techrev.videocall.R;
import com.techrev.videocall.network.NetworkInterface;
import com.techrev.videocall.network.RetrofitNetworkClass;
import com.techrev.videocall.utils.Constants;

import org.json.JSONException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PreviewRequestDocumentActivity extends AppCompatActivity {

    private static final String TAG = "PreviewRequestDocument";
    private ImageView iv_back;
    /*private PDFView pdf_viewer;*/
    private String docID, authToken;
    //public static String BASE_URL_VAL = "https://apias.digitalnotarize.com/api/";
    public static String BASE_URL_VAL = Constants.API_BASE_URL + "api/";
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

    byte[] downloadedData;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private PDFView mPdfView;
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
        //pdf_viewer = findViewById(R.id.idPDFView);
        if (getIntent() != null) {
            docID = getIntent().getStringExtra("DOC_ID");
            pdfurl = pdfurl+docID;
            //pdfurl = "https://custtestmobileapi.digitalnotarize.com/api/downloadDewFile?DocId=xCSkdAXSPNC9HY1zn8x8WCY91U5dCOAe"; // For testing
            Log.d(TAG , "PREVIEW PDF URL: "+pdfurl);
        }
        //new RetrievePDFfromUrl().execute(pdfurl);
        pdfWebview = findViewById(R.id.pdfWebview);
        mPdfView = findViewById(R.id.pdf_view);
        // Show loader while PDF is loading
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        //progressDialog.show();
        WebSettings webSettings = pdfWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        pdfWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                // Prevent URL loading
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Hide loader once PDF is loaded
                progressDialog.dismiss();
            }
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
        //loadPdfUrl();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, save the PDF file
                savePdfToFile(downloadedData); // Call your savePdfToFile method here
            } else {
                // Permission denied, show a message or handle accordingly
                Log.d(TAG, "onRequestPermissionsResult: Permission Denied");
            }
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
                dialog.dismiss();
                Log.d(TAG, "onResponse: Response From Server: "+response.body());
                if(response.isSuccessful() && response.body() != null){
                    try {
                        downloadedData = response.body().bytes();
                        Log.d(TAG, "onResponse: downloadAndPreviewPdf: PDF Data Length: "+ downloadedData.length);
                        savePdfToFile(downloadedData);
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

    private void savePdfToFile(byte[] bytes) {
        Log.d(TAG, "savePdfToFile: Saving file...");

        /*New Code*/
        File folder = new File(Constants.getPathPDF(PreviewRequestDocumentActivity.this));
        folder.mkdirs();
        File pdfFile = new File(folder, "Notarization_Doc.pdf");
        try {
            pdfFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(pdfFile));
            bos.write(bytes);
            bos.flush();
            bos.close();
            loadPdfFile(pdfFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*New Code*/

    }

    private void loadPdfFile(File file) {
        if (file.exists()) {
            Log.d(TAG, "loadPdfFile: Loading file...");
            // Create a file URL from the file path
            String fileUrl = "file://" + file.getAbsolutePath();
            // Load the file into WebView
            //pdfWebview.loadUrl(fileUrl);
            //mPdfView.fromFile(file.getAbsolutePath()).show();
            mPdfView.fromFile(file).show();
            //mPdfView.fromAsset("sample.pdf").show();
        } else {
            Log.e(TAG, "loadPdfFile: File does not exist");
        }
    }


}