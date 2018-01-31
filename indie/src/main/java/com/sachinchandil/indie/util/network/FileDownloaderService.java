package com.sachinchandil.indie.util.network;

import android.app.IntentService;
import android.content.Context;
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
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class FileDownloaderService extends IntentService
{
    public static final String ARGUMENT_FILE_URL = "sourceFileUrl";
    public static final String ARGUMENT_TARGET_FILE = "targetFilePath";
    public static final String ARGUMENT_RESUTL_RECIEVER = "resultReceiver";

    public static final String RESPONSE_TARGET_FILE = "targetFilePath";
    public static final String RESPONSE_DOWNLOAD_PROGRESS = "downloadProgress";

    public static final int RESPONSE_CODE_DOWNLOAD_RESULT = 100;
    public static final int RESPONSE_CODE_DOWNLOAD_PROGRESS = 101;
    public static boolean stopDownload;
    InputStream in;
    private long fileSize;

    public FileDownloaderService()
    {
        super("ImageDownloader");
    }

    public static void startAction(Context activity, String fileUrl, String targetFile, ResultReceiver receiver)
    {
        Intent intent = new Intent(activity, FileDownloaderService.class);
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_FILE_URL, fileUrl);
        bundle.putString(ARGUMENT_TARGET_FILE, targetFile);
        bundle.putParcelable(ARGUMENT_RESUTL_RECIEVER, receiver);
        intent.putExtras(bundle);
        activity.startService(intent);
    }

    public static String getFileName(String uri)
    {
        return uri.substring(uri.lastIndexOf("/"));
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        stopDownload = false;
        String fileUrl = intent.getStringExtra(ARGUMENT_FILE_URL);
        String targetFile = intent.getStringExtra(ARGUMENT_TARGET_FILE);
        ResultReceiver receiver = intent.getParcelableExtra(ARGUMENT_RESUTL_RECIEVER);

        String contentType = openConnection(fileUrl);
        String outputFilePath = null;
        if (contentType == null)
        {
            outputFilePath = null;
        } else if (contentType.contains("image"))
        {
            outputFilePath = downloadImage(targetFile);
        } else
        {
            outputFilePath = downloadFile(targetFile, receiver);
        }

        Bundle bundle = new Bundle();
        bundle.putString(RESPONSE_TARGET_FILE, outputFilePath);
        receiver.send(RESPONSE_CODE_DOWNLOAD_RESULT, bundle);

    }

    /**
     * publish progress to main thread to update progress UI.
     *
     * @param progress - percent value out of 100.
     */
    private void publishProgress(ResultReceiver receiver, int progress)
    {
        if (receiver != null)
        {
            Bundle bundle = new Bundle();
            bundle.putInt(RESPONSE_DOWNLOAD_PROGRESS, progress);
            receiver.send(RESPONSE_CODE_DOWNLOAD_PROGRESS, bundle);
        }
    }

    private String openConnection(String fileUrl)
    {
        try
        {
            URL url;
            url = new URL(fileUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK)
            {
                return null;
            }
            fileSize = connection.getContentLength();
            String contentType = connection.getHeaderFields().get("Content-Type").get(0);
            in = connection.getInputStream();
            return contentType;
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private String downloadImage(String outputFile)
    {
        try
        {
            File outFile = new File(outputFile);

            if (!outFile.getParentFile().exists())
                outFile.getParentFile().mkdirs();
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            if (bitmap == null)
                return null;
            FileOutputStream out = new FileOutputStream(outFile);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, outStream);
            byte[] buf = outStream.toByteArray();

            out.write(buf);

            out.close();
            in.close();
            return outputFile;
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private String downloadFile(String outputFile, ResultReceiver receiver)
    {
        try
        {
            File outFile = new File(outputFile);
            File tempfile = new File(outputFile + "_temp.pdf");
            if (!outFile.getParentFile().exists())
                outFile.getParentFile().mkdirs();

            FileOutputStream out = new FileOutputStream(tempfile);
            long downloaded = 0;
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1)
            {
                downloaded += len;
                out.write(buffer, 0, len);
                publishProgress(receiver, (int) (((downloaded * 100) / fileSize)));
                if (stopDownload)
                    break;
            }
            out.flush();
            out.close();
            if (stopDownload)
                tempfile.delete();
            else
                tempfile.renameTo(outFile);
            in.close();
            return outputFile;
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
