package com.tools.files.myreader.ocr.popups;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tools.files.myreader.R;

public class ExportDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    public EditText edtNameFile;
    private TextView tvCancel;
    private TextView tvAccept;
    private onExportListener onExportListener;

    private String mNameFile;

    public void setOnExportListener(ExportDialog.onExportListener onExportListener) {
        this.onExportListener = onExportListener;
    }

    public ExportDialog(@NonNull Context context, String nameFile) {
        super(context);
        this.mContext = context;
        this.mNameFile = nameFile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_export_pdf);

        funcStyle();
        initViews();
        initEvent();
    }

    private void funcStyle() {
        Window mWindow = getWindow();
        mWindow.setWindowAnimations(R.style.anim_open_dialog);
        mWindow.setGravity(Gravity.CENTER);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mWindow.setLayout(width, height);
        setCancelable(true);
        mWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void initViews() {
        edtNameFile = findViewById(R.id.edt_name_file);
        tvCancel = findViewById(R.id.tv_cancel_update);
        tvAccept = findViewById(R.id.tv_ok_update);

        edtNameFile.requestFocus();
        edtNameFile.setText(mNameFile);
    }

    private void initEvent() {
        tvCancel.setOnClickListener(this);
        tvAccept.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == tvAccept) {
            if (onExportListener!=null) {
                onExportListener.onExport();
                dismiss();
            }
        } else if (v == tvCancel) {
            dismiss();
        }
    }

    public interface onExportListener {
        void onExport();
    }
}
