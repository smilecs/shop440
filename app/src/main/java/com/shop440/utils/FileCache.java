package com.shop440.utils;

import android.content.Context;

import java.io.File;
import java.net.URLEncoder;

/**
 * Created by SMILECS on 8/10/16.
 */
public class FileCache {

    private File cacheDir;

    public FileCache(Context context) {
        // Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            cacheDir = new File(
                    android.os.Environment.getExternalStorageDirectory(),
                    "Shop440");
        }
        else {
            cacheDir = new File(context.getCacheDir(), "Shop440");
        }
        if (!cacheDir.exists()){
            cacheDir.mkdirs();
        }
    }

    public File getFile(String url, String type) {
        // I identify images by hashcode. Not a perfect solution, good for the
        // demo.
        String filename = URLEncoder.encode(url) + "." +type;
        // String filename = URLEncoder.encode(url);
        File f = new File(cacheDir, filename);
        return f;

    }

    public String directory(){
        return cacheDir.getAbsolutePath();
    }

    public void clear() {
        File[] files = cacheDir.listFiles();
        if (files == null)
            return;
        for (File f : files)
            f.delete();
    }

}
