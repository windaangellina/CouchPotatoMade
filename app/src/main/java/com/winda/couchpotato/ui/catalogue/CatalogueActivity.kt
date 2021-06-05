package com.winda.couchpotato.ui.catalogue

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.winda.couchpotato.R
import com.winda.couchpotato.databinding.ActivityCatalogueBinding
import com.winda.couchpotato.ui.catalogue.viewpager.SectionsPagerAdapter


class CatalogueActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCatalogueBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCatalogueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById<View>(R.id.toolbarApp) as Toolbar
        toolbar.setTitle(R.string.app_name)
        setSupportActionBar(toolbar)

        setTabbedLayout()
    }

    private fun setTabbedLayout(){
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)

        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)
        supportActionBar?.elevation = 0f
    }

}