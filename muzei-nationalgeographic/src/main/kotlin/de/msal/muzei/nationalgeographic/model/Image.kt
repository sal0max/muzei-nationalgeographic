package de.msal.muzei.nationalgeographic.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Image(
      @SerializedName("alt") val altDescription : String?,
      @SerializedName("title") var title : String?,
      @SerializedName("url") var url : String?,
      @SerializedName("width") val width : Int?,
      @SerializedName("height") val height : Int?,
)
