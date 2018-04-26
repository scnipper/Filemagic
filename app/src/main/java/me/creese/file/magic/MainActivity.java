package me.creese.file.magic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);

        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(layout);

        setParams();


        RecyclerView recyclerView = new RecyclerView(this);
        AdapterFiles adapter = new AdapterFiles();
        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FileCore fileCore = new FileCore();

        for (String listFile : fileCore.getListFilesCurrentDir()) {
            adapter.addItem(new ModelFiles(listFile,true));
        }

        layout.addView(recyclerView);


    }

    private void setParams() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        P.WIDTH = dm.widthPixels;
        P.HEIGHT = dm.heightPixels;
        P.DENSITY = getResources().getDisplayMetrics().density;
    }
}
