package me.creese.file.magic.views;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.Layout;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.creese.file.magic.FileCore;
import me.creese.file.magic.R;


/**
 * Created by scnipper on 26.04.2018.
 */

public class DirView extends LinearLayout {
    private final TextView textDir;

    public DirView(Context context, FileCore fileCore) {
        super(context);
        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        setClickable(true);
        setOrientation(HORIZONTAL);

        textDir = new TextView(getContext());
        textDir.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        textDir.setMaxLines(1);
        textDir.setEllipsize(TextUtils.TruncateAt.MARQUEE);

        textDir.setMovementMethod(new ScrollingMovementMethod());

        textDir.setGravity(Gravity.BOTTOM);

        textDir.setHorizontallyScrolling(true);
        textDir.setTextSize(20);

        textDir.setText(fileCore.getCurrentDir());

        EditText editDir = new EditText(getContext());
        editDir.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        editDir.setMaxLines(1);

        editDir.setMovementMethod(new ScrollingMovementMethod());

        editDir.setHorizontallyScrolling(true);
        editDir.setTextSize(20);

        ImageButton goBtn = new ImageButton(getContext());
        goBtn.setImageResource(R.drawable.ic_arrow_forward_white_36dp);
        goBtn.setBackground(null);

        goBtn.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        goBtn.setOnClickListener(l ->{
            removeView(editDir);
            removeView(goBtn);
            addView(textDir);


            fileCore.clearCurrentDir();
            fileCore.openDir(editDir.getText().toString());
        });

        ((LayoutParams) goBtn.getLayoutParams()).weight = 0.2f;

        ((LayoutParams) editDir.getLayoutParams()).weight = 0.8f;

        addView(textDir);


        textDir.setOnLongClickListener(v -> {
            editDir.setText(textDir.getText());
            removeView(textDir);

            addView(editDir);
            addView(goBtn);
            return true;
        });

    }


    public void setText(String text) {
        textDir.setText(text);
    }
}
