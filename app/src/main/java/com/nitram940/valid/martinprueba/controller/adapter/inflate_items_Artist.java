package com.nitram940.valid.martinprueba.controller.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nitram940.valid.martinprueba.R;
import com.nitram940.valid.martinprueba.model.helper.picture;
import com.nitram940.valid.martinprueba.model.model_json.Artist;

public class inflate_items_Artist {

    private Context context;
    private picture picture_picasso;

    public inflate_items_Artist(Context context, picture picture_picasso) {
        this.context = context;
        this.picture_picasso = picture_picasso;
    }


    public void inflate_Artist(@NonNull View v, @NonNull final Artist artist) {
        CardView artistLayout;
        TextView artistName;
        ImageView poster_path;
        RelativeLayout container_item;
        TextView textIsOffline;
        ImageView imgIsOffline;
        TextView tagline;
        TextView status;
        RecyclerView recyclerView;

        artistLayout = v.findViewById(R.id.movies_layout);
        artistName = v.findViewById(R.id.title);
        poster_path = v.findViewById(R.id.poster_path);
        textIsOffline = v.findViewById(R.id.textIsOffline);
        imgIsOffline = v.findViewById(R.id.imgIsOffline);

        //ui
        container_item = v.findViewById(R.id.container_item);
        //ui

        //detail
        status = v.findViewById(R.id.status);
        tagline = v.findViewById(R.id.tagline);

            container_item.setVisibility(View.VISIBLE);
            if (artist.isOffline()) {
                imgIsOffline.setImageResource(android.R.drawable.presence_offline);
                textIsOffline.setText(R.string.offline);
            } else {
                imgIsOffline.setImageResource(android.R.drawable.presence_online);
                textIsOffline.setText(R.string.online);
            }

            artistName.setText(artist.getName_artist());

        //!artist.getNoData()
            if (true) {
                //artist.isDetail() || artist.getInfoItem()
                if (true) {
                    status.setVisibility(View.VISIBLE);
                    //movieDescription.setMaxLines(Integer.MAX_VALUE);
                    artistName.setEllipsize(null);
                    artistName.setMaxLines(Integer.MAX_VALUE);
                    //movieDescription.setEllipsize(null);
                    tagline.setVisibility(View.VISIBLE);
                    //status.setText(artist.getStatus());
                } else {
                    artistLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            /**context.tartActivity(
                                    new Intent(context, FullscreenActivityDetailMovie.class)
                                            .putExtra(FullscreenActivityDetailMovie.ID_MOVIE, artist.getId())
                                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));*/
                        }
                    });
                    //movieDescription.setEllipsize(TextUtils.TruncateAt.END);
                    //movieDescription.setMaxLines(1);
                    tagline.setVisibility(View.GONE);
                    status.setVisibility(View.GONE);
                    artistName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    artistName.setMaxLines(1);
                }



                if (true) {

                    if (artist.getImageModelList() != null)
                        picture_picasso.LoadPicasso(artist.getImageModelList().get(0).getUrl(), poster_path, 1,false);
                } else {

                    if (artist.getImageModelList() != null)
                        picture_picasso.LoadPicasso(artist.getImageModelList().get(0).getUrl(), poster_path, 1,true);
                }
            }


    }


}

