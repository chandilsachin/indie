package com.sachinchandil.indie.util.network;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;

/**
 * <h1></h1>
 * <h1>public class WebServicesManager</h1>
 * <p>Handles operations related to web services and other remote data handling.</p>
 */
public class RestWebServicesManager
{
    public static final int TYPE_GET = 1;

    public static final int TYPE_POST = 2;

    InputStream in;

    NetworkManager netManager;

    public RestWebServicesManager(Context context)
    {
        netManager = new NetworkManager(context);
    }

    /**
     *
     * <h1>public Object excuteWebserviceRequest(String url,StringToObjectHandler handler)</h1>
     * <p>Requests webservice using HTTP GET Protocol.</p>
     *
     * @param url     - path to webservice.
     * @param handler - StringToObjectListener that holds method to convert raw string response to desired object.
     * @return an Object.
     * @throws IOException
     * @throws NoDataReceivedException       - Webservice did not returned any data.
     * @throws NoInternetConnectionException
     */
    public Object excuteWebserviceRequest(String url, StringToObjectHandler handler) throws IOException, NoDataReceivedException, NoInternetConnectionException
    {
        InputStream in = getRemoteFileStream(url);
        return handler.convertIntoObject(convertStreamToString(in).toString().trim());
    }

    public Object excuteWebserviceToDownloadFile(String url, StringToObjectHandler handler) throws IOException, NoDataReceivedException, NoInternetConnectionException
    {
        URLConnection connection = null;
        InputStream in = getRemoteFileStream(url,connection);
        connection.getHeaderFields();
        return handler.convertIntoObject(convertStreamToString(in).toString().trim());
    }



    /**
     * <h1></h1>
     * <h1>public Object excuteWebserviceRequest(String webservicePath,List<NameValuePair> parameters,StringToObjectHandler handler) throws ClientProtocolException, IOException, EmptyUrlException, NoDataReceivedException</h1>
     * <p>Requests webservice using HTTP POST Protocol</p>
     *
     * @param url     - path to webservice.
     * @param handler - StringToObjectListener that holds method to convert raw string response to desired object.
     * @return an Object
     * @throws IOException
     * @throws NoDataReceivedException - Webservice did not returned any data.
     */
    public Object excuteWebserviceRequest(String url, String params, StringToObjectHandler handler) throws IOException, NoDataReceivedException, NoInternetConnectionException
    {

        InputStream in = getRemoteFileStream(url + "?" + params);
        return handler.convertIntoObject(convertStreamToString(in).toString().trim());
    }


    /**
     *
     * <h1>private String convertStreamToString(InputStream is)</h1>
     * <p>converts input stream to Stirng.</p>
     *
     * @param is - InputStream containing data.
     * @return String containing converted text.
     * @throws IOException
     * @throws NoInternetConnectionException
     */
    private String convertStreamToString(InputStream is) throws IOException, NoInternetConnectionException
    {
        if (is != null)
        {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try
            {
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1)
                {
                    if (!netManager.isConnectedToInternet())
                    {
                        reader.close();
                        throw new NoInternetConnectionException();
                    }
                    writer.write(buffer, 0, n);
                }
            } finally
            {
                is.close();
            }
            return writer.toString();
        } else
        {
            return "";
        }
    }

    private File writeStreamToFile(InputStream is, File outputfile, ProgressListener progress) throws IOException, NoInternetConnectionException
    {
        if (is != null)
        {
            int total = is.available();
            int downloaded = 0;
            //FileWriter writer = new FileWriter(outputfile);
            FileOutputStream out = new FileOutputStream(outputfile);
            byte[] buffer = new byte[1024];
            try
            {
                int n = 0;
                while ((n = is.read(buffer)) != -1)
                {
                    if (!netManager.isConnectedToInternet())
                    {
                        boolean flag = true;
                        for (int count = 0; count < 5; count++)
                        {
                            try
                            {
                                wait(1000);
                            } catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                            if (netManager.isConnectedToInternet())
                            {
                                flag = false;
                                break;
                            }
                        }

                        if (flag)
                        {
                            is.close();
                            out.close();
                            throw new NoInternetConnectionException();
                        }
                    }
                    out.write(buffer, 0, n);
                    downloaded += n;
                    if(progress != null)
                        progress.onProgress(downloaded, total);
                }

            } finally
            {
                is.close();
                out.close();
            }
        }
        return outputfile;
    }

    /**
     *
     * <h1>public InputStream getRemoteFileStream(String remoteFilePath)</h1>
     * <p>Returns Input Stream of remote file.</p>
     *
     * @param remoteFilePath - path of remote file.
     * @return InputStream object.
     * @throws IOException
     * @throws NoDataReceivedException
     */
    public InputStream getRemoteFileStream(String remoteFilePath) throws IOException, NoDataReceivedException
    {
        URL url = new URL(remoteFilePath);
        URLConnection connection = url.openConnection();
        InputStream is = connection.getInputStream();
        connection.getContentLength();
        return is;
    }

    /**
     *
     * <h1>public InputStream getRemoteFileStream(String remoteFilePath)</h1>
     * <p>Returns Input Stream of remote file.</p>
     *
     * @param remoteFilePath - path of remote file.
     * @return InputStream object.
     * @throws IOException
     * @throws NoDataReceivedException
     */
    public InputStream getRemoteFileStream(String remoteFilePath, URLConnection connection) throws IOException, NoDataReceivedException
    {
        URL url = new URL(remoteFilePath);
        connection = url.openConnection();
        InputStream is = connection.getInputStream();
        connection.getContentLength();
        return is;
    }

    /**
     * <h1>public File downloadFile(String remoteFilePath, String pathToStore)</h1>
     * <p>Downloads file from remote location.</p>
     *
     * @param remoteFilePath - path of remote file.
     * @param pathToStore    - path to store file.
     * @return Returns file object of downloaded file.
     * @throws IOException
     * @throws NoInternetConnectionException
     * @throws NoDataReceivedException
     */
    public File downloadFile(String remoteFilePath, String pathToStore) throws IOException, NoInternetConnectionException, NoDataReceivedException
    {
        File outFile = new File(pathToStore);
        InputStream in = getRemoteFileStream(remoteFilePath);
        if (in != null)
        {
            FileWriter out = new FileWriter(outFile);
            out.write(convertStreamToString(in));
        }
        return outFile;
    }

    public File downloadFile(String remoteFilePath, String pathToStore, ProgressListener progress) throws IOException, NoInternetConnectionException, NoDataReceivedException
    {
        File outFile = new File(pathToStore);
        if (!outFile.getParentFile().exists())
            outFile.getParentFile().mkdirs();

        URL url = new URL(remoteFilePath);
        URLConnection connection = url.openConnection();
        InputStream is = connection.getInputStream();
        int total = connection.getContentLength();

        if (is != null)
        {
            int downloaded = 0;
            FileOutputStream out = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            try
            {
                int n = 0;
                while ((n = is.read(buffer)) != -1)
                {
                    if (!netManager.isConnectedToInternet())
                    {
                        boolean flag = true;
                        for (int count = 0; count < 5; count++)
                        {
                            try
                            {
                                wait(1000);
                            } catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                            if (netManager.isConnectedToInternet())
                            {
                                flag = false;
                                break;
                            }
                        }

                        if (flag)
                        {
                            is.close();
                            out.close();
                            throw new NoInternetConnectionException();
                        }
                    } else
                    {
                        out.write(buffer, 0, n);
                        downloaded += n;
                        if (progress != null)
                            progress.onProgress(downloaded, total);
                    }
                }

            } finally
            {
                is.close();
                out.close();
            }
        }
        return outFile;
        //return writeStreamToFile(getRemoteFileStream(remoteFilePath),outFile,progress);
    }
}
