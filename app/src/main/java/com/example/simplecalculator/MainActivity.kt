package com.example.simplecalculator

import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.simplecalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    private var firstNumber = ""
    private var operator = ""
    private var currentInput = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        WindowCompat.setDecorFitsSystemWindows(window, false)


        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)



        val numberButtons = listOf(
            binding.btn0,
            binding.btn1,
            binding.btn2,
            binding.btn3,
            binding.btn4,
            binding.btn5,
            binding.btn6,
            binding.btn7,
            binding.btn8,
            binding.btn9
        )



        // Numbers

        for(button in numberButtons){

            button.setOnClickListener {

                animateButton(button)

                currentInput += button.text

                updateExpression()

                updateResult()

            }

        }



        // 00 Button

        binding.btn00.setOnClickListener {

            animateButton(binding.btn00)

            currentInput += "00"

            updateExpression()

            updateResult()

        }





        // Decimal

        binding.btnDot.setOnClickListener {

            animateButton(binding.btnDot)


            if(!currentInput.contains(".")){

                currentInput += "."

                updateExpression()

                updateResult()

            }

        }


// Percentage

        binding.btnPercent.setOnClickListener {

            animateButton(binding.btnPercent)

            if(currentInput.isNotEmpty()){

                val value = currentInput.toDouble() / 100

                currentInput = formatResult(value)

                updateResult()

                updateExpression()

            }

        }


        // Operators

        binding.btnPlus.setOnClickListener {

            animateButton(binding.btnPlus)

            setOperator("+")

        }


        binding.btnMinus.setOnClickListener {

            animateButton(binding.btnMinus)

            setOperator("-")

        }


        binding.btnMultiply.setOnClickListener {

            animateButton(binding.btnMultiply)

            setOperator("*")

        }


        binding.btnDivide.setOnClickListener {

            animateButton(binding.btnDivide)

            setOperator("/")

        }





        // Equals

        binding.btnEquals.setOnClickListener {

            animateButton(binding.btnEquals)

            calculate()

        }





        // Clear

        binding.btnAC.setOnClickListener {

            animateButton(binding.btnAC)

            firstNumber = ""

            operator = ""

            currentInput = ""

            binding.tvExpression.text = ""

            binding.tvResult.text = "0"

        }






        // Delete

        binding.btnDel.setOnClickListener {

            animateButton(binding.btnDel)


            if(currentInput.isNotEmpty()){


                currentInput = currentInput.dropLast(1)


                updateExpression()


                binding.tvResult.text =
                    if(currentInput.isEmpty())
                        "0"
                    else
                        currentInput

            }

        }


    }







    // Button animation

    private fun animateButton(button: Button){


        button.performHapticFeedback(
            HapticFeedbackConstants.KEYBOARD_TAP
        )


        button.animate()
            .scaleX(0.88f)
            .scaleY(0.88f)
            .setDuration(60)
            .withEndAction {

                button.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(60)

            }

    }







    // Update result

    private fun updateResult(){


        binding.tvResult.text = currentInput


        binding.tvResult.alpha = 0f


        binding.tvResult.animate()
            .alpha(1f)
            .setDuration(120)

    }







    // Update expression line

    private fun updateExpression(){

        if(firstNumber.isNotEmpty() && operator.isNotEmpty()){

            binding.tvExpression.text =
                "$firstNumber $operator $currentInput"

        }

    }







    // Operator setup

    private fun setOperator(op: String){

        if(currentInput.isEmpty())
            return


        if(firstNumber.isNotEmpty() && operator.isNotEmpty()){

            calculate()

            firstNumber = currentInput

        }
        else{

            firstNumber = currentInput

        }


        operator = op

        currentInput = ""


        binding.tvExpression.text =
            "$firstNumber $operator"

    }








    // Calculate

    private fun calculate(){


        if(firstNumber.isEmpty() || currentInput.isEmpty())
            return



        val a = firstNumber.toDouble()

        val b = currentInput.toDouble()



        val result: Any = when(operator){


            "+" -> a+b

            "-" -> a-b

            "*" -> a*b

            "/" -> {

                if(b == 0.0)

                    "Error"

                else

                    a/b

            }


            else -> 0

        }




        binding.tvExpression.text =
            "$firstNumber $operator $currentInput"



        binding.tvResult.alpha = 0f


        binding.tvResult.text =
            formatResult(result)



        binding.tvResult.animate()
            .alpha(1f)
            .setDuration(200)



        currentInput =
            if(result is String)
                result
            else
                formatResult(result)


        firstNumber = currentInput
        operator = ""

    }







    // Remove .0 when not needed

    private fun formatResult(value: Any): String {


        if(value is String)
            return value



        val number = value as Double



        return if(number % 1 == 0.0){

            number.toInt().toString()

        }
        else{

            number.toString()

        }

    }

}