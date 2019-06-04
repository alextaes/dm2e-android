package com.example.dm2e.view.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;
import com.example.dm2e.R;
import com.example.dm2e.adapter.PictureAdapterRecyclerView;
import com.example.dm2e.model.Picture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private DatabaseReference firebaseDatabasePics;
    private ArrayList<Picture> pictures = new ArrayList<>();

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Crashlytics.log("Inicio "+ TAG);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        showToolbar("", false, view);
        final RecyclerView picturesRecycler = (RecyclerView) view.findViewById(R.id.pictureProfileRecycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        picturesRecycler.setLayoutManager(linearLayoutManager);

        final PictureAdapterRecyclerView[] pictureAdapterRecyclerView = {new PictureAdapterRecyclerView(pictures, R.layout.cardview_picture, getActivity())};
        picturesRecycler.setAdapter(pictureAdapterRecyclerView[0]);
        /////////////////////////////////

        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseDatabasePics = FirebaseDatabase.getInstance().getReference("Pictures").child(id);

        firebaseDatabasePics.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pictures.clear();

                for(DataSnapshot pictureSnapshot : dataSnapshot.getChildren()) {
                    Picture picture = pictureSnapshot.getValue(Picture.class);
                    Log.w(TAG,"id imagen: "+pictureSnapshot.getKey());
                    pictures.add(picture);

                    pictureAdapterRecyclerView[0] =
                            new PictureAdapterRecyclerView(pictures, R.layout.cardview_picture, getActivity());
                    picturesRecycler.setAdapter(pictureAdapterRecyclerView[0]);

                    Log.w(TAG,"url imagen: "+picture.getPicture());

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //////////////////////////////////

        return view;
    }

    public ArrayList<Picture> buildPictures(){
        ArrayList<Picture> pictures = new ArrayList<>();
        pictures.add(new Picture("https://www.novalandtours.com/highlight/156.jpg", "Pablo Ramírez", "4 días", "3 Me Gusta","Hola"));
        pictures.add(new Picture("https://www.novalandtours.com/highlight/417.jpg", "Juan Castro", "3 días", "10 Me Gusta","Hola"));
        pictures.add(new Picture("https://www.novalandtours.com/highlight/282.jpg", "David Salgado", "2 días", "9 Me Gusta","Hola"));
        return pictures;
    }



    public void showToolbar(String tittle, boolean upButton, View view){
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(tittle);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);


    }

}
