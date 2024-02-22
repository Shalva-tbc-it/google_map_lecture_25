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
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GoogleMapFragment : BaseFragment<FragmentGoogleMapBinding>(FragmentGoogleMapBinding::inflate),
    OnMapReadyCallback, BottomSheetFragment.PlaceSearchListener {

    private lateinit var bottomSheet: BottomSheetFragment
    private lateinit var googleMap: GoogleMap
    private var marker: Marker? = null


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
            bottomSheet = BottomSheetFragment()
            bottomSheet.setSearchListener(this) // Set the fragment as the listener
            bottomSheet.show(parentFragmentManager, "tag")
        }

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
                                10f
                            )
                        )
                    }
                }
        } else {

            onRequestPermissionsResult(
                1,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                intArrayOf(LOCATION_PERMISSION_REQUEST_CODE)
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
            onRequestPermissionsResult(
                1,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                intArrayOf(LOCATION_PERMISSION_REQUEST_CODE)
            )
        }

    }

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

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap

    }

    override fun placeSearch(latLng: LatLng) {
        val markerOptions = MarkerOptions()
            .position(latLng)
        marker = googleMap.addMarker(markerOptions)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
    }

}