package com.project.toko.domain.models.castModel

import com.google.gson.annotations.SerializedName

data class Data(
     @SerializedName("character") val character: Character,
     @SerializedName("role") val role: String,
     @SerializedName("voice_actors") val voice_actors: List<VoiceActor>
)