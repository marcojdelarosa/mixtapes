package com.mjdlr.mixtapes

import androidx.media3.common.MediaItem
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.SessionToken
import coil3.compose.AsyncImage
import com.mjdlr.mixtapes.ui.theme.MixtapesTheme
import androidx.core.net.toUri
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    lateinit var player:ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        player = ExoPlayer.Builder(this).build()
        setContent {
            MixtapesTheme {

                val padding = 4.dp
                var playerPosition by remember { mutableLongStateOf(0) }
                var sliderPosition by remember { mutableLongStateOf(0) }
                var trackDuration by remember { mutableLongStateOf(0) }
                val context = LocalContext.current

                LaunchedEffect(Unit) {
                    player.addMediaItem(MediaItem.fromUri(("android.resource://" + context.packageName + "/" + R.raw.sneaky_snitch).toUri()))
                    player.addMediaItem(MediaItem.fromUri(("android.resource://" + context.packageName + "/" + R.raw.pixel_peeker_polka).toUri()))
                }

                player.prepare()
                player.play()

                LaunchedEffect(player.currentPosition, player.isPlaying ) {
                    delay(1000)
                    playerPosition = player.currentPosition
                }

                LaunchedEffect(playerPosition) {
                    sliderPosition = playerPosition
                }

                LaunchedEffect(player.duration) {
                    if (player.duration > 0) {
                        trackDuration = player.duration
                    }
                }


                Column (
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AlbumArt(
                        ref = "https://i.ebayimg.com/images/g/mqYAAOSwmYVj2vfk/s-l140.jpg",
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                    )
                    Text(
                        text = "Song Title"
                    )
                    Text(
                        text = "Artist Name"
                    )

                    Slider(

                        value = sliderPosition.toFloat(),
                        onValueChange = { sliderPosition = it.toLong()},
                        onValueChangeFinished = {
                            playerPosition = sliderPosition
                            player.seekTo(sliderPosition)
                        },
                        valueRange = 0f..trackDuration.toFloat(),
                        modifier = Modifier.padding(32.dp)
                    )

                    Row (
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("00:00")
                        Text("00:00")
                    }

                    Row (
                        Modifier.fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button( onClick = {}, modifier = Modifier.padding(padding) ) { }
                        Button( onClick = { player.seekToPreviousMediaItem() }, modifier = Modifier.padding(padding) ) { }
                        Button( onClick = {
                            if (player.isPlaying) {
                                player.pause()
                            }
                            else {
                                player.play()
                            }
                        }, modifier = Modifier.padding(padding) ) { }
                        Button( onClick = { player.seekToNextMediaItem() }, modifier = Modifier.padding(padding) ) { }
                        Button( onClick = {}, modifier = Modifier.padding(padding) ) { }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlayerPreview() {

}


@Composable
fun AlbumArt(ref: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = ref,
        contentDescription = "Album Art",
        modifier = modifier
    )

}