package com.android.wxapi.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.wxapi.api.utils.Constants;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信客户端回调activity示例
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("WXEntryActivity", "onCreate");
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
        api.registerApp(Constants.APP_ID);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("WXEntryActivity", "onNewIntent");
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq arg0) {
        // TODO Auto-generated method stub
        Log.e("WXEntryActivity", "onReq");
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e("WXEntryActivity", "onResp");
        // TODO Auto-generated method stub
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            // 用户同意
            Log.e("WXEntryActivity", "onResp" + resp.errCode);
            Log.e("WXEntryActivity", "onResp" + resp.errStr);
            Log.e("WXEntryActivity", "onResp" + resp.openId);
        }
    }
}
