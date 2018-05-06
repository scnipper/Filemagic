package me.creese.file.magic.util;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.LinearLayout;

import me.creese.file.magic.P;

/**
 * Created by scnipper on 29.01.2018.
 */

public class LoadImage implements Runnable {


    private static LruCache<String, Bitmap> imageCache;
    private static Activity act;
    private final String path;
    private ImageView imageView;


    private LoadImage(String path, ImageView imageView) {
        this.path = path;

        this.imageView = imageView;

    }

    public static void setActivity(Activity activity) {
        act = activity;
    }

    public static LoadImage loadFull(String path, ImageView imageView) {

        if (imageCache == null) {
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory());

            // Use 1/8th of the available memory for this memory cache.
            final int cacheSize = maxMemory / 8;
            imageCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount();
                }
            };
        }
        LoadImage loadImage = new LoadImage(path, imageView);
        Thread thread = new Thread(loadImage, "Full image load");
        thread.start();
        return loadImage;
    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Реальные размеры изображения
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Вычисляем наибольший inSampleSize, который будет кратным двум
            // и оставит полученные размеры больше, чем требуемые
            while ((height / inSampleSize) > reqHeight
                    && (width / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    @Override
    public void run() {

        BitmapFactory.Options options = new BitmapFactory.Options();

        if (imageCache.get(path) != null) {

            act.runOnUiThread(()->{
                imageView.setImageBitmap(imageCache.get(path));
            });
            return;
        }

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int sampleSize = calculateInSampleSize(options, 128, 128);
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;

        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap tmp = BitmapFactory.decodeFile(path, options);

        int newWidth = imageView.getWidth();
        int newHeight = (int) (newWidth / ((float)tmp.getWidth()/tmp.getHeight()));




        if(newHeight > 0 && newWidth > 0)
        tmp = Bitmap.createScaledBitmap(tmp,newWidth,newHeight,false);
        if(newHeight > imageView.getHeight()) {
            tmp = Bitmap.createBitmap(tmp,0,0,tmp.getWidth(),imageView.getHeight());
        }


        try {
            imageCache.put(path, tmp);
        }
        catch (NullPointerException e) {
            e.printStackTrace();
        }


        Bitmap finalTmp = tmp;
        act.runOnUiThread(()->{



            imageView.setImageBitmap(finalTmp);

        });


    }

}
