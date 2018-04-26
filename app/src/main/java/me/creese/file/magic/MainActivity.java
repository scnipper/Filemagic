package me.creese.file.magic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private LinearLayout layout;
    private AdapterFiles adapter;
    private FileCore fileCore;
    private TextView textDir;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = new LinearLayout(this);

        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        setContentView(layout);
        fileCore = new FileCore(this);
        setParams();


        createViews();


        fileCore.openDir("/");




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

        textDir = new TextView(this);
        textDir.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        textDir.setMaxLines(1);
        textDir.setText(fileCore.getCurrentDir());
        textDir.setTextSize(20);
        layout.addView(textDir);
        layout.addView(recyclerView);
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

    public TextView getTextDir() {
        return textDir;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public void onBackPressed() {
        if(fileCore.getCurrentDir().equals("/")) super.onBackPressed();
        else {
            fileCore.backDir();
        }
    }
}
