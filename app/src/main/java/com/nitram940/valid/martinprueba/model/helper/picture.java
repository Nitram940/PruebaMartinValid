package com.nitram940.valid.martinprueba.model.helper;

import android.content.Context;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.nitram940.valid.martinprueba.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;

@Singleton
@Module
public class picture {

    private static boolean initialise = false;
    private static int newSizeW;
    private static int newSizeH;
    private boolean resize;
    private final Context context;
    private final Utils utils;
    private Picasso picasso;

    @Inject
    public picture(Context context, Utils utils) {
        this.utils = utils;
        this.context = context;
    }

    private Point getDisplaySize(@NonNull Display display) {
        DisplayMetrics displayMetrics = new DisplayMetrics();

        display.getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        return new Point(width, height);
    }

    public void LoadPicasso(final String uri, @NonNull final ImageView imageView, final Integer intento, boolean resize) {
        this.resize=resize;
        LoadPicasso(uri, imageView, intento, 0);
    }

    public void LoadPicasso(final int intDrawable, @NonNull final ImageView imageView) {
        LoadPicasso(null, imageView, 1, intDrawable);
    }

    private void loadSizes() {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        assert windowManager != null;
        windowManager.getDefaultDisplay().getMetrics(metrics);

        final Point displySize = getDisplaySize(windowManager.getDefaultDisplay());
        //final int size = (int) Math.ceil(Math.sqrt(displySize.x * displySize.y));
        newSizeW = displySize.x / 2;
        newSizeH = displySize.y * 2 / 5;

        initialise = true;
    }

    private void LoadPicasso(@Nullable final String uri, @NonNull final ImageView imageView, final Integer intent, final int intDrawable) {
        if (!initialise) {
            loadSizes();
            picasso = Picasso.with(context);
            picasso.setIndicatorsEnabled(false);
            picasso.setLoggingEnabled(false);
        }

        final RequestCreator requestCreatorLoadOffline;

        if (uri != null) {
            requestCreatorLoadOffline = picasso.load(uri);
        } else {
            requestCreatorLoadOffline = picasso.load(intDrawable);
        }

        requestCreatorLoadOffline.placeholder(R.drawable.picasso_progress_animation);
        requestCreatorLoadOffline.error(R.drawable.ic_error);

        if (intent == 1 || utils.getKEY_OFFLINE()) {
            requestCreatorLoadOffline.networkPolicy(NetworkPolicy.OFFLINE);
        }

        try {
            if (resize) {
                requestCreatorLoadOffline.resize(newSizeW, newSizeH);
            }

            requestCreatorLoadOffline.
                    into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            //Log.i("Picasso ","Exito imagen");
                        }

                        @Override
                        public void onError() {
                            if (intent == 1) {
                                LoadPicasso(uri, imageView, 2,resize);
                            }
                            //Log.i("Picasso ","no store cuadrado");
                        }
                    });
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

    }
}
