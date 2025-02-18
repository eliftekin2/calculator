package com.eliftekin.calculator

import android.health.connect.datatypes.units.Length
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.eliftekin.calculator.databinding.ActivityMainBinding
import org.mozilla.javascript.Context

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    private var canAddOperator = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun buttonClear(view: View) {
        binding.input.setText("")
        binding.result.setText("")

        canAddOperator = false
    }

    fun buttonDel(view: View) {
        val length = binding.input.length()
        val inputText = binding.input.text

        if (length>0)
            binding.input.text = binding.input.text.subSequence(0, length - 1)

        if(!inputText.last().equals("+") || !inputText.last().equals("-") || !inputText.last().equals("x") || !inputText.last().equals("/"))
            canAddOperator = true

    }

    fun buttonNumbers(view: View) {
        if(view is Button){
            binding.input.append(view.text)
            canAddOperator = true
        }
    }

    fun buttonOperators(view: View) {
        val input = binding.input

        if(input.text.isNotEmpty()){
            if (view is Button){
                if (canAddOperator){
                    input.append(view.text)
                    canAddOperator = false
                }
                else{
                    var resulttext = input.text.toString()
                    resulttext = resulttext.replace(resulttext.last().toString(), view.text.toString())
                    input.setText(resulttext)
                }
            }
        }
        else
            Toast.makeText(this, "Invalid format used.", Toast.LENGTH_SHORT).show()
    }

    fun buttonEquals(view: View) {
        var input = binding.input.text.toString()

        if (input.endsWith("+") || input.endsWith("-") || input.endsWith("x") || input.endsWith("/"))
            Toast.makeText(this, "Invalid format used.", Toast.LENGTH_SHORT).show()
        else
        input = input.replace("x", "*")

        calculate(input)
    }

    private fun calculate(input: String) {
        try {
            val context = Context.enter()
            context.optimizationLevel = -1

            val scriptable = context.initSafeStandardObjects()
            val result = context.evaluateString(scriptable, input, "Javascript", 1, null).toString()

            binding.result.text = result

        }
        catch (e: Exception){
            print(e.message)
        }
    }

}