package com.example.dm2e.view.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crashlytics.android.Crashlytics;
import com.example.dm2e.R;
import com.example.dm2e.adapter.PictureAdapterRecyclerView;
import com.example.dm2e.model.Picture;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    private static final String TAG = "SearchFragment";
    private SearchView searchView;
    private DatabaseReference firebaseDatabasePics;
    private ArrayList<Picture> pictures = new ArrayList<>();

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Crashlytics.log("Inicio "+ TAG);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        final RecyclerView picturesRecycler = (RecyclerView) view.findViewById(R.id.searchFragment);

        searchView = view.findViewById(R.id.searchView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        picturesRecycler.setLayoutManager(linearLayoutManager);

        final PictureAdapterRecyclerView[] pictureAdapterRecyclerView = {new PictureAdapterRecyclerView(pictures, R.layout.cardview_picture, getActivity())};
        picturesRecycler.setAdapter(pictureAdapterRecyclerView[0]);

        ///////////////////////////////////

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                final String text = searchView.getQuery().toString().toLowerCase();

                firebaseDatabasePics = FirebaseDatabase.getInstance().getReference("Pictures");

                firebaseDatabasePics.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        pictures.clear();

                        for (DataSnapshot pictureSnapshot : dataSnapshot.getChildren()) {
                            Picture picture = pictureSnapshot.getValue(Picture.class);
                           String title = picture.getTitle().toLowerCase();
                            Log.w(TAG, title+"///////"+text);
                            if(title.contains(text)){
                                pictures.add(picture);
                            }
                            pictureAdapterRecyclerView[0] =
                                    new PictureAdapterRecyclerView(pictures, R.layout.cardview_picture, getActivity());
                            picturesRecycler.setAdapter(pictureAdapterRecyclerView[0]);

                            Log.w(TAG, "url imagen: " + picture.getPicture());

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w(TAG, databaseError.getMessage());
                    }
                });
                return false;
            }
        });

        return view;
    }

}