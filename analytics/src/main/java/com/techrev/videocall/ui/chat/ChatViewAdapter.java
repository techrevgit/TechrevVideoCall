package com.techrev.videocall.ui.chat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.techrev.videocall.R;
import com.techrev.videocall.network.NetworkInterface;
import com.techrev.videocall.network.RetrofitNetworkClass;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Retrofit;

public class ChatViewAdapter extends ArrayAdapter implements ActivityCompat.OnRequestPermissionsResultCallback {

    Activity mContext;
    ArrayList<ChatDataModel.ChatData> dataModel;
    String userID;
    private static final String TAG = "ChatViewAdapter";
    private ArrayList elementList;

    public static String BASE_URL_VAL = "https://apias.digitalnotarize.com/api/";

    LinearLayout ll_section_from, ll_section_to, ll_section_to_text, ll_section_from_file, ll_section_to_file;
    TextView tv_chat_from_content, tv_chat_to_content, tv_chat_from_name, tv_chat_from_date,
            tv_chat_to_name, tv_chat_to_date, tv_from_file_name, tv_to_file_name;
    ImageView iv_chat_option, iv_file_from_download, iv_file_to_download;
    RetrofitNetworkClass networkClass;
    Retrofit retrofitLocal;
    NetworkInterface serviceLocal;
    private String authToken;
    private String meeting_ID;
    private int mPosition;
    private static final int FILE_PERMISSION_CODE = 1001;
    ProgressBar pb;
    Dialog dialog;
    int downloadedSize = 0;
    int totalSize = 0;

    //private PDFView pdfPreview;
    private ImageView imagePreview;
    private AlertDialog previewAttachmentAlertDialog;
    private AlertDialog.Builder builder;

    ProgressDialog progressDialog;

    private ChatMessageInterface messageInterface;
    private WebView pdfWebview = null;

    //String download_file_path = "https://assets.ctfassets.net/l3l0sjr15nav/29D2yYGKlHNm0fB2YM1uW4/8e638080a0603252b1a50f35ae8762fd/Get_Started_With_Smallpdf.pdf";
    //String download_file_path = "https://coderzheaven.com/sample_folder/sample_file.png";
    //String download_file_path = "https://apias.digitalnotarize.com/api/downloadDewFile?DocId=cb48051d1a1e36617c450c083edab1ee.pdf";

    public ChatViewAdapter(Activity context , ArrayList<ChatDataModel.ChatData> dataList
            , String userID, String meeting_ID, String authToken){
        super(context, R.layout.chat_view , dataList.size());
        this.mContext = context;
        this.dataModel = dataList;
        this.userID = userID;
        this.meeting_ID = meeting_ID;
        this.authToken = authToken;
        this.elementList = new ArrayList();
    }

    @Override
    public int getCount() {
        return dataModel.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.chat_view, null);

        mPosition = position;

        ll_section_from = mView.findViewById(R.id.chat_from);
        ll_section_to = mView.findViewById(R.id.chat_to);
        ll_section_to_text = mView.findViewById(R.id.chat_to_text);
        ll_section_to_file = mView.findViewById(R.id.chat_to_file);
        ll_section_from_file = mView.findViewById(R.id.chat_from_file);
        tv_chat_from_content = mView.findViewById(R.id.chat_from_content);
        tv_chat_to_content = mView.findViewById(R.id.chat_to_content);
        tv_chat_from_name = mView.findViewById(R.id.chat_from_name);
        tv_chat_to_name = mView.findViewById(R.id.chat_to_name);
        tv_chat_from_date = mView.findViewById(R.id.chat_from_date);
        tv_chat_to_date = mView.findViewById(R.id.chat_to_date);
        tv_from_file_name = mView.findViewById(R.id.file_from_name);
        tv_to_file_name = mView.findViewById(R.id.file_to_name);
        iv_chat_option = mView.findViewById(R.id.chat_option);
        iv_file_from_download = mView.findViewById(R.id.file_from_download);
        iv_file_to_download = mView.findViewById(R.id.file_to_download);

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        builder = new AlertDialog.Builder(mContext, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);

        iv_file_from_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)
                {
                    // Permission is not granted
                    ActivityCompat.requestPermissions(mContext, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, FILE_PERMISSION_CODE);
                }else {
                    downloadAndPreviewAttachment(dataModel.get(position).getOriginalFileName() , dataModel.get(position).getDocId());
                }
            }
        });

        iv_file_to_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)
                {
                    // Permission is not granted
                    ActivityCompat.requestPermissions(mContext, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, FILE_PERMISSION_CODE);
                }else {
                    downloadAndPreviewAttachment(dataModel.get(position).getOriginalFileName() , dataModel.get(position).getDocId());
                }
            }
        });

        elementList.add(iv_chat_option);

        networkClass = new RetrofitNetworkClass();
        retrofitLocal = networkClass.callingURL();
        serviceLocal = retrofitLocal.create(NetworkInterface.class);

        //Log.e(TAG , "User ID: "+userID);
        //Log.e(TAG , "Sender ID: "+dataModel.get(position).getSenderId());

        String createdAt = dataModel.get(position).getCreatedAt();
        Log.e(TAG , "Chat date before converted: "+createdAt);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        Date date = null;
        try {
            date = sdf.parse(createdAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String chat_date = new SimpleDateFormat("EEE, MM yyyy").format(date);
        Log.e(TAG , "Chat Date : "+chat_date);

        if(userID.equalsIgnoreCase(dataModel.get(position).getSenderId())){
            Log.e(TAG , "Inside if");

            if(dataModel.get(position).getMessage()!=null){
                ll_section_from.setVisibility(View.GONE);
                ll_section_to.setVisibility(View.VISIBLE);
                tv_chat_to_content.setVisibility(View.VISIBLE);
                ll_section_to_file.setVisibility(View.GONE);
                tv_chat_to_content.setText(dataModel.get(position).getMessage());
                tv_chat_to_name.setText("You");
                tv_chat_to_date.setText(""+chat_date);
            }else {
                Log.e(TAG , "My shared file name: "+dataModel.get(position).getOriginalFileName());
                ll_section_from.setVisibility(View.GONE);
                ll_section_to.setVisibility(View.VISIBLE);
                tv_chat_to_content.setVisibility(View.GONE);
                ll_section_to_file.setVisibility(View.VISIBLE);
                tv_to_file_name.setText(dataModel.get(position).getOriginalFileName());
                tv_chat_to_name.setText("You");
                tv_chat_to_date.setText(""+chat_date);
            }
        }else {
            if(dataModel.get(position).getMessage() != null) {
                ll_section_from.setVisibility(View.VISIBLE);
                ll_section_to.setVisibility(View.GONE);
                tv_chat_from_content.setVisibility(View.VISIBLE);
                ll_section_from_file.setVisibility(View.GONE);
                tv_chat_from_name.setText(dataModel.get(position).getSenderName());
                tv_chat_from_content.setText(dataModel.get(position).getMessage());
                tv_chat_from_date.setText(""+chat_date);
            }else {
                ll_section_from.setVisibility(View.VISIBLE);
                ll_section_to.setVisibility(View.GONE);
                tv_chat_from_content.setVisibility(View.GONE);
                ll_section_from_file.setVisibility(View.VISIBLE);
                tv_chat_from_name.setText(dataModel.get(position).getSenderName());
                tv_from_file_name.setText(dataModel.get(position).getOriginalFileName());
                tv_chat_from_date.setText(""+chat_date);
            }
        }

        iv_chat_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showQuickOptions(view , position);
            }
        });

        return mView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if(requestCode == FILE_PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //downloadAndPreviewAttachment(dataModel.get(ada).getOriginalFileName());
            } else {
                Toast.makeText(mContext, "File Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void downloadAndPreviewAttachment(String original_file_name , String docID){

        progressDialog.show();

        Log.e(TAG , "File name to split: "+original_file_name);
        Log.e(TAG , "Doc ID: "+docID);

        String filename = original_file_name.split("\\.")[0];
        String extension = original_file_name.split("\\.")[1];
        String attachment_download_url = BASE_URL_VAL+"api/downloadDewFile?DocId="+docID;

        Log.e(TAG , "Attachment download URL: "+attachment_download_url);

        View mView = mContext.getLayoutInflater().inflate(R.layout.preview_attachments , null);
        previewAttachmentAlertDialog = builder.create();
        previewAttachmentAlertDialog.setView(mView);

        //pdfPreview = mView.findViewById(R.id.pdf_preview);
        pdfWebview = mView.findViewById(R.id.pdfWebview);
        WebSettings webSettings = pdfWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        imagePreview = mView.findViewById(R.id.image_preview);
        ImageView iv_cross = mView.findViewById(R.id.cross_btn);

        iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG , "Closing the preview dialog");
                previewAttachmentAlertDialog.dismiss();
            }
        });

        if(extension.equalsIgnoreCase("pdf")){
            imagePreview.setVisibility(View.GONE);
            //pdfPreview.setVisibility(View.VISIBLE);
            pdfWebview.setVisibility(View.VISIBLE);

            new RetrivePDFfromUrl().execute(attachment_download_url);

        }else {
            imagePreview.setVisibility(View.VISIBLE);
            //pdfPreview.setVisibility(View.GONE);
            pdfWebview.setVisibility(View.GONE);

            GlideUrl glideUrl=new GlideUrl(BASE_URL_VAL+"api/downloadDewFile?DocId="+docID+"."+extension, new Headers() {
                @Override
                public Map<String, String> getHeaders() {
                    return getHeaderWithToken(mContext, authToken);
                }
            });

            Glide.with(mContext)
                    .asBitmap()
                    .load(glideUrl).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.file_error)
                    .into(imagePreview);

            previewAttachmentAlertDialog.show();
            progressDialog.dismiss();
        }
    }

    private static HashMap<String, String> getHeaderWithToken(Context context, String token) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Content-Type", "application/json");
        hashMap.put("Authorization","Basic "+token);
        return hashMap;
    }

    private void showQuickOptions(View v, int x){
        PopupMenu popup = new PopupMenu(mContext, v);
        popup.getMenuInflater().inflate(R.menu.chat_option_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (String.valueOf(item.getItemId())) {
                    case "Copy":
                        //Toast.makeText(mContext, "Selected Item: " +item.getTitle(), Toast.LENGTH_SHORT).show();
                        copyText(tv_chat_to_content.getText().toString());
                        return true;
                    case "Remove":
                        //Toast.makeText(mContext, "Selected Item: " +item.getTitle(), Toast.LENGTH_SHORT).show();
                        if(messageInterface != null){
                            try {
                                messageInterface.deleteMessage(dataModel.get(x).getMessageId());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    private void copyText(String data){
        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("data", data);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(mContext, "Message Copied", Toast.LENGTH_SHORT).show();
    }

    public ChatMessageInterface getMessageInterface() {
        return messageInterface;
    }

    public void setMessageInterface(ChatMessageInterface messageInterface) {
        this.messageInterface = messageInterface;
    }

    // create an async task class for loading pdf file from URL.
    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
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
            //pdfPreview.fromStream(inputStream).load();
            displayPDF(inputStream);
            previewAttachmentAlertDialog.show();
            progressDialog.dismiss();
        }
    }

    private void displayPDF(InputStream pdfInputStream) {
        try {
            // Read InputStream into a byte array
            byte[] pdfBytes = readInputStream(pdfInputStream);

            // Call the displayPDF method with the byte array
            displayPDF(pdfBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayPDF(byte[] pdfByteArray) {
        // Convert byte array to Base64
        String base64 = Base64.encodeToString(pdfByteArray, Base64.DEFAULT);
        // Create a data URI
        String dataUri = "data:application/pdf;base64," + base64;
        // Load the data URI in WebView
        pdfWebview.loadData(dataUri, "application/pdf", "base64");
        // Optional: Set a WebViewClient to handle redirects within the WebView
        pdfWebview.setWebViewClient(new WebViewClient());
    }

    private byte[] readInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        return byteArrayOutputStream.toByteArray();
    }


}
