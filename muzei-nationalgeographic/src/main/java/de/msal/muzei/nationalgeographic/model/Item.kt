package de.msal.muzei.nationalgeographic.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class Item(
      @SerializedName("entityLabel") val entityLabel : String?,
      var date : Calendar?,
      @SerializedName("uuid") val uuid : String?,
      @SerializedName("mediaImage") val image : Image?,
      @SerializedName("contributor") var contributor : String?,
      @SerializedName("credit") var credit : String?,
      @SerializedName("caption") var caption : String?,
)
