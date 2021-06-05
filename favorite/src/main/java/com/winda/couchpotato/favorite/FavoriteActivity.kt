package com.winda.couchpotato.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.winda.couchpotato.R
import com.winda.couchpotato.core.adapter.RecyclerShowAdapter
import com.winda.couchpotato.core.domain.model.Show
import com.winda.couchpotato.favorite.databinding.ActivityFavoriteBinding
import com.winda.couchpotato.favorite.di.favoriteModule
import com.winda.couchpotato.ui.detail.DetailActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteActivity : AppCompatActivity() {
    private val favoriteViewModel : FavoriteViewModel by viewModel()
    private lateinit var binding : ActivityFavoriteBinding

    private lateinit var showsAdapter: RecyclerShowAdapter
    private var listShow = ArrayList<Show>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // manually add koin modules
        loadKoinModules(favoriteModule)

        supportActionBar?.title = "Favorite Shows"

        // display list of favorite shows
        initRecyclerList()
        observeFavorite()
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    private fun observeFavorite() {
        favoriteViewModel.getListFavorite().observe(this, {
            listShow.clear()
            listShow.addAll(it)

            if (listShow.isEmpty()){
                setImageStatusVisibility(true)
            }
            else{
                setImageStatusVisibility(false)
            }
            showsAdapter.notifyDataSetChanged()
        })
    }

    private fun initRecyclerList(){
        // set recycler
        binding.recyclerListShow.setHasFixedSize(true)
        binding.recyclerListShow.layoutManager = LinearLayoutManager(applicationContext)

        // adapter
        initRecyclerViewAdapter()
    }

    private fun initRecyclerViewAdapter(){
        // adapter
        showsAdapter = RecyclerShowAdapter(listShow)
        binding.recyclerListShow.adapter = showsAdapter

        showsAdapter.setOnItemClickCallback(object : RecyclerShowAdapter.OnItemClickCallback {
            override fun onItemClickCallback(show: Show) {
                val detailIntent = Intent(applicationContext, DetailActivity::class.java)
                detailIntent.putExtra(DetailActivity.ARG_SHOW, show)
                startActivity(detailIntent)
            }
        })
    }

    private fun setImageStatusVisibility(showImage: Boolean){
        if (!showImage){
            binding.imgSearch.visibility = View.GONE
        }
        else{
            binding.imgSearch.visibility = View.VISIBLE
            binding.imgSearch.setImageResource(R.drawable.ic_undraw_empty_xct9)
        }
    }
}