package com.example.swudoit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.swudoit.fragment.FragmentDiary;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Note extends AppCompatActivity {

    // 필요한 변수 : 현재 날짜(today), 내용(content), 제목(title), 현재 유저(id), 사진
    // 카메라 : 카메라 버튼 클릭 -> 카메라 권한 허용 -> 사진 찍음 -> 디바이스 내 혹은 리소스에 적재 -> 데이터베이스 연동 -> 이름, 카피 업로드
    // 내용 입력 -> return String
    // 타이틀 입력 -> return String
    // 내용 or 타이틀 입력 하지 않음 -> 입력하라고 알람 뜨기

    private ImageView imgRoundShape;
    //사진이 저장되는 경로 - onActivityResult()로부터 받는 데이터
    private Uri mCaptureUri;
    //사진이 저장된 단말기상의 실제 경로
    public String mPhotoPath;
    //startActivityForResult() 에 넘겨주는 값, 이 값이 나중에 onActivityResult()로 돌아와서
    //내가 던진값인지를 구별할 때 사용하는 상수이다.
    public static final int REQUEST_IMAGE_CAPTURE = 200;

    String today;
    String title;
    String content;

    String image_name;
    String image_copied;

    String userIdx;

    boolean isCamera;

    ImageButton imgCamera;      // 카메라
    ImageButton btnreg;         // 작성
    EditText noteTitle;         // 타이틀
    EditText noteContent;       // 내용

    SharedPreferences prf;

    private File tempFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        //카메라를 사용하기 위한 퍼미션을 요청한다.
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        }, 0);

        imgRoundShape = findViewById(R.id.imgRoundShape);
        Button btnNoteCamera = findViewById(R.id.btnNoteCamera);
        //사진찍기 버튼
        btnNoteCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        Button btnNoteAlbum = findViewById(R.id.btnNoteAlbum);
        btnNoteAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAlbum();
            }
        });


        imgCamera = findViewById(R.id.imgCamera);
        btnreg = findViewById(R.id.btnreg);
        noteTitle = findViewById(R.id.noteTitle);
        noteContent = findViewById(R.id.noteContent);

        //prf = getSharedPreferences("ID", Context.MODE_PRIVATE);
        prf = MainActivity.sharedPreferences;

        userIdx = prf.getString("userIdx", null);

        Log.d("userIdx", userIdx);

        // 이미지 버튼 클릭 리스너, 카메라, 작성 버튼, 카메라 찍고 확인하면 true, 아니면 false
        ImageButton.OnClickListener imageClickListener = new ImageButton.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.imgCamera :
                        Log.d("Message", "카메라 입력하기");
                        isCamera = false;
                        break;
                    case R.id.btnreg :
                        Log.d("Message", "다이어리 입력하기");

                        // 에딧트 텍스트
                        title = noteTitle.getText().toString();
                        content = noteContent.getText().toString();
                        today = uploadToday();

                        // 카메라
                        if(!isCamera){
                            image_name = "";
                            image_copied = "";
                        }else{
                            image_name = "";
                            image_copied = "";
                        }

                        Log.d("Message", "Title : " + title);
                        Log.d("Message", "Content : " + content);
                        Log.d("Message", "Today : " + today);

                        if(title.isEmpty()){
                            AlertDialog.Builder ab = new AlertDialog.Builder(Note.this);

                            ab.setTitle("Error");
                            ab.setMessage("제목을 입력해주세요!");

                            ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                            ab.create();
                            ab.show();

                            return;
                        }

                        // 서버 통신 후 성공하면 Intent, 실패하면 Alert
                        //ConnectServer connectServerPost = new ConnectServer();
                        //connectServerPost.requestPost(title, content, today, userIdx, image_name, image_copied);


                        backgroundThreadShortToast(Note.this, "다이어리 업로드하였습니다.");
                        Intent noteListView = new Intent(Note.this, TabActivity.class);
                        startActivity(noteListView);

                        break;
                }
            }
        };

        imgRoundShape.setOnClickListener(imageClickListener);
        btnreg.setOnClickListener(imageClickListener);

    } //end onCreate

    private void takePicture() {

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mCaptureUri = Uri.fromFile( getOutPutMediaFile() );
        } else {
            mCaptureUri = FileProvider.getUriForFile(this,
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

            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();

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
                cursor = this.getContentResolver().query(photoUri, proj, null, null, null);

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
        Bitmap resizedBmp = getResizedBitmap(originalBm, 4, 1000, 1000);

        //줄어든 이미지를 다시 저장한다
        mPhotoPath = tempFile.getAbsolutePath();
        mCaptureUri = Uri.fromFile(tempFile);
//        saveBitmapToFileCache(resizedBmp, mPhotoPath);

        imgRoundShape.setImageBitmap(resizedBmp);
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
        Bitmap resizedBmp = getResizedBitmap(bitmap, 4, 1000, 1000);

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
        imgRoundShape.setImageBitmap( rotatedBmp );

        Toast.makeText(this, "사진경로: " + mPhotoPath, Toast.LENGTH_LONG).show();
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
    } //end camera

    private String uploadToday(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date time = new Date();

        String today = dateFormat.format(time);

        return today;
    }

    public class ConnectServer{
        OkHttpClient client = new OkHttpClient();

        String url = "http://13.125.111.255:3000/";

        public void requestPost(String title, String content, String today, String userIdx, String image_name, String image_copied){
            RequestBody body = new FormBody.Builder()
                    .add("title", title)
                    .add("content", content)
                    .add("today", today)
                    .add("userIdx", userIdx)
                    .add("image_name", image_name)
                    .add("image_copied", image_copied)
                    .build();

            final Request request = new Request.Builder()
                    .url(url+"user/diary")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("error", "Connect Server Error is " + e.toString());
                    backgroundThreadShortToast(Note.this, "서버 통신에 실패하였습니다.");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try{
                        Gson gson = new Gson();

                        String stl = gson.toJson(response.body().string());

                        if(response.isSuccessful()){
                            backgroundThreadShortToast(Note.this, "다이어리 업로드하였습니다.");
                            Intent noteListView = new Intent(Note.this, TabActivity.class);
                            startActivity(noteListView);
                        }else{
                            backgroundThreadShortToast(Note.this, "서버 통신에 실패하였습니다.");
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    // Toast를 위한 함수
    public static void backgroundThreadShortToast(final Context context, final String msg) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
