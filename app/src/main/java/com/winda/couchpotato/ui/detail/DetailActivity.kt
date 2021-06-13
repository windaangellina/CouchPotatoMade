package com.winda.couchpotato.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.winda.couchpotato.R
import com.winda.couchpotato.databinding.ActivityDetailBinding
import com.winda.couchpotato.core.data.viewmodel.ShowsViewModel
import com.winda.couchpotato.core.domain.model.Show
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding
    private var show: Show? = null
//    private lateinit var showViewModel : ShowsViewModel

    // viewModel
    private val showViewModel : ShowsViewModel by viewModel()

    // favorite
    private lateinit var countFavorite : LiveData<Int>
    private var isInFavorite : Boolean = false

    @FlowPreview
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(ARG_SHOW)){
            show = intent.getParcelableExtra(ARG_SHOW)
        }

        countFavorite = (show?.showId?.let { showViewModel.getCountFavoriteById(it) } ?: return)
        observeCountFavorite()

        setComponentValue()
    }

    companion object{
        const val ARG_SHOW = "showEntity"
    }

    private fun observeCountFavorite(){
        countFavorite.observe(this, {
            isInFavorite = it != 0
            setIconFavorite(isInFavorite)
        })
    }

    private fun setIconFavorite(isInFavorite : Boolean){
        if (!isInFavorite){
            binding.floatingButtonFavorite.setImageResource(R.drawable.ic_like_1_outline)
        }
        else{
            binding.floatingButtonFavorite.setImageResource(R.drawable.ic_like_1_fill)
        }
    }

    private fun setComponentValue(){
        binding.tvDetailTitle.text = show?.getTitleWithReleaseYear()
        binding.tvDetailUserScores.text = show?.getUserScoresAsString()
        binding.tvReleaseDate.text = show?.getReleaseDateAsString()
        binding.tvOverview.text = show?.overview

        // glide poster
        if (show?.posterUrl != null){
            Glide.with(applicationContext)
                .load(show?.posterUrl)
                .placeholder(R.drawable.app_icon_bg)
                .error(R.drawable.app_icon_bg) // will be displayed if the image cannot be loaded
                .dontAnimate()
                .into(binding.imgPoster)
        }

        // glide backdrop
        if (show?.backdropUrl != null){
            Glide.with(applicationContext)
                .load(show?.backdropUrl)
                .placeholder(R.drawable.app_icon_bg)
                .error(R.drawable.gradient_main) // will be displayed if the image cannot be loaded
                .dontAnimate()
                .into(binding.imgBackdrop)
        }

        setEvent()
    }

    private fun setEvent(){
        binding.iconShare.setOnClickListener {
             shareShow()
        }

        binding.floatingButtonFavorite.setOnClickListener { view ->
            changeFavoriteStatus(view)
        }
    }

    private fun shareShow(){
        val shareBody : String = "Check out " + show?.getTitleWithReleaseYear() + "\n" +
                "user vote : " + show?.getUserScoresAsString() + "\n \n" + show?.overview

        val shareIntent = Intent()
        shareIntent.apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, show?.title)
            putExtra(Intent.EXTRA_TEXT, shareBody)
        }
        startActivity(Intent.createChooser(shareIntent, "Share ${show?.title} using"))
    }

    private fun changeFavoriteStatus(view : View){
        var mode = "added to"

        if (!isInFavorite){
            show?.let { showViewModel.insertFavorite(it) }
        }
        else{
            mode = "removed from"
            show?.let { showViewModel.deleteFavorite(it) }
        }

        // reset count
        countFavorite = (show?.showId?.let { showViewModel.getCountFavoriteById(it) } ?: return)

        val message = "${show?.title} has been $mode favorite"
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setAction("Action", null).show()

    }
}