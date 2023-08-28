import android.util.Log
import com.google.gson.Gson
import com.project.toko.domain.models.castModel.CastModel
import com.project.toko.domain.models.detailModel.AnimeDetailModel
import com.project.toko.domain.models.newAnimeSearchModel.NewAnimeSearchModel
import com.project.toko.domain.models.staffModel.StaffModel
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


private const val BASE_URL = "https://api.jikan.moe/v4/anime/"
private const val BASE_URL2 = "https://api.jikan.moe/v4/anime?"

class MalApiService {

    private val gson = Gson()

    private fun makeApiRequest(urlString: String): String {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection

        connection.requestMethod = "GET"

        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val inputStream = connection.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream))
            val response = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()
            connection.disconnect()
            return response.toString()
        } else {
            connection.disconnect()
            throw Exception("Request failed with response code: $responseCode")
        }
    }


    fun getAnimeSearchByName(
        sfw: Boolean,
        page: Int = 1,
        nameOfAnime: String,
        type: String? = null,
        genres: String? = null,
        min_score: String? = null,
        max_score: String? = null,
        rating: String? = null,
        orderBy: String? = null,
        sort: String? = null
    ): NewAnimeSearchModel {
        val ratingParam = if (rating != null) "&rating=$rating" else ""
        val orderByParam = if (orderBy != null) "&orderBy=$orderBy" else "&orderBy=rank"
        val typeParam = if (type != null) "&type=$type" else ""
        val sortParam = if (sort != null) "&sort=$sort" else ""
        val genresParam = if (sort != null) "&genres=$genres" else ""
        val minParam = if (min_score != null) "&minParam=$min_score" else ""
        val maxParam = if (max_score != null) "&maxParam=$max_score" else ""
        val pageParam = "&page=$page"

        val urlString =
            "$BASE_URL2&sfw=$sfw$pageParam&q=$nameOfAnime$typeParam$genresParam$minParam$maxParam$ratingParam$orderByParam$sortParam"

        Log.d("getAnimeSearchByName", urlString)
        val jsonResponse = makeApiRequest(urlString)
        return gson.fromJson(jsonResponse, NewAnimeSearchModel::class.java)
    }

    fun getDetailsFromAnime(id: Int): AnimeDetailModel {
        val urlString = "$BASE_URL$id/full"
        val jsonResponse = makeApiRequest(urlString)
        Log.d("getDetailsFromAnime", urlString)
        return gson.fromJson(jsonResponse, AnimeDetailModel::class.java)
    }

    fun getCharactersFromId(id: Int): CastModel {
        val urlString = "$BASE_URL$id/characters"
        val jsonResponse = makeApiRequest(urlString)
        Log.d("getCharactersFromId", urlString)
        return gson.fromJson(jsonResponse, CastModel::class.java)
    }

    fun getStaffFromId(id: Int): StaffModel {
        val urlString = "$BASE_URL$id/staff"
        val jsonResponse = makeApiRequest(urlString)
        Log.d("getStaffFromId", urlString)
        return gson.fromJson(jsonResponse, StaffModel::class.java)
    }

    companion object {
        // Singleton instance
        val instance by lazy { MalApiService() }
    }
}
