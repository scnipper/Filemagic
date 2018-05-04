package me.creese.file.magic;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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


        if (dialogCopyAndMove == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.what_do)
                    .setItems(R.array.items_move_copy, (dialog, which) -> {
                        clickOnItem.what(which);
                    });


            dialogCopyAndMove = builder.create();
        }
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

        if (dialogRename == null) {


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
        }

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
        if (dialogDelete == null) {


            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setPositiveButton(R.string.ok, (dialog, which) -> {
                clickOnItem.what(which);
                hideKeyBoard(context);

            })
                    .setNegativeButton(R.string.cancel, null);


            dialogDelete = builder.create();
        }
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
    public void tickFullProgress(int progFull,String nameFull) {
        if(dialogTick != null) {

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
    }

    public void showKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


    }

    public interface ClickOnItem {
        void what(Object which);
    }
}
