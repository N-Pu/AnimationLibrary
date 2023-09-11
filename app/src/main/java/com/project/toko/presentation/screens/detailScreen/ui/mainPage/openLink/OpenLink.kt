package com.project.toko.presentation.screens.detailScreen.ui.mainPage.openLink

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.project.toko.presentation.theme.LightGreen

// Функция для создания кнопки, позволяющей открыть внешнюю ссылку
@Composable
fun OpenLinkButton(link: String) {
    // Создаем лаунчер для запуска внешних активностей
    val openLinkLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ){}

    Column {
        Button(
            onClick = {
                // Создаем интент для открытия ссылки
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(link)

                try {
                    // Пытаемся запустить активность для открытия ссылки
                    openLinkLauncher.launch(intent)
                } catch (e: ActivityNotFoundException) {
                    // Обработка исключения, если нет подходящего приложения для открытия ссылки
                    // Например, пользователь может не иметь браузера на устройстве
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = LightGreen)
        ) {
            Text(text = "Youtube link")
        }
    }
}
