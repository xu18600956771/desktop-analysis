package com.openthos.launcher.openthoslauncher.activity;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.view.KeyEvent;

import com.android.launcher3.Launcher;
import com.android.launcher3.R;
import com.openthos.launcher.openthoslauncher.adapter.HomeAdapter;
import com.openthos.launcher.openthoslauncher.adapter.ItemCallBack;
import com.openthos.launcher.openthoslauncher.adapter.RecycleCallBack;
import com.openthos.launcher.openthoslauncher.entity.Type;
import com.openthos.launcher.openthoslauncher.utils.OtoConsts;
import com.openthos.launcher.openthoslauncher.utils.DiskUtils;
import com.openthos.launcher.openthoslauncher.view.PropertyDialog;
import com.openthos.launcher.openthoslauncher.view.MenuDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Launcher implements RecycleCallBack {
    private RecyclerView mRecyclerView;
    public List<HashMap<String, Object>> mDatas;
    public HomeAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    public static Handler mHandler;
    private int mHeightNum = OtoConsts.MAX_LINE;
    private boolean mIsClicked = false;
    private boolean mIsRename = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOtoContentView(R.layout.activity_main);
        initData();
        init();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case OtoConsts.REFRESH:
                        File doc = new File(OtoConsts.DESKTOP_PATH);
                        File[] files = doc.listFiles();
                        break;
                    case OtoConsts.SORT:
                        mDatas.clear();
                        initData();
                        mAdapter.setData(mDatas);
                        mAdapter.notifyDataSetChanged();
                        break;
                    case OtoConsts.DELETE:
                        inner:
                        for (int i = 0; i < mDatas.size(); i++) {
                            if ((mDatas.get(i).get("path")).equals(msg.obj)) {
                                HashMap<String, Object> nullmap = new HashMap<>();
                                nullmap.put("name", "");
                                nullmap.put("isChecked", false);
                                nullmap.put("icon", -1);
                                nullmap.put("null", true);
                                nullmap.put("path", "");
                                nullmap.put("type", Type.blank);
                                mDatas.set(i, nullmap);
                                break inner;
                            }
                        }
                        mAdapter.setData(mDatas);
                        mAdapter.notifyDataSetChanged();
                        break;
                    case OtoConsts.NEWFOLDER:
                        inner:
                        for (int i = 0; i < mDatas.size(); i++) {
                            if ((mDatas.get(i).get("path")).equals("")) {
                                File root = new File(OtoConsts.DESKTOP_PATH);
                                for (int j = 1; ; j++) {
                                    File file = new File(root,
                                                        MainActivity.this.getResources()
                                                        .getString(R.string.new_folder) + j);
                                    if (!file.exists()) {
                                        file.mkdir();
                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put("name", file.getName());
                                        map.put("path", file.getAbsolutePath());
                                        map.put("isChecked", false);
                                        map.put("null", false);
                                        map.put("icon", R.drawable.ic_app_file);
                                        map.put("type", Type.directory);
                                        mDatas.set(i, map);
                                        break inner;
                                    }
                                }
                            }
                        }
                        mAdapter.setData(mDatas);
                        mAdapter.notifyDataSetChanged();
                        break;
                    case OtoConsts.RENAME:
                        mAdapter.isRename = true;
                        mAdapter.notifyDataSetChanged();
                        break;
                   case OtoConsts.PROPERTY:
                       PropertyDialog dialog = new PropertyDialog(MainActivity.this,
                                                                  (String) msg.obj);
                       dialog.showDialog();
                       break;

                }
            }
        };
    }

    private void initData() {
        List<HashMap<String, Object>>  userDatas = new ArrayList<>();
        String[] defaultName = getResources().getStringArray(R.array.default_icon_name);
        TypedArray defaultIcon = getResources().obtainTypedArray(R.array.default_icon);
        String[] paths = {"/", OtoConsts.RECYCLE_PATH};
        new Thread() {
            @Override
            public void run() {
                super.run();
                File recycle = DiskUtils.getRecycle();
                if (!recycle.exists()) {
                    recycle.mkdir();
                }
            }
        }.start();
        Type[] defaultType = {Type.computer, Type.recycle};
        mDatas = new ArrayList<>();
        for (int i = 0; i < defaultName.length; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", defaultName[i]);
            map.put("path", paths[i]);
            map.put("isChecked", false);
            map.put("null", false);
            map.put("icon", defaultIcon.getResourceId(i, R.mipmap.ic_launcher));
            map.put("type", defaultType[i]);
            mDatas.add(map);
        }
        initDesktop();
    }

    private void initDesktop() {
        List<HashMap<String, Object>> userDatas = new ArrayList<>();
        int num = getNum();
        File dir = new File(OtoConsts.DESKTOP_PATH);
        if (!dir.exists()) {
            dir.mkdir();
        }
        File[] files = dir.listFiles();
        if(files != null){
        for (int i = 0; i < files.length; i++) {
            HashMap<String, Object> map = new HashMap<>();
            if (files[i].isDirectory()) {
                map.put("name", files[i].getName());
                map.put("path", files[i].getAbsolutePath());
                map.put("isChecked", false);
                map.put("null", false);
                map.put("icon", R.drawable.ic_app_file);
                map.put("type", Type.directory);

            } else {
                map.put("name", files[i].getName());
                map.put("path", files[i].getAbsolutePath());
                map.put("isChecked", false);
                map.put("null", false);
                map.put("icon", R.drawable.ic_app_text);
                map.put("type", Type.file);
            }
            if(userDatas.size() < (num - mDatas.size())) {
                userDatas.add(map);
            }
        }}
        
        Collections.sort(userDatas, new Comparator<HashMap<String, Object>>() {

            @Override
            public int compare(HashMap<String, Object> object,
                               HashMap<String, Object> anotherObject) {
                String anotherObjectName = (String) anotherObject.get("name");
                String objectName = (String) object.get("name");
                return objectName.compareTo(anotherObjectName);
            }
        });
        mDatas.addAll(userDatas);
        
        while (mDatas.size() < num) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", "");
            map.put("isChecked", false);
            map.put("icon", -1);
            map.put("null", true);
            map.put("path", "");
            map.put("type", Type.blank);
            mDatas.add(map);
        }
    }

    private int getNum() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int widthPixels = dm.widthPixels;
        int widthNum = widthPixels / getResources().getDimensionPixelSize(R.dimen.icon_size);
        return widthNum * mHeightNum;
    }

    private void init() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getButtonState() == MotionEvent.BUTTON_SECONDARY) {
                        if (!MenuDialog.isExistMenu()) {
                            MenuDialog dialog = MenuDialog.getInstance(MainActivity.this,
                                                                       Type.blank, "/");
                            dialog.showDialog((int) event.getRawX(), (int) event.getRawY());
                        } else {
                            MenuDialog.setExistMenu(false);
                        }
                    }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!mIsClicked && mAdapter.pos != -1) {
                        mAdapter.getData().get(mAdapter.pos).put("isChecked", false);
                        mAdapter.pos = -1;
                        mAdapter.notifyDataSetChanged();
                    }
                    mIsClicked = false;
                }
                return false;
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(heightNum,
                                       StaggeredGridLayoutManager.HORIZONTAL));
        mAdapter = new HomeAdapter(mDatas, this);

        mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (event.getButtonState() == MotionEvent.BUTTON_SECONDARY) {
                        if (!MenuDialog.isExistMenu()) {
                            MenuDialog dialog = MenuDialog.getInstance(MainActivity.this,
                                                                       Type.blank, "/");
                            dialog.showDialog((int) event.getRawX(), (int) event.getRawY());
                        } else {
                           MenuDialog.setExistMenu(false);
                        }
                    }

                    if (!mIsClicked && mAdapter.pos != -1) {
                        mDatas.get(mAdapter.pos).put("isChecked", false);
                        mAdapter.pos = -1;
                        mAdapter.notifyDataSetChanged();
                    }
                    mIsClicked = false;
                    if (mIsRename) {
                        mIsRename = false;
                        mAdapter.notifyDataSetChanged();
                    }
                }
                return false;
            }
        });

        mItemTouchHelper = new ItemTouchHelper(new ItemCallBack(this));
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void itemOnClick(int position, View view) {
    }

    @Override
    public void onMove(int from, int to) {
        if (!(Boolean) mDatas.get(from).get("null")) {
            if (to > 0 && from > 0) {
                synchronized (this) {
                    if (from > to) {
                        int count = from - to;
                        for (int i = 0; i < count; i++) {
                            Collections.swap(mDatas, from - i, from - i - 1);
                        }
                    }
                    if (from < to) {
                        int count = to - from;
                        for (int i = 0; i < count; i++) {
                            Collections.swap(mDatas, from + i, from + i + 1);
                        }
                    }
                    mAdapter.setData(mDatas);
                    mAdapter.notifyItemMoved(from, to);
                    mAdapter.pos = to;
                }
            }
        }
    }
                 
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.isCtrlPressed()) {
            //if (keyCode == KeyEvent.KEYCODE_ESCAPE || keyCode == KeyEvent.KEYCODE_MENU) {
                return true;
            //}
        }
        return super.onKeyDown(keyCode, event);
    }
}
