package com.example.gozembcase.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.gozembcase.Model.User
import com.example.gozembcase.R
import com.example.gozembcase.Viewmodel.UserFactory
import com.example.gozembcase.Viewmodel.UserViewModel
import com.example.gozembcase.databinding.FragmentPasswordBinding
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



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        webstocketFragmentBinding= FragmentWebstocketBinding.inflate(layoutInflater)

        val text: ArrayList<String> = ArrayList()

        // Inflate the layout for this fragment


        val sharedPreference =  requireActivity().getSharedPreferences("save_uid", Context.MODE_PRIVATE)
        val myuid= sharedPreference.getString("uid", null)
        if (myuid != null) {

        }
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

                val socketListener = object : WebSocketClient.SocketListener {
                    override fun onMessage(message: String) {
                        Log.e("socketCheck onMessage", message)
                        Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
                        // use arrayadapter and define an array
                        val arrayAdapter: ArrayAdapter<*>
                        val text: ArrayList<String> = ArrayList()
                        text.add(message)

                        // access the listView from xml file
                        var mListView = webstocketFragmentBinding.listView
                        arrayAdapter = ArrayAdapter(requireActivity(),
                            android.R.layout.simple_list_item_1, text)
                        mListView.adapter = arrayAdapter
                    }

                }

                val v= webstocketFragmentBinding.root
                val button= v.findViewById<Button>(R.id.sendwebstocket)
                button.setOnClickListener{
                    webstocketFragmentBinding.textedit.text= Editable.Factory.getInstance().newEditable("")
                    Toast.makeText(requireActivity(), "Goood", Toast.LENGTH_SHORT).show()

                    //webSocketClient = WebSocketClient.getInstance()
                    //webSocketClient.setSocketUrl(source)
                    //webSocketClient.connect()
                    //webSocketClient.setListener(socketListener)
                    //webSocketClient.sendMessage("hello webstocket")
                    //webSocketClient.disconnect()
                }



            }

        val v= webstocketFragmentBinding.root
        val button= v.findViewById<Button>(R.id.sendwebstocket)
        // access the listView from xml file
        var mListView = webstocketFragmentBinding.listView
        val arrayAdapter: ArrayAdapter<*>
        arrayAdapter = ArrayAdapter(requireActivity(),
            android.R.layout.simple_list_item_1, text)
        button.setOnClickListener{

            webstocketFragmentBinding.textedit.text= Editable.Factory.getInstance().newEditable("")
            Toast.makeText(requireActivity(), "Goood", Toast.LENGTH_SHORT).show()

            text.add("message")
            arrayAdapter.notifyDataSetChanged()
            //webSocketClient = WebSocketClient.getInstance()
            //webSocketClient.setSocketUrl(source)
            //webSocketClient.connect()
            //webSocketClient.setListener(socketListener)
            //webSocketClient.sendMessage("hello webstocket")
            //webSocketClient.disconnect()
        }
        mListView.adapter = arrayAdapter

        return webstocketFragmentBinding.root
    }


}