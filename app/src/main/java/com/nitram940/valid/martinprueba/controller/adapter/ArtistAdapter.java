package com.nitram940.valid.martinprueba.controller.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nitram940.valid.martinprueba.R;
import com.nitram940.valid.martinprueba.model.helper.picture;
import com.nitram940.valid.martinprueba.model.model_json.Artist;

import java.util.ArrayList;
import java.util.List;


public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {

    private boolean categorizate = false;
    private List<Artist> artistList;
    private int rowLayout;
    private Context context;
    private picture picture_picasso;

    private inflate_items_Artist inflateItems;
    private int lastPosition = -1;


    public ArtistAdapter(@NonNull List<Artist> artistList, int rowLayout, Context context, picture picture_picasso) {
        this.categorizate = categorizate;
        this.artistList = artistList;
        this.rowLayout = rowLayout;
        this.context = context;
        this.picture_picasso = picture_picasso;
        this.inflateItems = new inflate_items_Artist(context, picture_picasso);
        if (artistList.size() == 0) IfNoArtist();
    }

    public ArtistAdapter(@NonNull Artist artist, int rowLayout, Context context, picture picture_picasso) {
        this.categorizate = categorizate;
        this.artistList = new ArrayList<>();
        this.artistList.add(artist);
        this.rowLayout = rowLayout;
        this.context = context;
        this.picture_picasso = picture_picasso;
        this.inflateItems = new inflate_items_Artist(context, picture_picasso);
        if (artistList.size() == 0) IfNoArtist();
    }

    public void setSearchResponse(List<Artist> artistList1) {
        this.artistList = artistList1;
        notifyDataSetChanged();
    }

    public void setSearchResponse(Artist artist) {
        this.artistList.clear();
        this.artistList.add(artist);
        notifyDataSetChanged();
    }

    private void IfNoArtist() {
        Artist artist = new Artist();

        artist.setName_artist(context.getString(R.string.noData));
        //artist.setOverview(context.getString(R.string.verifyConnection));
        artist.setOffline(true);
        //artist.setNoData(true);
        artistList.add(artist);
    }


    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, final int position) {
            inflateItems.inflate_Artist(holder.itemView, artistList.get(position));
        lastPosition = animViews.setAnimation(holder.itemView, position, lastPosition);
    }

    @Override
    public int getItemCount() {
        int size;
        if (artistList == null) {
            size = 0;
        } else {
            size = artistList.size();
        }
        return size;
    }

    static class ArtistViewHolder extends RecyclerView.ViewHolder {

        ArtistViewHolder(@NonNull View v) {
            super(v);
        }
    }
}