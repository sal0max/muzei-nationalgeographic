/*
 * Copyright 2014 Maximilian Salomon.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package de.msal.muzei.nationalgeographic;

import com.google.gson.GsonBuilder;

import de.msal.muzei.nationalgeographic.model.Feed;
import de.msal.muzei.nationalgeographic.model.Item;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.io.IOException;
import java.util.List;

class NationalGeographicService {

   private static final String API_URL = "https://www.nationalgeographic.co.uk";

   @SuppressWarnings("WeakerAccess")
   static Service getAdapter() {
      OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

      // add logging interceptor only for debug builds
      HttpLoggingInterceptor httpLogger = new HttpLoggingInterceptor();
      httpLogger.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BASIC : HttpLoggingInterceptor.Level.NONE);
      httpClient.addInterceptor(httpLogger);

      Retrofit ngAdapter = new Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create(
                  new GsonBuilder()
                        .registerTypeAdapter(Feed.class, new FeedDeserializer())
                        .create()))
            .client(httpClient.build())
            .build();

      return ngAdapter.create(Service.class);
   }

   interface Service {

      @GET("/page-data/photo-of-day/page-data.json")
      Call<Feed> getPhotoOfTheDayFeed();

      @GET("/page-data/photo-of-the-day/{year}/{month}/page-data.json")
      Call<Feed> getPhotoOfTheDayFeed(
            @Path("year") int year,
            @Path("month") String month
      );
   }

   public static Item getPhotoOfTheDay() throws IOException {
      Feed body = getAdapter().getPhotoOfTheDayFeed().execute().body();
      if (body != null)
         return body.getItems().get(0);
      else
         throw new IOException("Response was null.");
   }

   public static List<Item> getPhotosOfTheDay(int year, String month) throws IOException {
      Feed body = getAdapter().getPhotoOfTheDayFeed(year, month).execute().body();
      if (body != null)
         return body.getItems();
      else
         throw new IOException("Response was null.");
   }

}
