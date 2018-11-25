package de.msal.muzei.nationalgeographic

import android.text.Html
import com.google.gson.*
import de.msal.muzei.nationalgeographic.model.Item
import java.lang.reflect.Type

class ItemDeserializer : JsonDeserializer<Item> {

   override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Item {
      json as JsonObject

      val item =
            if (json.get("image") != null) {
               // old api
               val image = json.get("image").asJsonObject
               val item: Item = context.deserialize(image, Item::class.java)
               item.internal = json.get("internal")?.asBoolean
               item.publishDate = json.get("publishDate")?.asString
               item.pageUrlPhotoOfTheDay = json.get("pageUrl")?.asString
               item.imageUrlLarge = image.get("renditions")?.asJsonArray?.last()?.asJsonObject?.get("uri")?.asString
               item
            } else {
               // new api
               val item = Gson().fromJson(json, Item::class.java)
               item.imageUrlLarge = json.get("sizes")?.asJsonObject?.get("2048")?.asString
               item
            }

      // clean up photographer info
      item.photographer = stripHtml(item.photographer)
            .replace("Photograph by ", "")
            .replace(", National Geographic Your Shot", "")
            .replace(", National Geographic", "")
            .replace(", Your Shot", "")
            .replace(", My Shot", "")
            .trim()
      return item
   }

   private fun stripHtml(html: String): String {
      return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
         Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString()
      } else {
         @Suppress("DEPRECATION")
         Html.fromHtml(html).toString().trim { it <= ' ' }
      }
   }

}
