package de.msal.muzei.nationalgeographic

import com.google.gson.*
import com.google.gson.JsonParseException
import de.msal.muzei.nationalgeographic.model.Feed
import de.msal.muzei.nationalgeographic.model.Item
import java.lang.reflect.Type

/**
 * "Flattens" the Json as the relevant data is nested deeply
 */
class FeedDeserializer : JsonDeserializer<Feed> {

   @Throws(JsonParseException::class)
   override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Feed {
      val content = json.asJsonObject["result"]
            .asJsonObject["pageContext"]
            .asJsonObject["node"]
            .asJsonObject["data"]
            .asJsonObject["content"]

      return GsonBuilder()
            .registerTypeAdapter(Item::class.java, ItemDeserializer())
            .create()
            .fromJson(content, Feed::class.java)
   }

}
