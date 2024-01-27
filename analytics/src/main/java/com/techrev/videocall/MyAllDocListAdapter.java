package com.techrev.videocall;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class MyAllDocListAdapter extends RecyclerView.Adapter<MyAllDocListAdapter.MyViewHolder> {

    private static final String TAG = "MyAllDocListAdapter";
    private Activity mActivity;
    private List<MyAllDocListModel.AllDocs> mList;
    private List<String> selectedDocIdList;
    public Boolean isSelectAll = false;

    public interface OnDocsSelected {
        public abstract void onDocumentsSelected(List<String> selectedDocIdList);
    }

    private OnDocsSelected onDocsSelected;
    JsonObject obj;
    private String authToken;
    private MyViewHolder mViewHolder;
    private int mPosition;

    public MyAllDocListAdapter (Activity activity , String token, List<MyAllDocListModel.AllDocs> list, OnDocsSelected docsSelected) {
        this.mActivity = activity;
        this.authToken = token;
        this.mList = list;
        this.onDocsSelected = docsSelected;
        //Log.d(TAG, "MyAllDocListAdapter: "+new Gson().toJson(mList));
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_document_item_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Log.d(TAG, "onBindViewHolder: POSITION: "+position+" & DOC NAME: "+mList.get(position).getDocumentName());
        selectedDocIdList = new ArrayList<>();
        MyViewHolder viewHolder = (MyViewHolder) holder;
        mViewHolder = viewHolder;
        mPosition = position;
        viewHolder.tv_document_title.setVisibility(View.GONE);
        viewHolder.cb_document_title.setVisibility(View.VISIBLE);
        viewHolder.cb_document_title.setText(mList.get(position).getDocumentName());
        viewHolder.cb_document_title.setChecked(mList.get(position).isSelected());
        viewHolder.cb_document_title.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                /*obj = new JsonObject();
                if (b) {
                    obj.addProperty("DocId" , mList.get(position).getDocId());
                    selectedDocIdList.add(mList.get(position).getDocId());
                } else {
                    selectedDocIdList.remove(mList.get(position).getDocId());
                }
                Log.d(TAG, "onCheckedChanged: "+selectedDocIdList.size());
                onDocsSelected.onDocumentsSelected(selectedDocIdList);*/
                if (position != RecyclerView.NO_POSITION) {
                    mList.get(position).setSelected(b); // Update selected state of the document

                    if (b) {
                        selectedDocIdList.add(mList.get(position).getDocId());
                    } else {
                        selectedDocIdList.remove(mList.get(position).getDocId());
                    }

                    onDocsSelected.onDocumentsSelected(selectedDocIdList);
                }
            }
        });
        viewHolder.iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreOptionsInBottomSheet(position);
            }
        });

        /*obj = new JsonObject();
        if (isSelectAll) {
            viewHolder.cb_document_title.setChecked(true);
            obj.addProperty("DocId" , mList.get(position).getDocId());
            selectedDocIdList.add(mList.get(position).getDocId());
            Log.d(TAG, "onBindViewHolder: inside if"+selectedDocIdList.size());
            onDocsSelected.onDocumentsSelected(selectedDocIdList);
        } else {
            viewHolder.cb_document_title.setChecked(false);
            selectedDocIdList.remove(mList.get(position).getDocId());
            Log.d(TAG, "onBindViewHolder: inside else"+selectedDocIdList.size());
            onDocsSelected.onDocumentsSelected(selectedDocIdList);
        }*/

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+mList.size());
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_document_title;
        private CheckBox cb_document_title;
        private ImageView iv_more;

        public MyViewHolder(View itemView) {
            super(itemView);
            cb_document_title = itemView.findViewById(R.id.cb_document_title);
            tv_document_title = itemView.findViewById(R.id.tv_document_title);
            iv_more = itemView.findViewById(R.id.iv_more);
        }
    }

    private void showMoreOptionsInBottomSheet(int position) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mActivity);
        bottomSheetDialog.setContentView(R.layout.request_doc_list_more_bottom_sheet);

        TextView tv_document_name = bottomSheetDialog.findViewById(R.id.tv_document_name);
        TextView tv_view_document = bottomSheetDialog.findViewById(R.id.tv_view_document);
        TextView tv_delete_document = bottomSheetDialog.findViewById(R.id.tv_delete_document);
        TextView tv_cancel = bottomSheetDialog.findViewById(R.id.tv_cancel);

        tv_delete_document.setVisibility(View.GONE);
        tv_document_name.setText(mList.get(position).getDocumentName()+".pdf");

        tv_view_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
                Intent it = new Intent(mActivity , PreviewRequestDocumentActivity.class);
                it.putExtra("AUTH_TOKEN" , authToken);
                it.putExtra("DOC_ID" , mList.get(position).getDocId());
                mActivity.startActivity(it);
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.cancel();
            }
        });

        bottomSheetDialog.show();

    }

    public void clearSelectedDocIdList() {
        selectedDocIdList.clear();
    }

    public void isAllDocsSelected(boolean isSelectAll) {
        selectedDocIdList.clear();

        // Iterate through all items in the list
        for (MyAllDocListModel.AllDocs doc : mList) {
            doc.setSelected(isSelectAll); // Update the selected state for each item

            if (isSelectAll) {
                selectedDocIdList.add(doc.getDocId()); // Add all docIds when selecting all
            }
        }

        notifyDataSetChanged(); // Notify adapter that the dataset has changed
        Log.d(TAG, "isAllDocsSelected Selected Doc List: "+selectedDocIdList.size());
        onDocsSelected.onDocumentsSelected(selectedDocIdList);
    }

}
