package com.unal.edu.tictactoe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.FillCallback
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.get


class MainActivity : AppCompatActivity() {

    private val gameController = GameController()
    private lateinit var tablero:Array<Array<Char>>
    private var gameOver = false
    private var turnoJugador1 = true
    private lateinit var tableroLayout:LinearLayout
    private lateinit var reinciarPartidaBtn: androidx.appcompat.widget.AppCompatButton
    private var UserWins = 0
    private var AndroidWins = 0
    private var Empates = 0
    private lateinit var userWinsView: TextView
    private lateinit var androidWinsView: TextView
    private lateinit var empatesView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initComponets()
        initListeners()
        incializarPartida()
    }

    private fun initComponets(){
        tableroLayout = findViewById(R.id.tableroLayout)
        reinciarPartidaBtn = findViewById(R.id.StartBtn)
        userWinsView = findViewById(R.id.UserWins)
        androidWinsView = findViewById(R.id.AndroidWins)
        empatesView = findViewById(R.id.Empates)
    }

    private fun incializarPartida(){
        tablero = gameController.nuevoTablero()
        gameOver = false
        turnoJugador1 = true
        for(i in 0 until tableroLayout.childCount){
            val fila = tableroLayout[i] as LinearLayout
            for (j in 0 until fila.childCount) {
                val ficha = fila[j] as ImageView
                ficha.setImageDrawable(getDrawable(R.drawable.white))
            }
        }
    }

    private fun initListeners(){
        reinciarPartidaBtn.setOnClickListener {
            incializarPartida()
        }

        for(i in 0 until tableroLayout.childCount){
            val fila = tableroLayout[i] as LinearLayout
            for (j in 0 until fila.childCount){
                val ficha = fila[j] as ImageView
                    ficha.setOnClickListener{
                    if(!gameOver &&  tablero[i][j] == '-'){
                        setFichaEnTablero(i, j)
                        val estadoPartida = gameController.estadoPartida(turnoJugador1)
                        if(turnoJugador1 && estadoPartida == GameController.EstadoPartida.JUGADOR1_GANO){
                            showGameOverDialog("El Jugador 1 Ganó!")
                            UserWins += 1
                            actualizarEstadisticas()
                            gameOver = true
                        } else if(!turnoJugador1 && estadoPartida == GameController.EstadoPartida.JUGADOR2_GANO){
                            showGameOverDialog("Android Ganó!")
                            AndroidWins =+ 1
                            actualizarEstadisticas()
                            gameOver = true
                        } else if(estadoPartida == GameController.EstadoPartida.EMPATE){
                            showGameOverDialog("Empate!")
                            Empates += 1
                            actualizarEstadisticas()
                            gameOver = true
                        } else {
                            turnoJugador1 = !turnoJugador1
                            movimientoMaquina()
                        }
                    }
                }
            }
        }
    }

    private fun showGameOverDialog(message: String){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(message)
        alertDialog.setMessage("Desea jugar Nuevamente ?")
        alertDialog.setPositiveButton("Si") { dialog, which ->
            incializarPartida()
        }
        alertDialog.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }
        val dialog = alertDialog.create()
        dialog.show()
    }
    private fun movimientoMaquina() {
        val casillasLibres = mutableListOf<Pair<Int, Int>>()
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                if (tablero[i][j] == '-') {
                    casillasLibres.add(Pair(i, j))
                }
            }
        }

        if (casillasLibres.isNotEmpty()) {
            val (i, j) = casillasLibres.random()
            setFichaEnTablero(i, j)
            val estadoPartida = gameController.estadoPartida(turnoJugador1)

            if (!gameOver) {
                if (turnoJugador1 && estadoPartida == GameController.EstadoPartida.JUGADOR1_GANO) {
                    showGameOverDialog("El Jugador 1 Ganó!")
                    UserWins =+ 1
                    actualizarEstadisticas()
                    gameOver = true
                } else if (!turnoJugador1 && estadoPartida == GameController.EstadoPartida.JUGADOR2_GANO) {
                    showGameOverDialog("El Jugador 2 Ganó!")
                    AndroidWins += 1
                    actualizarEstadisticas()
                    gameOver = true
                } else if (estadoPartida == GameController.EstadoPartida.EMPATE) {
                    showGameOverDialog("Empate!")
                    Empates =+ 1
                    actualizarEstadisticas()
                    gameOver = true
                } else {
                    turnoJugador1 = !turnoJugador1
                }
            }
        }
    }
    private fun setFichaEnTablero(i: Int, j: Int) {
        if (i in 0 until 3 && j in 0 until 3 && tablero[i][j] == '-') {
            tablero[i][j] = if (turnoJugador1) 'O' else 'X'
            actualizarInterfazGrafica(i, j)
        }
    }
    private fun actualizarInterfazGrafica(i: Int, j: Int) {
        val fila = tableroLayout.getChildAt(i) as LinearLayout
        val casilla = fila.getChildAt(j) as ImageView
        val imagen = if (turnoJugador1) R.drawable.x_symbol_svgrepo_com else R.drawable.letter_o_svgrepo_com
        casilla.setImageResource(imagen)
    }

    private fun actualizarEstadisticas(){
        userWinsView.setText("Victorias del Usuario: " + UserWins)
        androidWinsView.setText("Victorias del a Máquina: " + AndroidWins)
        empatesView.setText("Empates: " + Empates)
    }


}