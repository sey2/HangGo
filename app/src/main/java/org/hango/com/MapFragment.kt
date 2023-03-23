package org.hango.com

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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