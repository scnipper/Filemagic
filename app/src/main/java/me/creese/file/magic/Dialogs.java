package me.creese.file.magic;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by scnipper on 30.04.2018.
 */

public class Dialogs {

    private  AlertDialog dialogCopyAndMove;
    private  AlertDialog dialogSureCopyAndMove;
    private  AlertDialog dialogRename;

    private static Dialogs instanse;
    private AlertDialog dialogDelete;

    private Dialogs() {
    }

    public static Dialogs getInstanse() {
        if(instanse == null)
        return new Dialogs();
        else return instanse;
    }

    public void showDialogCopyAndMove(Context context, ClickOnItem clickOnItem) {


        if(dialogCopyAndMove == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.what_do)
                    .setItems(R.array.items_move_copy, (dialog, which) -> {
                        clickOnItem.what(which);
                    });


            dialogCopyAndMove = builder.create();
        }
        dialogCopyAndMove.show();
    }

    public void showDialogSureCopyMove(Context context,ClickOnItem clickOnItem,int textTitle) {


        if(dialogSureCopyAndMove == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(textTitle)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        if(textTitle == R.string.copy_elem) clickOnItem.what(1);
                        if(textTitle == R.string.move_elem) clickOnItem.what(2);

                    })
                    .setNegativeButton(R.string.no, null);



            dialogSureCopyAndMove = builder.create();
        }

        dialogSureCopyAndMove.setTitle(textTitle);
        dialogSureCopyAndMove.show();
    }

    public void showDialogRename(Context context, ClickOnItem clickOnItem, String name) {
        EditText renameText = new EditText(context);

        if(dialogRename == null) {




            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.rename)
                    .setPositiveButton(R.string.ok, (dialog, which) -> {
                        clickOnItem.what(renameText.getText().toString());
                        hideKeyBoard(context);

                    })
                    .setNegativeButton(R.string.cancel, (dialog, which) ->  {
                        hideKeyBoard(context);
                    });



            dialogRename = builder.create();
        }

        renameText.setText(name);
        renameText.selectAll();
        dialogRename.setView(renameText,P.getPixelFromDP(15),0,P.getPixelFromDP(15),0);
        showKeyboard(context);

        dialogRename.show();
    }

    public void hideKeyBoard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

       // imm.hideSoftInputFromWindow()
    }

    public void showDeleteDialog(Context context,ClickOnItem clickOnItem) {
        if(dialogDelete == null) {




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
    public void destroy() {
        dialogCopyAndMove = null;
    }

    public void showKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


    }

    public interface ClickOnItem{
        void what(Object which);
    }
}
