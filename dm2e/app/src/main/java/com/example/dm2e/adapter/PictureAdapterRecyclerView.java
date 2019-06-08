package com.example.dm2e.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.dm2e.R;
import com.example.dm2e.model.Picture;
import com.example.dm2e.view.PictureDetailActivity;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;


public class PictureAdapterRecyclerView extends RecyclerView.Adapter<PictureAdapterRecyclerView.PictureViewHolder> {

    private ArrayList<Picture> pictures;
    private int resource;
    private Activity activity;

    public PictureAdapterRecyclerView(ArrayList<Picture> pictures, int resource, Activity activity) {
        this.pictures = pictures;
        this.resource = resource;
        this.activity = activity;
    }


    //inicializa nuestra clase PictureViewHolder
    @Override
    public PictureViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        //"Inflamos" el código XML programaticamente y lo pasamos como parametro al crear el nuevo objeto.
        View view = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);

        return new PictureViewHolder(view);
    }

    //Trabajamos el paso de datos de cada objeto contenido en la lista pictures
    @Override
    public void onBindViewHolder(PictureViewHolder holder, int position) {
        final Picture picture = pictures.get(position);

        holder.usernameCard.setText(picture.getUserName());
        holder.likeNumberCard.setText(String.valueOf(picture.getLikes()));

        //Recurso para traer imagenes desde Internet a traves de la URL
        Picasso.get().load(picture.getPicture()).into(holder.pictureCard);

        holder.pictureCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity, PictureDetailActivity.class);

                String user = picture.getUserName();
                String pic = picture.getPicture();
                String title = picture.getTitle();
                String desc = picture.getDescription();
                int likes = picture.getLikes();

                intent.putExtra("user",user);
                intent.putExtra("pic",pic);
                intent.putExtra("title",title);
                intent.putExtra("desc",desc);
                intent.putExtra("likes",likes);

                //Para que la transicion funcione correctamente, tiene que correr una version
                //superior a Lollipop
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Explode explode = new Explode();
                    explode.setDuration(1000);
                    activity.getWindow().setExitTransition(explode);
                    activity.startActivity(intent,
                            ActivityOptionsCompat.makeSceneTransitionAnimation(activity, view,
                                    activity.getString(R.string.transitionname_picture)).toBundle());
                } else {

                    activity.startActivity(intent);

                }
            }
        });

    }

    //Devuelve la cantidad de objetos
    @Override
    public int getItemCount() {
        return pictures.size();
    }

    public class PictureViewHolder extends RecyclerView.ViewHolder {

        private ImageView pictureCard;
        private TextView usernameCard;
        private TextView likeNumberCard;


        public PictureViewHolder(@NonNull View itemView) {
            super(itemView);

            pictureCard = (ImageView) itemView.findViewById(R.id.pictureCard);
            usernameCard = (TextView) itemView.findViewById(R.id.userNameCard);
            likeNumberCard = (TextView) itemView.findViewById(R.id.likeNumberCard);

        }
    }
}
