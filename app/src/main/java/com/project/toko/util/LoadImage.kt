//import android.graphics.BitmapFactory
//import androidx.compose.foundation.Image
//import androidx.compose.runtime.*
//import androidx.compose.ui.graphics.ImageBitmap
//import androidx.compose.ui.graphics.asImageBitmap
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import java.io.InputStream
//import java.net.HttpURLConnection
//import java.net.URL
//
//@Composable
//fun LoadImage(imageUrl: String){
//    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
//
//    val coroutineScope = rememberCoroutineScope()
//
//    // Загружаем изображение в корутине
//    LaunchedEffect(imageUrl) {
//        coroutineScope.launch(Dispatchers.IO) {
//            val bitmap = com.project.toko.util.loadImageBitmap(imageUrl)
//            imageBitmap = bitmap
//        }
//    }
//
//    // Отображаем изображение, когда оно загружено
//    imageBitmap?.let { bitmap ->
//        val rememberBitMap = remember{ bitmap }
//
//        Image(
//            bitmap = rememberBitMap,
//            contentDescription = null // Укажите подходящий текст описания
//        )
//    }
//}
//
//// Функция для загрузки изображения
//private suspend fun com.project.toko.util.loadImageBitmap(url: String): ImageBitmap {
//    return withContext(Dispatchers.IO) {
//        val connection = URL(url).openConnection() as HttpURLConnection
//        connection.doInput = true
//        connection.connect()
//        val inputStream: InputStream = connection.inputStream
//        val bitmap = BitmapFactory.decodeStream(inputStream)
//        bitmap.asImageBitmap()
//    }
//}

package com.project.toko.util

import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap


suspend fun loadImageBitmap(url: String): ImageBitmap {
    return withContext(Dispatchers.IO) {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        val inputStream: InputStream = connection.inputStream
        val bitmap = BitmapFactory.decodeStream(inputStream)
        bitmap.asImageBitmap()
    }
}
