
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.hango.com.Data.Travel
import org.hango.com.databinding.RecommendItemBinding

class TravelAdapter: RecyclerView.Adapter<Holder>() {
    var itemList: ArrayList<Travel> = ArrayList()
    private var context: View? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RecommendItemBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false)

        context = parent

        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val travel = itemList[position]
        holder.setTravel(travel, context)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun deleteList() {
        itemList = ArrayList()
    }
}

class Holder(val binding: RecommendItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun setTravel(travel: Travel, context: View?) {
        binding.cityTextView.text = travel.city
        binding.spotTextView.text = travel.spot
        Glide.with(context!!).load(travel.img).into(binding.backgroundImg)
    }

    init {
        itemView.setOnClickListener { v ->
            Toast.makeText(
                itemView.getContext(),
                "클릭됨",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}