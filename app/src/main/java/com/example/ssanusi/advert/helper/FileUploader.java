package com.example.ssanusi.advert.helper;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.ssanusi.advert.advertApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class FileUploader {
    private static RequestBody requestFile;
    private final static String TAG = FileUploader.class.getSimpleName();

    @NonNull
    public static RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(okhttp3.MultipartBody.FORM, descriptionString);
    }

    @Nullable
    public static MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        Context context = advertApp.getInstance();
        assert context != null;

        File file = new File(fileUri.getPath());

        // create RequestBody instance from file
        //RequestBody requestFile = RequestBody.create(MediaType.parse(context.getContentResolver().getType(fileUri)), file);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }


//    @NonNull
//    @TargetApi(19)
//    public static MultipartBody.Part prepareFilePart(String partName, ImageCapture imageCapture) {
//        Context context = advertApp.getInstance();
////       File file = FileUtils.getFile(context, fileUri);
//        File file = new File(Uri.parse(imageCapture.getFileUrl()).getPath());
//
//        //File file = FileUtils.getFile(context, fileUri);
//        // create RequestBody instance from file
//        //RequestBody requestFile = RequestBody.create(MediaType.parse(context.getContentResolver().getType(fileUri)), file);
//
//        if (imageCapture.getFileUrl().endsWith(".jpg") || imageCapture.getFileUrl().endsWith(".png")){
//            Log.i(TAG,"It is entering the image block");
//            requestFile = RequestBody.create(MediaType.parse("image/*"), file);
//        } else {
//            Log.i(TAG,"It is entering the video block");
//            requestFile = RequestBody.create(MediaType.parse("video/*"), file);
//            // MultipartBody.Part is used to send also the actual file name
//        }
//        return MultipartBody.Part.createFormData(partName, imageCapture.getFileName() /*file.getName()*/,requestFile);
//    }

    public static long getFolderSize(File file) {
        long size = 0;
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                size += getFolderSize(child);
            }
        } else {
            size = file.length();
        }
        return size;
    }

    public static String getFolderSizeLabel(File file) {
        long size = getFolderSize(file) / 1024; // Get size and convert bytes into Kb.
        if (size >= 1024) {
            return (size / 1024) + " Mb";
        } else {
            return size + " Kb";
        }
    }


//    public static void createApplicationFolder() {
//        File f = new File(Environment.getExternalStorageDirectory(), File.separator + Config.VIDEO_COMPRESSOR_APPLICATION_DIR_NAME);
//        f.mkdirs();
//        f = new File(Environment.getExternalStorageDirectory(), File.separator + Config.VIDEO_COMPRESSOR_APPLICATION_DIR_NAME + Config.VIDEO_COMPRESSOR_COMPRESSED_VIDEOS_DIR);
//        f.mkdirs();
//        f = new File(Environment.getExternalStorageDirectory(), File.separator + Config.VIDEO_COMPRESSOR_APPLICATION_DIR_NAME + Config.VIDEO_COMPRESSOR_TEMP_DIR);
//        f.mkdirs();
//    }

    public static File saveTempFile(String fileName, Context context, Uri uri) {

        File mFile = null;
        ContentResolver resolver = context.getContentResolver();
        InputStream in = null;
        FileOutputStream out = null;

        try {
            in = resolver.openInputStream(uri);

            //mFile = new File(Environment.getExternalStorageDirectory() + "/Video" + Config.VIDEO_COMPRESSOR_TEMP_DIR , fileName);

            out = new FileOutputStream(mFile, false);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            out.flush();
        } catch (IOException e) {
            Log.e(TAG, "", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e(TAG, "", e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    Log.e(TAG, "", e);
                }
            }
        }
        return mFile;
    }

}
