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

import retrofit.http.GET;

public interface NationalGeographicService {

   @GET("/ng/photography/photo-of-the-day/")
   Rss getResponse();

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

      @Element
      @Namespace(reference = "http://rssnamespace.org/feedburner/ext/1.0")
      String origLink;
   }

}
