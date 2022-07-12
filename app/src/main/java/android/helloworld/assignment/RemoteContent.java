package android.helloworld.assignment;

import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RemoteContent extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... urls) {
        String response = "";
        try {
            URL url = new URL(urls[0]);
            try {
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(false);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                try {
                    InputStream inputStream;
                    int status = urlConnection.getResponseCode();

                    if (status != HttpURLConnection.HTTP_OK)  {
                        inputStream = urlConnection.getErrorStream();
                    } else  {
                        inputStream = urlConnection.getInputStream();
                    }
                    response = readStream(inputStream);
                } finally {
                    urlConnection.disconnect();
                }
            } catch (java.io.IOException ioex) {
                response = ioex.toString();
                Log.e("PlaceholderFragment", "Error ", ioex);
            }
        } catch (MalformedURLException muex) {
            response = muex.toString();
            Log.e("PlaceholderFragment", "Error ", muex);
        } catch (Exception e) {
            response = e.toString();
            Log.e("PlaceholderFragment", "Error ", e);
        }
        return response;
    }


    @Override
    protected void onPostExecute(String result) {
        String source = result;

        int pos_start = source.indexOf("<url>") + 5;
        int pos_end = source.indexOf("</url>");
        String URL = source.substring(pos_start, pos_end);
        JukeboxActivity.txtUrl.setText(URL);

        int pos_begin = source.indexOf("<title>") + 7;
        int pos_the_end = source.indexOf("</title>");
        String Title = source.substring(pos_begin, pos_the_end);
        JukeboxActivity.txtTitle.setText(Title);

        int start = source.indexOf("<artist>") + 8;
        int end = source.indexOf("</artist>");
        String Artist = source.substring(start, end);
        JukeboxActivity.txtArtist.setText(Artist);
    }

    private String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }


}
