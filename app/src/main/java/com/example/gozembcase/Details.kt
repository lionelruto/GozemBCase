package com.example.gozembcase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.gozembcase.adapter.ViewPagerAdapter
import com.example.gozembcase.databinding.ActivityDetailsBinding
import com.example.gozembcase.fragments.MapsFragment
import com.example.gozembcase.fragments.ProfileFragment
import com.example.gozembcase.fragments.webstocketFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class Details : AppCompatActivity() {

    private lateinit var activityDetailsBinding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDetailsBinding= ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(activityDetailsBinding.root)

        //ajout du viewpaget
        val viewpager= activityDetailsBinding.viewpager
        val adapter= ViewPagerAdapter(supportFragmentManager, lifecycle)
        adapter.addFragment(ProfileFragment())
        adapter.addFragment(MapsFragment())
        adapter.addFragment(webstocketFragment())
        viewpager.orientation= ViewPager2.ORIENTATION_HORIZONTAL
        viewpager.adapter= adapter
        val uid=   intent.extras?.getString("uid") ?: "Aucun utilisateur trouvé"

        val navigationBarView= activityDetailsBinding.bottomNavigation as BottomNavigationView


        navigationBarView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.item_1 -> {
                    // Respond to navigation item 1 click
                    viewpager.currentItem = 0
                    true
                }
                R.id.item_2 -> {
                    // Respond to navigation item 2 click
                    viewpager.currentItem = 1
                    true
                }
                R.id.item_3 -> {
                    // Respond to navigation item 3 click
                    viewpager.currentItem = 2
                    true
                }
                else -> false
            }
        }




        viewpager?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }

            override fun onPageScrolled(position: Int,
                                        positionOffset: Float,
                                        positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }
        })
    }


}