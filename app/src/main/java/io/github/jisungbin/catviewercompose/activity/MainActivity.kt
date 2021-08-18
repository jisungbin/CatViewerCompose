package io.github.jisungbin.catviewercompose.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import io.github.jisungbin.catviewercompose.R
import io.github.jisungbin.catviewercompose.api.CatClient
import io.github.jisungbin.catviewercompose.model.Cat
import io.github.jisungbin.catviewercompose.viewmodel.CatViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = { Toolbar() },
                content = { Content() }
            )
        }
    }

    @Composable
    private fun Toolbar(vm: CatViewModel = viewModel()) {
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
                        vm.reloadCat()
                        toast(getString(R.string.searching_cats))
                    }
                )
            }
        }
    }

    @OptIn(ExperimentalCoilApi::class)
    @Composable
    private fun Content(vm: CatViewModel = viewModel()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            vm.cat.value?.let { cat ->
                Text(text = cat.tags.joinToString(","))
                Image(
                    painter = rememberImagePainter(cat.toImageUrl()),
                    contentDescription = null,
                    modifier = Modifier.padding(top = 15.dp)
                )
            } ?: Text(text = stringResource(R.string.searching_cats))
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun Cat.toImageUrl() = "${CatClient.BaseUrl}/cat/$id"
}
