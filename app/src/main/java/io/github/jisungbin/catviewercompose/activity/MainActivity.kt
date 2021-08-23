package io.github.jisungbin.catviewercompose.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.skydoves.landscapist.coil.CoilImage
import dagger.hilt.android.AndroidEntryPoint
import io.github.jisungbin.catviewercompose.R
import io.github.jisungbin.catviewercompose.viewmodel.CatViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val loadingDialogVisible = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Init()
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = { Toolbar() },
                content = { Content() }
            )
        }
    }

    @Composable
    private fun Init() {
        LoadingDialog(visible = loadingDialogVisible)
    }

    @Composable
    private fun Toolbar() {
        TopAppBar(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentPadding = PaddingValues(start = 16.dp)
        ) {
            Text(text = stringResource(R.string.app_name))
        }
    }

    @Composable
    private fun Content() {
        val vm: CatViewModel = viewModel()
        val pagingCatImageUrls = vm.catPager.collectAsLazyPagingItems()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            pagingCatImageUrls.apply {
                when {
                    loadState.refresh is LoadState.NotLoading && itemCount == 0 -> {
                        loadingDialogVisible.value = true
                    }
                    else -> {
                        loadingDialogVisible.value = false

                        val errorState = loadState.source.append as? LoadState.Error
                            ?: loadState.source.prepend as? LoadState.Error
                            ?: loadState.append as? LoadState.Error
                            ?: loadState.prepend as? LoadState.Error
                        if (errorState != null) {
                            item {
                                ErrorItem(exceptionMessage = errorState.error.message ?: "")
                            }
                        }
                    }
                }
            }

            items(items = pagingCatImageUrls) { catImageUrl ->
                CoilImage(imageModel = catImageUrl!!, contentScale = ContentScale.FillBounds)
            }
        }
    }

    @Composable
    private fun LoadingDialog(visible: MutableState<Boolean>) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.cat))
        val progress by animateLottieCompositionAsState(composition)

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
    private fun ErrorItem(exceptionMessage: String) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error))
        val progress by animateLottieCompositionAsState(composition)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(
                    R.string.dialog_error_label,
                    exceptionMessage
                )
            )
            LottieAnimation(
                modifier = Modifier.size(150.dp),
                composition = composition,
                progress = progress,
            )
        }
    }
}
