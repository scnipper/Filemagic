package me.creese.file.magic;

/**
 * Created by scnipper on 29.04.2018.
 */



public interface OpenWithHandler {
    void what(WhatOpenFile what);

    enum WhatOpenFile {
        TEXT("text/*"),
        AUDIO("audio/*"),
        IMAGE("image/*"),
        VIDEO("video/*"),
        OTHER("*/*");

        private final String text;

        WhatOpenFile(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }
}
