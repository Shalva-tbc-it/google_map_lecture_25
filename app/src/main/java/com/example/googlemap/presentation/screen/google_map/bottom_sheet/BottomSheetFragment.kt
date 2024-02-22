package com.example.googlemap.presentation.screen.google_map.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.googlemap.R
import com.example.googlemap.databinding.FragmentBottomSheetBinding
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment : BottomSheetDialogFragment(), OnMapReadyCallback {


    private var placeSearchListener: PlaceSearchListener? = null

    interface PlaceSearchListener {
        fun placeSearch(latLng: LatLng)
    }

    private lateinit var googleMap: GoogleMap
    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCurrentPlace()

    }


    private fun getCurrentPlace() {
        Places.initialize(requireContext(), getString(R.string.google_map_api_key))
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))
        autocompleteFragment.setPlaceFields(
            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
        )
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                place.latLng?.let {
                    placeSearchListener?.placeSearch(it)
                }
                dismiss()
            }

            override fun onError(status: Status) {
                Toast.makeText(requireContext(), status.toString(), Toast.LENGTH_SHORT).show()
                dismiss()
            }
        })
    }


    fun setSearchListener(listener: PlaceSearchListener) {
        placeSearchListener = listener
    }

    override fun onMapReady(gMap: GoogleMap) {
        googleMap = gMap
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
