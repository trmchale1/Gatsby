package com.example.trmchale.gatsby005;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.provider.MediaStore;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.graphics.Bitmap;
import android.os.ResultReceiver;
import java.io.FileNotFoundException;
import java.io.InputStream;
import android.widget.ImageView;
import java.io.ByteArrayOutputStream;
/**
 * Created by trmchale on 6/17/17.
 */

public class Image extends AppCompatActivity {
    Bitmap bigbits;
    // PICK_PHOTO_CODE is a constant integer
    public final static int PICK_PHOTO_CODE = 1046;
    android.widget.ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_view);
        imageView = (android.widget.ImageView) findViewById(R.id.imageView);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPickPhoto(view);

            }
        });
    }

    // Trigger gallery selection for a photo
    public void onPickPhoto(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bmp = null;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_CODE && resultCode == Image.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                bmp = BitmapFactory.decodeStream(inputStream);
                this.bigbits = bmp;
                this.imageView.setImageBitmap(bigbits);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                //Now you can do whatever you want with your inpustrmarthastuart@gmail.com  eam, save it as file, upload to a server, decode a bitmap...
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 0, stream);
                byte[] byteArray = stream.toByteArray();
                Intent passToPhotoProcessing = new Intent(this, PhotoProcessing.class);
                passToPhotoProcessing.putExtra("image", byteArray);
                // new code
                passToPhotoProcessing.putExtra(PhotoProcessing.BUNDLED_LISTENER, new ResultReceiver(new Handler()) {
                    @Override
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        super.onReceiveResult(resultCode, resultData);
                        if (resultCode == Image.RESULT_OK) {
                            byte[] bytes = resultData.getByteArray("image");
                            bigbits = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            imageView.setImageBitmap(bigbits);
                        } else {
                            Log.d("Error", "Image did not recieve from processing");
                        }
                    }
                });
                startService(passToPhotoProcessing);
            }
        }

    }
}
