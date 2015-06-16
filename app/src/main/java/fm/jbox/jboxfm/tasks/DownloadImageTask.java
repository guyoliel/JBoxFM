package fm.jbox.jboxfm.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Log.i("picture",urldisplay);
        if(!urldisplay.contains("https"))
        urldisplay = urldisplay.replace("http","https");

        Log.i("picture",urldisplay);
        if (urldisplay.contains("facebook"))
            urldisplay+="?height=80&width=80";
        Bitmap mIcon11 = null;
        try {
            URL u = new URL(urldisplay);
            InputStream in = new BufferedInputStream(u.openConnection().getInputStream());
            Log.i("download","1");
            mIcon11 = BitmapFactory.decodeStream(in);
            Log.i("download",""+mIcon11);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}