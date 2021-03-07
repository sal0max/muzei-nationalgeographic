package de.msal.muzei.nationalgeographic.model

import com.google.gson.annotations.SerializedName

data class Feed(
      @SerializedName("images") val items: List<Item>
)
