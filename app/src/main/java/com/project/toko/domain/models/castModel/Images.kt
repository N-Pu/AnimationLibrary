package com.project.toko.domain.models.castModel

import com.google.gson.annotations.SerializedName

data class Images(
     @SerializedName("jpg") val jpg: Jpg,
     @SerializedName("webp") val webp: Webp
)