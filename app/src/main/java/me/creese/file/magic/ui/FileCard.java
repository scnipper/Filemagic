package me.creese.file.magic.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.creese.file.magic.FileCore;
import me.creese.file.magic.P;
import me.creese.file.magic.R;

/**
 * Created by scnipper on 25.04.2018.
 */

public class FileCard extends CardView {
    private final FileCore fileCore;
    private TextView textName;
    private ImageView icon;
    private boolean isDir;

    public FileCard(@NonNull Context context, FileCore fileCore) {
        super(context);
        this.fileCore = fileCore;
        setParams();
        addViews();
    }

    private void addViews() {
        icon = new ImageView(getContext());



        LinearLayout layout = new LinearLayout(getContext());
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        textName = new TextView(getContext());

        layout.addView(icon);
        layout.addView(textName);

        ((LinearLayout.LayoutParams) icon.getLayoutParams()).leftMargin = P.getPixelFromDP(10);
        ((LinearLayout.LayoutParams) icon.getLayoutParams()).topMargin = P.getPixelFromDP(10);
        ((LinearLayout.LayoutParams) icon.getLayoutParams()).bottomMargin = P.getPixelFromDP(10);

        ((LinearLayout.LayoutParams) textName.getLayoutParams()).leftMargin = P.getPixelFromDP(15);
        textName.setTextColor(0xffff0000);





        addView(layout);

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
            setIcon(R.drawable.file);
        }
    }

    private void setParams() {
        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        ((LinearLayout.LayoutParams) getLayoutParams()).setMargins(P.getPixelFromDP(10),
                P.getPixelFromDP(10),P.getPixelFromDP(10),0);
        setRadius(P.getPixelFromDP(7));


        setOnClickListener(l -> {
            if(isDir)
            fileCore.openDir(textName.getText().toString());
        });
    }
}
