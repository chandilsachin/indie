package com.sachinchandil.indie.util.network;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.ResultReceiver;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ImageDownloaderService extends IntentService
{
    public static final String ARGUMENT_FILE_URL = "fileUrl";
    public static final String ARGUMENT_DOWNLOADED_FILE_PATH = "filePath";
    public static final String ARGUMENT_BASE_DIRECTORY = "baseDirectory";
    public static final String ARGUMENT_RESUTL_RECIEVER = "resultReceiver";

    public static final int RESULT_DOWNLOAD = 100;

    public ImageDownloaderService()
    {
        super("ImageDownloader");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {

        String imageUrl = intent.getStringExtra(ARGUMENT_FILE_URL);
        String baseDirectory = intent.getStringExtra(ARGUMENT_BASE_DIRECTORY);
        ResultReceiver receiver = intent.getParcelableExtra(ARGUMENT_RESUTL_RECIEVER);

        String filePath = downloadImage(imageUrl, baseDirectory);
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_DOWNLOADED_FILE_PATH, filePath);
        receiver.send(RESULT_DOWNLOAD, bundle);

    }

    private String downloadImage(String imagePath, String baseDirectory)
    {

        URL url;
        try
        {
            url = new URL(imagePath);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK)
            {
                return null;
            }
            InputStream in = connection.getInputStream();
            String outFilePath = baseDirectory + "/" + imagePath.substring(imagePath.lastIndexOf("/") + 1);
            File outFile = new File(outFilePath);

            if (!outFile.getParentFile().exists())
                outFile.getParentFile().mkdirs();
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            if (bitmap == null)
                return null;
            FileOutputStream out = new FileOutputStream(outFilePath);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, outStream);
            byte[] buf = outStream.toByteArray();
            // byte[] buffer = new byte[1024];
            // while(in.read(buffer) != -1)
            {
                out.write(buf);
            }
            out.close();
            in.close();
            return outFilePath;
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

}
