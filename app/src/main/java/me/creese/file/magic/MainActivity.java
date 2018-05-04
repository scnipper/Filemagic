package me.creese.file.magic;

import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

import me.creese.file.magic.util.LoadImage;
import me.creese.file.magic.views.DirView;

import static android.view.MenuItem.SHOW_AS_ACTION_ALWAYS;


public class MainActivity extends AppCompatActivity {

    public static final int GROUP_ACTIONS = 0;
    public static final int GROUP_MOVE_COPY = 1;
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
    private int whatDoFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoadImage.setActivity(this);
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

        toolbar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                P.getPixelFromDP(40)));


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

                    Dialogs.getInstanse().showKeyboard(this);
                    whichCreate.show();

                });


        dialogCreate = builder.create();


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

    public void addIconsToToolbar(int gId) {
        toolbar.getMenu().setGroupVisible(gId, true);

    }

    public void hideToolbarIcon(int gId) {
        toolbar.getMenu().setGroupVisible(gId, false);
    }

    @Override
    public void onBackPressed() {
        if (fileCore.getCurrentDir().equals(fileCore.getRootDir())) super.onBackPressed();
        else {
            hideToolbarIcon(GROUP_ACTIONS);
            fileCore.backDir();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(GROUP_ACTIONS, 11, 0, "")
                .setIcon(R.drawable.ic_delete_white_18dp)
                .setShowAsAction(SHOW_AS_ACTION_ALWAYS);
        menu.add(GROUP_ACTIONS, 12, 0, "")
                .setIcon(R.drawable.ic_compare_arrows_white_18dp)
                .setShowAsAction(SHOW_AS_ACTION_ALWAYS);
        menu.add(GROUP_ACTIONS, 13, 0, "")
                .setIcon(R.drawable.ic_mode_edit_white_18dp)
                .setShowAsAction(SHOW_AS_ACTION_ALWAYS);

        menu.add(GROUP_MOVE_COPY, 21, 0, "")
                .setIcon(R.drawable.ic_close_white_18dp)
                .setShowAsAction(SHOW_AS_ACTION_ALWAYS);
        menu.add(GROUP_MOVE_COPY, 22, 0, "")
                .setIcon(R.drawable.ic_check_white_18dp)
                .setShowAsAction(SHOW_AS_ACTION_ALWAYS);


        hideToolbarIcon(GROUP_MOVE_COPY);
        hideToolbarIcon(GROUP_ACTIONS);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case 11:

                adapter.saveSelectedFiles();
                fileCore.deleteFiles(adapter.getSavedFiles());
                hideToolbarIcon(GROUP_ACTIONS);
                break;
            case 12:
                Dialogs.getInstanse().showDialogCopyAndMove(this, which -> {

                    Integer w = (Integer) which;

                    if (w == 0) {
                        whatDoFile = R.string.copy_elem;
                    }
                    if (w == 1) {
                        whatDoFile = R.string.move_elem;
                    }
                    hideToolbarIcon(GROUP_ACTIONS);
                    adapter.setModeMoveAndCopy(true);
                    adapter.saveSelectedFiles();
                    fileCore.saveCurDir();

                    addIconsToToolbar(GROUP_MOVE_COPY);
                });
                break;
            case 13:

                File fileRename = adapter.getSelectedFile();
                String name = fileRename.getName();


                Dialogs.getInstanse().showDialogRename(this, which -> {


                    fileCore.renameFile(fileRename, (String) which);

                    hideToolbarIcon(GROUP_ACTIONS);
                    adapter.deselectAll();
                }, name);
                break;
            case 21:
                hideToolbarIcon(GROUP_MOVE_COPY);
                adapter.setModeMoveAndCopy(false);
                break;
            case 22:
                //hideToolbarIcon(GROUP_MOVE_COPY);
                Dialogs.getInstanse().showDialogSureCopyMove(this, which -> {
                    Integer w = ((Integer) which);

                    if (w == 1) {
                        // do it

                        fileCore.moveFile(adapter.getSavedFiles(), true);

                        adapter.getSavedStates().clear();
                    }
                    if (w == 2) {
                        //fileCore.moveFile(adapter.getSavedFiles(), false);
                    }

                    Dialogs.getInstanse().showTickDialog(this,w);
                    hideToolbarIcon(GROUP_MOVE_COPY);
                    adapter.setModeMoveAndCopy(false);
                }, whatDoFile);


        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Dialogs.getInstanse().destroy();
    }
}
