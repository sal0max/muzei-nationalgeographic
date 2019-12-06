package de.msal.muzei.nationalgeographic.model

import com.google.gson.annotations.SerializedName

data class Image(
      @SerializedName("id") val id : String?,
      @SerializedName("uri") val uri : String?,
      @SerializedName("title") val title : String?,
      @SerializedName("caption") val caption : String?,
      @SerializedName("credit") var credit : String?,
      @SerializedName("asset_source") val asset_source : String?,
      @SerializedName("alt_text") val alt_text : String?,
      @SerializedName("aspect_ratio") val aspect_ratio : Double?,
      @SerializedName("height") val height : Int?,
      @SerializedName("width") val width : Int?,
      @SerializedName("renditions") val renditions : List<Renditions>?,
      @SerializedName("croppings") val croppings : List<String>?
)
