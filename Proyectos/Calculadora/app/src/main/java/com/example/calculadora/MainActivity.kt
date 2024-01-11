package com.example.calculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.widget.addTextChangedListener
import com.example.calculadora.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.tvOperation.run {
            addTextChangedListener { charSequence ->
                if (Operations.canRepalceOperator(charSequence.toString())) {
                    val newSrt = "${text.substring(0, text.length - 2)}${text.substring(text.length - 1)}"
                    text = newSrt
                }
            }
        }
    }



    fun onClickButton(view:View){
        val valueStr = (view as Button).text.toString()
        val operation =  binding.tvOperation.text.toString()

        when(view.id) {
            R.id.btnDelete -> {

                binding.tvOperation.run {
                    if (text.isNotEmpty()) text = operation.substring(0, text.length-1)
                }
            }

            R.id.btnClear -> {
                binding.tvOperation.text = ""
                binding.tvResult.text = ""
            }
            R.id.btnResolve -> checkOrResolve(operation, true)

            R.id.btnPoin ->  addPoint(valueStr, operation)

            R.id.btnMulti,
            R.id.btnDiv,
            R.id.btnSub,
            R.id.btnSum -> {
                checkOrResolve(operation,false)

                addOperator(valueStr, operation)
            }
            else->binding.tvOperation.append(valueStr)

        }
    }

    private fun addPoint(pointStr: String, operation: String) {
        if(!operation.contains(Constants.POINT)){
            binding.tvOperation.append(pointStr)
        }else{
            val operator = Operations.getOperator(operation)

            val values = Operations.divideOperation(operator,operation)

            if (values.isNotEmpty()){
                val numOne = values[0]!!
                if (values.size > 1){
                    val numTwo = values[1]!!
                    if (numOne.contains(Constants.POINT) && !numTwo.contains(Constants.POINT)){
                        binding.tvOperation.append(pointStr)
                    }
                }else{
                    if (numOne.contains(Constants.POINT)){
                        binding.tvOperation.append(pointStr)
                    }
                }
            }

        }
    }

    private fun addOperator(operator: String, operation: String) {
        val lastElement = if (operation.isEmpty()) ""
        else operation.substring(operation.length - 1)
        if (operator== Constants.OPERATOR_SUB){
            if (operator.isEmpty() || lastElement != Constants.OPERATOR_SUB && lastElement != Constants.POINT){
                binding.tvOperation.append(operator)
            }
        }else{
            if (operation.isNotEmpty() && lastElement != Constants.POINT){
                binding.tvOperation.append(operator)
            }
        }

    }
    private fun checkOrResolve(operation: String, isFromResolve: Boolean){
        Operations.tryResolve(operation, isFromResolve, object : OnResolveListener{
            override fun onShowResult(result: Double) {
                binding.tvResult.text = String.format(Locale.ROOT,"%.3f", result)

                if (binding.tvResult.text.isNotEmpty() && !isFromResolve){
                    binding.tvOperation.text = binding.tvResult.text
                }
            }

            override fun onShowMessage(errorRes: Int) {
                showMessage(errorRes)
            }
        })
    }

    private fun showMessage(erroRes:Int) {
        Snackbar.make(binding.root, erroRes, Snackbar.LENGTH_SHORT)
            .setAnchorView(binding.llTop).show()
    }






}