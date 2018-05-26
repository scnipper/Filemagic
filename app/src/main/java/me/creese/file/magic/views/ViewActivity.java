package me.creese.file.magic.views;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.creese.file.magic.P;
import me.creese.file.magic.util.Saves;

/**
 * Created by scnipper on 20.05.2018.
 */

public class ViewActivity extends LinearLayout {
    private final Intent intent;
    private final ResolveInfo resolveInfo;
    private final CheckBox checkBox;
    private final AlertDialog dialog;
    private ImageView icon;
    private TextView name;

    public ViewActivity(@NonNull Context context, Intent intent, ResolveInfo resolveInfo, CheckBox checkBox, AlertDialog dialog) {
        super(context);
        this.intent = intent;
        this.resolveInfo = resolveInfo;
        this.checkBox = checkBox;
        this.dialog = dialog;
        setParams();
        setViews();

    }

    private void setViews() {
        icon = new ImageView(getContext());
        addView(icon);
        ((LayoutParams) icon.getLayoutParams()).gravity = Gravity.CENTER_VERTICAL;
        icon.setMaxWidth(P.getPixelFromDP(50));

        name = new TextView(getContext());
        name.setTextSize(20);

        addView(name);
        ((LayoutParams) name.getLayoutParams()).gravity = Gravity.CENTER_VERTICAL;
    }

    private void setParams() {
        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(HORIZONTAL);

        setOnClickListener(l -> {

            if (checkBox.isChecked()) {
                Saves.getInstanse().save.putString(intent.getStringExtra("ext"),
                        resolveInfo.activityInfo.packageName+"/"+resolveInfo.activityInfo.name).apply();
            }
            else {
                if(intent.getBooleanExtra("how",false)){
                    Saves.getInstanse().save.remove(intent.getStringExtra("ext")).apply();
                }
            }

            intent.setClassName(resolveInfo.activityInfo.packageName,resolveInfo.activityInfo.name);

            dialog.dismiss();
            getContext().startActivity(intent);
        });

    }
    public void setName(CharSequence res) {
        try {
            name.setText(res);
        }
        catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }
    public void setIcon(Drawable res) {
        icon.setImageDrawable(res);
    }
}
