package me.creese.file.magic.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

import me.creese.file.magic.FileCore;
import me.creese.file.magic.ModelFiles;
import me.creese.file.magic.OpenWithHandler;
import me.creese.file.magic.P;
import me.creese.file.magic.R;
import me.creese.file.magic.util.LoadImage;

/**
 * Created by scnipper on 25.04.2018.
 */

public class FileCard extends CardView {
    private final FileCore fileCore;
    private TextView textName;
    private ImageView icon;
    private boolean isDir;
    private TextView textSize;
    private TextView textPerm;
    private TextView textDate;
    private boolean longClick;
    private FrameLayout frameLayout;
    private boolean select;
    //private boolean selectedMode;
    private ModelFiles model;
    private CheckBox checkBox;

    public FileCard(@NonNull Context context, FileCore fileCore) {
        super(context);
        this.fileCore = fileCore;
        setParams();
        addViews();
    }

    private void addViews() {
        icon = new ImageView(getContext());

        frameLayout = new FrameLayout(getContext());
        frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));




        LinearLayout layout = new LinearLayout(getContext());
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout horLayout = new LinearLayout(getContext());
        horLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        horLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout verLayout = new LinearLayout(getContext());
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        verLayout.setOrientation(LinearLayout.VERTICAL);

        checkBox = new CheckBox(getContext());
        checkBox.setVisibility(GONE);
        checkBox.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        ((LayoutParams) checkBox.getLayoutParams()).gravity = Gravity.CENTER_VERTICAL|Gravity.END;
        checkBox.setOnClickListener(l->{
            if(model.isModeCopyAndMove()) return;
            setSelect();
        });

        textName = new TextView(getContext());
        textName.setTextColor(0xffCFD6ED);

        textSize = new TextView(getContext());
        textSize.setTextColor(0xffFCB2CE);
        textSize.setTextSize(10);
        textSize.setLayoutParams(new LinearLayout.LayoutParams(P.getPixelFromDP(120), ViewGroup.LayoutParams.WRAP_CONTENT));

        textPerm = new TextView(getContext());
        textPerm.setTextColor(0xffFCB2CE);
        textPerm.setTextSize(10);

        textDate = new TextView(getContext());
        textDate.setTextColor(0xffFCB2CE);
        textDate.setTextSize(10);

        horLayout.addView(textSize);
        horLayout.addView(textPerm);
        horLayout.addView(textDate);
        frameLayout.addView(checkBox);


        verLayout.addView(textName);
        verLayout.addView(horLayout);

        layout.addView(icon);
        layout.addView(verLayout);


        ((LinearLayout.LayoutParams) icon.getLayoutParams()).leftMargin = P.getPixelFromDP(10);
        ((LinearLayout.LayoutParams) icon.getLayoutParams()).topMargin = P.getPixelFromDP(10);
        ((LinearLayout.LayoutParams) icon.getLayoutParams()).bottomMargin = P.getPixelFromDP(10);

        ((LinearLayout.LayoutParams) verLayout.getLayoutParams()).leftMargin = P.getPixelFromDP(15);
        ((LinearLayout.LayoutParams) textSize.getLayoutParams()).topMargin = P.getPixelFromDP(17);
        ((LinearLayout.LayoutParams) textPerm.getLayoutParams()).topMargin = P.getPixelFromDP(17);
        ((LinearLayout.LayoutParams) textDate.getLayoutParams()).topMargin = P.getPixelFromDP(17);
        ((LinearLayout.LayoutParams) textDate.getLayoutParams()).leftMargin = P.getPixelFromDP(50);






        frameLayout.addView(layout);
        addView(frameLayout);

    }
    private void setIcon(int iconId) {
        icon.setImageResource(iconId);
    }

    public void setName(String name) {
        textName.setText(name);
    }


    public void setDir(boolean dir) {
        isDir = dir;
        if(isDir) {
            setIcon(R.drawable.ic_folder_white_36dp);
        }
        else {
            setIcon(R.drawable.ic_insert_drive_file_white_36dp);
        }
    }



    private void setParams() {

        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        ((LinearLayout.LayoutParams) getLayoutParams()).setMargins(P.getPixelFromDP(10),
                P.getPixelFromDP(10),P.getPixelFromDP(10),0);
        setRadius(P.getPixelFromDP(7));
        setPreventCornerOverlap(false);

        setOnClickListener(l -> {
            if(longClick) {
                longClick = false;
                return;
            }

            if(model.isSelectedMode()) {
                setSelect();
                return;
            }








            if(isDir) {
                if(!model.isSelect())
                fileCore.openDir(textName.getText().toString());
            }
            else if(!model.isModeCopyAndMove()){

                fileCore.getActivity().showDialogOpenWith(what -> {
                    Intent intent = new Intent();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                        Uri contentUri = FileProvider.getUriForFile(getContext(),
                                fileCore.getActivity().getApplicationContext().getPackageName() + ".provider",
                                new File(fileCore.getCurrentDir().toString() + textName.getText()));
                        Intent openFileIntent = new Intent(Intent.ACTION_VIEW);
                        openFileIntent.setDataAndTypeAndNormalize(contentUri, what.toString());
                        openFileIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        fileCore.getActivity().startActivity(openFileIntent);
                    } else {

                        Uri fileUri = Uri.fromFile(new File(fileCore.getCurrentDir().toString() + textName.getText()));

                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(fileUri, what.toString());
                        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        fileCore.getActivity().startActivity(intent);
                    }



                });



            }
        });

        setOnLongClickListener(l -> {
            if((model.isSelectedMode() && model.isSelect()) || model.isModeCopyAndMove()) return false;

            longClick = true;

            setSelect();


            return false;
        });
    }

    public void setSelect() {


        if(select) {
            deselect();
            return;
        }


        showCheckBox();

        checkBox.setChecked(true);
        model.setSelect(true);
        select = true;
        frameLayout.setBackgroundColor(0xff26746B);
        fileCore.getActivity().getAdapter().setSelectedMode();
        fileCore.getActivity().getAdapter().checkIsMoreOneSelected(true);


    }
    public void showCheckBox() {
        checkBox.setVisibility(VISIBLE);
    }

    public void setTextSize(String textSize) {
        this.textSize.setText(textSize);
    }

    public void setPerm(String perm) {
        textPerm.setText(perm);
    }
    public void setDate(String date) {
        textDate.setText(date);
    }

    public void clear() {
        frameLayout.setBackgroundColor(0);
        select = false;
        if (model.isSelectedMode()) {
            checkBox.setChecked(false);
        }
        else {
            hideCheckBox();
        }

    }

    private void deselect() {

        frameLayout.setBackgroundColor(0);
        select = false;
        model.setSelect(false);
        fileCore.getActivity().getAdapter().checkIsEmptySelect();
        checkBox.setChecked(false);
        fileCore.getActivity().getAdapter().checkIsMoreOneSelected(false);
    }



    public void setModel(ModelFiles model) {
        this.model = model;
    }

    public void loadPreviewImage() {

        LoadImage.loadFull(fileCore.getCurrentDir()+model.getName(),icon);
    }

    public void hideCheckBox() {
        checkBox.setChecked(false);
        checkBox.setVisibility(GONE);
    }
}
