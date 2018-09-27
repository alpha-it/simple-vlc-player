package com.masterwok.simplevlcplayer.fragments

import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.BundleCompat
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.masterwok.simplevlcplayer.R
import com.masterwok.simplevlcplayer.components.PlayerControlComponent
import com.masterwok.simplevlcplayer.contracts.MediaPlayer
import com.masterwok.simplevlcplayer.services.binders.MediaPlayerServiceBinder


internal class CastPlayerFragment : Fragment()
        , PlayerControlComponent.Callback
        , MediaPlayer.Callback {

    private val serviceBinder: IBinder? get() = BundleCompat.getBinder(arguments!!, MediaPlayerServiceBinder)
    private val mediaUri: Uri get() = arguments!!.getParcelable(MediaUriKey)
    private val subtitleUri: Uri? get() = arguments!!.getParcelable(SubtitleUriKey)
    private val subtitleDestinationUri: Uri get() = arguments!!.getParcelable(SubtitleDestinationUriKey)
    private val subtitleLanguageCode: String get() = arguments!!.getString(SubtitleLanguageCodeKey)
    private val openSubtitlesUserAgent: String get() = arguments!!.getString(OpenSubtitlesUserAgentKey)

    companion object {

        const val Tag = "tag.castplayerfragment"

        private const val MediaPlayerServiceBinder = "bundle.mediaplayerservicebinder"
        private const val MediaUriKey = "bundle.mediauri"
        private const val SubtitleUriKey = "bundle.subtitleuri"
        private const val SubtitleDestinationUriKey = "bundle.subtitledestinationuri"
        private const val SubtitleLanguageCodeKey = "bundle.subtitlelanguagecode"
        private const val OpenSubtitlesUserAgentKey = "bundle.useragent"

        @JvmStatic
        fun createInstance(
                mediaPlayerServiceBinder: MediaPlayerServiceBinder
                , mediaUri: Uri
                , subtitleUri: Uri?
                , subtitleDestinationUri: Uri
                , subtitleLanguageCode: String
                , openSubtitlesUserAgent: String
        ): CastPlayerFragment = CastPlayerFragment().apply {
            arguments = Bundle().apply {
                putParcelable(MediaUriKey, mediaUri)
                putParcelable(SubtitleUriKey, subtitleUri)
                putParcelable(SubtitleDestinationUriKey, subtitleDestinationUri)
                putString(SubtitleLanguageCodeKey, subtitleLanguageCode)
                putString(OpenSubtitlesUserAgentKey, openSubtitlesUserAgent)

                BundleCompat.putBinder(this, MediaPlayerServiceBinder, mediaPlayerServiceBinder)
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(
            R.layout.fragment_player_renderer,
            container,
            false
    )

    override fun onPlayPauseButtonClicked() {
    }

    override fun onCastButtonClicked() {
    }

    override fun onProgressChanged(progress: Int) {
    }

    override fun onProgressChangeStarted() {
    }

    override fun onSubtitlesButtonClicked() {
    }

    override fun onPlayerOpening() {
    }

    override fun onPlayerSeekStateChange(canSeek: Boolean) {
    }

    override fun onPlayerPlaying() {
    }

    override fun onPlayerPaused() {
    }

    override fun onPlayerStopped() {
    }

    override fun onPlayerEndReached() {
    }

    override fun onPlayerError() {
    }

    override fun onPlayerTimeChange(timeChanged: Long) {
    }

    override fun onBuffering(buffering: Float) {
    }

    override fun onPlayerPositionChanged(positionChanged: Float) {
    }

    override fun onSubtitlesCleared() {
    }

}


//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import com.masterwok.simplevlcplayer.R
//import kotlinx.android.synthetic.main.fragment_player_renderer.*
//
//class RendererPlayerFragment : BasePlayerFragment() {
//
//    override fun onConnected() {
//        if (serviceBinder?.isPlaying == true) {
//            return
//        }
//
//        startPlayback()
//    }
//
//    override fun onDisconnected() {
//        this.serviceBinder = null
//    }
//
//    override fun configure(
//            isPlaying: Boolean,
//            time: Long,
//            length: Long
//    ) = componentPlayerControl.configure(
//            isPlaying,
//            time,
//            length
//    )
//
//    override fun onCreateView(
//            inflater: LayoutInflater,
//            container: ViewGroup?,
//            savedInstanceState: Bundle?
//    ): View? = inflater.inflate(
//            R.layout.fragment_player_renderer,
//            container,
//            false
//    )
//
//    override fun onViewCreated(
//            view: View,
//            savedInstanceState: Bundle?
//    ) {
//        super.onViewCreated(view, savedInstanceState)
//
//        subscribeToViewComponents()
//    }
//
//    private fun subscribeToViewComponents() {
//        componentPlayerControl.registerCallback(this)
//    }
//
//    private fun startPlayback() {
//        serviceBinder?.setMedia(context!!, mediaUri!!)
//        serviceBinder?.setSubtitle(subtitleUri)
//        serviceBinder?.play()
//    }
//
//}