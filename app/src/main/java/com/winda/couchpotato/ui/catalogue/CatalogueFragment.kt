package com.winda.couchpotato.ui.catalogue

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.winda.couchpotato.core.adapter.RecyclerShowAdapter
import com.winda.couchpotato.core.data.network.Status
import com.winda.couchpotato.core.data.viewmodel.ShowsViewModel
import com.winda.couchpotato.core.domain.model.Show
import com.winda.couchpotato.core.utils.FunctionLibrary
import com.winda.couchpotato.R
import com.winda.couchpotato.databinding.FragmentCatalogueBinding
import com.winda.couchpotato.ui.catalogue.viewpager.PageViewModel
import com.winda.couchpotato.ui.detail.DetailActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class CatalogueFragment : Fragment() {
    // tab layout
    private lateinit var pageViewModel: PageViewModel
    private var categoryId : Int = -1

    // data
    private lateinit var binding: FragmentCatalogueBinding
    private lateinit var showsAdapter : RecyclerShowAdapter
    private var listShow = ArrayList<Show>()

    // viewModel
    @FlowPreview
    private val showsViewModel : ShowsViewModel by viewModel()

    // arguments
    private var tabTitle : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tabTitle = it.getString(ARG_TAB_TITLE).toString()
        }

        pageViewModel = ViewModelProvider(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }

        // get category
        categoryId = if (tabTitle.lowercase(Locale.ROOT) == "movies"){
            1
        }
        else{
            2
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // untuk menampilkan option menu dari fragment
        setHasOptionsMenu(true)
        binding = FragmentCatalogueBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerList()

        binding.progressBar.visibility = View.GONE
        setImageStatusVisibility(showImage = true, noResult = false, isError = false)

        observeLoadingStatus()
        observeResponseCode()
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val ARG_TAB_TITLE = "tab_title"

        @JvmStatic
        fun newInstance(sectionNumber: Int, tabTitle: String): CatalogueFragment {
            return CatalogueFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                    putString(ARG_TAB_TITLE, tabTitle)
                }
            }
        }
    }

    private fun initRecyclerList(){
        // set recycler
        binding.recyclerListShow.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }

        // adapter
        initRecyclerViewAdapter()
    }

    private fun initRecyclerViewAdapter(){
        // adapter
        showsAdapter = RecyclerShowAdapter(listShow)
        binding.recyclerListShow.adapter = showsAdapter

        // on click
        showsAdapter.setOnItemClickCallback(object : RecyclerShowAdapter.OnItemClickCallback {
            override fun onItemClickCallback(show: Show) {
                val detailIntent = Intent(context, DetailActivity::class.java)
                detailIntent.putExtra(DetailActivity.ARG_SHOW, show)
                startActivity(detailIntent)
            }
        })
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.option_search, menu)

        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            /*
            Gunakan method ini ketika search selesai atau OK
             */
            override fun onQueryTextSubmit(query: String): Boolean {
                if (categoryId == 1) {
                    observeMoviesList(query)
                } else {
                    observeTvShowsList(query)
                }
                return true
            }

            /*
            Gunakan method ini untuk merespon tiap perubahan huruf pada searchView
             */
            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    listShow.clear()
                    initRecyclerViewAdapter()
                    setImageStatusVisibility(showImage = true, noResult = false, isError = false)
                }
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorite_menu -> {
                val uri = Uri.parse("couchpotatoapp://favorite")
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    private fun observeLoadingStatus(){
        showsViewModel.loadingStatus.observe(viewLifecycleOwner, {
            if (it) {
                setImageStatusVisibility(showImage = false, noResult = false, isError = false)
            }
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    private fun observeResponseCode(){
        showsViewModel.responseCode.observe(viewLifecycleOwner, {
            val idDrawableError : Int = when (it) {
                401 -> {
                    R.drawable.ic_undraw_warning_cyit
                }
                403 -> {
                    R.drawable.ic_undraw_access_denied_re_awnf
                }
                404 -> {
                    R.drawable.ic_undraw_page_not_found_su7k
                }
                else -> {
                    R.drawable.ic_undraw_cancel_u1it
                }
            }

            if (it != 200){
                binding.imgSearch.visibility = View.VISIBLE
                binding.imgSearch.setImageResource(idDrawableError)
            }
        })
    }

    private fun setImageStatusVisibility(showImage: Boolean, noResult: Boolean, isError : Boolean){
        if (!showImage){
            binding.imgSearch.visibility = View.GONE
        }
        else{
            binding.imgSearch.visibility = View.VISIBLE
            if (noResult){
                if (!isError){
                    binding.imgSearch.setImageResource(R.drawable.ic_undraw_empty_xct9)
                }
                else{
                    binding.imgSearch.setImageResource(R.drawable.ic_undraw_access_denied_re_awnf)
                }
            }
            else{
                binding.imgSearch.setImageResource(R.drawable.ic_undraw_searching_p5ux)
            }
        }
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    private fun observeMoviesList(searchKeyword : String){
        showsViewModel.getSearchMovies(searchKeyword).observe(viewLifecycleOwner, {
                resourceList ->
            if (resourceList != null){
                when(resourceList.status){
                    Status.SUCCESS -> {
                        listShow.clear()
                        resourceList.data?.let { listShow.addAll(it) }
                        if (listShow.size == 0) {
                            setImageStatusVisibility(showImage = true, noResult = true, isError = false)
                        } else {
                            setImageStatusVisibility(showImage = false, noResult = false, isError = false)
                        }
                        initRecyclerViewAdapter()
                        binding.progressBar.visibility = View.GONE
                    }
                    Status.ERROR -> {
                        binding.progressBar.visibility = View.GONE
                        FunctionLibrary.makeToast(requireContext(), "connection lost ...")
                        setImageStatusVisibility(showImage = true, noResult = true, isError = true)
                    }
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    private fun observeTvShowsList(searchKeyword: String){
        showsViewModel.getSearchTvShows(searchKeyword).observe(viewLifecycleOwner, { resourceList ->
            if (resourceList != null){
                when(resourceList.status){
                    Status.SUCCESS -> {
                        listShow.clear()
                        resourceList.data?.let { listShow.addAll(it) }

                        if (listShow.size == 0) {
                            setImageStatusVisibility(showImage = true, noResult = true, isError = false)
                        } else {
                            setImageStatusVisibility(showImage = false, noResult = false, isError = false)
                        }
                        initRecyclerViewAdapter()
                        binding.progressBar.visibility = View.GONE
                    }
                    Status.ERROR -> {
                        binding.progressBar.visibility = View.GONE
                        FunctionLibrary.makeToast(requireContext(), "connection lost ...")
                        setImageStatusVisibility(showImage = true, noResult = true, isError = true)
                    }
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }
}