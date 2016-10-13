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

import de.msal.muzei.nationalgeographic.model.Feed;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

class NationalGeographicService {

   private static final String API_URL = "http://www.nationalgeographic.com";

   static Service getAdapter() {
      Retrofit ngAdapter = new Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

      return ngAdapter.create(Service.class);
   }

   interface Service {

      @GET("/photography/photo-of-the-day/_jcr_content/.gallery.json")
      Call<Feed> getPhotoOfTheDayFeed();

      @GET("/photography/photo-of-the-day/_jcr_content/.gallery.{year}-{month}.json")
      Call<Feed> getPhotoOfTheDayFeed(
            @Path("year") int year,
            @Path("month") int month
      );
   }

}
