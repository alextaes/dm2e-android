package com.example.dm2e.view;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.example.dm2e.Dm2eApplication;
import com.example.dm2e.R;
import com.example.dm2e.model.Picture;
import com.example.dm2e.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.io.ByteArrayOutputStream;

public class NewPostActivity extends AppCompatActivity {

    private static final String TAG = "NewPostActivity";
    private ImageView imgPhoto;
    private Button btnCreatePost;
    private String photoPath;
    private TextInputEditText edtTitle, edtDescription;
    private Dm2eApplication app;
    private StorageReference storageReference;
    private DatabaseReference firebaseDataPictures, reference;
    private ProgressBar progressBarPost;
    private String id;
    private String username = "default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        Crashlytics.log("Inicio "+ TAG);

        app = (Dm2eApplication) getApplicationContext();
        storageReference = app.getStorageReference();
        id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //firebaseDataPictures = FirebaseDatabase.getInstance().getReference("Pictures").child(id);
        firebaseDataPictures = FirebaseDatabase.getInstance().getReference("Pictures");

        imgPhoto = (ImageView) findViewById(R.id.imgPhoto);
        btnCreatePost = (Button) findViewById(R.id.btnCreatePost);
        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
        progressBarPost = (ProgressBar) findViewById(R.id.progressbarPost);

        hideProgressBar();


        if( getIntent().getExtras() != null ) {
            photoPath = getIntent().getExtras().getString("PHOTO_PATH_TEMP");
            showPhoto();
        }

        btnCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();
            }
        });




        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username = dataSnapshot.getValue(User.class).getName();
                Log.w(TAG, "USER Nombre --> " + dataSnapshot.getValue(User.class).getName());
                Log.w(TAG, "Variable Nombre --> " + username);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Error al obtener id -->" + databaseError.getCode());

            }
        });

    }

    private void uploadPhoto() {

        showProgressBar();
        final String titulo = edtTitle.getText().toString().trim();
        final String descripcion = edtDescription.getText().toString();


        if(!TextUtils.isEmpty(titulo)) {

            final String idPic = firebaseDataPictures.push().getKey();

            imgPhoto.setDrawingCacheEnabled(true);
            imgPhoto.buildDrawingCache();

            Bitmap bitmap = imgPhoto.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            byte[] photoByte = baos.toByteArray();
            String photoName = photoPath.substring(photoPath.lastIndexOf("/")+1, photoPath.length());

            StorageReference photoReference = storageReference.child("postImages/" + photoName);

            UploadTask uploadTask = photoReference.putBytes(photoByte);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error al subir la foto: " + e.toString());
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    Uri downloadUrl;
                    String photoUrl;

                    Log.w(TAG, "NOMBRE -->" + username);

                    while (!uriTask.isSuccessful());
                    downloadUrl = uriTask.getResult();
                    photoUrl = downloadUrl.toString();
                    Log.w(TAG, "URL PHOTO > " + photoUrl);

                    Picture picture = new Picture(idPic, photoUrl, username, id, titulo, descripcion);

                    //Anidamos las publicaciones del ususario bajo su id en la base de datos
                    firebaseDataPictures
                            .child(idPic)
                            .setValue(picture).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Toast.makeText(NewPostActivity.this, getString(R.string.new_publication), Toast.LENGTH_LONG).show();
                            } else {
                                //display a failure message
                                Toast.makeText(NewPostActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    hideProgressBar();
                    finish();
                }


            });

        } else {
            Toast.makeText(this, "Introduzca título", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPhoto() {
        Picasso.get().load(photoPath).into(imgPhoto);
    }

    public void showProgressBar() {
        progressBarPost.setVisibility(View.VISIBLE);
    }


    public void hideProgressBar() {
        progressBarPost.setVisibility(View.GONE);

    }


}
