package me.creese.file.magic;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import me.creese.file.magic.ui.FileCard;
import me.creese.file.magic.util.SortFiles;

/**
 * Created by scnipper on 25.04.2018.
 */

public class AdapterFiles extends RecyclerView.Adapter<AdapterFiles.RecycleHolder> {


    private ArrayList<ModelFiles> items;
    private ArrayList<File> savedFiles;
    private FileCore fileCore;
    private boolean slectedMode;
    private HashMap<String, ArrayList<ModelFiles>> savedStates;
    private boolean modeMoveCopy;
    private boolean isSaveData;

    public AdapterFiles() {
        items = new ArrayList<>();
        savedStates = new HashMap<>();
        savedFiles = new ArrayList<>();
    }

    public void addItem(ModelFiles model) {
        if (modeMoveCopy) model.setModeCopyAndMove(true);
        items.add(model);
    }

    @NonNull
    @Override
    public AdapterFiles.RecycleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecycleHolder(new FileCard(parent.getContext(), fileCore));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFiles.RecycleHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ArrayList<ModelFiles> getItems() {
        return items;
    }

    public void setItems(ArrayList<ModelFiles> items) {
        this.items = items;
    }

    public void clear() {
        if (items.size() > 0) {
            items.clear();
            notifyDataSetChanged();
        }
    }

    public void setFileCore(FileCore fileCore) {
        this.fileCore = fileCore;
    }

    public void setSelectedMode() {
        if (!slectedMode) {
            for (int i = 0; i < getItemCount(); i++) {
                items.get(i).setSelectedMode(true);

            }

            fileCore.getActivity().addIconsToToolbar(MainActivity.GROUP_ACTIONS);
            fileCore.getActivity().getFab().hide();
            fileCore.getActivity().getTextDir().showCheckBox();

            slectedMode = true;
            notifyDataSetChanged();
        }
    }

    public void deselectAll() {
        for (int i = 0; i < getItemCount(); i++) {
            items.get(i).setSelect(false);
            items.get(i).setSelectedMode(false);
        }
        slectedMode = false;
        fileCore.getActivity().getFab().show();
    }

    public void deleteSelected() {
        for (int i = 0; i < getItemCount(); i++) {
            items.get(i).setSelectedMode(false);

            if (items.get(i).isSelect()) {
                notifyItemRemoved(i);
                items.remove(i);
                i--;
            } else {
                notifyItemChanged(i);
            }
        }

        fileCore.getActivity().getTextDir().hideCheckBox();

        slectedMode = false;


    }

    public void setModeMoveAndCopy(boolean mode) {
        boolean isCallDataChanged = false;
        for (int i = 0; i < getItemCount(); i++) {
            items.get(i).setModeCopyAndMove(mode);
            items.get(i).setSelectedMode(false);
            if (!mode) {
                if (items.get(i).isSelect()) {
                    items.get(i).setSelect(false);
                    isCallDataChanged = true;
                }
            }
        }


        if (!mode) slectedMode = false;
        else fileCore.getActivity().getTextDir().hideCheckBox();
        modeMoveCopy = mode;
        isSaveData = mode;
        /*if (!mode && isCallDataChanged)*/ notifyDataSetChanged();
    }

    public void checkIsEmptySelect() {
        for (int i = 0; i < getItemCount(); i++) {
            if (items.get(i).isSelect()) return;
        }
        fileCore.getActivity().hideToolbarIcon(MainActivity.GROUP_ACTIONS);
        fileCore.getActivity().getTextDir().hideCheckBox();
        deselectAll();
        notifyDataSetChanged();
    }

    public boolean restoreData() {
        if (savedStates.size() > 0) {
            ArrayList<ModelFiles> state = savedStates.get(fileCore.getCurrentDir());
            if (state != null) {
                items = (ArrayList<ModelFiles>) state.clone();
                notifyDataSetChanged();
                savedStates.clear();
                isSaveData = true;
                return true;
            }

        }
        return false;
    }

    public void saveData() {
        if (isSaveData) {
            savedStates.put(fileCore.getCurrentDir().toString(), (ArrayList<ModelFiles>) items.clone());
            isSaveData = false;

        }
    }

    public File getSelectedFile() {
        for (int i = 0; i < getItemCount(); i++) {
            if (items.get(i).isSelect()) {
                return new File(fileCore.getCurrentDir() + items.get(i).getName());
            }
        }
        return null;
    }

    public void saveSelectedFiles() {
        savedFiles.clear();
        for (int i = 0; i < getItemCount(); i++) {
            if (items.get(i).isSelect()) {
                savedFiles.add(new File(fileCore.getCurrentDir() + items.get(i).getName()));
            }
        }
    }

    public File[] getSavedFiles() {
        return savedFiles.toArray(new File[0]);
    }

    public HashMap<String, ArrayList<ModelFiles>> getSavedStates() {
        return savedStates;
    }

    public void selectAll() {
        for (int i = 0; i < getItemCount(); i++) {
            items.get(i).setSelect(true);
        }
        notifyDataSetChanged();
        checkIsMoreOneSelected();
    }

    public void checkIsMoreOneSelected() {
        int count = 0;
        for (int i = 0; i < getItemCount(); i++) {
            if(items.get(i).isSelect()) count++;
            if(count > 1) {
               // fileCore.getActivity().hideIconRename();
                fileCore.getActivity().hideToolbarIcon(MainActivity.GROUP_FILE_OPTIONS);
                fileCore.getActivity().hideToolbarIcon(MainActivity.GROUP_ICON_RENAME);
                return;
            }
        }

        if(count == 1) {
            //fileCore.getActivity().showIconRename();
            fileCore.getActivity().addIconsToToolbar(MainActivity.GROUP_ICON_RENAME);
            if(getSelectedFile().isFile())
            fileCore.getActivity().addIconsToToolbar(MainActivity.GROUP_FILE_OPTIONS);
        }
        else if(count == 0 ) {
            fileCore.getActivity().hideToolbarIcon(MainActivity.GROUP_FILE_OPTIONS);
            fileCore.getActivity().hideToolbarIcon(MainActivity.GROUP_ICON_RENAME);
        }

    }

    public void sortItem(SortArrays howSort) {
        switch (howSort) {
            case BY_NAME_HIGH:
                SortFiles<String> sortFiles = new SortFiles<>();
                sortFiles.sorting(true,this,"getName");

                break;
            case BY_NAME_LOW:
                SortFiles<String> sortFiles1 = new SortFiles<>();
                sortFiles1.sorting(false,this,"getName");
                break;

            case BY_DATE_HIGH:
                SortFiles<Long> sortFiles2 = new SortFiles<>();
                sortFiles2.sorting(true,this,"getLongDate");
                break;
            case BY_DATE_LOW:
                SortFiles<Long> sortFiles3 = new SortFiles<>();
                sortFiles3.sorting(false,this,"getLongDate");
                break;
            case BY_TYPE_LOW:
                SortFiles<String> sortFiles4 = new SortFiles<>();
                sortFiles4.sorting(false,this,"getExtension");
                break;
            case BY_TYPE_HIGH:
                SortFiles<String> sortFiles5 = new SortFiles<>();
                sortFiles5.sorting(true,this,"getExtension");
        }
    }




    public class RecycleHolder extends RecyclerView.ViewHolder {

        private final FileCard fileCard;

        public RecycleHolder(View itemView) {
            super(itemView);

            fileCard = (FileCard) itemView;

        }

        public void bind(int position) {
            ModelFiles modelFiles = items.get(position);

            fileCard.setModel(modelFiles);

            fileCard.clear();

            fileCard.setName(modelFiles.getName());
            fileCard.setDir(modelFiles.isDir());
            fileCard.setTextSize(modelFiles.getSize());
            fileCard.setPerm(modelFiles.getPermissons());
            fileCard.setDate(modelFiles.getDate());

            if (modelFiles.isSelect()) fileCard.setSelect();
            if (modelFiles.isSelectedMode()) fileCard.showCheckBox();

            if(modelFiles.isModeCopyAndMove()) fileCard.hideCheckBox();

            if (modelFiles.isLoadImagePreview()) fileCard.loadPreviewImage();
            //fileCard.setSelectedMode(items.get(position).isSelectedMode());


        }
    }

    enum SortArrays {
        BY_NAME_HIGH,
        BY_NAME_LOW,
        BY_TYPE_HIGH,
        BY_TYPE_LOW,
        BY_DATE_HIGH,
        BY_DATE_LOW
    }
}
