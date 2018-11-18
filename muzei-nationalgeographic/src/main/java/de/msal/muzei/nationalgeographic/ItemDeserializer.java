package de.msal.muzei.nationalgeographic;

import android.text.Html;
import com.google.gson.*;
import de.msal.muzei.nationalgeographic.model.Item;

import java.lang.reflect.Type;

public class ItemDeserializer implements JsonDeserializer<Item> {

   @Override
   public Item deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
      JsonObject jsonObject = json.getAsJsonObject();
      JsonElement image = jsonObject.get("image");

      Item item;
      if (image != null) { // old/private api
         item = context.deserialize(image, Item.class);
         item.setInternal(jsonObject.get("internal").getAsBoolean());
         item.setPageUrlPhotoOfTheDay(jsonObject.get("pageUrl").getAsString());
         item.setPublishDate(jsonObject.get("publishDate").getAsString());
         return item;
      } else { // new/public api
         item = new Gson().fromJson(jsonObject, Item.class);
      }

      // clean up photographer info
      item.setPhotographer(stripHtml(item.getPhotographer())
            .replace("Photograph by ", "")
            .replace(", National Geographic Your Shot", "")
            .replace(", National Geographic", "")
            .replace(", Your Shot", "")
            .replace(", My Shot", "")
            .trim()
      );
      return item;
   }

   private String stripHtml(String html) {
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
         return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString();
      } else {
         //noinspection deprecation
         return Html.fromHtml(html).toString().trim();
      }
   }

}
