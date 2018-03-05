package com.nitram940.valid.martinprueba.model.callback;


import android.support.annotation.NonNull;

import com.nitram940.valid.martinprueba.model.model_json.ArtistResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


public interface LastFmApis {
    /**
	 *
	 * @param apiKey
	 * @param country
	 * @param page
	 */
	@NonNull
	@GET("2.0/")
    Observable<ArtistResponse> getTopArtist(
			@Query("method") String method,
			@Query("api_key") String apiKey,
			@Query("country") String country,
			@Query("page") int page,
			@Query("format") String format);

    /**
	 *
	 * @param apiKey
	 * @param language
	 * @param page
	 */
	/**@NonNull
	@GET("movie/popular")
    Observable<MoviesResponse> getTopTracks(@Query("api_key") String apiKey, @Query("language") String language, @Query("page") int page);
	*/
}
