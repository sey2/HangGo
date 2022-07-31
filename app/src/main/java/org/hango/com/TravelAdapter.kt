import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import org.hango.com.Travel
import org.hango.com.databinding.RecommendItemBinding

class TravelAdapter: RecyclerView.Adapter<Holder>() {
    var listData = mutableListOf<Travel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RecommendItemBinding.inflate(LayoutInflater.from(parent.context),
            parent, false);

        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val travel = listData.get(position)
        holder.setTravel(travel)
    }
}

class Holder(val binding: RecommendItemBinding): RecyclerView.ViewHolder(binding.root){

    init{
        binding.root.setOnClickListener{
            Toast.makeText(binding.root.context, "클릭됨",Toast.LENGTH_LONG).show()
        }
    }

    fun setTravel(travel: Travel){
        binding.cityTextView.text = travel.city
        binding.spotTextView.text = travel.spot
        binding.relativeLayout.background = travel.img

    }
}