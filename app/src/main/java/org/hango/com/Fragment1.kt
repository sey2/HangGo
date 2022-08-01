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
        todayAdapter.listData = todayTravelLoadData()
        _binding.todayRecycler.adapter = todayAdapter
        _binding.todayRecycler.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        var hotSpotAdapter = TravelAdapter()
        hotSpotAdapter.listData = hotTravelloadData()
        _binding.hotRecycler.adapter = hotSpotAdapter
        _binding.hotRecycler.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        var famousAdapter = TravelAdapter()
        famousAdapter.listData = famousRestaurantLoadData()
        _binding.famousRecycler.adapter = famousAdapter
        _binding.famousRecycler.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        return _binding.root
    }


    fun todayTravelLoadData(): MutableList<Travel>{
        val data : MutableList<Travel> = mutableListOf()

        data.add(Travel("제주도","세화 해변", ResourcesCompat.getDrawable(this.resources, R.drawable.back_1, null)))
        data.add(Travel("제주도", "아르떼 뮤지엄",
            ResourcesCompat.getDrawable(this.resources, R.drawable.back_3, null)))
        data.add(Travel("제주도", "태웃개",
            ResourcesCompat.getDrawable(this.resources, R.drawable.back_2, null)))
        data.add(Travel("제주도", "정월드",
            ResourcesCompat.getDrawable(this.resources, R.drawable.back_4, null)))

        return data
    }

    fun hotTravelloadData(): MutableList<Travel>{
        val data : MutableList<Travel> = mutableListOf()

        data.add(Travel("서울","잠실 롯데월드", ResourcesCompat.getDrawable(this.resources, R.drawable.hot_1, null)))
        data.add(Travel("전주", "한옥 마을",
            ResourcesCompat.getDrawable(this.resources, R.drawable.hot_2, null)))
        data.add(Travel("담양", "죽녹원",
            ResourcesCompat.getDrawable(this.resources, R.drawable.hot_3, null)))
        data.add(Travel("부산", "롯데월드",
            ResourcesCompat.getDrawable(this.resources, R.drawable.hot_4, null)))

        return data
    }

    fun famousRestaurantLoadData(): MutableList<Travel>{
        val data : MutableList<Travel> = mutableListOf()

        data.add(Travel("광주","솥밥솥밥", ResourcesCompat.getDrawable(this.resources, R.drawable.famous_1, null)))
        data.add(Travel("목동", "델리커리",
            ResourcesCompat.getDrawable(this.resources, R.drawable.famous_2, null)))
        data.add(Travel("파주", "톰바그",
            ResourcesCompat.getDrawable(this.resources, R.drawable.famous_3, null)))
        data.add(Travel("서울", "퓨전 선술집",
            ResourcesCompat.getDrawable(this.resources, R.drawable.famous_4, null)))

        return data
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}