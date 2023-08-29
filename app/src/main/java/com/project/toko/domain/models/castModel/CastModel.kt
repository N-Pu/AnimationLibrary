package com.project.toko.domain.models.castModel

import com.google.gson.annotations.SerializedName

data class CastModel(

     @SerializedName("data") val data: List<Data>
)