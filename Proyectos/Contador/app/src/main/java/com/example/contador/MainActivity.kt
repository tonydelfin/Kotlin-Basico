package com.example.contador

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.contador.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var count:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setCounter()

        binding.btnSum.setOnClickListener {
            count++
            setCounter()
        }
        binding.btnSum.setOnLongClickListener {
            count=  0
            setCounter()
            true
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.run {
            putInt(PARAM_COUNT,count)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        count = savedInstanceState.getInt(PARAM_COUNT)
        setCounter()
        super.onRestoreInstanceState(savedInstanceState)
    }

    private fun setCounter() {
        binding.tvCount.text = count.toString()
    }
    companion object{
        private const val PARAM_COUNT:String = "param_count"
    }
}