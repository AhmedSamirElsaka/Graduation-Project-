package com.example.graduationproject.player

import android.content.ComponentName
import android.content.Context
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.*
import com.example.graduationproject.data.model.Music
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PlayerController (
    private val player: ExoPlayer,
    private var currentSong: MutableStateFlow<Music>,
    private var currentMediaPosition: MutableStateFlow<Float>,
    private var currentMediaDurationInMinutes: MutableStateFlow<Long>,
    private var currentMediaProgressInMinutes: MutableStateFlow<Long>,
    private var isPausePlayClicked: MutableStateFlow<Boolean>,
    private var isPlayerBuffering: MutableStateFlow<Boolean>,
    private var isRepeatClicked: MutableStateFlow<Boolean>,
    private val viewModelScope: CoroutineScope
) : Player.Listener{

    var duration: Long = 0

    private lateinit var controller: ListenableFuture<MediaController>

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        currentMediaPosition.value = 0f

        if (mediaItem != null) {
//            currentSong.value = toMusicItem(mediaItem)
        }

    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
        isPausePlayClicked.value = isPlaying
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)

        when (playbackState) {
            Player.STATE_ENDED -> {
                if (player.hasNextMediaItem()) {
                    if(player.hasNextMediaItem()) nextItem()
                }
            }
            Player.STATE_BUFFERING -> {
                isPlayerBuffering.value = true


            }
            Player.STATE_IDLE -> {
                currentMediaProgressInMinutes.value = 0L
                currentMediaDurationInMinutes.value = 0L
                isPlayerBuffering.value = false
            }
            Player.STATE_READY -> {
                isPlayerBuffering.value = false
            }
        }
    }


    fun  pauseOrPlay() {
        if (player.isPlaying) {
            player.pause()
        } else {
            player.play()
        }
//       currentSong.value = toMusicItem(player.currentMediaItem!!)
    }

    fun repeatClick() {
        if (isRepeatClicked.value) {
            isRepeatClicked.value = false
            player.repeatMode = Player.REPEAT_MODE_OFF
        }else{
            isRepeatClicked.value = true
            player.repeatMode =  Player.REPEAT_MODE_ONE
        }
    }

    fun seekForward() {
       player.seekForward()
    }
    fun seekBack() {
        player.seekBack()
    }




    fun nextItem() {
        if (player.hasNextMediaItem())   player.seekToNextMediaItem()
    }


    fun previousItem() {
        if (player.hasPreviousMediaItem())   player.seekToPreviousMediaItem()
    }

//    private fun getMetaDataFromItem(item: Music): MediaMetadata {
//        return MediaMetadata.Builder()
//            .setTitle(item.title)
//            .setAlbumTitle(item.album)
//            .setDisplayTitle(item.title)
//            .setArtist( item.artist)
//            .setAlbumArtist(item.artist)
//            .setArtworkUri(item.image.toUri())
//            .build()
//    }



    fun updatePlayerSeekProgress(pos: Long) {
        currentMediaProgressInMinutes.value = pos
        val progress = pos.toFloat() / duration.toFloat()
        if (!progress.isNaN()) currentMediaPosition.value = progress
    }




}