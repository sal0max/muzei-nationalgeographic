
package de.msal.muzei.nationalgeographic.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Item {

   @SerializedName("title")
   @Expose
   private String title;

   @SerializedName("caption")
   @Expose
   private String description;

   @SerializedName(value = "altText", alternate = {"alt_text"})
   @Expose
   private String altText;

   @SerializedName(value = "originalUrl", alternate = {"uri"})
   @Expose
   private String imageUrl;

   private String imageUrlLarge;

   @SerializedName("full-path-url")
   @Expose
   private String pageUrl;

   @SerializedName("pageUrl")
   @Expose
   private String pageUrlPhotoOfTheDay;

   @SerializedName("credit")
   @Expose
   private String photographer;

   @SerializedName("internal")
   @Expose
   private Boolean internal;

   @SerializedName("publishDate")
   @Expose
   private String publishDate;

   /**
    * @return The title
    */
   public String getTitle() {
      return title;
   }

   /**
    * @param title The title
    */
   public void setTitle(String title) {
      this.title = title;
   }

   /**
    * @return The description
    */
   public String getDescription() {
      return description;
   }

   /**
    * @param description The description
    */
   public void setDescription(String description) {
      this.description = description;
   }

   /**
    * @return The photographer
    */
   public String getPhotographer() {
      return photographer;
   }

   /**
    * @param photographer The photographer
    */
   public void setPhotographer(String photographer) {
      this.photographer = photographer;
   }

   /**
    * @return The altText
    */
   public String getAltText() {
      return altText;
   }

   /**
    * @param altText The altText
    */
   public void setAltText(String altText) {
      this.altText = altText;
   }

   /**
    * @return The pageUrl
    */
   public String getPageUrl() {
      return pageUrl;
   }

   /**
    * @param pageUrl The full-path-url
    */
   public void setPageUrl(String pageUrl) {
      this.pageUrl = pageUrl;
   }

   /**
    * @return The imageUrl
    */
   public String getImageUrl() {
      return imageUrl;
   }

   /**
    * @param imageUrl The imageUrl
    */
   public void setImageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
   }

   /**
    * @return The imageUrlLarge
    */
   public String getImageUrlLarge() {
      return imageUrlLarge;
   }

   /**
    * @param imageUrlLarge The imageUrlLarge
    */
   public void setImageUrlLarge(String imageUrlLarge) {
      this.imageUrlLarge = imageUrlLarge;
   }

   /**
    * @return The internal
    */
   public Boolean getInternal() {
      return internal;
   }

   /**
    * @param internal The internal
    */
   public void setInternal(Boolean internal) {
      this.internal = internal;
   }

   /**
    * @return The pageUrlPhotoOfTheDay
    */
   public String getPageUrlPhotoOfTheDay() {
      return pageUrlPhotoOfTheDay;
   }

   /**
    * @param pageUrlPhotoOfTheDay The pageUrlPhotoOfTheDay
    */
   public void setPageUrlPhotoOfTheDay(String pageUrlPhotoOfTheDay) {
      this.pageUrlPhotoOfTheDay = pageUrlPhotoOfTheDay;
   }

   /**
    * @return The publishDate
    */
   public String getPublishDate() {
      return publishDate;
   }

   /**
    * @param publishDate The publishDate
    */
   public void setPublishDate(String publishDate) {
      this.publishDate = publishDate;
   }

}
