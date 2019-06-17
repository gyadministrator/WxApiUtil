package com.android.wxapiutil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.wxapi.WXApi;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_title;
    private EditText et_text;
    private TextView tv_share;
    private WXApi wxApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        wxApi = WXApi.getInstance();
        wxApi.initApi(this, "wxd930ea5d5a258f4f");
    }

    private void initView() {
        et_title = findViewById(R.id.et_title);
        et_text = findViewById(R.id.et_text);
        tv_share = findViewById(R.id.tv_share);
        tv_share.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String title = et_title.getText().toString();
        String text = et_text.getText().toString();
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(text)) {
            return;
        }
        wxApi.shareText(wxApi.WXSceneSession, title, text, "123");
    }
}
