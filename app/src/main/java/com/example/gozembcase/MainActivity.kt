package com.example.gozembcase

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.gozembcase.databinding.ActivityMainBinding
import com.example.gozembcase.databinding.LogincardviewBinding

class MainActivity : AppCompatActivity(), Communicator {

    private lateinit var emailFragment: Fragment
    private lateinit var activity: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity= ActivityMainBinding.inflate(layoutInflater)
        setContentView(activity.root)

        emailFragment= EmailFragment()



        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView, emailFragment).addToBackStack(null).commit()
    }

    //used to pass data between emailfragment and passwordfragment

    override fun passData(textEdit: String) {
        val bundle = Bundle()
        bundle.putString("email", textEdit)
        val transaction = this.supportFragmentManager?.beginTransaction()
        val fragment2 = PasswordFragment()
        fragment2.arguments= bundle
        transaction?.replace(R.id.fragmentContainerView, fragment2)
        transaction?.addToBackStack(null)
        transaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction?.commit()
    }


}