package de.msal.muzei.nationalgeographic

import android.text.Html
import com.google.gson.*
import de.msal.muzei.nationalgeographic.model.Item
import java.lang.reflect.Type

class ItemDeserializer : JsonDeserializer<Item> {

   override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Item {
      json as JsonObject

      val item = Gson().fromJson(json, Item::class.java)

      // clean up photographer info
      item.image?.credit = stripHtml(item.image?.credit)
            ?.replace("Photograph by ", "")
            ?.replace(", National Geographic Your Shot", "")
            ?.replace(", National Geographic", "")
            ?.replace(", Your Shot", "")
            ?.replace(", My Shot", "")
            ?.trim()
      return item
   }

   private fun stripHtml(html: String?): String? {
      return when {
         html == null -> {
            html
         }
         android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N -> {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString().trim { it <= ' ' }
         }
         else -> {
            @Suppress("DEPRECATION")
            Html.fromHtml(html).toString().trim { it <= ' ' }
         }
      }
   }

}
