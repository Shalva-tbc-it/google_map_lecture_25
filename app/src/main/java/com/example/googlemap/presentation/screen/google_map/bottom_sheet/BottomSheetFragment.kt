package com.example.googlemap.presentation.screen.google_map.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.googlemap.R
import com.example.googlemap.databinding.FragmentBottomSheetBinding
import com.example.googlemap.presentation.screen.google_map.OnPlaceSelectedListener
import com.google.android.datatransport.runtime.logging.Logging.d
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class BottomSheetFragment : BottomSheetDialogFragment() {

    var onPlaceSelectedListener: OnPlaceSelectedListener? = null


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
        getSearchLocationByName()
    }

    private fun placeSelected(place: Place) {
        onPlaceSelectedListener?.onPlaceSelected(place)
    }

    private fun getSearchLocationByName() {
        viewLifecycleOwner.lifecycleScope.launch {
            Places.initialize(requireContext(), "AIzaSyB5teYKnVeIDRPG0bt07UbKbrHF0OLp3JI")
            val autocompleteFragment =
                childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

            autocompleteFragment.setPlaceFields(
                listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
            )

            autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
                override fun onPlaceSelected(place: Place) {
                    place?.let {
                        placeSelected(it)
                        dismiss()
                    }
                }

                override fun onError(status: Status) {
                    d("placeError", status.toString())
                }
            })
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
