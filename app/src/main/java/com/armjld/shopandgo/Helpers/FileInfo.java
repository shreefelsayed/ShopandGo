package com.armjld.shopandgo.Helpers;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.webkit.MimeTypeMap;

import java.io.IOException;
import java.net.URISyntaxException;

public class FileInfo {

    Context mContext;

    public FileInfo(Context mContext) {
        this.mContext = mContext;
    }

    public static Bitmap getCircleImage(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    @SuppressLint("Range")
    public static String getName(Context context, Uri uri) {
        String fileName = null;
        try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }
        }
        return fileName;
    }

    public static long getSize(Context context, Uri uri) {
        if (uri == null || context == null) return 0;
        String fileSize = null;
        try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                if (!cursor.isNull(sizeIndex)) {
                    fileSize = cursor.getString(sizeIndex);
                }
            } else return 0;
        }

        if (fileSize == null) return 0;
        return Long.parseLong(fileSize) / 1024;
    }

    /**
     * It returns the file extension of the file pointed to by the given URI
     *
     * @param uri The uri of the file you want to get the file type of.
     * @return The file type of the file being uploaded.
     */
    public static String getFileType(Context mContext, Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mContext.getContentResolver().getType(uri));
    }


    @SuppressLint({"NewApi", "Recycle"})
    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{split[1]};
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor;
            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception ignored) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static Bitmap resizeBitmap(Bitmap source, int maxLength) {
        try {
            if (source.getHeight() >= source.getWidth()) {
                if (source.getHeight() <= maxLength) { // if image already smaller than the required height
                    return source;
                }

                double aspectRatio = (double) source.getWidth() / (double) source.getHeight();
                int targetWidth = (int) (maxLength * aspectRatio);

                return Bitmap.createScaledBitmap(source, targetWidth, maxLength, false);
            } else {
                if (source.getWidth() <= maxLength) { // if image already smaller than the required height
                    return source;
                }

                double aspectRatio = ((double) source.getHeight()) / ((double) source.getWidth());
                int targetHeight = (int) (maxLength * aspectRatio);

                return Bitmap.createScaledBitmap(source, maxLength, targetHeight, false);
            }
        } catch (Exception e) {
            return source;
        }
    }

    public Bitmap resizePhoto(Bitmap bitmap) {
        bitmap = resizeBitmap(bitmap, 750);

        return bitmap;
    }

    public Bitmap resizePhoto(Uri photoUri, int max) {
        Bitmap bitmap = null;
        try {
            Bitmap source = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), photoUri);
            bitmap = resizeBitmap(source, max);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Uri uri = null;

        try {
            uri = Uri.parse(getFilePath(mContext, photoUri));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (uri != null) {
            bitmap = rotateImage(bitmap, uri);
        }

        return bitmap;
    }

    private Bitmap rotateImage(Bitmap bitmap, Uri uri) {
        ExifInterface exifInterface = null;
        try {
            if (uri == null) {
                return bitmap;
            }
            exifInterface = new ExifInterface(String.valueOf(uri));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (exifInterface != null) {
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            if (orientation == 6 || orientation == 3 || orientation == 8) {
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                } else {
                    matrix.postRotate(270);
                }
                return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            } else {
                return bitmap;
            }
        } else {
            return bitmap;
        }

    }
}
