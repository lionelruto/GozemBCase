package com.example.gozembcase

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import com.bumptech.glide.Glide
import com.example.gozembcase.Model.MapModel
import com.example.gozembcase.Model.User
import com.example.gozembcase.Model.Webstocket
import com.example.gozembcase.Viewmodel.UserFactory
import com.example.gozembcase.Viewmodel.UserViewModel
import com.example.gozembcase.databinding.ActivityRegisterBinding
import com.example.gozembcase.repository.UserRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class Register : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.filledButton.setOnClickListener {
            val name = binding.textname.text.toString()
            val mail = binding.textmail.text.toString()
            val pass = binding.textpass.text.toString()
            val passconf = binding.textpassconf.text.toString()
            val img= "https://miro.medium.com/v2/resize:fit:1400/1*65Wfi4Lrmal2c0xrdHnPlA.jpeg"
            val websocet= "wss://s8994.fra1.piesocket.com/v3/1?api_key=5u4GX60EvqAn8gcClTVZ95GHiaRCCNpfnSjLiNdN&notify_self=1"

            if (pass.isEmpty() or passconf.isEmpty() or name.isEmpty() or mail.isEmpty()){

            }else {

                if (pass.equals(passconf)){
                    binding.passconf.isErrorEnabled=false

                    //appel du viewModel pour enregistrement des informations
                    val auth = FirebaseAuth.getInstance()
                    val db = FirebaseFirestore.getInstance()
                    val userRepos= UserRepo(auth, db)
                    val userFactory= UserFactory(userRepos)
                    val userViewModel: UserViewModel = ViewModelProvider(this, userFactory).get(
                        UserViewModel::class.java)
                    val content = hashMapOf(
                        "image" to img,
                        "name" to name,
                        "email" to mail,
                    )
                    val contentMap = hashMapOf(
                        "title" to "location",
                        "pin" to "name",
                        "lat" to "1.32",
                        "lng" to "1.658",

                        )
                    val contentWebsocet = hashMapOf(
                        "title" to "information",
                        "source" to websocet,
                        "value" to "Loading",

                        )

                    val user= User("profil", content)
                    val mapModel= MapModel("map", contentMap)
                    val mapWebsocket= Webstocket("data", contentWebsocet)

                    userViewModel.inserUser(user, mapModel, mail, pass, mapWebsocket)
                    userViewModel.getInser.observe(this){ state->
                        val str = Gson().toJson(state).toString()
                        val result= JSONObject(str).getString("result")
                        Toast.makeText(this, result, Toast.LENGTH_LONG).show()
                        val intent= Intent(this, Details::class.java).apply{}
                        intent.putExtra("uid", result)
                        startActivity(intent)
                    }

                }else{
                    binding.passconf.isErrorEnabled=true
                    binding.passconf.error= "Mot de passe non conforme"
                }
            }

        }

    }

}