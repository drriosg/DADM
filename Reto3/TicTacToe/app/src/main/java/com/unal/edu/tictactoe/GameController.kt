package com.unal.edu.tictactoe

class GameController {

    enum class EstadoPartida{
        JUGADOR1_GANO, JUGADOR2_GANO, EMPATE, NO_TERMINO
    }
    private lateinit var tablero: Array<Array<Char>>

    fun nuevoTablero(): Array<Array<Char>>{
        tablero = Array(size = 3, { Array(size = 3, {'-'}) })
        return tablero
    }

    fun estadoPartida(turniJugador1: Boolean): EstadoPartida{
        if(turniJugador1){
            val fila1 = tablero[0][0] == 'O' && tablero[0][1] == 'O' && tablero[0][2] == 'O'
            val fila2 = tablero[1][0] == 'O' && tablero[1][1] == 'O' && tablero[1][2] == 'O'
            val fila3 = tablero[2][0] == 'O' && tablero[2][1] == 'O' && tablero[2][2] == 'O'
            val columna1 = tablero[0][0] == 'O' && tablero[1][0] == 'O' && tablero[2][0] == 'O'
            val columna2 = tablero[0][1] == 'O' && tablero[1][1] == 'O' && tablero[2][1] == 'O'
            val columna3 = tablero[0][2] == 'O' && tablero[1][2] == 'O' && tablero[2][2] == 'O'
            val diag1 = tablero[0][0] == 'O' && tablero[1][1] == 'O' && tablero[2][2] == 'O'
            val diag2 = tablero[0][2] == 'O' && tablero[1][1] == 'O' && tablero[2][0] == 'O'
            if(fila1 || fila2 || fila3 || columna1 || columna2 || columna3 || diag1 || diag2){
                return EstadoPartida.JUGADOR1_GANO
            } else if (checkTableroLleno()){
                return EstadoPartida.EMPATE
            } else {
                return EstadoPartida.NO_TERMINO
            }
        } else {
            val fila1 = tablero[0][0] == 'X' && tablero[0][1] == 'X' && tablero[0][2] == 'X'
            val fila2 = tablero[1][0] == 'X' && tablero[1][1] == 'X' && tablero[1][2] == 'X'
            val fila3 = tablero[2][0] == 'X' && tablero[2][1] == 'X' && tablero[2][2] == 'X'
            val columna1 = tablero[0][0] == 'X' && tablero[1][0] == 'X' && tablero[2][0] == 'X'
            val columna2 = tablero[0][1] == 'X' && tablero[1][1] == 'X' && tablero[2][1] == 'X'
            val columna3 = tablero[0][2] == 'X' && tablero[1][2] == 'X' && tablero[2][2] == 'X'
            val diag1 = tablero[0][0] == 'X' && tablero[1][1] == 'X' && tablero[2][2] == 'X'
            val diag2 = tablero[0][2] == 'X' && tablero[1][1] == 'X' && tablero[2][0] == 'X'
            if(fila1 || fila2 || fila3 || columna1 || columna2 || columna3 || diag1 || diag2){
                return EstadoPartida.JUGADOR2_GANO
            } else if (checkTableroLleno()){
                return EstadoPartida.EMPATE
            } else {
                return  EstadoPartida.NO_TERMINO
            }
        }
    }

    private fun checkTableroLleno(): Boolean{
        for (i in tablero.indices){
            for (j in tablero.indices){
                if(tablero[i][j] == '-'){
                    return false
                }
            }
        }
        return true
    }
}