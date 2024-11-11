package com.westerra.release.places

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.westerra.release.R
import com.westerra.release.constants.Constants.DENVER_LATITUDE
import com.westerra.release.constants.Constants.DENVER_LONGITUDE
import com.westerra.release.databinding.FindUsCustomViewBinding
import com.westerra.release.extensions.fadeGone
import com.westerra.release.extensions.toMarkerIcon

class FindUsCustomFragment : Fragment() {

    // This property is only valid between onCreateView and onDestroyView.
    private var _binding: FindUsCustomViewBinding? = null
    val binding get() = _binding!!
    private var googleMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FindUsCustomViewBinding.inflate(inflater, container, false)
        binding.toolbar.apply {
            setNavigationIcon(R.drawable.backbase_ic_close)
            setNavigationOnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }
        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)?.getMapAsync {
            googleMap = it
            binding.progressbar.fadeGone()
            it.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(DENVER_LATITUDE, DENVER_LONGITUDE),
                    10.0F,
                ),
            )
        }
        binding.searchButton.setOnClickListener {
            doSearchAttempt()
        }
        return binding.root
    }

    private fun doSearchAttempt() {
        context ?. let { context ->
            googleMap ?. let { map ->
                val cherryCreekMarker = MarkerOptions()
                    .position(LatLng(39.710848, -104.943401))
                    .title("Cherry Creek Branch/ATM - Westerra Credit Union")
                    .icon(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.westerra_marker,
                        )?.toMarkerIcon(scalingFactor = 5),
                    )
                map.addMarker(cherryCreekMarker)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
