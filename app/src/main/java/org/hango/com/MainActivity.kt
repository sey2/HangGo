package org.hango.com

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.ListFragment
import org.hango.com.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setFragemnt()
    }

    fun setFragemnt(){
        val listFragment: Fragment1 = Fragment1()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.frame_layout, listFragment)
        transaction.commit()
    }
}