package de.msal.muzei.nationalgeographic

import com.github.sisyphsu.dateparser.DateParserUtils
import com.google.gson.*
import com.google.gson.JsonParseException
import de.msal.muzei.nationalgeographic.model.Image
import de.msal.muzei.nationalgeographic.model.Item
import java.lang.reflect.Type

/**
 * "Flattens" the Json as the relevant data is nested and also cleans up some fields
 */
class ItemDeserializer : JsonDeserializer<Item> {

   @Throws(JsonParseException::class)
   override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Item {
      val item = GsonBuilder()
            .registerTypeAdapter(Image::class.java, ImageDeserializer())
            .create()
            .fromJson(json.asJsonObject["entity"], Item::class.java)

      // clean up photographer info
      item.credit = item.credit
            ?.stripHtml()
            ?.extractName()
      item.contributor = item.contributor
            ?.stripHtml()
            ?.extractName()
      // clean up description
      item.caption = item.caption
            ?.stripHtml()

      // add date
      item.entityLabel?.replace("pod-", "")?.let {
         item.date = DateParserUtils.parseCalendar(it)
      }

      return item
   }

   private fun String?.stripHtml(): String? {
      // don't need full html escaping. just remove the <p> tags as nothing else seems to exist
      return this?.replace("<p>", "")?.replace("</p>", "")?.trim()
      //return when {
      //   this != null -> Html.escapeHtml(this)
      //   else -> this
      //}
   }

   private fun String?.extractName(): String? {
      return this
            ?.replace("Photograph by ", "", true)
            ?.replace(", National Geographic Your Shot", "", true)
            ?.replace(", National Geographic Image Collection", "", true)
            ?.replace(", National Geographic", "", true)
            ?.replace(", Your Shot", "", true)
            ?.replace(", My Shot", "", true)
            ?.replace(", nat geo image collection", "", true)
            ?.trim()
   }

}
