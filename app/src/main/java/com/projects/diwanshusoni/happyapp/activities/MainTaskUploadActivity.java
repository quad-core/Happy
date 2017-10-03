package com.projects.diwanshusoni.happyapp.activities;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.UploadTask;
import com.projects.diwanshusoni.happyapp.R;
import com.projects.diwanshusoni.happyapp.constants.constants;
import com.projects.diwanshusoni.happyapp.pojos.PojoSubmitedTask;
import com.projects.diwanshusoni.happyapp.pojos.PojoTaskTodo;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;

public class MainTaskUploadActivity extends AppCompatActivity {

    private static final int GALLERY_REQUEST = 1;
    private TextView tv_taskNumber;
    private EditText et_postText;
    private ImageView iv_aboutTaskImage;
    private ImageButton btn_uploadVideo, btn_uploadImage, btn_uploadAudio;
    private Button btn_submitTask;
    private ProgressBar pb_uploadImage, pb_submitTask;


    private LinearLayout linearLayout;
    private TextView textViewttd_head, textViewttd_createdBy, textViewttd_createdOn, textViewttd_totalSubmissions, textViewttd_averageScore, textViewttd_taskDescription;

    private PojoTaskTodo taskTodo;
    private PojoSubmitedTask pojoSubmitedTask ;

    //alerDialog
    private ProgressDialog dialog ;
    private ImageView iv_imagePreview;
    private Uri imageuri1 ;

    //uploading image
    private boolean isImageUploaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_task_upload);
        link();
        setActionBar();
        listenerSetup();
        taskTodo = (PojoTaskTodo) getIntent().getSerializableExtra("pojoTaskTodo");
        pojoSubmitedTask = new PojoSubmitedTask();
        pojoSubmitedTask.setUserName(constants.USER.getDisplayName());
        pojoSubmitedTask.setTaskUid(taskTodo.getTaskUid());
        pojoSubmitedTask.setUserProfilePicImageUrl(""+constants.USER.getPhotoUrl());

        final PojoTaskTodo pojoTaskTodo = taskTodo;
        YoYo.with(Techniques.Shake)
                .onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public void call(Animator animator) {
                        textViewttd_head.setText(pojoTaskTodo.getTaskHeading());
                        textViewttd_createdOn.setText(pojoTaskTodo.getDate().split(" ")[1]+" "+taskTodo.getDate().split(" ")[2]+" "+taskTodo.getDate().split(" ")[5]);
                        textViewttd_createdBy.setText(pojoTaskTodo.getCreatedBy());
                        textViewttd_taskDescription.setText(pojoTaskTodo.getTaskDescription());
                    }
                })
                .duration(500)
                .playOn(linearLayout);

        setTaskData();
    }

    private void listenerSetup() {
        btn_submitTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitTaskProcess();
            }
        });

        btn_uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImageProcess();
            }
        });

        iv_aboutTaskImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void uploadImageProcess() {
        addImage();
    }


    private void createImagePreviewDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainTaskUploadActivity.this);
        View view = getLayoutInflater().inflate(R.layout.showselected_image_alert_layout, null);
        AlertDialog dialog;

        iv_imagePreview = (ImageView) view.findViewById(R.id.image_preview_alert_id);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageuri1=null;
            }
        });
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (imageuri1 == null){
                    Toast.makeText(MainTaskUploadActivity.this, "No Image Selected !", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    beginImageUploadProcess();
                  //  Toast.makeText(MainTaskUploadActivity.this, "BOLO TARA RARA", Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    private void beginImageUploadProcess() {
        pb_uploadImage.setVisibility(View.VISIBLE);
        btn_uploadImage.setVisibility(View.INVISIBLE);
        //freezeUntillAddQue("Please Wait\nUploading Question Image");
        constants.REF_TASK_IMAGE_STORAGE.child(imageuri1.getLastPathSegment()).putFile(imageuri1).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @SuppressWarnings("VisibleForTests")
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    pb_uploadImage.setVisibility(View.INVISIBLE);
                    btn_uploadImage.setVisibility(View.VISIBLE);
                    btn_uploadImage.setImageURI(imageuri1);
                    pojoSubmitedTask.setTaskImageUrl(task.getResult().getDownloadUrl().toString());
                    isImageUploaded = true;
                    // dialog.dismiss();
                    //sendDataToFb();
                }else {
                    Toast.makeText(MainTaskUploadActivity.this, "Image Not Uploaded", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void freezeUntillAddQue(String message) {
        dialog = new ProgressDialog(MainTaskUploadActivity.this);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage(message);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
    }


    private void addImage() {
        Intent gallery = new Intent();
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri imageuri = data.getData();
            CropImage.activity(imageuri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            createImagePreviewDialog();
            if (resultCode == RESULT_OK) {
                imageuri1 = result.getUri();
                iv_imagePreview.setImageURI(imageuri1);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void submitTaskProcess() {
        if (!et_postText.getText().toString().trim().isEmpty()){
            if (!isImageUploaded){
                Toast.makeText(this, "image not Uploaded", Toast.LENGTH_SHORT).show();
            }
            else {
                pojoSubmitedTask.setDateSubmittedOn(""+Calendar.getInstance().getTime());
                pojoSubmitedTask.setTaskExperience(et_postText.getText().toString());
                subitFinalTask();
            }
        }
        else {
            Toast.makeText(this, "Please post your Task Experience", Toast.LENGTH_SHORT).show();
        }
    }

    private void subitFinalTask() {
        btn_submitTask.setVisibility(View.INVISIBLE);
        pb_submitTask.setVisibility(View.VISIBLE);
        constants.REF_SUBMITTED_TASKS_NODE.child(taskTodo.getTaskUid()+"_"+constants.USER.getEmail().replace(".", "_")).setValue(pojoSubmitedTask)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            btn_submitTask.setVisibility(View.VISIBLE);
                            Toast.makeText(MainTaskUploadActivity.this, "SUBMITTED", Toast.LENGTH_SHORT).show();
                            pb_submitTask.setVisibility(View.INVISIBLE);
                            finish();
                        }
                        else {

                        }

                    }
                });
    }

    private void setTaskData() {
        tv_taskNumber.setText(tv_taskNumber.getText().toString()+" "+taskTodo.getTaskUid());
    }

    private void link() {
        tv_taskNumber = (TextView) findViewById(R.id.tv_taskuid_main_id);
        et_postText = (EditText) findViewById(R.id.et_task_exp_main_id);
        btn_uploadImage = (ImageButton) findViewById(R.id.btn_img_id);
        btn_submitTask = (Button) findViewById(R.id.btn_submit_main_id);
        pb_submitTask = (ProgressBar) findViewById(R.id.pb_btn_submit_main_id);
        pb_uploadImage = (ProgressBar) findViewById(R.id.progress_img_id);
        iv_aboutTaskImage = (ImageView) findViewById(R.id.iv_maintaskupload_about_id);



        linearLayout = (LinearLayout) findViewById(R.id.linearlay_taskinfo_tasktodo_dash_id);
        textViewttd_averageScore = (TextView) findViewById(R.id.tv_dash_tasktodo_taskaveragescore_id);
        textViewttd_createdBy = (TextView) findViewById(R.id.tv_dash_tasktodo_createdby_id);
        textViewttd_createdOn = (TextView) findViewById(R.id.tv_dash_tasktodo_createdon_id);
        textViewttd_head = (TextView) findViewById(R.id.tv_dash_tasktodo_taskhead_id);
        textViewttd_totalSubmissions = (TextView) findViewById(R.id.tv_dash_tasktodo_tasktotalsubmissions_id);
        textViewttd_taskDescription = (TextView) findViewById(R.id.tv_dash_tasktodo_taskdescription_id);
    }

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        try {
            actionBar.setTitle("HappyApp : Submit Task");
        }
        catch (NullPointerException e){

        }
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
