package com.example.dm2e.post.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.crashlytics.android.Crashlytics;
import com.example.dm2e.R;
import com.example.dm2e.adapter.PictureAdapterRecyclerView;
import com.example.dm2e.model.Picture;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final int REQUEST_CAMERA = 1;
    private static final String TAG = "HomeFragment";
    private FloatingActionButton fabCamera;
    private String photoPathTemp = "";

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Crashlytics.log("Inicio "+ TAG);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        showToolbar(getResources().getString(R.string.tab_home), false, view);
        RecyclerView picturesRecycler = (RecyclerView) view.findViewById(R.id.pictureRecycler);

        fabCamera = (FloatingActionButton) view.findViewById(R.id.fabCamera);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        picturesRecycler.setLayoutManager(linearLayoutManager);

        PictureAdapterRecyclerView pictureAdapterRecyclerView =
                new PictureAdapterRecyclerView(buildPictures(), R.layout.cardview_picture, getActivity());
        picturesRecycler.setAdapter(pictureAdapterRecyclerView);


        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        return view;
    }

    private void takePicture() {

        Intent intentTakePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if( intentTakePicture.resolveActivity(getActivity().getPackageManager()) != null ) {

            File photoFile = null;

            try {
                photoFile = createImageFile();
            }catch (Exception e) {
                e.getMessage();
                Crashlytics.logException(e);
            }

            //Cuando tomamos la foto, comprueba que se haya almacenado en la variable
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(getActivity(), "com.example.dm2e", photoFile);
                intentTakePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intentTakePicture, REQUEST_CAMERA);
            }


        }
    }

    //Creamos la imagen, la almacenamos con nombre unico usando el timeStamp
    //guardamos el path de la imagen
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HH-mm-ss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        //Almacenamos en una variable File el directorio por defecto para almacenar imagenes
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File photo = File.createTempFile(imageFileName, ".jpg", storageDir);

        photoPathTemp = "file:" + photo.getAbsolutePath();

        return photo;

    }

    //Cuando la foto haya sido tomada, nos manda al NewPostActivity para darle
    //titulo y descripcion
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CAMERA && resultCode == getActivity().RESULT_OK) {
            Log.d("HomeFragment","CAMERA OK!");
            Intent i = new Intent(getActivity(), NewPostActivity.class);
            i.putExtra("PHOTO_PATH_TEMP", photoPathTemp);
            startActivity(i);

        }
    }

    public ArrayList<Picture> buildPictures(){
        ArrayList<Picture> pictures = new ArrayList<>();
        pictures.add(new Picture("https://www.novalandtours.com/highlight/156.jpg", "Pablo Ramírez", "4 días", "3 Me Gusta"));
        pictures.add(new Picture("https://www.novalandtours.com/highlight/417.jpg", "Juan Castro", "3 días", "10 Me Gusta"));
        pictures.add(new Picture("https://www.novalandtours.com/highlight/282.jpg", "David Salgado", "2 días", "9 Me Gusta"));
        return pictures;
    }

    public void showToolbar(String tittle, boolean upButton, View view){
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(tittle);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);


    }

}