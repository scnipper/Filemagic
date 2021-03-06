package me.creese.file.magic;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
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
import java.io.IOException;

import me.creese.file.magic.util.LoadImage;
import me.creese.file.magic.util.Saves;
import me.creese.file.magic.views.DirView;

import static android.view.MenuItem.SHOW_AS_ACTION_ALWAYS;
import static android.view.MenuItem.SHOW_AS_ACTION_NEVER;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final int GROUP_ACTIONS = 0;
    public static final int GROUP_MOVE_COPY = 1;
    public static final int GROUP_FILE_OPTIONS = 2;
    public static final int GROUP_ICON_RENAME = 3;
    private static final int GROUP_FILE_OPTIONS_ALWAYS_SHOW = 4;
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
    private boolean hideRename;
    private boolean isHideIconRename;
    private FloatingActionButton fab;
    private float fabY;
    private boolean isFbaAnim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LoadImage.setActivity(this);
        Saves.init(this);
        fabY = 0;
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
        navigationView.inflateMenu(R.menu.activity_main2_drawer);

        navigationView.setNavigationItemSelectedListener(this);

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


        fab = new FloatingActionButton(this);
        fab.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        fab.setImageResource(R.drawable.ic_add_white_24dp);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (dy < 0) {

                    fabShow();

                } else if (dy > 0) {
                    fabHide();
                }
            }
        });


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

        dialogOpenWith.show();
    }

    private void setParams() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        P.WIDTH = dm.widthPixels;
        P.HEIGHT = dm.heightPixels;
        P.DENSITY = getResources().getDisplayMetrics().density;
    }

    public void fabHide() {
        if (!isFbaAnim) {
            isFbaAnim = true;
            fab.animate().translationY(P.HEIGHT/4)
                    .setDuration(150)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isFbaAnim = false;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    })
                    .start();
        }
    }

    public void fabShow() {
        if (!isFbaAnim) {
            isFbaAnim = true;
            fab.animate().translationY(0)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isFbaAnim = false;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    })
                    .setDuration(150).start();
        }
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

    public void showIconRename() {
        isHideIconRename = true;
        invalidateOptionsMenu();
    }

    public void hideIconRename() {
        hideRename = true;
        isHideIconRename = false;
        invalidateOptionsMenu();
    }

    public FloatingActionButton getFab() {
        return fab;
    }


    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (fileCore.getCurrentDir().equals(fileCore.getRootDir())) super.onBackPressed();
            else {
                hideToolbarIcon(GROUP_ACTIONS);
                fileCore.backDir();
            }
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
        menu.add(GROUP_ICON_RENAME, 13, 0, "")
                .setIcon(R.drawable.ic_mode_edit_white_18dp)
                .setShowAsAction(SHOW_AS_ACTION_ALWAYS);


        menu.add(GROUP_FILE_OPTIONS, 14, 0, R.string.open_how)
                .setShowAsAction(SHOW_AS_ACTION_NEVER);
        menu.add(GROUP_FILE_OPTIONS_ALWAYS_SHOW, 15, 0, R.string.sort)
                .setShowAsAction(SHOW_AS_ACTION_NEVER);

        menu.add(GROUP_MOVE_COPY, 21, 0, "")
                .setIcon(R.drawable.ic_close_white_18dp)
                .setShowAsAction(SHOW_AS_ACTION_ALWAYS);
        menu.add(GROUP_MOVE_COPY, 22, 0, "")
                .setIcon(R.drawable.ic_check_white_18dp)
                .setShowAsAction(SHOW_AS_ACTION_ALWAYS);

        hideToolbarIcon(GROUP_MOVE_COPY);
        hideToolbarIcon(GROUP_FILE_OPTIONS);
        hideToolbarIcon(GROUP_ICON_RENAME);
        hideToolbarIcon(GROUP_ACTIONS);



        /*if (!hideRename) {

            hideToolbarIcon(GROUP_ACTIONS);
        } else {
            if (isHideIconRename) hideRename = true;
            menu.findItem(13).setVisible(isHideIconRename);
        }*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case 11:

                Dialogs.getInstanse().showDeleteDialog(this, which -> {
                    adapter.saveSelectedFiles();
                    fileCore.deleteFiles(adapter.getSavedFiles());
                    //adapter.deselectAll();
                    //adapter.notifyDataSetChanged();
                    hideToolbarIcon(GROUP_ACTIONS);
                    hideToolbarIcon(GROUP_ICON_RENAME);
                    textDir.hideCheckBox();
                });

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
                    hideToolbarIcon(GROUP_ICON_RENAME);
                    adapter.deselectAll();
                    adapter.notifyDataSetChanged();
                    textDir.hideCheckBox();
                }, name);
                break;
            case 14:
                showDialogOpenWith(what -> fileCore.openFile(what.toString(),
                        adapter.getSelectedFile().getName(), null, true));
                break;
            case 15:
                Dialogs.getInstanse().showSortDialog(this);
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
                        fileCore.moveFile(adapter.getSavedFiles(), false);
                        adapter.getSavedStates().clear();
                    }

                    Dialogs.getInstanse().showTickDialog(this, w);
                    hideToolbarIcon(GROUP_MOVE_COPY);
                    adapter.setModeMoveAndCopy(false);
                }, whatDoFile);


        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Dialogs.getInstanse().destroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                try {
                    fileCore.clearCurrentDir();
                    fileCore.openDir(Environment.getExternalStorageDirectory().getCanonicalPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.nav_pictures:
                try {
                    fileCore.clearCurrentDir();
                    fileCore.openDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getCanonicalPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.nav_videos:
                try {
                    fileCore.clearCurrentDir();
                    fileCore.openDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getCanonicalPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.nav_music:
                try {
                    fileCore.clearCurrentDir();
                    fileCore.openDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getCanonicalPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.nav_loadings:
                try {
                    fileCore.clearCurrentDir();
                    fileCore.openDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getCanonicalPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
