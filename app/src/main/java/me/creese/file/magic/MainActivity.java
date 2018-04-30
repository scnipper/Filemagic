package me.creese.file.magic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.creese.file.magic.views.DirView;

import static android.view.MenuItem.SHOW_AS_ACTION_ALWAYS;


public class MainActivity extends AppCompatActivity {

    private LinearLayout layout;
    private AdapterFiles adapter;
    private FileCore fileCore;
    private DirView textDir;
    private RecyclerView recyclerView;
    private FrameLayout emptyFolder;

    private AlertDialog dialogCreate;
    private AlertDialog dialogOpenWith;
    private DrawerLayout drawerLayout;
    private FrameLayout frameLayout;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        drawerLayout = new DrawerLayout(this);
        drawerLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        frameLayout = new FrameLayout(this);
        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        layout = new LinearLayout(this);

        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        drawerLayout.addView(frameLayout);
        setContentView(drawerLayout);
        frameLayout.addView(layout);


        fileCore = new FileCore(this);
        setParams();


        createViews();


        fileCore.openRootDir();


    }

    @SuppressLint("ResourceType")
    private void createViews() {




        toolbar = new Toolbar(this);

        toolbar.setTitle("");

        toolbar.setBackgroundColor(0xff008D7C);
        

        layout.addView(toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = new NavigationView(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();



        navigationView.setLayoutParams(new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        ((DrawerLayout.LayoutParams) navigationView.getLayoutParams()).gravity = Gravity.START;

        drawerLayout.addView(navigationView);
        recyclerView = new RecyclerView(this);
        adapter = new AdapterFiles();
        adapter.setFileCore(fileCore);


        recyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setPadding(0, 0, 0, P.getPixelFromDP(10));
        recyclerView.setClipToPadding(false);

        textDir = new DirView(this, fileCore);


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
        fab.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        fab.setImageResource(R.drawable.ic_add_white_24dp);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        EditText nameElement = new EditText(this);

        nameElement.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        nameElement.setLines(1);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (nameElement.getId() == 111) {
                    fileCore.createFile(nameElement.getText().toString());
                } else {
                    fileCore.createFolder(nameElement.getText().toString());
                }

            }
        }).setNegativeButton(R.string.cancel, null).setView(nameElement);

        AlertDialog whichCreate = builder.create();


        builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.create)
                .setItems(R.array.items_create, (dialog, which) -> {
                    if (which == 0) {
                        whichCreate.setTitle(R.string.file);
                        nameElement.setText(R.string.file);
                        nameElement.setId(111);
                    } else {
                        whichCreate.setTitle(R.string.folder);
                        nameElement.setText(R.string.folder);
                        nameElement.setId(222);
                    }
                    nameElement.selectAll();

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    whichCreate.show();

                });


        dialogCreate = builder.create();


        AlertDialog fileOrFolderCreate = builder.create();


        fab.setOnClickListener(l -> {

            dialogCreate.show();
        });

        ((FrameLayout.LayoutParams) fab.getLayoutParams()).gravity = Gravity.BOTTOM | Gravity.END;
        ((FrameLayout.LayoutParams) fab.getLayoutParams()).rightMargin = P.getPixelFromDP(16);
        ((FrameLayout.LayoutParams) fab.getLayoutParams()).bottomMargin = P.getPixelFromDP(16);
        frameLayout.addView(fab);


    }

    public void showDialogOpenWith(OpenWithHandler handler) {
        if (dialogOpenWith == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.open_how)
                    .setItems(R.array.items_open_with, (dialog, which) -> {

                        switch (which) {
                            case 0:
                                handler.what(OpenWithHandler.WhatOpenFile.TEXT);
                                break;
                            case 1:
                                handler.what(OpenWithHandler.WhatOpenFile.AUDIO);
                                break;
                            case 2:
                                handler.what(OpenWithHandler.WhatOpenFile.IMAGE);
                                break;
                            case 3:
                                handler.what(OpenWithHandler.WhatOpenFile.VIDEO);
                                break;
                            case 4:
                                handler.what(OpenWithHandler.WhatOpenFile.OTHER);
                                break;

                        }


                    });


            dialogOpenWith = builder.create();
        }
        dialogOpenWith.show();
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

        if (recyclerView.getParent() == null) {
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

    public void addIconsToToolbar() {
        toolbar.getMenu().add(0,11,0,"");
        toolbar.getMenu().getItem(0).setShowAsAction(SHOW_AS_ACTION_ALWAYS);
        toolbar.getMenu().getItem(0).setIcon(R.drawable.ic_delete_white_18dp);

    }

    @Override
    public void onBackPressed() {
        if (fileCore.getCurrentDir().equals("/")) super.onBackPressed();
        else {
            fileCore.backDir();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main2, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case 11:
                adapter.deleteSelected();
        }
        return true;
    }
}
