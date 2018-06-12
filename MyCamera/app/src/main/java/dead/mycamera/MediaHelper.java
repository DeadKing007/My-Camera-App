package dead.mycamera;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import static android.app.Activity.RESULT_OK;

public class MediaHelper {
    private static  final String AppDirectory="my-app";
    public  static Uri getOutput(int type){
        return Uri.fromFile(getOutputMediaFile(type));//to convert mediafile into uri
    }

    public static File getOutputMediaFile(int type) {
        File mediaDirectory=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),AppDirectory);

        if (!mediaDirectory.exists()){
            if (!mediaDirectory.mkdirs())
                Log.e("Error","Error");
        }
        String filename="media-"+System.currentTimeMillis();
        File mediaFile;
        if (type==1){
            mediaFile=new File(mediaDirectory.getPath()+File.separator+filename+".jpg");
        }else if (type==2)
            mediaFile=new File(mediaDirectory.getPath()+File.separator+filename+".mp4");
        else
            return null;

        return mediaFile;
    }


    public static Boolean IsCameraAvailabe(Context context) {

        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
            return true;

        return false;

    }


    public static boolean isMarshmellow(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            return  true;
        }
        return false;
    }

    public static boolean checkREquestpermission(Context context){

        int result= ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        return  result==PackageManager.PERMISSION_GRANTED;
    }

    public static void AskPermisson(Activity activity,int requestcode){
        ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},requestcode);
    }

    public  static boolean isVideoRequestPermissionGranted(Context context){

    int isrequestVideo = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO);




        int isStoragerequest=ContextCompat.checkSelfPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE);


        if (isrequestVideo==PackageManager.PERMISSION_GRANTED  && isStoragerequest
                ==PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        return false;
    }

    public  static void requsetAccess(Activity activity,int requestCode){
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
            Log.d(MediaHelper.class.getName(),"Error");
    }
}
