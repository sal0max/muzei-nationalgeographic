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

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Root;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.GET;

public class NationalGeographicService {

   private static final String API_URL = "http://feeds.nationalgeographic.com";

   public interface Service {

      @GET("/ng/photography/photo-of-the-day/")
      Call<Rss> getPhotoOfTheDayFeed();
   }

   public static Service getAdapter() {
      Retrofit ngAdapter = new Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(SimpleXmlConverterFactory.createNonStrict())
            .build();

      return ngAdapter.create(Service.class);
   }

   @Root(name = "rss")
   @Namespace(reference = "http://rssnamespace.org/feedburner/ext/1.0", prefix = "feedburner")
   static class Rss {
      @Element(name = "channel")
      Feed feed;
      List<Photo> getPhotos() {
         return feed.getPhotos();
      }
   }

   @Root(name = "channel")
   static class Feed {
      @ElementList(name = "item", inline = true)
      List<Photo> photos;
      List<Photo> getPhotos() {
         return photos;
      }
   }

   @Root(name = "item")
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
   }

   @Root()
   static class Enclusure {
      @Attribute
      String url;
   }

}
