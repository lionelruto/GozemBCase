package com.example.gozembcase

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.gozembcase.databinding.FragmentEmailBinding
import com.example.gozembcase.databinding.LogincardviewBinding

/**
 * A simple [Fragment] subclass.
 * Use the [EmailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EmailFragment : Fragment() {
    private lateinit var logincardviewBinding: LogincardviewBinding
    private lateinit var fragmentEmailBinding: FragmentEmailBinding
    private lateinit var dataCommunicator: Communicator


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflater.inflate(R.layout.fragment_email, container, false)
        fragmentEmailBinding= FragmentEmailBinding.inflate(layoutInflater)
        logincardviewBinding= LogincardviewBinding.inflate(layoutInflater)
       val include=  fragmentEmailBinding.logInc

        //repondre au evenements sur l'editex
        include.textedit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable) {
                //afficher l'icone chaque fois qu'on saisi un texte
                if (p0.length > 0){
                    include.editInfo.isEndIconVisible= true
                    include.editInfo.isErrorEnabled= false
                    include.editInfo.endIconDrawable= ContextCompat.getDrawable(requireActivity()!!, R.drawable.ic_outline_cancel_24)
                }else{
                    include.editInfo.isEndIconVisible= false
                    include.editInfo.isErrorEnabled= false

                }

            }

        })

        include.editInfo.setEndIconOnClickListener { v ->
            include.textedit.setText("")
        }

        //redirection vers l'enregistrement
        fragmentEmailBinding.noacc.setOnClickListener{
            val intent2= Intent(requireActivity(), Register::class.java).apply {  }
            startActivity(intent2)
        }

        include.filledButton.setOnClickListener { v ->
            //recuperer le texte
            val email= include.textedit.text.toString()

            if (email.isEmpty()){
                include.editInfo.isErrorEnabled= true
                include.editInfo.error= "saissir une valeur"
            }else{
                include.editInfo.isErrorEnabled= false
                validation(email, include)
            }
        }
        // Inflate the layout for this fragment
        return fragmentEmailBinding.root
    }

    fun validation(text: String, v: LogincardviewBinding){

         if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            v.editInfo.isErrorEnabled= true
            v.editInfo.error= "Adresse email invalide, ex: abc@example.com"
        } else {
             v.editInfo.isErrorEnabled= false
             //send data to second fragment
              dataCommunicator = activity as Communicator
             dataCommunicator.passData(text)

             }



    }

}