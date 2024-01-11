package com.example.notas

import java.lang.NumberFormatException

fun main(){
    try {

        println("Dame el numero 1: ")
        val n1 = readln().toDouble()
        println("Dame el numero 2: ")
        val n2 = readln().toDouble()
        val res = n1 + n2
        println(res)

    }catch (e:NumberFormatException){
        println("Datos incorrectos $e")

    }

    while ()
}