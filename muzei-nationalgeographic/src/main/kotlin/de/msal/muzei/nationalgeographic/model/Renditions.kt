package de.msal.muzei.nationalgeographic.model

import com.google.gson.annotations.SerializedName

data class Renditions(
      @SerializedName("width") val width: Int,
      @SerializedName("uri") val uri: String,
      @SerializedName("density") val density: Int,
      @SerializedName("media-selector") val mediaSelector: String
)
