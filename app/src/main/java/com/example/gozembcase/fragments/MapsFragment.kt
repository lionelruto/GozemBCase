package com.example.gozembcase.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.gozembcase.Details
import com.example.gozembcase.R
import com.example.gozembcase.Viewmodel.UserFactory
import com.example.gozembcase.Viewmodel.UserViewModel
import com.example.gozembcase.repository.UserRepo
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import org.json.JSONObject
import java.io.IOException
import java.util.*
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable





class MapsFragment : Fragment() {
    private var gMap: GoogleMap? = null
    var locationManager: LocationManager? = null
    var mFusedLocationClient: FusedLocationProviderClient? = null
    var currentLocation = LatLng(20.5, 78.9)
    val drawble = requireActivity()!!.resources.getDrawable(R.drawable.icons8_car_30__1_,requireActivity().theme)
    val bitmapImage= drawableToBitmap(drawble)

    private val callback =
        OnMapReadyCallback { googleMap ->

            /**
             * Manipulates the map once available.
             * This callback is triggered when the map is ready to be used.
             * This is where we can add markers or lines, add listeners or move the camera.
             * In this case, we just add a marker near Sydney, Australia.
             * If Google Play services is not installed on the device, the user will be prompted to
             * install it inside the SupportMapFragment. This method will only be triggered once the
             * user has installed Google Play services and returned to the app.
             */
            /**
             * Manipulates the map once available.
             * This callback is triggered when the map is ready to be used.
             * This is where we can add markers or lines, add listeners or move the camera.
             * In this case, we just add a marker near Sydney, Australia.
             * If Google Play services is not installed on the device, the user will be prompted to
             * install it inside the SupportMapFragment. This method will only be triggered once the
             * user has installed Google Play services and returned to the app.
             */
            /*gmapRepository.getGMapPlace().enqueue(object : Callback<Places> {
                override fun onResponse(call: Call<Places>, response: Response<Places>) {
                    response.javaClass.name
                    val rep: String = response.body().toString()
                    Toast.makeText(activity, response.body().toString(), Toast.LENGTH_LONG).show()
                }

                override fun onFailure(call: Call<Places>, t: Throwable) {}
            })*/
            var title: String = "Information"
            var pin= "source"
            var lat= 20.5
            var lng= 78.9

            val sharedPreference =  requireActivity().getSharedPreferences("save_uid", Context.MODE_PRIVATE)
            val myuid= sharedPreference?.getString("uid", null)
            if (myuid != null) {
                Toast.makeText(requireActivity(), "Goood", Toast.LENGTH_SHORT).show()


                val auth = FirebaseAuth.getInstance()
                val db = FirebaseFirestore.getInstance()
                val userRepos= UserRepo(auth, db)
                val userFactory= UserFactory(userRepos)
                val userViewModel: UserViewModel = ViewModelProvider(this, userFactory).get(
                    UserViewModel::class.java)
                userViewModel.getMap(myuid)
                userViewModel.getMap.observe(viewLifecycleOwner) { state ->

                    val str = Gson().toJson(state).toString()
                    val result= JSONObject(str).getJSONObject("result")
                     title=  result.getJSONObject("content").getString("title")
                     pin = result.getJSONObject("content").getString("pin")
                     lat = result.getJSONObject("content").getString("lat").toDouble()
                     lng = result.getJSONObject("content").getString("lng").toDouble()


                }


            }



            //val uid= auth.currentUser!!.uid

            //val bitmap = BitmapFactory.decodeResource(resources, R.drawable.)

            gMap = googleMap
            val sydney = LatLng(lat, lng)

            gMap!!.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney").icon(BitmapDescriptorFactory.fromBitmap(bitmapImage!!)))
            gMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))

            // Enable the zoom controls for the map
            gMap!!.uiSettings.isZoomControlsEnabled = true
            // Animating to zoom the marker
            gMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10F));
            gMap!!.setOnMarkerDragListener(object : OnMarkerDragListener {
                override fun onMarkerDrag(marker: Marker) {}
                override fun onMarkerDragEnd(marker: Marker) {
                    val latLng = marker.position
                    val geocoder = Geocoder(context!!, Locale.getDefault())
                    try {
                        val address =
                            geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)!![0]
                        Toast.makeText(requireActivity(), address.toString(), Toast.LENGTH_LONG)
                            .show()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

                override fun onMarkerDragStart(marker: Marker) {}
            })
            gMap!!.setOnMapClickListener { point ->
                val marker = gMap!!.addMarker(MarkerOptions().position(point))
            }
            // Initializing fused location client


            mFusedLocationClient =
                LocationServices.getFusedLocationProviderClient(requireActivity())
            lastLocation
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        checkPermission()
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    // Get current location
    @get:SuppressLint("MissingPermission")
    private val lastLocation: Unit
        private get() {
            if (isLocationEnabled) {
                mFusedLocationClient!!.lastLocation.addOnCompleteListener { task ->
                    val location = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        //val bitmapImage= BitmapFactory.decodeResource(resources, R.drawable.icons8_car_30__1_)
                        currentLocation = LatLng(
                            location.latitude,
                            location.longitude
                        )
                        gMap!!.clear()
                        gMap!!.addMarker(MarkerOptions().position(currentLocation).icon(BitmapDescriptorFactory.fromBitmap(bitmapImage!!)))
                        gMap!!.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                currentLocation,
                                16f
                            )
                        )
                    }
                }
            } else {
                Toast.makeText(requireActivity(), "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }

    // Check if location permissions are
    // granted to the application
    fun checkPermission() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            var fineLocationGranted: Boolean? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)
            }
            var coarseLocationGranted: Boolean? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                coarseLocationGranted =result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)
            }
            if (fineLocationGranted != null && fineLocationGranted) {
                // Precise location access granted.
            } else if (coarseLocationGranted != null && coarseLocationGranted) {
            } else {
                // No location access granted.
            }
        }

        //lancer la permission pour avoir la position
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    // Get current location, if shifted
    // from previous location
    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    // If current location could not be located, use last location
    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val mLastLocation = locationResult.lastLocation
            currentLocation = LatLng(mLastLocation!!.latitude, mLastLocation.longitude)
        }
    }

    // function to check if GPS is on
    private val isLocationEnabled: Boolean
        private get() {
            locationManager =
                requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager!!.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        }

    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap =
            Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        drawable.draw(canvas)
        return bitmap
    }
}