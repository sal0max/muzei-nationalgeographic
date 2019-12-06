package de.msal.muzei.nationalgeographic.model

import com.google.gson.annotations.SerializedName

data class Feed(
      @SerializedName("galleryTitle") val galleryTitle: String,
      @SerializedName("previousEndpoint") val previousEndpoint: String,
      @SerializedName("nextEndpoint") val nextEndpoint: String,
      @SerializedName("items") val items: List<Item>
)
