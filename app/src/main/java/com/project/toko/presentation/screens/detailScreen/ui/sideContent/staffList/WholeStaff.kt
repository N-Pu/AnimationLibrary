package com.project.toko.presentation.screens.detailScreen.ui.sideContent.staffList

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.project.toko.domain.models.staffModel.Person
import com.project.toko.presentation.screens.detailScreen.vm.DetailScreenViewModel


@Composable
fun ShowWholeStaff(

    viewModel: DetailScreenViewModel,
    modifier: Modifier
) {

    val staffState by viewModel.staffList.collectAsStateWithLifecycle()

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 265.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(staffState) { data ->
            SingleStaffMember(
                person = data.person,
                positions = data.positions,
                modifier = modifier
            )
        }
        item { Spacer(modifier = modifier.height(50.dp)) }

    }

}


@Composable
fun SingleStaffMember(
    person: Person,
    positions: List<String>,

    modifier: Modifier
) {

    var isVisible by remember {
        mutableStateOf(false)
    }
    val painter = rememberAsyncImagePainter(model = person.images.jpg.image_url)

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally(
            animationSpec = TweenSpec(durationMillis = 500)
        ),
        exit = slideOutHorizontally(
            animationSpec = TweenSpec(durationMillis = 500)
        )
    ) {

        Card(modifier = modifier) {
            Row {
                Column {
                    Image(
                        painter = painter,
                        contentDescription = person.name,
                        modifier = modifier.size(123.dp, 150.dp)
                    )
                    Text(
                        text = person.name,
                        textAlign = TextAlign.Center,
                        modifier = modifier.width(100.dp)
                    )
                }
                Column {
                    positions.forEach { position ->
                        Text(text = position)
                    }
                }
            }

        }

    }

    LaunchedEffect(person) {
        isVisible = true
    }


}


