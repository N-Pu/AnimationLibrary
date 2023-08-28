package com.project.toko.presentation.screens.detailScreen.mainPage.openLink

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


@Composable
fun OpenLinkButton(link: String) {
    val openLinkLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ){}

    Column {
        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(link)

                try {
                    openLinkLauncher.launch(intent)
                } catch (e: ActivityNotFoundException) {
                    // Обработка исключения, если нет подходящего приложения для открытия ссылки
                }
            }
            , colors = ButtonDefaults.buttonColors(containerColor = LightGreen)
        ) {
            Text(text = "Youtube link")
        }
    }
}
