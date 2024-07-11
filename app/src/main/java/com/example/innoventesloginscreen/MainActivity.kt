package com.example.innoventesloginscreen

import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.innoventesloginscreen.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        viewModel = ViewModelProvider(this)[ActivityViewModel::class.java]
        viewModel.isAllDataValid = MutableLiveData()
        viewModel.isAllDataValid.value = arrayListOf(false,false,false,false)

        initUi()
        observers()
        setClickListeners()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initUi() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun observers() {
        viewModel.isAllDataValid.observe(this){
            if(it[0] && it[1] && it[2] && it[3]){
                binding.next.isEnabled = true
            }else{
                binding.next.isEnabled = false
            }
            if(!it[0]) binding.pan.background = resources.getDrawable(R.drawable.rectangular_border_background_drawable)
            else binding.pan.background = resources.getDrawable(R.drawable.rectangular_border_background_drawable_default)

            if(!it[1]) binding.date.background = resources.getDrawable(R.drawable.rectangular_border_background_drawable)
            else binding.date.background = resources.getDrawable(R.drawable.rectangular_border_background_drawable_default)

            if(!it[2]) binding.month.background = resources.getDrawable(R.drawable.rectangular_border_background_drawable)
            else binding.month.background = resources.getDrawable(R.drawable.rectangular_border_background_drawable_default)

            if(!it[3]) binding.year.background = resources.getDrawable(R.drawable.rectangular_border_background_drawable)
            else binding.year.background = resources.getDrawable(R.drawable.rectangular_border_background_drawable_default)

        }
    }

    private fun setClickListeners() {
        binding.apply {
            pan.doAfterTextChanged {
                validatePan(it)
            }
            date.doAfterTextChanged {
                validateDate(it)
            }
            month.doAfterTextChanged {
                 validateMonth(it)
            }
            year.doAfterTextChanged {
                validateYear(it)
            }
            next.setOnClickListener {
                Toast.makeText(this@MainActivity, "Details submitted successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            noPan.setOnClickListener {
                finish()
            }
        }

    }

    private fun validateYear(it: Editable?) {
        val year = if (binding.year.text?.length !=0) binding.year.text?.toString()?.toInt()?:0 else null
        year?.let {
            if(Integer.parseInt(it.toString()) > 2024 || Integer.parseInt(it.toString()) == 0 || Integer.parseInt(it.toString()) < 1900 ){
                var validData = ArrayList<Boolean>(viewModel.isAllDataValid.value)
                validData[3] = false
                viewModel.isAllDataValid.value = validData
                return
            }else{
                var validData = ArrayList<Boolean>(viewModel.isAllDataValid.value)
                validData[3] = true
                viewModel.isAllDataValid.value = validData
            }
        }
        validateMonth(binding.month.text)
        validateDate(binding.date.text)
        if(year == null){
            var validData = ArrayList<Boolean>(viewModel.isAllDataValid.value)
            validData[3] = false
            viewModel.isAllDataValid.value = validData
        }
    }

    private fun validateMonth(it: Editable?) {
        val month = if (binding.month.text?.length !=0) binding.month.text?.toString()?.toInt()?:0 else null
        month?.let {
            if(Integer.parseInt(it.toString()) !in 1..12 ){
                var validData = ArrayList<Boolean>(viewModel.isAllDataValid.value)
                validData[2] = false
                viewModel.isAllDataValid.value = validData
                return
            }else{
                var validData = ArrayList<Boolean>(viewModel.isAllDataValid.value)
                validData[2] = true
                viewModel.isAllDataValid.value = validData
            }
        }
        validateDate(binding.date.text)
        if(month == null){
            var validData = ArrayList<Boolean>(viewModel.isAllDataValid.value)
            validData[2] = false
            viewModel.isAllDataValid.value = validData
        }
    }

    private fun validateDate(it: Editable?) {
        var isLeapYear = false
        val year =  if (binding.year.text?.length !=0) binding.year.text?.toString()?.toInt()?:1 else null
        val month = if (binding.month.text?.length !=0) binding.month.text?.toString()?.toInt()?:0 else null
        val date = if( binding.date.text?.length !=0) binding.date.text?.toString()?.toInt()?:0 else null
        if (year != null) {
            if (year % 4 == 0) {
                if (year % 100 == 0) {
                    isLeapYear = year % 400 == 0
                }
            }
        }
        month?.let {
            if (Integer.parseInt(it.toString()) == 2 && !isLeapYear) {
                date?.let {
                    if( Integer.parseInt(binding.date.text.toString()) !in 1..28){
                        var validData = ArrayList<Boolean>(viewModel.isAllDataValid.value)
                        validData[1] = false
                        viewModel.isAllDataValid.value = validData
                        return
                    }else{
                        var validData = ArrayList<Boolean>(viewModel.isAllDataValid.value)
                        validData[1] = true
                        viewModel.isAllDataValid.value = validData
                    }
                }
            }
            if (Integer.parseInt(it.toString()) == 2 && isLeapYear) {
                date?.let {
                    if( Integer.parseInt(binding.date.text.toString()) !in 1..29){
                        var validData = ArrayList<Boolean>(viewModel.isAllDataValid.value)
                        validData[1] = false
                        viewModel.isAllDataValid.value = validData
                        return
                    }else{
                        var validData = ArrayList<Boolean>(viewModel.isAllDataValid.value)
                        validData[1] = true
                        viewModel.isAllDataValid.value = validData
                    }
                }
            }
        }
        date?.let {
            if(Integer.parseInt(binding.date.text.toString()) !in 1.. 31){
                var validData = ArrayList<Boolean>(viewModel.isAllDataValid.value)
                validData[1] = false
                viewModel.isAllDataValid.value = validData
                return
            }else{
                var validData = ArrayList<Boolean>(viewModel.isAllDataValid.value)
                validData[1] = true
                viewModel.isAllDataValid.value = validData
            }
        }
        if(date == null ){
            var validData = ArrayList<Boolean>(viewModel.isAllDataValid.value)
            validData[1] = false
            viewModel.isAllDataValid.value = validData
        }
    }


    private fun validatePan(it: Editable?) {
        val patter = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$".toRegex()
        val isValid = patter.matches(it.toString())
       if(isValid){
           binding.panContainer.boxStrokeColor = resources.getColor(R.color.green)
           var validData = ArrayList<Boolean>(viewModel.isAllDataValid.value)
           validData[0] = true
           viewModel.isAllDataValid.value = validData

       }else{
           binding.panContainer.boxStrokeColor = resources.getColor(R.color.red)
           var validData = ArrayList<Boolean>(viewModel.isAllDataValid.value)
           validData[0] = false
           viewModel.isAllDataValid.value = validData
       }
    }

}