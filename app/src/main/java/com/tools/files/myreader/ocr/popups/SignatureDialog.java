package com.tools.files.myreader.ocr.popups;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.raed.drawingview.DrawingView;
import com.raed.drawingview.brushes.BrushSettings;
import com.tools.files.myreader.R;

public class SignatureDialog extends Dialog implements View.OnClickListener, View.OnTouchListener {

    private Activity mContext;
    private TextView tvCancel;
    private TextView tvAccept;
    private TextView tvSignHint;

    private ImageView imgColor;
    private ImageView imgErase;
    private ImageView imgListSign;
    private ImageView imgColorOrgan;
    private ImageView imgColorRed;
    private ImageView imgColorBlue;
    private ImageView imgColorBlack;
    private ImageView imgColorWhite;

    private LinearLayout lnColorSign;
    private BrushSettings brushSettings;
    private DrawingView mDrawingView;
    private onDrawListener onDrawListener;

    private boolean isClickColor = true;

    public void setOnExportListener(onDrawListener onDrawListener) {
        this.onDrawListener = onDrawListener;
    }

    public SignatureDialog(@NonNull Activity context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_signature);

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
        tvCancel = findViewById(R.id.tv_signal_cancel);
        tvAccept = findViewById(R.id.tv_signal_confirm);
        tvSignHint = findViewById(R.id.tv_sign_hint);
        imgColor = findViewById(R.id.iv_sign_color);
        imgErase = findViewById(R.id.iv_erase);
        imgListSign = findViewById(R.id.iv_sign_list);
        imgColorOrgan = findViewById(R.id.iv_sign_color_orange);
        imgColorRed = findViewById(R.id.iv_sign_color_red);
        imgColorBlue = findViewById(R.id.iv_sign_color_blue);
        imgColorBlack = findViewById(R.id.iv_sign_color_black);
        imgColorWhite = findViewById(R.id.iv_sign_color_white);
        lnColorSign = findViewById(R.id.ln_sign_color);
        mDrawingView = findViewById(R.id.drawing_view);

        brushSettings = mDrawingView.getBrushSettings();
        brushSettings.setSelectedBrush(0);
        brushSettings.setSelectedBrushSize(0.1f);
    }

    private void initEvent() {
        tvCancel.setOnClickListener(this);
        tvAccept.setOnClickListener(this);
        imgColor.setOnClickListener(this);
        imgErase.setOnClickListener(this);
        imgListSign.setOnClickListener(this);
        imgColorOrgan.setOnClickListener(this);
        imgColorRed.setOnClickListener(this);
        imgColorBlue.setOnClickListener(this);
        imgColorBlack.setOnClickListener(this);
        imgColorWhite.setOnClickListener(this);
        mDrawingView.setOnTouchListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == tvAccept) {
            if (onDrawListener != null) {
                onDrawListener.onDraw(mDrawingView.exportDrawingWithoutBackground());
                dismiss();
            }
        } else if (v == tvCancel) {
            dismiss();

        } else if (v == imgColor) {
            if (isClickColor) {
                lnColorSign.setVisibility(View.VISIBLE);
                isClickColor = false;
            } else {
                lnColorSign.setVisibility(View.GONE);
                isClickColor = true;
            }

        } else if (v == imgErase) {

        } else if (v == imgListSign) {

        } else if (v == imgColorOrgan) {
            brushSettings.setColor(ResourcesCompat.getColor(mContext.getResources(), R.color.sign_orange, null));
            lnColorSign.setVisibility(View.GONE);
            isClickColor = true;

        } else if (v == imgColorRed) {
            brushSettings.setColor(ResourcesCompat.getColor(mContext.getResources(), R.color.sign_red, null));
            lnColorSign.setVisibility(View.GONE);
            isClickColor = true;

        } else if (v == imgColorBlue) {
            brushSettings.setColor(ResourcesCompat.getColor(mContext.getResources(), R.color.sign_blue, null));
            lnColorSign.setVisibility(View.GONE);
            isClickColor = true;

        } else if (v == imgColorBlack) {
            brushSettings.setColor(ResourcesCompat.getColor(mContext.getResources(), R.color.sign_black, null));
            lnColorSign.setVisibility(View.GONE);
            isClickColor = true;

        } else if (v == imgColorWhite) {
            brushSettings.setColor(ResourcesCompat.getColor(mContext.getResources(), R.color.color_text_white, null));
            lnColorSign.setVisibility(View.GONE);
            isClickColor = true;

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return onTouchDraw(mContext, v, event);
    }

    public boolean onTouchDraw(Activity activity, View view, MotionEvent motionEvent) {
        tvSignHint.setVisibility(View.GONE);
        lnColorSign.setVisibility(View.GONE);
        if (motionEvent.getAction() == 1) {
            tvAccept.setTextColor(ResourcesCompat.getColor(activity.getResources(), R.color.blue, null));
            tvAccept.setEnabled(true);
        }
        return false;
    }


    public interface onDrawListener {
        void onDraw(Bitmap bitmap);
    }
}
