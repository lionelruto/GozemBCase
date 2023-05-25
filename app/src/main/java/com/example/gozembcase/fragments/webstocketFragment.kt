package com.example.gozembcase.fragments

import android.content.Context
import android.os.Bundle
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
    var msg =""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        webstocketFragmentBinding= FragmentWebstocketBinding.inflate(layoutInflater)
        val webSocketClient = WebSocketClient.getInstance()


        // Inflate the layout for this fragment

        val sharedPreference =  requireActivity().getSharedPreferences("save_uid", Context.MODE_PRIVATE)
        val myuid= sharedPreference?.getString("uid", null)
        Toast.makeText(requireActivity(), "Vous êtes connecté par défaut", Toast.LENGTH_SHORT).show()
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
                Log.e("checkState", JSONObject(str).getJSONObject("result").toString())
                val result= JSONObject(str).getJSONObject("result")
                var title=  result.getJSONObject("content").getString("title")
                var source = result.getJSONObject("content").getString("source")
                var value = result.getJSONObject("content").getString("value")
                webSocketClient.setSocketUrl(source)
                webSocketClient.connect()
                webSocketClient.setListener(socketListener)

            }

        }
        val mListView = webstocketFragmentBinding.listView
        val arrayAdapter: ArrayAdapter<*>
        val textList:ArrayList<String> = ArrayList()
        arrayAdapter= ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1,textList )

        val v= webstocketFragmentBinding.root
        val butdisconnect= v.findViewById<Button>(R.id.webdisconnect)
        butdisconnect.setOnClickListener{
            webSocketClient.disconnect()
            Toast.makeText(requireActivity(), "Vous êtes bien deconnecté", Toast.LENGTH_LONG).show()

        }

        val reconnect= v.findViewById<Button>(R.id.webreconnect)
        reconnect.setOnClickListener{
            webSocketClient.connect()
            webSocketClient.setListener(socketListener)
            Toast.makeText(requireActivity(), "Vous êtes bien reconnecté", Toast.LENGTH_SHORT).show()

        }

        val button= v.findViewById<Button>(R.id.sendwebstocket)
        button.setOnClickListener{

            val message= webstocketFragmentBinding.textedit.text.toString()
            webstocketFragmentBinding.textedit.text= Editable.Factory.getInstance().newEditable("")
            Toast.makeText(requireActivity(), "Goood", Toast.LENGTH_LONG).show()
            //text.add("message")
            // webSocketClient = WebSocketClient.getInstance()
            //webSocketClient.setSocketUrl(source)

            webSocketClient.sendMessage(message)



            if (msg.isEmpty()){
                Toast.makeText(requireActivity(), "Probleme avec le websocket/ déconnecté", Toast.LENGTH_LONG).show()

            }else{

                arrayAdapter.add(message)
                arrayAdapter.notifyDataSetChanged()
            }


            //webSocketClient.disconnect()

        }
        mListView.adapter = arrayAdapter

        return webstocketFragmentBinding.root

    }
    val socketListener = object : WebSocketClient.SocketListener {
        override fun onMessage(message: String) {
            msg= message
           // mListView.adapter= arrayAdapter
            Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()

        }

    }

}