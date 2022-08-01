package org.hango.com

import TravelAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import org.hango.com.databinding.Fragment1Binding
import org.hango.com.databinding.FragmentMapBinding

class MapFragment : Fragment() {

    private var _binding: MapFragment? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val _binding = FragmentMapBinding.inflate(inflater,container, false)


        return _binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}