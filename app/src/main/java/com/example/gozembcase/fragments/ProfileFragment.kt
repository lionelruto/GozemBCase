package com.example.gozembcase.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.*
import com.bumptech.glide.Glide
import com.example.gozembcase.Details
import com.example.gozembcase.MainActivity
import com.example.gozembcase.Model.User
import com.example.gozembcase.R
import com.example.gozembcase.SendFragmentMessages
import com.example.gozembcase.Viewmodel.UserFactory
import com.example.gozembcase.Viewmodel.UserViewModel
import com.example.gozembcase.databinding.FragmentPasswordBinding
import com.example.gozembcase.databinding.FragmentProfileBinding
import com.example.gozembcase.repository.UserRepo
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.reflect.typeOf

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {

    private lateinit var fragmentprofilebiding: FragmentProfileBinding
    private lateinit var Myuid: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentprofilebiding= FragmentProfileBinding.inflate(layoutInflater)
        val sharedPreference =  requireActivity().getSharedPreferences("save_uid", Context.MODE_PRIVATE)
        val myuid= sharedPreference?.getString("uid", null)
        //recupération du bundle passé par le fragment email à travers la mainactivity a l'aide de l'interface Communicator
        val bundle = this.arguments
        if (myuid != null) {
            Toast.makeText(requireActivity(), "Goood", Toast.LENGTH_SHORT).show()


            Toast.makeText(requireActivity(), "Goood", Toast.LENGTH_SHORT).show()
            val uid= Firebase.auth.currentUser?.uid
            val auth = FirebaseAuth.getInstance()
            val db = FirebaseFirestore.getInstance()
            val userRepos= UserRepo(auth, db)
            val userFactory= UserFactory(userRepos)
            val userViewModel: UserViewModel = ViewModelProvider(this, userFactory).get(
                UserViewModel::class.java)
            userViewModel.getUser(myuid)
            userViewModel.getUser.observe(viewLifecycleOwner) { state ->
                val str = Gson().toJson(state).toString()
                val result= JSONObject(str).getJSONObject("result")
                val type = result.getString("type")
                val email=  result.getJSONObject("content").getString("email")
                val nom = result.getJSONObject("content").getString("name")
                val imglink= result.getJSONObject("content").getString("image")
                val name = fragmentprofilebiding.thename
                val mail= fragmentprofilebiding.themail
                val img= fragmentprofilebiding.profileImage
                name.text= Editable.Factory.getInstance().newEditable(nom)
                mail.text=Editable.Factory.getInstance().newEditable(email)
                Glide.with(this).load(imglink).into(img);


            }



        }




        fragmentprofilebiding.filledButton.setOnClickListener {
                //boutton pour la deconnextion
            val sharedPreference = requireActivity().getSharedPreferences("save_uid", Context.MODE_PRIVATE)
            sharedPreference?.edit()?.remove("uid")?.apply() //remove sharepreference if exist
            sharedPreference?.edit()?.clear()?.apply()
            val auth = FirebaseAuth.getInstance()
            val db = FirebaseFirestore.getInstance()
            val userRepos= UserRepo(auth, db)
            val userFactory= UserFactory(userRepos)
            val userViewModel: UserViewModel = ViewModelProvider(this, userFactory).get(
                UserViewModel::class.java)
            val out = userViewModel.logOut()
            val intent= Intent(requireActivity(), MainActivity::class.java).apply{}
            startActivity(intent)
            }




        // Inflate the layout for this fragment
        return fragmentprofilebiding.root
    }




}


