package daniel.shoppinglist.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by Daniel on 03/07/2016.
 */
public class ModelCloudinary {
    Cloudinary cloudinary;

    public ModelCloudinary(){
        cloudinary = new Cloudinary("cloudinary://554635342325428:FHG-NRmJskBM5OnUQsnkK_IAE_I@dbsvw3h7x");
    }

    public void saveImage(final Bitmap img, final String name, final Model.Listener listener){
        Thread T = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    img.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                    byte[] bitMapData = stream.toByteArray();
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(bitMapData);

                    //String finalName = name.substring(0);
                    Map res = cloudinary.uploader().upload(inputStream, ObjectUtils.asMap("public_id", name.replace("@", "x")));

                    Log.d("TAG","save image to url" + res.get("url"));

                    listener.onResult(null);
                } catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        });

        T.start();
    }

    public void getImage(final String imageName, final Model.Listener listener){
        Thread T = new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                try {
                    //url = new URL(cloudinary.url().generate(imageName).replace("%40", "@"));
                    url = new URL(cloudinary.url().generate(imageName.replace("@", "x") + ".jpg"));
                    Log.d("TAG", "load image from url" + url);
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                    listener.onResult(bmp);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    listener.onCancel();
                } catch (IOException e) {
                    e.printStackTrace();
                    listener.onCancel();
                }

                Log.d("TAG", "url" + url);
            }
        });

        T.start();
    }
}
