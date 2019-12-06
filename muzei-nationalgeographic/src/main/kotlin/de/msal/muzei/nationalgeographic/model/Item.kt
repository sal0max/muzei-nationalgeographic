package de.msal.muzei.nationalgeographic.model

import com.google.gson.annotations.SerializedName

data class Item(
      @SerializedName("image") val image: Image?,
      @SerializedName("internal") val internal: Boolean?,
      @SerializedName("pageUrl") val pageUrl: String?,
      @SerializedName("publishDate") val publishDate: String?
)
