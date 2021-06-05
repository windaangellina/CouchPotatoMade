package com.winda.couchpotato.ui.catalogue.viewpager

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.winda.couchpotato.R
import com.winda.couchpotato.ui.catalogue.CatalogueFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_movies,
    R.string.tab_tv_shows
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        return CatalogueFragment.newInstance(position + 1, context.resources.getString(TAB_TITLES[position]))
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // FavoriteShowEntity 2 total pages.
        return 2
    }
}