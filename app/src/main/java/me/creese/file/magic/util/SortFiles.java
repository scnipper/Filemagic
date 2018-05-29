package me.creese.file.magic.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import me.creese.file.magic.AdapterFiles;
import me.creese.file.magic.ModelFiles;

public class SortFiles<T> {

    public SortFiles() {
    }

    public void sorting(boolean what, AdapterFiles adapterFiles, String method) {
        Method whatGet = null;
        ArrayList<T> names = new ArrayList<>();
        for (int i = 0; i < adapterFiles.getItemCount(); i++) {
            try {
                whatGet = adapterFiles.getItems().get(i).getClass().getMethod(method,null);
                names.add((T) whatGet.invoke(adapterFiles.getItems().get(i),null));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        T[] arrNames = (T[]) names.toArray();
        if(what)
            Arrays.sort(arrNames);
        else Arrays.sort(arrNames, Collections.reverseOrder());

        ArrayList<ModelFiles> newItems = new ArrayList<>();


        for (int i = 0; i < arrNames.length; i++) {
            ModelFiles m = null;
            for (int j = 0; j < adapterFiles.getItemCount(); j++) {
                try {
                    if(whatGet.invoke(adapterFiles.getItems().get(j),null).equals(arrNames[i])) {
                        m = adapterFiles.getItems().get(j);

                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            newItems.add(m);
            adapterFiles.getItems().remove(m);
        }

        adapterFiles.setItems(newItems);
        adapterFiles.notifyDataSetChanged();
    }
}
