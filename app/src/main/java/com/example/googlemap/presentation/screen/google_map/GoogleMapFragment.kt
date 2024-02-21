package com.example.googlemap.presentation.screen.google_map

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.example.googlemap.R
import com.example.googlemap.databinding.FragmentGoogleMapBinding
import com.example.googlemap.presentation.common.base.BaseFragment
import com.example.googlemap.presentation.screen.google_map.bottom_sheet.BottomSheetFragment
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GoogleMapFragment : BaseFragment<FragmentGoogleMapBinding>(FragmentGoogleMapBinding::inflate),
    OnMapReadyCallback, OnPlaceSelectedListener {

    private lateinit var googleMap: GoogleMap


    override fun bind() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?

        mapFragment?.getMapAsync(this)

    }

    override fun bindViewActionListener() {
        binding.myLocationButton.setOnClickListener {
            onMyLocationButtonClick()
        }
        binding.searchBar.setOnClickListener {
            BottomSheetFragment().show(parentFragmentManager, "Search")
        }

    }

    override fun bindObserves() {

    }

    private fun onMyLocationButtonClick() {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            val fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(requireActivity())
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {

                        val currentLatLng = LatLng(location.latitude, location.longitude)

                        googleMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                currentLatLng,
                                15f
                            )
                        )
                    }
                }
        } else {

            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }

    }

//    private fun getSearchLocationByName() {
//        Places.initialize(requireContext(), "AIzaSyB5teYKnVeIDRPG0bt07UbKbrHF0OLp3JI")
//        val autocompleteFragment =
//            childFragmentManager.findFragmentById(R.id.autocomplete) as AutocompleteSupportFragment
//
//        autocompleteFragment.setPlaceFields(
//            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
//        )
//
//        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
//            override fun onPlaceSelected(place: Place) {
//                place?.let {
//                    showSelectedPlace(it)
//                }
//            }
//
//            override fun onError(status: Status) {
//
//            }
//        })
//    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation()
                }
            }

            else -> onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    fun showSelectedPlace(place: Place) {
        val location = place.latLng
        location?.let {
            googleMap.addMarker(
                MarkerOptions()
                    .position(it)
                    .title("Marker in ${place.name}")
            )
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(it))
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap

    }

    override fun onPlaceSelected(place: Place) {
        place?.let {
            googleMap.addMarker(
                MarkerOptions()
                    .position(it.latLng)
                    .title("Marker in ${place.name}")
            )
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(it.latLng))
        }

    }

//    override fun onPlaceSelected(place: Place) {
//
//        showSelectedPlace(place)
////        val location = place.latLng
////
////        location?.let {
////            val cityName = place.name
////            googleMap.addMarker(
////                MarkerOptions()
////                    .position(it)
////                    .title("Marker in $cityName")
////            )
////            googleMap.moveCamera(CameraUpdateFactory.newLatLng(it))
////        }
//    }

}