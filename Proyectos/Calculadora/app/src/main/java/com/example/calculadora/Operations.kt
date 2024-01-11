package com.example.calculadora

import java.lang.NumberFormatException

class Operations {
    companion object{
        fun getOperator(operation: String): String {
            var operator = if(operation.contains(Constants.OPERATOR_MULTI)){
                Constants.OPERATOR_MULTI
            }
            else if (operation.contains(Constants.OPERATOR_DIV)){
                Constants.OPERATOR_DIV
            }
            else if (operation.contains(Constants.OPERATOR_SUM)){
               Constants.OPERATOR_SUM
            }
            else{
                Constants.OPERATOR_NULL
            }
            if (operator == Constants.OPERATOR_NULL && operation.lastIndexOf(Constants.OPERATOR_SUB) > 0 ){
                operator = Constants.OPERATOR_SUB
            }
            return operator
        }
        fun canRepalceOperator(charSequence: CharSequence): Boolean {
            if (charSequence.length < 2) return false

            val lastElement = charSequence[charSequence.length-1].toString()
            val penultimateElement = charSequence[charSequence.length-2].toString()

            return (lastElement == Constants.OPERATOR_MULTI ||
                    lastElement == Constants.OPERATOR_DIV ||
                    lastElement == Constants.OPERATOR_SUM) &&
                    (penultimateElement == Constants.OPERATOR_MULTI ||
                            penultimateElement == Constants.OPERATOR_DIV ||
                            penultimateElement == Constants.OPERATOR_SUM ||
                            penultimateElement == Constants.OPERATOR_SUB)
        }
         fun tryResolve(operationRef: String, isFromResolve: Boolean, listener: OnResolveListener) {
            if (operationRef.isEmpty()) return

            var operation = operationRef

            if (operation.contains(Constants.POINT) && operation.lastIndexOf(Constants.POINT) == operation.length - 1){
                operation = operation.substring(0,operation.length -1)
            }

            val operator = Operations.getOperator(operation)

            val values = divideOperation(operator, operation)



            if (values.size > 1){
                try {
                    val numOne = values[0]!!.toDouble()
                    val numTwo = values[1]!!.toDouble()

                    listener.onShowResult(getResult(numOne,operator,numTwo))
                }catch (e: NumberFormatException){
                    if (isFromResolve) listener.onShowMessage(R.string.message_num_incorrect)
                }
            }else{
                if (isFromResolve && operator!= Constants.OPERATOR_NULL)
                    listener.onShowMessage(R.string.expres_messge_incorrect)
            }
        }

        fun divideOperation(operator: String, operation: String): Array<String?> {
            var values = arrayOfNulls<String>(0)
            if (operator  != Constants.OPERATOR_NULL){
                if (operator == Constants.OPERATOR_SUB){
                    val index = operation.lastIndexOf(Constants.OPERATOR_SUB)
                    if (index < operation.length-1){
                        values = arrayOfNulls<String>(2)

                        values[0] = operation.substring(0, index)
                        values[1] = operation.substring(index+1)

                    }else{
                        values = arrayOfNulls<String>(1)
                        values[0] = operation.substring(0, index)
                    }
                }
                else{
                    values = operation.split(operator).dropLastWhile { it == "" }.toTypedArray()
                }
            }
            return values

        }

        fun getResult(numOne:Double, operator:String, numTwo:Double): Double{
            var result = 0.0

            return when(operator){
                Constants.OPERATOR_MULTI ->  numOne * numTwo
                Constants.OPERATOR_DIV -> numOne / numTwo
                Constants.OPERATOR_SUM -> numOne + numTwo
                //Constants.OPERATOR_SUB -> result = numOne - numTwo
                else -> numOne - numTwo
            }


        }
    }
}