package me.creese.file.magic;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;



import me.creese.file.magic.views.DirView;



public class MainActivity extends AppCompatActivity {

    private LinearLayout layout;
    private AdapterFiles adapter;
    private FileCore fileCore;
    private DirView textDir;
    private RecyclerView recyclerView;
    private FrameLayout emptyFolder;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        frameLayout = new FrameLayout(this);
        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        layout = new LinearLayout(this);

        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.VERTICAL);

        setContentView(frameLayout);
        frameLayout.addView(layout);

        fileCore = new FileCore(this);
        setParams();


        createViews();


        fileCore.openRootDir();




    }

    private void createViews() {
        recyclerView = new RecyclerView(this);
        adapter = new AdapterFiles();
        adapter.setFileCore(fileCore);


        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setPadding(0,0,0,P.getPixelFromDP(10));
        recyclerView.setClipToPadding(false);

        textDir = new DirView(this,fileCore);



        layout.addView(textDir);
        layout.addView(recyclerView);


        emptyFolder = new FrameLayout(this);

        emptyFolder.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        TextView emptyText = new TextView(this);
        emptyText.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        emptyText.setTextSize(20);
        ((FrameLayout.LayoutParams) emptyText.getLayoutParams()).gravity = Gravity.CENTER;
        emptyFolder.addView(emptyText);


        FloatingActionButton fab = new FloatingActionButton(this);
        fab.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        fab.setImageResource(R.drawable.ic_add_white_24dp);
        ((FrameLayout.LayoutParams) fab.getLayoutParams()).gravity = Gravity.BOTTOM|Gravity.END;
        ((FrameLayout.LayoutParams) fab.getLayoutParams()).rightMargin = P.getPixelFromDP(16);
        ((FrameLayout.LayoutParams) fab.getLayoutParams()).bottomMargin = P.getPixelFromDP(16);
        frameLayout.addView(fab);



    }

    private void setParams() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        P.WIDTH = dm.widthPixels;
        P.HEIGHT = dm.heightPixels;
        P.DENSITY = getResources().getDisplayMetrics().density;
    }

    public AdapterFiles getAdapter() {
        return adapter;
    }

    public DirView getTextDir() {
        return textDir;
    }

    public RecyclerView getRecyclerView() {

        if(recyclerView.getParent() == null) {
            layout.removeView(emptyFolder);
            layout.addView(recyclerView);
        }

        return recyclerView;
    }

    public void addEmptySign(int what) {
        layout.removeView(recyclerView);
        layout.addView(emptyFolder);
        ((TextView) emptyFolder.getChildAt(0)).setText(what);

    }

    @Override
    public void onBackPressed() {
        if(fileCore.getCurrentDir().equals("/")) super.onBackPressed();
        else {
            fileCore.backDir();
        }
    }
}
