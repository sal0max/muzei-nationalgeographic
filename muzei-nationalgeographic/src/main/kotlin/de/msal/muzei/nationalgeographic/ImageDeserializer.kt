package de.msal.muzei.nationalgeographic

import com.google.gson.*
import com.google.gson.JsonParseException
import de.msal.muzei.nationalgeographic.model.Image
import java.lang.reflect.Type

/**
 * "Flattens" the Json as the relevant data is nested deeply
 */
class ImageDeserializer : JsonDeserializer<Image> {

   @Throws(JsonParseException::class)
   override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Image {
      val image = Gson().fromJson(json, Image::class.java)

      // change /files/screenshot-2021-03-04-at-11.17.05.png
      // to     https://static.nationalgeographic.co.uk/files/styles/image_3200/public/screenshot-2021-03-04-at-11.17.05.png.webp
      image.url = image.url
            ?.replace("/files/", "https://static.nationalgeographic.co.uk/files/styles/image_3200/public/")
            ?.replace(".png", ".png.webp")
            ?.replace(".jpg", ".webp")

      return image
   }

}
