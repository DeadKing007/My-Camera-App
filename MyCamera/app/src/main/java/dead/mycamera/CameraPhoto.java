package dead.mycamera;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static android.media.ExifInterface.ORIENTATION_ROTATE_180;
import static android.media.ExifInterface.ORIENTATION_ROTATE_270;
import static android.media.ExifInterface.ORIENTATION_ROTATE_90;

public class CameraPhoto extends AppCompatActivity {

    private int RequestCOde=1110;
    Uri uri;
    ImageView imageView;
    private int requestcode=1001;
    private ExifInterface exifObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_photo);
        Button photo = findViewById(R.id.TakePhoto);
        imageView = findViewById(R.id.imageView);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (MediaHelper.isMarshmellow()) {
                    if (!MediaHelper.checkREquestpermission(CameraPhoto.this))
                        MediaHelper.AskPermisson((Activity) CameraPhoto.this, requestcode);
                    else
                        imageCapture();
                } else
                    imageCapture();
            }
        });
    }

        private void imageCapture() {
            if (MediaHelper.IsCameraAvailabe(getApplicationContext())){
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                uri=MediaHelper.getOutput(1);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
                startActivityForResult(intent,RequestCOde);

            }else
                Toast.makeText(this,"Camera Not Present",Toast.LENGTH_SHORT).show();
        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode ==RequestCOde && resultCode == RESULT_OK)){
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inSampleSize=8;

            if (uri!=null) {
                final Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath(), options);

                if(imageView.getDrawable() != null){
                    try {
                        exifObject = new ExifInterface(uri.getPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int orientation = exifObject.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                    Bitmap imageRotate = rotateBitmap(bitmap,orientation);
                    imageView.setImageBitmap(imageRotate);
                }else{
                    Toast.makeText(this, "Image photo is not yet set", Toast.LENGTH_LONG).show();
                }

            }
            }
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==requestcode && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            imageCapture();
        }
        else
            Toast.makeText(this,"Access Denied",Toast.LENGTH_SHORT).show();
    }


    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {//To avoid rotation
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }
}

