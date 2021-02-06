package com.lynhill.ghpc.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.Strings;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.ml.vision.text.RecognizedLanguage;

import com.lynhill.ghpc.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class OCR extends BaseActivity {

    private static String TAG = OCR.class.getSimpleName();
    private int requestPermissionID = 786;
    private TextView mTextView;
    private ImageView showImage;
    private Button captureImageBtn, detectTextBtn;
    private SurfaceView mCameraView;
    private CameraSource mCameraSource;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Uri outPutfileUri;
    private Uri resultUri;
    ArrayList<String> userInfo;
    private int READ_STORAGE_PERMISSION = 787;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_c_r);
        findViews();
        checkPermissions();
//        if (cameraPermission()) {
//            if (mikePermission()) {
//                    callingCamera();
//            } else {
////                finish();
//                Toast.makeText(this, "read storage", Toast.LENGTH_SHORT).show();
//            }
//        } else {
////            finish();
//            Toast.makeText(this, "camera", Toast.LENGTH_SHORT).show();
//        }
//    startCameraSource();
    }

    public void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent();
        } else {
            requestStoragePermission();
        }
    }

    public void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, requestPermissionID);
    }


    private void findViews() {
        showImage = findViewById(R.id.image);
        captureImageBtn = findViewById(R.id.capture_image);
        detectTextBtn = findViewById(R.id.detect_text_image);
        mTextView = findViewById(R.id.text_view);
        mCameraView = findViewById(R.id.surfaceView);
        clickListener();
    }

    private void clickListener() {


        detectTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userInfo = new ArrayList<>();
                detectTextFromImage();
            }
        });
        captureImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
//              callingCamera();
            }
        });
    }

    private void callingCamera() {
        dispatchTakePictureIntent();
//        if (!cameraPermission()){
//            dispatchTakePictureIntent();
//        }else{
//            finish();
//        }
    }

    private void detectTextFromImage() {

        if (resultUri != null) {
            detectTextBtn.setVisibility(View.VISIBLE);
            FirebaseVisionImage firebaseVisionImage = null;
            try {
                firebaseVisionImage = FirebaseVisionImage.fromFilePath(this, resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
            Task<FirebaseVisionText> result =
                    detector.processImage(firebaseVisionImage)
                            .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                                @Override
                                public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                    // Task completed successfully
                                    displayTextFromImage(firebaseVisionText);
                                }
                            })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Task failed with an exception
                                            // ...
                                        }
                                    });
        } else {

        }


    }

    private void displayTextFromImage(FirebaseVisionText firebaseVisionText) {
        String resultText = firebaseVisionText.getText();
        List<FirebaseVisionText.TextBlock> blocks = firebaseVisionText.getTextBlocks();
        if (blocks.size() == 0) {
//            Log.e(TAG, "displayTextFromImage: not Text is there");
            Toast.makeText(this, "Can't read this is image.Please try again", Toast.LENGTH_SHORT).show();
        } else {

//            Log.e(TAG, "displayTextFromImage: result Text " + resultText);
            mTextView.setText(resultText);
            for (FirebaseVisionText.TextBlock block : blocks) {
                String blockText = block.getText();
                Float blockConfidence = block.getConfidence();
                List<RecognizedLanguage> blockLanguages = block.getRecognizedLanguages();
                Point[] blockCornerPoints = block.getCornerPoints();
                Rect blockFrame = block.getBoundingBox();
//                Log.e(TAG, "displayTextFromImage: block Text ==> " + blockText + "\n confidence level of block Text ==>" + blockConfidence);
                for (FirebaseVisionText.Line line : block.getLines()) {
                    String lineText = line.getText();
                    Float lineConfidence = line.getConfidence();
                    List<RecognizedLanguage> lineLanguages = line.getRecognizedLanguages();
                    Point[] lineCornerPoints = line.getCornerPoints();
                    Rect lineFrame = line.getBoundingBox();
//                    Log.e(TAG, "displayTextFromImage: line Text ==> " + lineText + "\n confidence level of line Text ==>" + lineConfidence);
                    userInfo.add(lineText);
                    for (FirebaseVisionText.Element element : line.getElements()) {
                        String elementText = element.getText();
                        Float elementConfidence = element.getConfidence();
                        List<RecognizedLanguage> elementLanguages = element.getRecognizedLanguages();
                        Point[] elementCornerPoints = element.getCornerPoints();
                        Rect elementFrame = element.getBoundingBox();
//                        Log.e(TAG, "displayTextFromImage: elementText Text ==> " + elementText + "\n confidence level of elementText  ==>" + elementConfidence);

                    }
                }
            }
        }
        if (!userInfo.isEmpty()) {
            Intent intent = new Intent();
            intent.putExtra("data", resultText);
            Bundle args = new Bundle();
            intent.putExtra("user_info", userInfo);
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else {

        }
    }

    private void dispatchTakePictureIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(),
                "MyPhoto.jpg");
        outPutfileUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutfileUri);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        try {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        } catch (ActivityNotFoundException e) {
//            e.printStackTrace();
//            // display error state to the user
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e(TAG, "onRequestPermissionsResult: " + requestCode + "  ==   " + requestPermissionID);
        if (grantResults.length > 0 && requestCode == requestPermissionID) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
                // Do_SOme_Operation();
                Toast.makeText(this, " granted", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "not granted", Toast.LENGTH_SHORT).show();
            finish();
//            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            imageBitmap = (Bitmap) extras.get("data");
//            showImage.setImageBitmap(imageBitmap);
            String uri = outPutfileUri.toString();
            Log.e("uri-:", uri);
//            Toast.makeText(this, outPutfileUri.toString(),Toast.LENGTH_LONG).show();
//                showImage.setImageURI(outPutfileUri);
            CropImage.activity(outPutfileUri)
                    .start(this);

//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), outPutfileUri);
//                Drawable d = new BitmapDrawable(getResources(), bitmap);
////                showImage.setImageDrawable(d);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                showImage.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    private void startCameraSource() {
        //Create the TextRecognizer
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!textRecognizer.isOperational()) {
            Log.w(TAG, "Detector dependencies not loaded yet");
        } else {

            //Initialize camerasource to use high resolution and set Autofocus on.
            mCameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(2.0f)
                    .build();

            /**
             * Add call back to SurfaceView and check if camera permission is granted.
             * If permission is granted we can start our cameraSource and pass it to surfaceView
             */
            mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(OCR.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    requestPermissionID);
                            return;
                        }
                        mCameraSource.start(mCameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                /**
                 * Release resources for cameraSource
                 */
                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    mCameraSource.stop();
                }
            });

            //Set the TextRecognizer's Processor.
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {


                }

                /**
                 * Detect all the text from camera using TextBlock and the values into a stringBuilder
                 * which will then be set to the textView.
                 * */
                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size() != 0) {

                        mTextView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; i < items.size(); i++) {
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                }
                                mTextView.setText(stringBuilder.toString());
                            }
                        });

                    }
                }
            });
        }
    }
}