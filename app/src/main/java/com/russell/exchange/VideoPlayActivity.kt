package com.russell.exchange

import android.os.Bundle

import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity


class VideoPlayActivity : AppCompatActivity() {
    private lateinit var videoView:VideoView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setContentView(videoView)

        initVideoView()
    }
    private fun initVideoView() {
        videoView.setVideoPath(intent.getStringExtra("url"))
        videoView.setOnCompletionListener {
            videoView.start()
        }
    }

    private fun init() {
        videoView = VideoView(this)


    }
    override fun onDestroy() {
        super.onDestroy()
        videoView.suspend()
    }
}