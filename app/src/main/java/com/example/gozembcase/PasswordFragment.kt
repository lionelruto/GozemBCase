package com.example.gozembcase

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.gozembcase.Model.User
import com.example.gozembcase.Viewmodel.UserFactory
import com.example.gozembcase.Viewmodel.UserViewModel
import com.example.gozembcase.data.Ressource
import com.example.gozembcase.databinding.FragmentPasswordBinding
import com.example.gozembcase.databinding.LogincardviewBinding
import com.example.gozembcase.repository.UserRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 * Use the [PasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PasswordFragment : Fragment(){
    private lateinit var passwordFragment: FragmentPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        passwordFragment= FragmentPasswordBinding.inflate(layoutInflater)
        val include=  passwordFragment.logInc
        //changement dynamique des information de l'include dans le fragment
        include.editInfo.hint= "Entrez le mot de passe"
        include.filledButton.text= "valider"
        include.textedit.transformationMethod= PasswordTransformationMethod.getInstance()
        include.textedit.inputType= InputType.TYPE_CLASS_TEXT or
                InputType.TYPE_TEXT_VARIATION_PASSWORD

        include.editInfo.isEndIconVisible= true
        include.editInfo.endIconDrawable= ContextCompat.getDrawable(requireActivity(), R.drawable.ic_baseline_remove_red_eye_24)
        include.editInfo.setEndIconOnClickListener { v ->
            include.textedit.inputType= InputType.TYPE_CLASS_TEXT
        }

        include.filledButton.setOnClickListener { v ->

            Toast.makeText(requireActivity(), "selected", Toast.LENGTH_LONG).show()
            val password= include.textedit.text.toString()

            if (password.isEmpty()){
                include.editInfo.isErrorEnabled= true
                include.editInfo.error= "Entrez un mot de passe"
            }else{
                include.editInfo.isErrorEnabled= false
                //recupération du bundle passé par le fragment email à travers la mainactivity a l'aide de l'interface Communicator
                val bundle = this.arguments
                if (bundle != null) {
                    val intent= Intent(requireActivity(), Details::class.java).apply{}
                    startActivity(intent)

                    /*val mail= bundle.getString("email")

                    val auth = FirebaseAuth.getInstance()
                    val db = FirebaseFirestore.getInstance()
                    val userRepos= UserRepo(auth, db)
                    val userFactory= UserFactory(userRepos)
                    //appel du viewModel pour enregistrement des informations
                    val userViewModel: UserViewModel = ViewModelProvider(this, userFactory).get(
                        UserViewModel::class.java)
                    userViewModel.login(mail!!, password)
                    userViewModel.getLogin.observe(viewLifecycleOwner){ state ->

                        when(state){
                            is Ressource.success ->{
                                Toast.makeText(
                                    requireActivity(),
                                    "Authentication reuissis.",
                                    Toast.LENGTH_SHORT,
                                ).show()
                                val str = Gson().toJson(state).toString()
                                val result= JSONObject(str).getString("result")
                                val intent= Intent(requireActivity(), Details::class.java).apply{}
                                val sharedPreference =  requireActivity().getSharedPreferences("save_uid", Context.MODE_PRIVATE)
                                var editor = sharedPreference.edit()
                                editor.putString("uid",result)
                                editor.commit()
                                intent.putExtra("uid", result)
                                startActivity(intent)
                            }
                            else -> {
                                Toast.makeText(
                                    requireActivity(),
                                    "Authentication Echoué.",
                                    Toast.LENGTH_SHORT,
                                ).show()
                            }
                        }


                    }

                     */
                }


            }
        }
        return passwordFragment.root
    }

}