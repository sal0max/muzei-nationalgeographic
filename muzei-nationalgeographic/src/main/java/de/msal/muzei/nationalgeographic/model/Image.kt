package de.msal.muzei.nationalgeographic.model

import com.google.gson.annotations.SerializedName

data class Image(
      @SerializedName("alt") val altDescription : String?,
      @SerializedName("title") val title : String?,
      @SerializedName("url") var url : String?,
      @SerializedName("width") val width : Int?,
      @SerializedName("height") val height : Int?,
)
