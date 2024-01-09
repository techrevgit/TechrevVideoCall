package com.techrev.videocall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import com.github.barteksc.pdfviewer.PDFView;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class PreviewRequestDocumentActivity extends AppCompatActivity {

    private static final String TAG = "PreviewRequestDocument";
    private ImageView iv_back;
    private PDFView pdf_viewer;
    private String docID;
    public static String BASE_URL_VAL = "https://apias.digitalnotarize.com/api/";
    private String pdfurl = BASE_URL_VAL + "downloadDewFile?DocId=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*Added By Ruepsh*/
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        /*Added By Ruepsh*/
        setContentView(R.layout.activity_preview_request_document);
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
            Log.d(TAG , "PREVIEW PDF URL: "+pdfurl);
        }
        new RetrievePDFfromUrl().execute(pdfurl);
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