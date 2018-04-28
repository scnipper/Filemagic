package me.creese.file.magic;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.creese.file.magic.ui.FileCard;

/**
 * Created by scnipper on 25.04.2018.
 */

public class AdapterFiles extends RecyclerView.Adapter<AdapterFiles.RecycleHolder> {


    private final ArrayList<ModelFiles> items;
    private FileCore fileCore;

    public AdapterFiles() {
        items = new ArrayList<>();
    }
    public void addItem(ModelFiles model) {
        items.add(model);
    }

    @NonNull
    @Override
    public AdapterFiles.RecycleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecycleHolder(new FileCard(parent.getContext(),fileCore));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFiles.RecycleHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void clear() {
        if(items.size() > 0) {
            items.clear();
            notifyDataSetChanged();
        }
    }

    public void setFileCore(FileCore fileCore) {
        this.fileCore = fileCore;
    }


    public class RecycleHolder extends RecyclerView.ViewHolder {

        private final FileCard fileCard;

        public RecycleHolder(View itemView) {
            super(itemView);

            fileCard = (FileCard) itemView;

        }

        public void bind(int position) {
            fileCard.setName(items.get(position).getName());
            fileCard.setDir(items.get(position).isDir());
            fileCard.setTextSize(items.get(position).getSize());
            fileCard.setPerm(items.get(position).getPermissons());
            fileCard.setDate(items.get(position).getDate());

        }
    }
}
