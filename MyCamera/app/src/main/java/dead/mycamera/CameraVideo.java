package dead.mycamera;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class CameraVideo extends AppCompatActivity {

     int requestcode=1001;
    VideoView videoView;
    Button video;

    Uri videoUri;
     int requestCOde=1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_video);

         video=findViewById(R.id.TakeVideo);
        videoView=findViewById(R.id.Video_View);
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MediaHelper.isMarshmellow()){
                    if (MediaHelper.isVideoRequestPermissionGranted(CameraVideo.this)){

                        captureVideo();
                    }else {
                        MediaHelper.requsetAccess(CameraVideo.this, requestcode);
                        }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==requestcode && (grantResults[0]== PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED )){
            Toast.makeText(this,"Allowed",Toast.LENGTH_LONG).show();
            Log.d("Eroor","Request denied");
            video.setText("donne");
            captureVideo();
        }
    }

        public void captureVideo() {

        if (MediaHelper.IsCameraAvailabe(this)) {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            videoUri = MediaHelper.getOutput(2);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);

            startActivityForResult(intent,requestCOde);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==requestCOde && resultCode==RESULT_OK){
            if (null!=videoUri) {
                videoView.setVideoPath(videoUri.getPath());
                videoView.start();
            }
        }
    }
}
