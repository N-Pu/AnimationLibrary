package com.project.toko.domain.models.castModel
import com.google.gson.annotations.SerializedName
data class VoiceActor(
     @SerializedName("language") val language: String,
     @SerializedName("person") val person: Person
)