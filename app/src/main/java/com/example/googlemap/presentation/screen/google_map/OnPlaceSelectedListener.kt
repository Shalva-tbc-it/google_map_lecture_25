package com.example.googlemap.presentation.screen.google_map

import com.google.android.libraries.places.api.model.Place

interface OnPlaceSelectedListener {
    fun onPlaceSelected(place: Place)
}