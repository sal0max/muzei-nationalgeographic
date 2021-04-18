package de.msal.muzei.nationalgeographic

import com.github.sisyphsu.dateparser.DateParserUtils
import com.google.gson.*
import com.google.gson.JsonParseException
import de.msal.muzei.nationalgeographic.model.Image
import de.msal.muzei.nationalgeographic.model.Item
import java.lang.Exception
import java.lang.reflect.Type
import java.util.*

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

      // clean up the info
      item.image?.title = item.image?.title
            ?.trim()
      item.credit = item.credit
            ?.stripHtml()
            ?.extractName()
            ?.capitalizeWords()
            ?.trim()
      item.contributor = item.contributor
            ?.stripHtml()
            ?.extractName()
            ?.capitalizeWords()
            ?.trim()
      // clean up description
      item.caption = item.caption
            ?.stripHtml()
            ?.trim()

      // try to get the date - not always possible as not always set
      item.entityLabel
            ?.replace("pod-", "", true)
            ?.replace("pod ", "", true)
            ?.substringBefore(".") // remove .jpg
            ?.let {
               try {
                  // parse
                  DateParserUtils.preferMonthFirst(false)
                  // allow date fields be separated by dash
                  DateParserUtils.registerStandardRule("(?<dayOrMonth>\\d{1,2}-\\d{1,2})-(?<year>\\d{2})$")
                  item.date = DateParserUtils.parseCalendar(it)
               } catch (ignored: Exception) { }
            }

      return item
   }

   private fun String?.stripHtml(): String? {
      // don't need full html escaping. try it with regex instead
      return this?.replace("<.*?>".toRegex() , "")?.trim()
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

   private fun String.capitalizeWords(): String {
         return split(" ").joinToString(" ") { it.toLowerCase(Locale.ROOT).capitalize(Locale.ROOT) }
   }

}
