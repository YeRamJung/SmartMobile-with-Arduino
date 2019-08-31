package com.example.swudoit.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
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
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.swudoit.MainActivity;
import com.example.swudoit.Note;
import com.example.swudoit.R;
import com.example.swudoit.musicList;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;

import static android.app.Activity.RESULT_OK;

public class FragmentHome extends Fragment {

    private ImageView mImgCamera;
    //사진이 저장되는 경로 - onActivityResult()로부터 받는 데이터
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

    private static final int REQUEST_ENABLE_BT = 10; // 블루투스 활성화 상태
    private BluetoothAdapter bluetoothAdapter; // 블루투스 어댑터
    private Set<BluetoothDevice> devices; // 블루투스 디바이스 데이터 셋
    private BluetoothDevice bluetoothDevice; // 블루투스 디바이스
    private BluetoothSocket bluetoothSocket = null; // 블루투스 소켓
    private OutputStream outputStream = null; // 블루투스에 데이터를 출력하기 위한 출력 스트림
    private InputStream inputStream = null; // 블루투스에 데이터를 입력하기 위한 입력 스트림
    private Thread workerThread = null; // 문자열 수신에 사용되는 쓰레드
    private byte[] readBuffer; // 수신 된 문자열을 저장하기 위한 버퍼
    private int readBufferPosition; // 버퍼 내 문자 저장 위치


    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        /*소리가 일정 이상이면 진동 울리도록 하기*/
        //진동
        FrameLayout vibratorLayout = view.findViewById(R.id.vibratorLayout);
        vibratorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator vibrator = (Vibrator)getActivity().getSystemService (Context.VIBRATOR_SERVICE);
                vibrator.vibrate(3000);
                Toast.makeText(getActivity(), "아이가 울고 있어요ㅜㅠㅜㅠㅜㅠㅜㅠㅜㅠㅜㅠㅠㅜㅠ", Toast.LENGTH_SHORT).show();
            }
        });


//        /*블루투스*/
//        bt = new BluetoothSPP(getActivity()); //Initializing
//
//        // 블루투스 활성화하기
//        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); // 블루투스 어댑터를 디폴트 어댑터로 설정
//
//        if (bluetoothAdapter == null) { // 디바이스가 블루투스를 지원하지 않을 때
//            Toast.makeText(getActivity(), "블루투스가 지원되지 않는 기기입니다.", Toast.LENGTH_SHORT).show();
//            getActivity().finish();
//        } else { // 디바이스가 블루투스를 지원 할 때
//
//            if (bluetoothAdapter.isEnabled()) { // 블루투스가 활성화 상태 (기기에 블루투스가 켜져있음)
//                selectBluetoothDevice(); // 블루투스 디바이스 선택 함수 호출
//            } else { // 블루투스가 비활성화 상태 (기기에 블루투스가 꺼져있음)
//
//                // 블루투스를 활성화 하기 위한 다이얼로그 출력
//                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//
//                // 선택한 값이 onActivityResult 함수에서 콜백된다.
//                startActivityForResult(intent, REQUEST_ENABLE_BT);
//
//            }
//
//        }

//        if (!bt.isBluetoothAvailable()) { //블루투스 사용 불가
//            Toast.makeText(getContext()
//                    , "Bluetooth is not available"
//                    , Toast.LENGTH_SHORT).show();
//            getActivity().finish();
//        }
//
//        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() { //데이터 수신
//            public void onDataReceived(byte[] data, String message) {
//                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() { //연결됐을 때
//            public void onDeviceConnected(String name, String address) {
//                Toast.makeText(getContext()
//                        , "Connected to " + name + "\n" + address
//                        , Toast.LENGTH_SHORT).show();
//            }
//
//            public void onDeviceDisconnected() { //연결해제
//                Toast.makeText(getContext()
//                        , "Connection lost", Toast.LENGTH_SHORT).show();
//            }
//
//            public void onDeviceConnectionFailed() { //연결실패
//                Toast.makeText(getContext()
//                        , "Unable to connect", Toast.LENGTH_SHORT).show();
//            }
//        }); //end 블루투스

        //디바이스 내에서 블투 연결 상태 권한 확인 받고 > 어떤 기기들이랑 연결이 되어있는건지 확인 > 그 중에서 어떤 거의 권한을 요청 받아서 확인할 수 있는지
        //로그인 할 때부터 확인


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

        try {
            userId = view.findViewById(R.id.txtPerson);

            String user = MainActivity.sharedPreferences.getString("id", null);

            userId.setText(user);
        } catch (NullPointerException n) {
            n.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //무드등 on/off
        lamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lnum == 0) {
                    lamp.setSelected(true);
                    lnum = 1;
                    Toast.makeText(getActivity(), "무드등 on", Toast.LENGTH_SHORT).show();
                } else {
                    lamp.setSelected(false);
                    lnum = 0;
                    Toast.makeText(getActivity(), "무드등 off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //가습기 on/off
        humi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hnum == 0) {
                    humi.setSelected(true);
                    hnum = 1;
                    Toast.makeText(getActivity(), "가습기 on", Toast.LENGTH_SHORT).show();
                } else {
                    humi.setSelected(false);
                    hnum = 0;
                    Toast.makeText(getActivity(), "가습기 off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //에어컨 on/off
        aircon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (anum == 0) {
                    aircon.setSelected(true);
                    anum = 1;
                    Toast.makeText(getActivity(), "에어컨 on", Toast.LENGTH_SHORT).show();
                } else {
                    aircon.setSelected(false);
                    anum = 0;
                    Toast.makeText(getActivity(), "에어컨 off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //모빌 on/off
        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mnum == 0) {
                    mobile.setSelected(true);
                    mnum = 1;
                    Toast.makeText(getActivity(), "모빌 on", Toast.LENGTH_SHORT).show();
                } else {
                    mobile.setSelected(false);
                    mnum = 0;
                    Toast.makeText(getActivity(), "모빌 off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //노래 on/off
        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (snum == 0) {
                    sound.setSelected(true);
                    snum = 1;
                } else {
                    sound.setSelected(false);
                    snum = 0;
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
            mCaptureUri = Uri.fromFile(getOutPutMediaFile());
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

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQUEST_ENABLE_BT:

                if (requestCode == RESULT_OK) { // '사용'을 눌렀을 때
                    selectBluetoothDevice(); // 블루투스 디바이스 선택 함수 호출
                } else { // '취소'를 눌렀을 때
                    // 여기에 처리 할 코드를 작성하세요.
                    //TODO

                }

                break;

        }  //블루투스

        if (resultCode != RESULT_OK) {

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
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        int exifOrientation;
        int exifDegree;
        if (exif != null) {
            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            exifDegree = exifOrientToDegree(exifOrientation);
        } else {
            exifDegree = 0;
        }
        Bitmap rotatedBmp = roate(resizedBmp, exifDegree);
        mImgCamera.setImageBitmap(rotatedBmp);

        Toast.makeText(getActivity(), "사진경로: " + mPhotoPath, Toast.LENGTH_LONG).show();
    }

    private int exifOrientToDegree(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
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
    public static Bitmap getResizedBitmap(Bitmap srcBmp, int size, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = size;
        Bitmap resized = Bitmap.createScaledBitmap(srcBmp, width, height, true);
        return resized;
    }

    public static Bitmap getResizedBitmap(Resources resources, int id, int size, int width, int height) {
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

//    public void onDestroy() {
//        super.onDestroy();
//        bt.stopService(); //블루투스 중지
//    }
//
//    public void onStart() {
//        super.onStart();
//        if (!bt.isBluetoothEnabled()) { //
//            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
//        } else {
//            if (!bt.isServiceAvailable()) {
//                bt.setupService();
//                bt.startService(BluetoothState.DEVICE_OTHER); //DEVICE_ANDROID는 안드로이드 기기 끼리
//                setup();
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

        //모빌
        Button btnMobile = getActivity().findViewById(R.id.btnMobile); //데이터 전송
        btnMobile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bt.send("4", true);
            }
        });

        //에어컨
        Button btnAircon = getActivity().findViewById(R.id.btnAircon); //데이터 전송
        btnAircon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bt.send("5", true);
            }
        });

        //가습기
        Button btnHumi = getActivity().findViewById(R.id.btnHumi); //데이터 전송
        btnHumi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bt.send("6", true);
            }
        });


    }

    //    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//
//            case REQUEST_ENABLE_BT :
//
//                if(requestCode == RESULT_OK) { // '사용'을 눌렀을 때
//                    selectBluetoothDevice(); // 블루투스 디바이스 선택 함수 호출
//                }
//
//                else { // '취소'를 눌렀을 때
//                    // 여기에 처리 할 코드를 작성하세요.
//                    //TODO
//
//                }
//
//                break;
//
//        }
//
//    }
    public void selectBluetoothDevice() {

        // 이미 페어링 되어있는 블루투스 기기를 찾습니다.
        devices = bluetoothAdapter.getBondedDevices();
        // 페어링 된 디바이스의 크기를 저장
        /**/
        int pariedDeviceCount = devices.size();
        // 페어링 되어있는 장치가 없는 경우
        if (pariedDeviceCount == 0) {
            // 페어링을 하기위한 함수 호출
            //TODO

        }

        // 페어링 되어있는 장치가 있는 경우
        else {
            // 디바이스를 선택하기 위한 다이얼로그 생성
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("페어링 되어있는 블루투스 디바이스 목록");
            // 페어링 된 각각의 디바이스의 이름과 주소를 저장
            List<String> list = new ArrayList<>();
            // 모든 디바이스의 이름을 리스트에 추가
            for (BluetoothDevice bluetoothDevice : devices) {
                list.add(bluetoothDevice.getName());
            }
            list.add("취소");

            // List를 CharSequence 배열로 변경
            final CharSequence[] charSequences = list.toArray(new CharSequence[list.size()]);
            list.toArray(new CharSequence[list.size()]);

            // 해당 아이템을 눌렀을 때 호출 되는 이벤트 리스너
            builder.setItems(charSequences, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // 해당 디바이스와 연결하는 함수 호출
                    connectDevice(charSequences[which].toString());
                }
            });


            // 뒤로가기 버튼 누를 때 창이 안닫히도록 설정
            builder.setCancelable(false);
            // 다이얼로그 생성
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }

    public void connectDevice(String deviceName) {

        // 페어링 된 디바이스들을 모두 탐색
        for (BluetoothDevice tempDevice : devices) {
            // 사용자가 선택한 이름과 같은 디바이스로 설정하고 반복문 종료
            if (deviceName.equals(tempDevice.getName())) {
                bluetoothDevice = tempDevice;
                break;
            }
        }

        // UUID 생성
        UUID uuid = java.util.UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        // Rfcomm 채널을 통해 블루투스 디바이스와 통신하는 소켓 생성
        try {

            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            bluetoothSocket.connect();
            // 데이터 송,수신 스트림을 얻어옵니다.
            outputStream = bluetoothSocket.getOutputStream();
            inputStream = bluetoothSocket.getInputStream();
            // 데이터 수신 함수 호출
            receiveData();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveData() {
        final Handler handler = new Handler();
        // 데이터를 수신하기 위한 버퍼를 생성
        readBufferPosition = 0;
        readBuffer = new byte[1024];

        // 데이터를 수신하기 위한 쓰레드 생성
        workerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (Thread.currentThread().isInterrupted()) {
                    try {
                        // 데이터를 수신했는지 확인합니다.
                        int byteAvailable = inputStream.available();
                        // 데이터가 수신 된 경우
                        if (byteAvailable > 0) {
                            // 입력 스트림에서 바이트 단위로 읽어 옵니다.
                            byte[] bytes = new byte[byteAvailable];
                            inputStream.read(bytes);
                            // 입력 스트림 바이트를 한 바이트씩 읽어 옵니다.
                            for (int i = 0; i < byteAvailable; i++) {
                                byte tempByte = bytes[i];
                                // 개행문자를 기준으로 받음(한줄)
                                if (tempByte == '\n') {
                                    // readBuffer 배열을 encodedBytes로 복사
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    // 인코딩 된 바이트 배열을 문자열로 변환
                                    final String text = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;
                                    handler.post(new Runnable() {

                                        @Override

                                        public void run() {
                                            // 텍스트 뷰에 출력
                                            //textViewReceive.append(text + "\n");
                                        }

                                    });

                                } // 개행 문자가 아닐 경우

                                else {
                                    readBuffer[readBufferPosition++] = tempByte;
                                }
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        // 1초마다 받아옴
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        workerThread.start();
    }
}
