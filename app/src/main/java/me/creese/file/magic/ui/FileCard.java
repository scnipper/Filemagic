package me.creese.file.magic.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.creese.file.magic.P;
import me.creese.file.magic.R;

/**
 * Created by scnipper on 25.04.2018.
 */

public class FileCard extends CardView {
    private TextView textName;

    public FileCard(@NonNull Context context) {
        super(context);

        setParams();
        addViews();
    }

    private void addViews() {
        ImageView icon = new ImageView(getContext());

        icon.setImageResource(R.drawable.ic_folder_white_36dp);

        LinearLayout layout = new LinearLayout(getContext());
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        textName = new TextView(getContext());

        layout.addView(icon);
        layout.addView(textName);

        ((LinearLayout.LayoutParams) textName.getLayoutParams()).leftMargin = P.getPixelFromDP(15);
        textName.setTextColor(0xffff0000);





        addView(layout);

    }

    public void setName(String name) {
        textName.setText(name);
    }

    private void setParams() {
        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        ((LinearLayout.LayoutParams) getLayoutParams()).setMargins(P.getPixelFromDP(10),
                P.getPixelFromDP(10),P.getPixelFromDP(10),0);
        setRadius(P.getPixelFromDP(10));

    }
}
