package io.github.jisungbin.catviewercompose.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.skydoves.landscapist.coil.CoilImage
import dagger.hilt.android.AndroidEntryPoint
import io.github.jisungbin.catviewercompose.R
import io.github.jisungbin.catviewercompose.api.CatResult
import io.github.jisungbin.catviewercompose.api.NetworkException
import io.github.jisungbin.catviewercompose.util.NetworkUtil
import io.github.jisungbin.catviewercompose.viewmodel.CatViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val loadingDialogVisible = mutableStateOf(false)
    private val errorDialogVisible = mutableStateOf(false)
    private val errorException = mutableStateOf(Exception())
    private val catImageUrls = mutableStateOf(emptyList<String>())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            InitCatState()
            LoadingDialog(visible = loadingDialogVisible)
            ErrorDialog(visible = errorDialogVisible, exception = errorException)

            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = { Toolbar() },
                content = { Content() }
            )
        }
    }

    @Composable
    private fun InitCatState() {
        val vm: CatViewModel = viewModel()

        when (val result = vm.cat.value) {
            CatResult.Loading -> loadingDialogVisible.value = true
            else -> {
                loadingDialogVisible.value = false
                if (result is CatResult.Success) {
                    val catImageUrls = result.catImageUrls
                    this.catImageUrls.value = catImageUrls
                    if (NetworkUtil.isNetworkAvailable(applicationContext)) {
                        vm.insertAllToDatabase(catImageUrls)
                    }
                } else if (result is CatResult.Fail) {
                    if (result.exception is NetworkException) {
                        vm.loadImageUrlsFromDatabase()
                    } else {
                        errorDialogVisible.value = true
                        errorException.value = result.exception
                    }
                }
            }
        }
    }

    @Composable
    private fun Toolbar() {
        val vm: CatViewModel = viewModel()

        TopAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.app_name))
                Icon(
                    painter = painterResource(R.drawable.ic_round_refresh_24),
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        vm.loadCats(0)
                    }
                )
            }
        }
    }

    @Composable
    private fun Content() {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (catImageUrls.value.isNotEmpty()) {
                items(items = catImageUrls.value) { catImageUrl ->
                    CoilImage(imageModel = catImageUrl, contentScale = ContentScale.FillBounds)
                }
            } else {
                item {
                    Text(text = stringResource(R.string.dialog_loading_label))
                }
            }
        }
    }

    @Composable
    private fun LoadingDialog(visible: MutableState<Boolean>) {
        if (visible.value) {
            AlertDialog(
                modifier = Modifier.size(250.dp),
                onDismissRequest = { visible.value = false },
                buttons = {},
                text = {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = stringResource(R.string.dialog_loading_label))
                        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.cat))
                        val progress by animateLottieCompositionAsState(composition)
                        LottieAnimation(
                            modifier = Modifier.size(200.dp),
                            composition = composition,
                            progress = progress,
                        )
                    }
                },
                shape = RoundedCornerShape(30.dp)
            )
        }
    }

    @Composable
    private fun ErrorDialog(visible: MutableState<Boolean>, exception: State<Exception>) {
        if (visible.value) {
            AlertDialog(
                onDismissRequest = { visible.value = false },
                buttons = {},
                text = {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(
                                R.string.dialog_error_label,
                                exception.value.message!!
                            )
                        )
                        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error))
                        val progress by animateLottieCompositionAsState(composition)
                        LottieAnimation(
                            modifier = Modifier.size(20.dp),
                            composition = composition,
                            progress = progress,
                        )
                    }
                }
            )
        }
    }
}
