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

import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import java.util.List;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.SimpleXMLConverter;
import retrofit.http.GET;

public class NationalGeographicService {

   private static final String API_URL = "http://feeds.nationalgeographic.com";

   public interface Service {

      @GET("/ng/photography/photo-of-the-day/")
      Rss getPhotoOfTheDayFeed();
   }

   public static Service getAdapter() {
      RestAdapter ngAdapter = new RestAdapter.Builder()
            .setEndpoint(API_URL)
            .setConverter(new SimpleXMLConverter())
            .setErrorHandler(new retrofit.ErrorHandler() {
               @Override
               public Throwable handleError(RetrofitError retrofitError) {
                  Response response = retrofitError.getResponse();
                  if (response == null) {
                     return new RemoteMuzeiArtSource.RetryException();
                  }
                  int statusCode = response.getStatus();
                  if (retrofitError.getKind() == RetrofitError.Kind.NETWORK
                        || (500 <= statusCode && statusCode < 600)) {
                     return new RemoteMuzeiArtSource.RetryException();
                  }
                  return retrofitError;
               }
            })
            .build();

      return ngAdapter.create(Service.class);
   }

   @Root(strict = false, name = "rss")
   @Namespace(reference = "http://rssnamespace.org/feedburner/ext/1.0", prefix = "feedburner")
   static class Rss {

      @Element(name = "channel")
      Feed feed;

      List<Photo> getPhotos() {
         return feed.getPhotos();
      }
   }

   @Root(strict = false, name = "channel")
   static class Feed {

      @ElementList(name = "item", inline = true)
      List<Photo> photos;

      List<Photo> getPhotos() {
         return photos;
      }
   }

   @Root(strict = false, name = "item")
   static class Photo {

      @Element
      String title;

      @Element
      String link;

      @Element
      String description;

      /**
       * pubDate can be used as unique ID
       */
      @Element
      String pubDate;

      @Element
      Enclusure enclosure;

      @Root(strict = false)
      static class Enclusure {

         @Attribute
         String url;
      }
   }

}
