package com.masterwok.simplevlcplayer.activities

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import com.masterwok.simplevlcplayer.R
import com.masterwok.simplevlcplayer.dagger.injectors.InjectableAppCompatActivity
import com.masterwok.simplevlcplayer.fragments.BasePlayerFragment
import com.masterwok.simplevlcplayer.fragments.CastPlayerFragment
import com.masterwok.simplevlcplayer.fragments.LocalPlayerFragment
import com.masterwok.simplevlcplayer.services.MediaPlayerService
import com.masterwok.simplevlcplayer.services.binders.MediaPlayerServiceBinder

class MediaPlayerActivity : InjectableAppCompatActivity() {

    companion object {
        const val MediaUri = BasePlayerFragment.MediaUri
        const val SubtitleUri = BasePlayerFragment.SubtitleUri
        const val SubtitleDestinationUri = BasePlayerFragment.SubtitleDestinationUri
        const val OpenSubtitlesUserAgent = BasePlayerFragment.OpenSubtitlesUserAgent
        const val SubtitleLanguageCode = BasePlayerFragment.SubtitleLanguageCode
    }

    private var mediaPlayerServiceBinder: MediaPlayerServiceBinder? = null
    private var localPlayerFragment: LocalPlayerFragment? = null
    private var castPlayerFragment: CastPlayerFragment? = null

    private val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action ?: return

            when (action) {
                MediaPlayerService.RendererClearedAction -> showLocalPlayerFragment(mediaPlayerServiceBinder!!)
                MediaPlayerService.RendererSelectionAction -> showRendererPlayerFragment(mediaPlayerServiceBinder!!)
            }
        }
    }

    private val mediaPlayerServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            mediaPlayerServiceBinder = iBinder as MediaPlayerServiceBinder

            showLocalPlayerFragment(iBinder)
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            mediaPlayerServiceBinder = null
        }
    }

    private fun createLocalPlayerFragment(
            serviceBinder: MediaPlayerServiceBinder
    ): LocalPlayerFragment = LocalPlayerFragment.createInstance(
            mediaPlayerServiceBinder = serviceBinder
            , mediaUri = intent.getParcelableExtra(MediaUri)
            , subtitleUri = intent.getParcelableExtra(SubtitleUri)
            , subtitleDestinationUri = intent.getParcelableExtra(SubtitleDestinationUri)
            , openSubtitlesUserAgent = intent.getStringExtra(OpenSubtitlesUserAgent)
            , subtitleLanguageCode = intent.getStringExtra(SubtitleLanguageCode)
    )

    private fun createCastPlayerFragment(
            serviceBinder: MediaPlayerServiceBinder
    ): CastPlayerFragment = CastPlayerFragment.createInstance(
            mediaPlayerServiceBinder = serviceBinder
            , mediaUri = intent.getParcelableExtra(MediaUri)
            , subtitleUri = intent.getParcelableExtra(SubtitleUri)
            , subtitleDestinationUri = intent.getParcelableExtra(SubtitleDestinationUri)
            , openSubtitlesUserAgent = intent.getStringExtra(OpenSubtitlesUserAgent)
            , subtitleLanguageCode = intent.getStringExtra(SubtitleLanguageCode)
    )

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.framelayout_fragment_container, fragment)
                .commit()
    }

    private fun showLocalPlayerFragment(mediaPlayerServiceBinder: MediaPlayerServiceBinder) {
        castPlayerFragment = null
        localPlayerFragment = createLocalPlayerFragment(mediaPlayerServiceBinder)
        showFragment(localPlayerFragment!!)
    }

    private fun showRendererPlayerFragment(mediaPlayerServiceBinder: MediaPlayerServiceBinder) {
        localPlayerFragment = null
        castPlayerFragment = createCastPlayerFragment(mediaPlayerServiceBinder)
        showFragment(castPlayerFragment!!)
    }

    private fun registerRendererBroadcastReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(MediaPlayerService.RendererClearedAction)
        intentFilter.addAction(MediaPlayerService.RendererSelectionAction)

        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(broadCastReceiver, intentFilter)
    }

    private fun bindMediaPlayerService() = bindService(
            Intent(applicationContext, MediaPlayerService::class.java)
            , mediaPlayerServiceConnection
            , Context.BIND_AUTO_CREATE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_media_player)
    }

    override fun onStart() {
        super.onStart()

        bindMediaPlayerService()
        registerRendererBroadcastReceiver()

        startService(Intent(applicationContext, MediaPlayerService::class.java))
    }

    override fun onStop() {
        unbindService(mediaPlayerServiceConnection)

        LocalBroadcastManager
                .getInstance(this)
                .unregisterReceiver(broadCastReceiver)

        castPlayerFragment = null

        super.onStop()
    }

    override fun onBackPressed() {
        // Always ensure that we stop the media player service when navigating back.
        stopService(Intent(applicationContext, MediaPlayerService::class.java))

        super.onBackPressed()
    }


}