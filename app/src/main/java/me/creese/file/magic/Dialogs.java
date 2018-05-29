package me.creese.file.magic;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import me.creese.file.magic.views.ViewActivity;

/**
 * Created by scnipper on 30.04.2018.
 */

public class Dialogs {

    private static Dialogs instanse;
    private AlertDialog dialogCopyAndMove;
    private AlertDialog dialogSureCopyAndMove;
    private AlertDialog dialogRename;
    private AlertDialog dialogDelete;
    private AlertDialog dialogTick;
    private ProgressBar progressOneFile;
    private ProgressBar progressFull;
    private TextView text1;
    private TextView text2;

    private Dialogs() {
    }

    public static Dialogs getInstanse() {
        if (instanse == null) {
            instanse = new Dialogs();
        }
        return instanse;

    }

    public void showDialogCopyAndMove(Context context, ClickOnItem clickOnItem) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.what_do)
                .setItems(R.array.items_move_copy, (dialog, which) -> {
                    clickOnItem.what(which);
                });


        dialogCopyAndMove = builder.create();

        dialogCopyAndMove.show();
    }

    public void showDialogSureCopyMove(Context context, ClickOnItem clickOnItem, int textTitle) {


        if (dialogSureCopyAndMove == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(textTitle)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        if (textTitle == R.string.copy_elem) clickOnItem.what(1);
                        if (textTitle == R.string.move_elem) clickOnItem.what(2);

                    })
                    .setNegativeButton(R.string.no, null);


            dialogSureCopyAndMove = builder.create();
        }

        dialogSureCopyAndMove.setTitle(textTitle);
        dialogSureCopyAndMove.show();
    }

    public void showDialogRename(Context context, ClickOnItem clickOnItem, String name) {
        EditText renameText = new EditText(context);


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.rename)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    clickOnItem.what(renameText.getText().toString());
                    hideKeyBoard(context);

                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    hideKeyBoard(context);
                });


        dialogRename = builder.create();


        renameText.setText(name);
        renameText.selectAll();
        dialogRename.setView(renameText, P.getPixelFromDP(15), 0, P.getPixelFromDP(15), 0);
        showKeyboard(context);

        dialogRename.show();
    }

    public void hideKeyBoard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        // imm.hideSoftInputFromWindow()
    }

    public void showDeleteDialog(Context context, ClickOnItem clickOnItem) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            clickOnItem.what(which);
            //hideKeyBoard(context);

        }).setTitle(R.string.is_delete_this)
                .setNegativeButton(R.string.no, null);


        dialogDelete = builder.create();

        dialogDelete.show();
    }

    public void showTickDialog(Context context, Integer type) {


        if (dialogTick == null) {
            LinearLayout layout = new LinearLayout(context);
            layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            layout.setOrientation(LinearLayout.VERTICAL);


            progressOneFile = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);

            progressOneFile.setIndeterminate(false);
            progressOneFile.setMax(100);


            progressFull = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
            progressFull.setIndeterminate(false);

            progressFull.setMax(100);


            text1 = new TextView(context);

            text2 = new TextView(context);


            layout.addView(text1);
            layout.addView(progressFull);
            layout.addView(text2);
            layout.addView(progressOneFile);


            ((LinearLayout.LayoutParams) progressFull.getLayoutParams()).leftMargin = P.getPixelFromDP(20);
            ((LinearLayout.LayoutParams) progressFull.getLayoutParams()).rightMargin = P.getPixelFromDP(20);

            ((LinearLayout.LayoutParams) progressOneFile.getLayoutParams()).leftMargin = P.getPixelFromDP(20);
            ((LinearLayout.LayoutParams) progressOneFile.getLayoutParams()).rightMargin = P.getPixelFromDP(20);
            ((LinearLayout.LayoutParams) progressOneFile.getLayoutParams()).bottomMargin = P.getPixelFromDP(20);


            ((LinearLayout.LayoutParams) text1.getLayoutParams()).leftMargin = P.getPixelFromDP(20);
            ((LinearLayout.LayoutParams) text1.getLayoutParams()).topMargin = P.getPixelFromDP(20);

            ((LinearLayout.LayoutParams) text2.getLayoutParams()).leftMargin = P.getPixelFromDP(20);
            ((LinearLayout.LayoutParams) text2.getLayoutParams()).topMargin = P.getPixelFromDP(20);


            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false)
                    .setView(layout);


            dialogTick = builder.create();
        }
        int title = 0;
        if (type == 1) {
            title = R.string.copy_elem_progress;
        }
        if (type == 2) {
            title = R.string.move_elem_progress;
        }
        dialogTick.setTitle(title);
        dialogTick.show();

    }

    public void tickFullProgress(int progFull, String nameFull) {
        if (dialogTick != null) {

            text1.setText(nameFull);
            progressFull.setProgress(progFull);
            if (progressFull.getProgress() == progressFull.getMax()) {
                dialogTick.dismiss();
            }
        }
    }

    public void tickFileProgress(int progFile, String nameFile) {

        if (dialogTick != null) {


            progressOneFile.setProgress(progFile);

            text2.setText(nameFile);


        }
    }


    public void destroy() {
        dialogCopyAndMove = null;
        dialogRename = null;
    }

    public void showKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


    }

    public void showDialogListActivities(Context context, List<ResolveInfo> pkgAppsList, Intent intent) {

        LinearLayout root = new LinearLayout(context);
        root.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        root.setOrientation(LinearLayout.VERTICAL);

        ScrollView scrollView = new ScrollView(context);
        scrollView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        scrollView.addView(linearLayout);

        root.addView(scrollView);

        ((LinearLayout.LayoutParams) scrollView.getLayoutParams()).weight = 0.9f;

        CheckBox checkBox = new CheckBox(context);
        checkBox.setText(R.string.remember_choice);
        root.addView(checkBox);


        if (intent.getStringExtra("ext") == null) {
            checkBox.setEnabled(false);
        } else if (intent.getBooleanExtra("how", false)) {
            checkBox.setChecked(true);
        }


        ((LinearLayout.LayoutParams) checkBox.getLayoutParams()).weight = 0.1f;


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.open_with)
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    hideKeyBoard(context);
                });


        AlertDialog dialog = builder.create();


        for (ResolveInfo resolveInfo : pkgAppsList) {
            ViewActivity viewActivity = new ViewActivity(context, intent, resolveInfo, checkBox, dialog);
            viewActivity.setIcon(resolveInfo.loadIcon(context.getPackageManager()));
            viewActivity.setName(resolveInfo.loadLabel(context.getPackageManager()));
            linearLayout.addView(viewActivity);


        }

        dialog.setView(root, P.getPixelFromDP(15), 0, P.getPixelFromDP(15), 0);


        dialog.show();
    }

    public void showSortDialog(MainActivity context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.sort);

        LinearLayout root = new LinearLayout(context);

        root.setOrientation(LinearLayout.HORIZONTAL);
        root.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));


        root.addView(genElement(context, R.string.name, id -> {
            if (id == 1) {
                context.getAdapter().sortItem(AdapterFiles.SortArrays.BY_NAME_HIGH);
            } else {
                context.getAdapter().sortItem(AdapterFiles.SortArrays.BY_NAME_LOW);
            }

            dialogDelete.dismiss();

        }));
        root.addView(genElement(context, R.string.type, id -> {
            if (id == 1) {
                context.getAdapter().sortItem(AdapterFiles.SortArrays.BY_TYPE_HIGH);
            } else {
                context.getAdapter().sortItem(AdapterFiles.SortArrays.BY_TYPE_LOW);
            }
            dialogDelete.dismiss();
        }));
        root.addView(genElement(context, R.string.date, id -> {
            if (id == 1) {
                context.getAdapter().sortItem(AdapterFiles.SortArrays.BY_DATE_HIGH);
            } else {
                context.getAdapter().sortItem(AdapterFiles.SortArrays.BY_DATE_LOW);
            }
            dialogDelete.dismiss();
        }));


        dialogDelete = builder.create();

        dialogDelete.setView(root);
        dialogDelete.show();
    }

    private LinearLayout genElement(Context context, int idTitle, OnClickSortBtn click) {
        LinearLayout name = new LinearLayout(context);
        name.setOrientation(LinearLayout.VERTICAL);

        name.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        ((LinearLayout.LayoutParams) name.getLayoutParams()).weight = 0.3f;

        TextView titleName = new TextView(context);

        titleName.setText(idTitle);
        titleName.setTextSize(18);
        titleName.setGravity(Gravity.CENTER_HORIZONTAL);
        ImageButton iconTop = new ImageButton(context);
        iconTop.setImageResource(R.drawable.baseline_vertical_align_top_white_36);
        iconTop.setBackground(null);
        ImageButton iconBottom = new ImageButton(context);
        iconBottom.setBackground(null);
        iconBottom.setImageResource(R.drawable.baseline_vertical_align_bottom_white_36);

        iconTop.setOnClickListener(l -> click.click(1));
        iconBottom.setOnClickListener(l -> click.click(0));

        name.addView(titleName);

        name.addView(iconTop);
        name.addView(iconBottom);
        return name;
    }

    interface OnClickSortBtn {
        void click(int id);
    }

    public interface ClickOnItem {
        void what(Object which);
    }
}
