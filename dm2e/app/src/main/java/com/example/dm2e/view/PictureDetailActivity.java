package com.example.dm2e.view;

import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.example.dm2e.Dm2eApplication;
import com.example.dm2e.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class PictureDetailActivity extends AppCompatActivity {

    private static final String PHOTO_NAME = "JPEG_20190521_16-58-22_6704900775955634225.jpg";
    private static final String TAG = "PictureDetailActivity";

    private ImageView imageHeader;
    private Dm2eApplication app;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_detail);

        Crashlytics.log("Inicio "+ TAG);

        app = (Dm2eApplication) getApplicationContext();
        storageReference = app.getStorageReference();

        imageHeader = (ImageView) findViewById(R.id.imageHeader);

        showToolbar("",true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setEnterTransition(new Fade());
        }
        
        showData();
    }

    private void showData() {
        storageReference.child("postImages/"+PHOTO_NAME)
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri.toString()).into(imageHeader);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PictureDetailActivity.this, "Ocurrió un error al traer la foto", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                Crashlytics.logException(e);
            }
        });
    }

    public void showToolbar(String tittle, boolean upButton){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(tittle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolBar);

    }
}
