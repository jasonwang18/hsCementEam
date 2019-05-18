package com.supcon.mes.module_olxj.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.LogUtil;
import com.supcon.common.view.view.js.BaseBridgeWebViewClient;
import com.supcon.common.view.view.js.BridgeHandler;
import com.supcon.common.view.view.js.BridgeUtil;
import com.supcon.common.view.view.js.BridgeWebView;
import com.supcon.common.view.view.js.CallBackFunction;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.middleware.EamApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.Util;
import com.supcon.mes.module_olxj.R;
import com.supcon.mes.module_olxj.model.bean.OLXJAreaEntity;
import com.supcon.mes.module_olxj.model.bean.OLXJTaskEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by zhangwenshuai1 on 2018/3/12.
 */

public class OLXJTaskListAdapter extends BaseListDataRecyclerViewAdapter<OLXJTaskEntity> {

    private int expandPosition = -1;
    private List<OLXJAreaEntity> mOLXJAreaEntities;
    private boolean map;
    private ViewHolder viewHolder;

    public OLXJTaskListAdapter(Context context) {
        super(context);
    }

    public void setAreaEntities(List<OLXJAreaEntity> olxjAreaEntities) {
        this.mOLXJAreaEntities = olxjAreaEntities;
        notifyDataSetChanged();
    }

    public void setMap(boolean map) {
        this.map = map;
    }

    public void setLisenter(RecyclerView contentView) {
        contentView.setOnTouchListener(new RecyclerViewOnTouchListener());

    }

    public boolean isAllFinished() {
        boolean result = true;
        if (mOLXJAreaEntities == null) {
            return result;
        }
        for (OLXJAreaEntity areaEntity : mOLXJAreaEntities) {
            if (!"1".equals(areaEntity.finishType)) {
                return false;
            }
        }

        return result;
    }

    public boolean isExpand() {
        return expandPosition == viewHolder.getAdapterPosition();
    }

    @Override
    protected BaseRecyclerViewHolder<OLXJTaskEntity> getViewHolder(int viewType) {
        viewHolder = new ViewHolder(context, parent);
        return viewHolder;
    }

    class ViewHolder extends BaseRecyclerViewHolder<OLXJTaskEntity> implements View.OnClickListener {

        @BindByTag("listLayout")
        LinearLayout listLayout;
        @BindByTag("mapLayout")
        LinearLayout mapLayout;

        @BindByTag("itemXJPathIndex")
        TextView itemXJPathIndex;  //序号

        @BindByTag("itemXJPath")
        TextView itemXJPath;  //路线

        @BindByTag("taskResponsiblePerson")
        TextView taskResponsiblePerson; //负责人

        @BindByTag("taskStartEndTime")
        TextView taskStartEndTime;  //起止时间

        @BindByTag("taskStatus")
        TextView taskStatus;  //任务状态

        @BindByTag("taskExpandBtn")
        ImageView taskExpandBtn;

        @BindByTag("taskAreaListView")
        RecyclerView taskAreaListView;

        @BindByTag("progressBar")
        ProgressBar progressBar;
        @BindByTag("webView")
        BridgeWebView webView;

        OLXJAreaListAdapter mOLXJAreaListAdapter;

        private long currentId;

        private boolean isFold = true;

        public ViewHolder(Context context, ViewGroup parent) {
            super(context, parent);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_olxj_task;
        }

        @Override
        protected void initView() {
            super.initView();
            taskAreaListView.setLayoutManager(new LinearLayoutManager(context));  //线性布局
            mOLXJAreaListAdapter = new OLXJAreaListAdapter(context);
            taskAreaListView.setAdapter(mOLXJAreaListAdapter);

            webView.setFocusableInTouchMode(false);
            webView.setWebViewClient(new MapWebViewClient(webView));
            webView.setWebChromeClient(new MyWebChromeClient());
            WebSettings settings = webView.getSettings();
            settings.setAppCacheEnabled(true);
            settings.setJavaScriptEnabled(true);
            settings.setAllowFileAccess(true);
            settings.setDatabaseEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setGeolocationEnabled(true);
//            settings.setBuiltInZoomControls(true); // 显示放大缩小
            settings.setSupportZoom(true); // 可以缩放
            settings.setDisplayZoomControls(false);
            settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

            settings.setLoadWithOverviewMode(true);
            // 通过addJavascriptInterface()将Java对象映射到JS对象
            //参数1：Javascript对象名
            //参数2：Java对象名
//            webView.addJavascriptInterface(new AndroidtoJs() {
//                @Override
//                public void signIn(String id) {
//
////                    onItemChildViewClick(taskExpandBtn, 1, getItem(position));
//                }
//            }, "android");



        }

        @Override
        protected void initListener() {
            super.initListener();

            taskExpandBtn.setOnClickListener(v -> {

                int position = getAdapterPosition();
                boolean isExPand = expandPosition == position;
                if (!isExPand) {
                    expand(position);
                    onItemChildViewClick(taskExpandBtn, 1, getItem(position));
                } else {
                    shrink(position);
                    onItemChildViewClick(taskExpandBtn, 0, getItem(position));
                }


            });

            mOLXJAreaListAdapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
                onItemChildViewClick(taskAreaListView, action, obj);
            });


            if(webView!=null){
                webView.registerHandler("areaClick", new BridgeHandler() {

                    @Override
                    public void handler(String data, CallBackFunction function) {
                        LogUtil.e("areaClick" + data);
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            String code = jsonObject.getString("id");
                            if(mOLXJAreaEntities == null){
                                onItemChildViewClick(taskExpandBtn, 0, getItem(0));
                            }
                            else if (!TextUtils.isEmpty(code))
                                for (OLXJAreaEntity areaEntity : mOLXJAreaEntities) {
                                    if (code.equals(areaEntity._code)) {
                                        onItemChildViewClick(taskAreaListView, 0, areaEntity);
                                        return;
                                    }
                                }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                });
            }
        }

        private void shrink(int position) {
            expandPosition = -1;
            notifyItemChanged(position);

        }

        private void expand(int position) {
//            int oldPosition = expandPosition;
            expandPosition = position;
            notifyItemChanged(expandPosition);
//            notifyItemChanged(oldPosition);
        }

        private void doShrink() {
            taskExpandBtn.setImageResource(R.drawable.ic_zk);
            taskAreaListView.setVisibility(View.GONE);
        }

        @SuppressLint("CheckResult")
        private void doExpand(int position) {

            taskExpandBtn.setImageResource(R.drawable.ic_sq);
            taskAreaListView.setVisibility(View.VISIBLE);

            OLXJTaskEntity taskEntity = getItem(position);

            if (currentId == taskEntity.id) {
                mOLXJAreaListAdapter.notifyDataSetChanged();
                return;
            }

            mOLXJAreaListAdapter.clear();


            if (mOLXJAreaEntities == null || mOLXJAreaEntities.size() == 0) {

//                ToastUtils.show(context,"无巡检区域列表");
                return;
            }

            mOLXJAreaListAdapter.addList(mOLXJAreaEntities);
            mOLXJAreaListAdapter.notifyDataSetChanged();

            currentId = taskEntity.id;


        }

        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();
            OLXJTaskEntity item = getItem(adapterPosition);
            onItemChildViewClick(v, 0, item);  //点击事件传递给Activity

        }


        @Override
        protected void update(OLXJTaskEntity data) {
            int position = getAdapterPosition();
            if (map) {
                listLayout.setVisibility(View.GONE);
                taskExpandBtn.setVisibility(View.GONE);
                mapLayout.setVisibility(View.VISIBLE);
            } else {
                mapLayout.setVisibility(View.GONE);
                listLayout.setVisibility(View.VISIBLE);
                taskExpandBtn.setVisibility(View.VISIBLE);
            }

            //当页面正在加载时，禁止链接的点击事件
            Map<String, String> header = new HashMap<>();
            String url = "http://" + EamApplication.getIp() + ":" + EamApplication.getPort()
                    + Constant.WebUrl.XJ + data.id + "&WorkGroupID=" + data.workGroupID.id;
            if (!TextUtils.isEmpty(EamApplication.getCooki())) {
                header.put("Cookie", EamApplication.getCooki());
            }
            if (!TextUtils.isEmpty(EamApplication.getAuthorization())) {
                header.put("Authorization", EamApplication.getAuthorization());
            }
            webView.loadUrl(url, header);


            if (data.isStart) {
                taskStatus.setBackgroundResource(R.drawable.sh_task_status_going);
            }

            if (expandPosition == position) {
                doExpand(position);
            } else {
                doShrink();
            }

            itemXJPathIndex.setText(String.valueOf(position + 1));
//            taskTableNo.setText(data.tableNo);
            itemXJPath.setText(data.workGroupID.name);
            taskResponsiblePerson.setText(data.resstaffID.name);
            taskStartEndTime.setText(String.format("%s  ~  %s",
                    DateUtil.dateFormat(data.starTime, "MM-dd HH:mm:ss"), DateUtil.dateFormat(data.endTime, "MM-dd HH:mm:ss")));
            taskStatus.setText(data.state);

        }

        /**
         * @author zhangwenshuai1
         * @date 2018/4/4
         * @description 开始时间判断
         */
        private boolean startJudge(OLXJTaskEntity taskEntity) {

            if (taskEntity.starTime == null) {
                return false;
            }
            if (taskEntity.startAdv > 0) {
                long startTimeLong = taskEntity.starTime;
                if ((startTimeLong - ((taskEntity.startAdv) * 60 * 60 * 1000)) > new Date().getTime()) {
                    SnackbarHelper.showError(itemView, "该巡检任务不允许提前" + taskEntity.startAdv + "小时开始");
                    return true;
                }
            }
            if (taskEntity.startDelay > 0) {
                long startTimeLong = taskEntity.starTime;
                if ((startTimeLong + ((taskEntity.startDelay) * 60 * 60 * 1000)) < new Date().getTime()) {
                    SnackbarHelper.showError(itemView, "该巡检任务不允许延迟" + taskEntity.startDelay + "小时开始");
                    return true;
                }
            }

            return false;

        }

        /**
         * @author zhangwenshuai1
         * @date 2018/4/4
         * @description 结束时间判断
         */
        private boolean endJudge(OLXJTaskEntity taskEntity) {

            if (taskEntity.endTime == null) {

                return false;
            }

            if (taskEntity.endDelay > 0) {
                long endTimeLong = taskEntity.endTime;
                if ((endTimeLong + ((taskEntity.endDelay) * 60 * 60 * 1000)) < new Date().getTime()) {
                    SnackbarHelper.showError(itemView, "该巡检任务不允许延迟" + taskEntity.endDelay + "小时结束");
                    return true;
                }
            }

            return false;
        }

        private class MyWebViewClient extends WebViewClient {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                        , context.getResources().getDisplayMetrics().heightPixels - Util.dpToPx(context, 160));
                webView.setLayoutParams(lp);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);


            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        }

        private class MapWebViewClient extends BaseBridgeWebViewClient {

            public MapWebViewClient(BridgeWebView webView) {
                super(webView);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            protected boolean dealUrl(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                        , DisplayUtil.getScreenHeight(context) - DisplayUtil.dip2px(170, context));
                webView.setLayoutParams(lp);
                BridgeUtil.webViewLoadLocalJs(webView, "xj.js");

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

        }

        private class MyWebChromeClient extends WebChromeClient {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (map) {
                    if (newProgress == 100) {
                        // 网页加载完成
                        progressBar.setVisibility(View.GONE);
                    } else {
                        // 加载中
                        progressBar.setVisibility(View.VISIBLE);
                        progressBar.setProgress(newProgress);
                    }
                }
                super.onProgressChanged(view, newProgress);
            }
        }
    }

    public class RecyclerViewOnTouchListener implements View.OnTouchListener {


        private int mLastY;
        private int mCurrentY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            //获取WebView所在item的顶部相对于其父控件（即RecyclerView的父控件）的距离
            if (viewHolder.mapLayout.getVisibility() != View.VISIBLE
                    || !isInView(viewHolder.mapLayout, event)) {
                return false;
            }

            //计算dy，用来判断滑动方向。dy<0-->向上滑动；dy>0-->向下滑动。
            int dy = 0;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mLastY = (int) event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mCurrentY = (int) event.getY();
                    dy = mCurrentY - mLastY;
                    mLastY = mCurrentY;
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    dy = (int) (event.getY() - mLastY);
                    mLastY = 0;
                    mCurrentY = 0;
                    break;
            }

            //如果WebView顶部距离其父控件距离未0，即WebView顶部滑动到RecyclerView父控件顶部重合时，
            // 此时需要拦截滑动事件交给WebView处理。
            if (shouldIntercept(viewHolder.webView, dy)) {
                viewHolder.webView.onTouchEvent(event);
                return true;
            }
            return true;
        }

        /**
         * 是否拦截滑动事件，判断的逻辑是：<br/>
         * 1,如果是向上滑动，并且webview能够向上滑动，则拦截事件；<br/>
         * 2,如果是向下滑动，并且webview能够向下滑动，则拦截事件。
         *
         * @param view 判断能够滑动的view
         * @param dy   滑动间距
         * @return true拦截，false不拦截。
         */
        private boolean shouldIntercept(View view, int dy) {
            //canScrollVertically方法的第二个参数direction，传1时返回是否能够向上滑动，传-1时返回能否向下滑动。
            //dy<0-->向上滑动；dy>0-->向下滑动。
            boolean scrollUp = dy < 0 && ViewCompat.canScrollVertically(view, 1);
            boolean scrollDown = dy > 0 && ViewCompat.canScrollVertically(view, -1);
            return scrollUp || scrollDown || dy == 0;
        }

        /**
         * 判断触摸的点是否在View范围内
         */
        private boolean isInView(View v, MotionEvent event) {
            Rect frame = new Rect();
            v.getHitRect(frame);
            float eventX = event.getX();
            float eventY = event.getY();
            return frame.contains((int) eventX, (int) eventY);
        }
    }

}
