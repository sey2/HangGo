package org.hango.com;

import TravelAdapter
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.hango.com.Data.Travel
import org.hango.com.Data.TravelViewModel
import org.hango.com.databinding.FragmentHomeBinding
import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDate


class HomeFragment : Fragment() {
    private lateinit var todayAdapter: TravelAdapter
    private lateinit var hotelAdapter: TravelAdapter
    private lateinit var famousAdapter: TravelAdapter
    private lateinit var model: TravelViewModel
    private lateinit var mContext: Context
    private var binding: FragmentHomeBinding? = null

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        model = ViewModelProvider(this).get(TravelViewModel::class.java)
        todayAdapter = TravelAdapter()
        setAdapter(todayAdapter, binding!!.todayRecycler)
        loadData(container, todayAdapter, "areaBasedList", "", "1")

        hotelAdapter = TravelAdapter()
        setAdapter(hotelAdapter!!, binding!!.hotRecycler)
        loadData(container, hotelAdapter, "searchStay", "", "1")

        val current_date: LocalDate = LocalDate.now()
        val date: Array<String> = current_date.toString().split("-").toTypedArray()

        famousAdapter = TravelAdapter()
        setAdapter(famousAdapter!!, binding!!.famousRecycler)
        loadData(container, famousAdapter, "searchFestival", date[0] + date[1] + date[2], "1")

        attachListener(container, date)
        Log.d("Activity2", "HomeFragment")

        model?.let{model.userinfo?.observe(viewLifecycleOwner) { userInfoData ->
                Log.d("callUrl", "change: " + userInfoData.profileUri!!)
                Glide.with(this).load(userInfoData.profileUri!!).into(binding!!.profile)
            }
        }

        return binding!!.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mContext = context
        }
    }

    // 앱 첫 실행순서 HomeFragment -> MainActivity -> HomeFragment (onResume)
    override fun onResume() {
        super.onResume()

        if (model.userinfo?.value?.profileUri == null) setProfileFromCloud()
        else
            binding!!.userName.setText(model.userinfo!!.value?.userName ?: "NULL")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun attachListener(container: ViewGroup?, date: Array<String>) {
        // 검색어 입력 후 자판 내리기
        binding!!.searchEditText.setOnKeyListener{ view, keyCode, event ->
            //Enter key Action
            if(event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER){
                val imm  = mContext.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding!!.searchEditText.windowToken, 0)
                searchData(container!!, date)
                true
            }
            false
        }

        binding!!.searchBtn.setOnClickListener { v ->
            //자판 감추기
            val imm =
                mContext.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0) //hide keyboard
            searchData(container!!, date)
        }
    }

    private fun searchData(container: ViewGroup, date: Array<String>) {
        val local = convertLocalNum(binding!!.searchEditText.text.toString())
        if (local == "-1") {
            binding!!.searchEditText.setText("")
            binding!!.searchEditText.hint = "올바르지 않은 검색어 입니다."
            return
        }
        binding!!.searchEditText.setText("")
        binding!!.searchEditText.hint = "검색어를 입력해주세요"
        deleteAdapter()
        model.deleteList()
        loadData(container, todayAdapter, "areaBasedList", "", local)
        loadData(container, hotelAdapter, "searchStay", "", local)
        loadData(container, famousAdapter, "searchFestival", date[0] + date[1] + date[2], local)
    }

    private fun setAdapter(adapter: TravelAdapter, recyclerView: RecyclerView?) {
        recyclerView!!.adapter = adapter
        recyclerView!!.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL, false
        )
    }

    private fun deleteAdapter() {
        todayAdapter.deleteList()
        hotelAdapter.deleteList()
        famousAdapter.deleteList()
    }

    private fun loadData(
        view: ViewGroup?,
        adapter: TravelAdapter?,
        search: String,
        eventDate: String,
        local: String
    ) {
        val params: HashMap<String, String> = HashMap()
        params["ServiceKey"] = BuildConfig.TRAVEL_API_KEY
        params["numOfRows"] = "10"
        params["pageNo"] = "1"
        params["MobileOS"] = "ETC"
        params["MobileApp"] = "AppTest"
        params["arrange"] = "P"
        params["listYN"] = "Y"
        params["areaCode"] = local //1 서울 //39 제주도 //5 광주 // 6 부산
        params["_type"] = "json"

        var url =
            "http://api.visitkorea.or.kr/openapi/service/rest/KorService/$search"

        url = addParams(url, params)

        if (eventDate != "")
            url += "&eventStartDate=$eventDate"

        val requestQueue = Volley.newRequestQueue(view?.context)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { jsonObject ->
                try {
                    Log.d("Json", jsonObject.toString())
                    val response = jsonObject.get("response") as JSONObject

                    val body = response["body"] as JSONObject
                    Log.d("Json", "execute2")
                    val items = body["items"] as JSONObject
                    Log.d("Json", "execute3")
                    val itemArray = items.getJSONArray("item")
                    Log.d("Json", "execute4")

                    var arItem: ArrayList<Travel> = ArrayList()

                    for (i in 0 until itemArray.length()) {
                        Log.d("Josn", "foorLoop in")
                        val item = itemArray.getJSONObject(i)
                        var travel: Travel = Travel()

                        Log.d(
                            "Json", item.getString("addr1") + " " +
                                    item.getString("title") + " " + item.getString("firstimage") + " " +
                                    item.getDouble("mapx") + " " + item.getDouble("mapy")
                        )

                        travel.spot = parseAddress(item.getString("addr1"))
                        travel.city = parseTitle(item.getString("title"))
                        travel.img = item.getString("firstimage")

                        if (travel.img == "") continue

                        travel.mapX = item.getDouble("mapx")
                        travel.mapY = item.getDouble("mapy")

                        arItem.add(travel)
                        model.add(travel)
                    }

                    if (arItem.size > 0) {
                        adapter!!.itemList = arItem
                        adapter.notifyDataSetChanged()
                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, object : Response.ErrorListener {
                override fun onErrorResponse(error: VolleyError?) {
                    Log.d("Json", "error")
                }
            })

        requestQueue.add(jsonObjectRequest)
    }

    private fun addParams(url: String, mapParam: HashMap<String, String>?): String {
        val stringBuilder = StringBuilder("$url?")
        if (mapParam != null) {
            for (key in mapParam.keys) {
                stringBuilder.append("$key=")
                stringBuilder.append(mapParam[key].toString() + "&")
            }
        }
        return stringBuilder.toString()
    }

    private fun parseAddress(addr: String): String {
        val str = addr.split(" ").toTypedArray()
        return if (str.size > 1) str[0] + str[1] else addr
    }

    private fun parseTitle(title: String): String {
        return if (title.length >= 12) title.substring(0, 12) + ".." else title
    }

    private fun convertLocalNum(local: String): String {
        if (local == "제주" || local == "제주도") return "39"
        val arr1 = arrayOf("서울", "인천", "대전", "대구", "광주", "부산", "울산", "세종")
        val arr2 = arrayOf("서울특별시", "인천광역시", "대전광역시", "대구광역시", "광주광역시", "부산광역시", "울산광역시", "세종특별자치시")
        for (i in arr1.indices) {
            if (local == arr1[i] || local == arr2[i]) {
                return (i + 1).toString()
            }
        }
        return "-1"
    }

    private fun setProfileFromCloud() {
        val storage: FirebaseStorage = FirebaseStorage.getInstance()
        val storageRef: StorageReference = storage.getReference()

        val imgRef: StorageReference = storageRef.child(
            "profile/" +  model.userinfo?.value?.userID.toString() + "profile.png"
        )

        if (imgRef != null) {
            imgRef.getDownloadUrl().addOnSuccessListener(object : OnSuccessListener<Uri?> {
                override fun onSuccess(uri: Uri?) {
                    Log.d("callUrl", "sucess$uri")
                    Glide.with(mContext).load(uri).error(R.drawable.user_ic).into(binding!!.profile)
                }
            }).addOnFailureListener(object : OnFailureListener {
                override fun onFailure(e: Exception) {
                    Log.d("test", e.toString())
                }
            })
        }
    }
}