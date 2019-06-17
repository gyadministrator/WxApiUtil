package com.android.wxapi;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.android.wxapi.api.utils.Constants;
import com.android.wxapi.api.utils.Util;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXGameVideoFileObject;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Description: WeChatOpenSdkSample
 * Created by gy(1984629668@qq.com)
 * Created Time on 2019/6/17 8:52
 */
public class WXApi {
    private static WXApi wxApi;
    private final int PERMISSIONS_REQUEST_STORAGE = 1;
    public final int WXSceneSession = SendMessageToWX.Req.WXSceneSession;//会话
    public final int WXSceneTimeline = SendMessageToWX.Req.WXSceneTimeline;//朋友圈
    public final int WXSceneFavorite = SendMessageToWX.Req.WXSceneFavorite;//收藏
    private IWXAPI api;

    private WXApi() {
    }

    /**
     * 实例化对象
     *
     * @return wxapi
     */
    public static WXApi getInstance() {
        if (wxApi == null) {
            synchronized (WXApi.class) {
                if (wxApi == null) {
                    wxApi = new WXApi();
                }
            }
        }
        return wxApi;
    }

    /**
     * 初始化WxAPI
     *
     * @param activity 当前activity
     * @param appId    appId
     * @return IWXAPI
     */
    public IWXAPI initApi(Activity activity, String appId) {
        checkPermission(activity);
        api = WXAPIFactory.createWXAPI(activity, appId, false);
        api.registerApp(appId);
        Constants.APP_ID = appId;
        return api;
    }

    /**
     * 取消注册微信
     */
    public void unRegisterApi() {
        if (api != null) {
            api.unregisterApp();
        }
    }

    /**
     * 检测所需权限
     *
     * @param activity activity
     */
    private void checkPermission(Activity activity) {
        int permissionCheck = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_STORAGE);
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    /**
     * 分享文本
     *
     * @param mTargetScene 场景
     * @param title        标题
     * @param text         文本
     * @param tagName      tagName
     */
    public void shareText(int mTargetScene, String title, String text, String tagName) {
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(text)) {
            return;
        }
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.title = title;
        msg.description = text;
        msg.mediaTagName = tagName;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
        req.scene = mTargetScene;

        if (api != null) {
            api.sendReq(req);
        }
    }

    /**
     * 分享图片
     *
     * @param mTargetScene 场景
     * @param path         路径
     * @param thumbWidth   长度
     * @param thumbHeight  高度
     */
    public void shareImage(int mTargetScene, String path, int thumbWidth, int thumbHeight) {
        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(path);

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap bmp = BitmapFactory.decodeFile(path);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, thumbWidth, thumbHeight, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = mTargetScene;
        if (api != null) {
            api.sendReq(req);
        }
    }

    /**
     * 分享音乐
     *
     * @param mTargetScene 场景
     * @param musicUrl     音乐地址
     * @param imageUrl     图片地址
     * @param title        标题
     * @param description  描述
     * @param thumbWidth   宽度
     * @param thumbHeight  高度
     */
    public void shareMusic(int mTargetScene, String musicUrl, String imageUrl, String title, String description, int thumbWidth, int thumbHeight) {
        WXMusicObject music = new WXMusicObject();
        music.musicUrl = musicUrl;
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = music;
        msg.title = title;
        msg.description = description;

        Bitmap bmp = BitmapFactory.decodeFile(imageUrl);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, thumbWidth, thumbHeight, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("music");
        req.message = msg;
        req.scene = mTargetScene;
        if (api != null) {
            api.sendReq(req);
        }
    }

    /**
     * 分享视频
     *
     * @param mTargetScene 场景
     * @param videoPath    视频地址
     * @param title        标题
     * @param description  描述
     */
    public void shareVideo(int mTargetScene, String videoPath, String title, String description) {
        final WXGameVideoFileObject gameVideoFileObject = new WXGameVideoFileObject();
        gameVideoFileObject.filePath = videoPath;

        final WXMediaMessage msg = new WXMediaMessage();
        msg.setThumbImage(Util.extractThumbNail(videoPath, 150, 150, true));
        msg.title = title;
        msg.description = description;
        msg.mediaObject = gameVideoFileObject;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("video");
        req.message = msg;
        req.scene = mTargetScene;
        if (api != null) {
            api.sendReq(req);
        }
    }

    /**
     * 分享网页
     *
     * @param mTargetScene 场景
     * @param pageUrl      网页地址
     * @param imageUrl     图片地址
     * @param title        标题
     * @param description  描述
     * @param thumbWidth   宽度
     * @param thumbHeight  高度
     */
    public void sharePage(int mTargetScene, String pageUrl, String imageUrl, String title, String description, int thumbWidth, int thumbHeight) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = pageUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = description;
        Bitmap bmp = BitmapFactory.decodeFile(imageUrl);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, thumbWidth, thumbHeight, true);
        bmp.recycle();
        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("page");
        req.message = msg;
        req.scene = mTargetScene;
        if (api != null) {
            api.sendReq(req);
        }
    }

    public void requestAuth() {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo,snsapi_friend,snsapi_message,snsapi_contact";
        req.state = "none";
        if (api != null) {
            api.sendReq(req);
        }
    }
}
