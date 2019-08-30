package com.example.swudoit.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swudoit.MainActivity;
import com.example.swudoit.Note;
import com.example.swudoit.R;
import com.example.swudoit.musicList;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

public class FragmentHome extends Fragment {

    private ImageView mImgCamera;
    //사진이 저장도니 경로 - onActivityResult()로부터 받는 데이터
    private Uri mCaptureUri;
    //사진이 저장된 단말기상의 실제 경로
    public String mPhotoPath;
    //startActivityForResult() 에 넘겨주는 값, 이 값이 나중에 onActivityResult()로 돌아와서
    //내가 던진값인지를 구별할 때 사용하는 상수이다.
    public static final int REQUEST_IMAGE_CAPTURE = 200;

    private ImageButton lamp;
    private ImageButton humi;
    private ImageButton aircon;
    private ImageButton mobile;
    private ImageButton sound;

    private int lnum, hnum, anum, mnum, snum = 0;

    TextView userId;

    private File tempFile;

    private BluetoothSPP bt;

    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        bt = new BluetoothSPP(getActivity()); //Initializing


        //카메라를 사용하기 위한 퍼미션을 요청한다.
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        }, 0);

        mImgCamera = view.findViewById(R.id.imgCamera);
        ImageButton btnCamera = view.findViewById(R.id.btnCamera);
        //사진찍기 버튼
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        ImageButton btnAlbum = view.findViewById(R.id.btnAlbum);
        btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAlbum();
            }
        });

        lamp = view.findViewById(R.id.btnLamp);
        humi = view.findViewById(R.id.btnHumi);
        aircon = view.findViewById(R.id.btnAircon);
        mobile = view.findViewById(R.id.btnMobile);
        sound = view.findViewById(R.id.btnSound);

        try{
            userId = view.findViewById(R.id.txtPerson);

            String user = MainActivity.sharedPreferences.getString("id", null);

            userId.setText(user);
        }catch (NullPointerException n){
            n.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

        //무드등 on/off
        lamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lnum==0){
                    lamp.setSelected(true);
                    lnum=1;
                }
                else{
                    lamp.setSelected(false);
                    lnum=0;
                }
            }
        });

        //가습기 on/off
        humi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hnum==0){humi.setSelected(true);
                    hnum=1;
                }
                else{
                    humi.setSelected(false);
                    hnum=0;
                }
            }
        });

        //에어컨 on/off
        aircon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(anum==0){
                    aircon.setSelected(true);
                    anum=1;
                }
                else{
                    aircon.setSelected(false);
                    anum=0;
                }
            }
        });

        //모빌 on/off
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mnum==0){
                    mobile.setSelected(true);
                    mnum=1;
                }
                else{
                    mobile.setSelected(false);
                    mnum=0;
                }
            }
        });

        //노래 on/off
        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(snum==0){
                    sound.setSelected(true);
                    snum=1;
                }
              else{
                  sound.setSelected(false);
                  snum=0;
              }
            }
        });


        //노래 버튼 눌러서 페이지 이동
        ImageButton btnSound = view.findViewById(R.id.btnSound);
        btnSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), musicList.class);
                startActivity(i);
            }
        });

        return view;

    }//end onCreateView


    private void takePicture() {

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mCaptureUri = Uri.fromFile( getOutPutMediaFile() );
        } else {
            mCaptureUri = FileProvider.getUriForFile(getContext(),
                    "com.example.swudoit", getOutPutMediaFile());
        }

        i.putExtra(MediaStore.EXTRA_OUTPUT, mCaptureUri);

        //내가 원하는 액티비티로 이동하고, 그 액티비티가 종료되면 (finish되면)
        //다시금 나의 액티비티의 onActivityResult() 메서드가 호출되는 구조이다.
        //내가 어떤 데이터를 받고 싶을때 상대 액티비티를 호출해주고 그 액티비티에서
        //호출한 나의 액티비티로 데이터를 넘겨주는 구조이다. 이때 호출되는 메서드가
        //onActivityResult() 메서드 이다.
        startActivityForResult(i, REQUEST_IMAGE_CAPTURE);

    }

    //갤러리에서 이미지 가져오기
    private void goToAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }
    private static final int PICK_FROM_ALBUM = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {

            Toast.makeText(getActivity(), "취소 되었습니다.", Toast.LENGTH_SHORT).show();

            if (tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        Log.e("test", tempFile.getAbsolutePath() + " 삭제 성공");
                        tempFile = null;
                    }
                }
            }

            return;
        }

        if (requestCode == PICK_FROM_ALBUM) {

            Uri photoUri = data.getData();

            Cursor cursor = null;

            try {

                /*
                 *  Uri 스키마를
                 *  content:/// 에서 file:/// 로  변경한다.
                 */
                String[] proj = {MediaStore.Images.Media.DATA};

                assert photoUri != null;
                cursor = getActivity().getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            setImage();

        } else if (requestCode == REQUEST_IMAGE_CAPTURE) { //카메라로부터 오는 데이터를 취득한다.
            sendPicture();
        }
    }

    //갤러리에서 받아온 이미지 넣기
    private void setImage() {

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);
        Bitmap resizedBmp = getResizedBitmap(originalBm, 4, 100, 100);

        //줄어든 이미지를 다시 저장한다
        mPhotoPath = tempFile.getAbsolutePath();
        mCaptureUri = Uri.fromFile(tempFile);
//        saveBitmapToFileCache(resizedBmp, mPhotoPath);

        mImgCamera.setImageBitmap(resizedBmp);
    }

    private File getOutPutMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "cameraDemo");
        if(!mediaStorageDir.exists()) {
            if(!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File file = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

        mPhotoPath = file.getAbsolutePath();

        return file;
    }

    private void sendPicture() {
        Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath);
        Bitmap resizedBmp = getResizedBitmap(bitmap, 4, 100, 100);

        bitmap.recycle();

        //뒤집어져 들어온 사진을 다시 원상복구 시킨다.
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(mPhotoPath);
        } catch(Exception e) {
            e.printStackTrace();
        }
        int exifOrientation;
        int exifDegree;
        if(exif != null) {
            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            exifDegree = exifOrientToDegree(exifOrientation);
        } else {
            exifDegree = 0;
        }
        Bitmap rotatedBmp = roate(resizedBmp, exifDegree);
        mImgCamera.setImageBitmap( rotatedBmp );

        Toast.makeText(getActivity(), "사진경로: " + mPhotoPath, Toast.LENGTH_LONG).show();
    }

    private int exifOrientToDegree(int exifOrientation) {
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        }
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        }
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap roate(Bitmap bmp, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(),
                matrix, true);
    }

    //비트맵의 사이즈를 줄여준다.
    public static Bitmap getResizedBitmap(Bitmap srcBmp, int size, int width, int height){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = size;
        Bitmap resized = Bitmap.createScaledBitmap(srcBmp, width, height, true);
        return resized;
    }

    public static Bitmap getResizedBitmap(Resources resources, int id, int size, int width, int height){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = size;
        Bitmap src = BitmapFactory.decodeResource(resources, id, options);
        Bitmap resized = Bitmap.createScaledBitmap(src, width, height, true);
        return resized;
    }

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        //카메라로부터 오는 데이터를 취득한다.
//        if(resultCode == getActivity().RESULT_OK) {
//            if(requestCode == REQUEST_IMAGE_CAPTURE) {
//                sendPicture();
//            }
//        }
//    }


    //아두이노 블루투스 연결
    public void setup() {
        //무드등
        Button btnLamp = getActivity().findViewById(R.id.btnLamp); //데이터 전송
        btnLamp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bt.send("3", true);
            }
        });
//        //가습기
//        Button btnHumi = getActivity().findViewById(R.id.btnHumi); //데이터 전송
//        btnHumi.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                bt.send("3", true);
//            }
//        });
        //모빌
        Button btnMobile = getActivity().findViewById(R.id.btnMobile); //데이터 전송
        btnMobile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bt.send("4", true);
            }
        });
    }

}
