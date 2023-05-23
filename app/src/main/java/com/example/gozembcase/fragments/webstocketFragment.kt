package com.example.gozembcase.fragments

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gozembcase.R
import com.example.gozembcase.Viewmodel.UserFactory
import com.example.gozembcase.Viewmodel.UserViewModel
import com.example.gozembcase.databinding.FragmentWebstocketBinding
import com.example.gozembcase.repository.UserRepo
import com.example.gozembcase.webstocket.WebSocketClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import org.json.JSONObject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [webstocketFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class webstocketFragment : Fragment() {
    private lateinit var webSocketClient: WebSocketClient
    private val socketKey = "5u4GX60EvqAn8gcClTVZ95GHiaRCCNpfnSjLiNdN"
    //private val roomId=
    private lateinit var webstocketFragmentBinding: FragmentWebstocketBinding
    private var isMessage= false

    val text: ArrayList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        webstocketFragmentBinding= FragmentWebstocketBinding.inflate(layoutInflater)
        val webSocketClient = WebSocketClient.getInstance()


        // Inflate the layout for this fragment

        val sharedPreference =  requireActivity().getSharedPreferences("save_uid", Context.MODE_PRIVATE)
        val myuid= sharedPreference?.getString("uid", null)
        if (myuid != null) {

            val auth = FirebaseAuth.getInstance()
            val db = FirebaseFirestore.getInstance()
            val userRepos= UserRepo(auth, db)
            val userFactory= UserFactory(userRepos)
            val userViewModel: UserViewModel = ViewModelProvider(this, userFactory).get(
                UserViewModel::class.java)
            userViewModel.getWebsocket(myuid!!)
            userViewModel.getWebsocket.observe(viewLifecycleOwner) { state ->

                val str = Gson().toJson(state).toString()
                val result= JSONObject(str).getJSONObject("result")
                var title=  result.getJSONObject("content").getString("title")
                var source = result.getJSONObject("content").getString("source")
                var value = result.getJSONObject("content").getString("value")
                webSocketClient.setSocketUrl(source)
                webSocketClient.connect()

            }

        }

        val v= webstocketFragmentBinding.root
        val button= v.findViewById<Button>(R.id.sendwebstocket)
        button.setOnClickListener{

            val message= webstocketFragmentBinding.textedit.text.toString()
            webstocketFragmentBinding.textedit.text= Editable.Factory.getInstance().newEditable("")
            Toast.makeText(requireActivity(), "Goood", Toast.LENGTH_SHORT).show()
            //text.add("message")
            // webSocketClient = WebSocketClient.getInstance()
            //webSocketClient.setSocketUrl(source)
            //webSocketClient.connect()
            webSocketClient.sendMessage(message)

            webSocketClient.setListener(socketListener)

            //webSocketClient.disconnect()


                isMessage= false

        }
        //mListView.adapter = arrayAdapter

        return webstocketFragmentBinding.root

    }
    val socketListener = object : WebSocketClient.SocketListener {
        override fun onMessage(message: String) {
            var mListView = webstocketFragmentBinding.listView
            Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()

        }

    }

}