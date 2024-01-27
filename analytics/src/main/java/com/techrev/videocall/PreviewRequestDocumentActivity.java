package com.techrev.videocall;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Added By Rupesh*/
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
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

    // create an async task class for loading pdf file from URL.
    public class RetrievePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            // we are using inputstream
            // for getting out PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            pdf_viewer.fromStream(inputStream).load();
        }
    }

}