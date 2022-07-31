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

class Fragment1 : Fragment() {

    private var _binding: Fragment1Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val _binding = Fragment1Binding.inflate(inflater,container, false)

        var todayAdapter = TravelAdapter()
        todayAdapter.listData = loadData()
        _binding.todayRecycler.adapter = todayAdapter
        _binding.todayRecycler.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        var hotSpotAdapter = TravelAdapter()
        hotSpotAdapter.listData = loadData()
        _binding.hotRecycler.adapter = hotSpotAdapter
        _binding.hotRecycler.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        return _binding.root
    }


    fun loadData(): MutableList<Travel>{
        val data : MutableList<Travel> = mutableListOf()

        data.add(Travel("제주도","세화 해변", ResourcesCompat.getDrawable(this.resources, R.drawable.sehwa, null)))
        data.add(Travel("제주도", "아르떼 뮤지엄",
            ResourcesCompat.getDrawable(this.resources, R.drawable.sehwa, null)))
        data.add(Travel("제주도", "태웃개",
            ResourcesCompat.getDrawable(this.resources, R.drawable.sehwa, null)))
        data.add(Travel("제주도", "태웃개",
            ResourcesCompat.getDrawable(this.resources, R.drawable.sehwa, null)))

        return data
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}