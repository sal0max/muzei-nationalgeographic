package de.msal.muzei.nationalgeographic.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Feed(
      @SerializedName("images") val items: List<Item>
)
