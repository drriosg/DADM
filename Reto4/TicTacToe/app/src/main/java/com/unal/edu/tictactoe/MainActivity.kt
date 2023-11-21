package com.unal.edu.tictactoe

import android.app.GameManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.service.autofill.FillCallback
import android.view.ContextMenu
import android.view.View
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
    private lateinit var reinciarPartidaBtn: AppCompatButton
    private var UserWins = 0
    private var AndroidWins = 0
    private var Empates = 0
    private lateinit var userWinsView: TextView
    private lateinit var androidWinsView: TextView
    private lateinit var empatesView: TextView
    private lateinit var dificultyBtn: AppCompatButton
    private lateinit var exitBtn: AppCompatButton
    private lateinit var dificultadTextView: TextView
    private lateinit var successSound: MediaPlayer
    private lateinit var errorSound: MediaPlayer
    private lateinit var winSound: MediaPlayer
    private lateinit var startSoud: MediaPlayer
    val handler = Handler(Looper.getMainLooper())

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
        dificultyBtn = findViewById(R.id.DificultyBtn)
        exitBtn = findViewById(R.id.ExitBtn)
        gameController.setDifficultyLevel(GameController.NivelesDificultad.DIFICIL)
        dificultadTextView = findViewById(R.id.dificultadTextview)
        successSound = MediaPlayer.create(this, R.raw.success)
        errorSound = MediaPlayer.create(this, R.raw.error)
        winSound = MediaPlayer.create(this, R.raw.win)
        startSoud = MediaPlayer.create(this, R.raw.start)
    }

    private fun incializarPartida(nivelDificultad: GameController.NivelesDificultad = GameController.NivelesDificultad.DIFICIL){
        gameController.setDifficultyLevel(nivelDificultad)
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
        dificultadTextView.setText("Dificultad: " + gameController.getDifficultyLevel())
    }

    private fun showExitDialog (){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Saliendo del Juego")
        alertDialog.setMessage("Seguro que desea salir ?")
        alertDialog.setPositiveButton("Si") { dialog, which ->
            finish()
        }
        alertDialog.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }
        val dialog = alertDialog.create()
        dialog.show()
    }

    private fun showConfirmationMessage(message: String) {
        showToast(message)
    }

    private fun showToast(message: String) {
        // Lógica para mostrar un Toast
    }

    private fun dificultyDialog (){
        val difficulties = arrayOf(
            GameController.NivelesDificultad.FACIL,
            GameController.NivelesDificultad.INTERMEDIO,
            GameController.NivelesDificultad.DIFICIL
        )

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Selecciona la dificultad")
            .setItems(difficulties.map { it.name }.toTypedArray()) { dialog, which ->
                val selectedDifficulty = difficulties[which]
                when (selectedDifficulty) {
                    GameController.NivelesDificultad.FACIL -> {
                        incializarPartida(GameController.NivelesDificultad.FACIL)
                    }
                    GameController.NivelesDificultad.INTERMEDIO -> {
                        incializarPartida(GameController.NivelesDificultad.INTERMEDIO)
                    }
                    GameController.NivelesDificultad.DIFICIL -> {
                        incializarPartida(GameController.NivelesDificultad.DIFICIL)
                    }
                }
                incializarPartida(selectedDifficulty)
                showConfirmationMessage("Has seleccionado: ${selectedDifficulty.name}")
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                // Código para manejar la cancelación
                dialog.dismiss()
            }

        val dialog = builder.create()
        dialog.show()
    }

    private fun initListeners(){
        reinciarPartidaBtn.setOnClickListener {
            successSound.start()
            incializarPartida()

        }

        dificultyBtn.setOnClickListener {
            successSound.start()
            dificultyDialog()
        }

        exitBtn.setOnClickListener{
            errorSound.start()
            showExitDialog()
        }

        for(i in 0 until tableroLayout.childCount){
            val fila = tableroLayout[i] as LinearLayout
            for (j in 0 until fila.childCount){
                val ficha = fila[j] as ImageView
                    ficha.setOnClickListener{
                    startSoud.start()
                    if(!gameOver &&  tablero[i][j] == '-'){
                        setFichaEnTablero(i, j)
                        val estadoPartida = gameController.estadoPartida(turnoJugador1)
                        if(turnoJugador1 && estadoPartida == GameController.EstadoPartida.JUGADOR1_GANO){
                            showGameOverDialog("El Jugador 1 Ganó!")
                            UserWins += 1
                            winSound.start()
                            actualizarEstadisticas()
                            gameOver = true
                        } else if(!turnoJugador1 && estadoPartida == GameController.EstadoPartida.JUGADOR2_GANO){
                            showGameOverDialog("Android Ganó!")
                            AndroidWins =+ 1
                            errorSound.start()
                            actualizarEstadisticas()
                            gameOver = true
                        } else if(estadoPartida == GameController.EstadoPartida.EMPATE){
                            showGameOverDialog("Empate!")
                            Empates += 1
                            errorSound.start()
                            actualizarEstadisticas()
                            gameOver = true
                        } else {
                            turnoJugador1 = !turnoJugador1
                            handler.postDelayed({
                                movimientoMaquina()
                            }, 500)
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
        startSoud.start()
        val casillasLibres = mutableListOf<Pair<Int, Int>>()

        // Recorre el tablero para encontrar casillas libres
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                if (tablero[i][j] == '-') {
                    casillasLibres.add(Pair(i, j))
                }
            }
        }

        if(gameController.getDifficultyLevel() == GameController.NivelesDificultad.FACIL){



        } else if (gameController.getDifficultyLevel() == GameController.NivelesDificultad.INTERMEDIO){

            // Verifica si el jugador humano puede ganar en el próximo movimiento y bloquea
            for ((i, j) in casillasLibres) {
                tablero[i][j] = 'X'
                val estadoPartida = gameController.estadoPartida(true) // El segundo argumento indica que es el turno del jugador humano
                tablero[i][j] = '-'

                if (estadoPartida == GameController.EstadoPartida.JUGADOR1_GANO) {
                    setFichaEnTablero(i, j)
                    turnoJugador1 = !turnoJugador1
                    val estadoPartidaDespues = gameController.estadoPartida(false)
                    if (estadoPartidaDespues == GameController.EstadoPartida.JUGADOR2_GANO) {
                        showGameOverDialog("El Jugador 2 Ganó!")
                        winSound.start()
                        AndroidWins += 1
                        actualizarEstadisticas()
                    }
                    return
                }
            }

            // Verifica si la máquina puede ganar en el próximo movimiento
            for ((i, j) in casillasLibres) {
                tablero[i][j] = 'O'
                val estadoPartida = gameController.estadoPartida(false) // El segundo argumento indica que es el turno de la máquina
                tablero[i][j] = '-'

                if (estadoPartida == GameController.EstadoPartida.JUGADOR2_GANO) {
                    setFichaEnTablero(i, j)
                    turnoJugador1 = !turnoJugador1
                    val estadoPartidaDespues = gameController.estadoPartida(false)
                    if (estadoPartidaDespues == GameController.EstadoPartida.JUGADOR2_GANO) {
                        showGameOverDialog("El Jugador 2 Ganó!")
                        winSound.start()
                        AndroidWins += 1
                        actualizarEstadisticas()
                    }
                    return
                }
            }

        } else if (gameController.getDifficultyLevel() == GameController.NivelesDificultad.DIFICIL){

            for ((i, j) in casillasLibres) {
                tablero[i][j] = 'O'
                val estadoPartida = gameController.estadoPartida(false) // El segundo argumento indica que es el turno de la máquina
                tablero[i][j] = '-'

                if (estadoPartida == GameController.EstadoPartida.JUGADOR2_GANO) {
                    setFichaEnTablero(i, j)
                    turnoJugador1 = !turnoJugador1
                    val estadoPartidaDespues = gameController.estadoPartida(false)
                    if (estadoPartidaDespues == GameController.EstadoPartida.JUGADOR2_GANO) {
                        showGameOverDialog("El Jugador 2 Ganó!")
                        winSound.start()
                        AndroidWins += 1
                        actualizarEstadisticas()
                    }
                    return
                }
            }

            for ((i, j) in casillasLibres) {
                tablero[i][j] = 'X'
                val estadoPartida = gameController.estadoPartida(true) // El segundo argumento indica que es el turno del jugador humano
                tablero[i][j] = '-'

                if (estadoPartida == GameController.EstadoPartida.JUGADOR1_GANO) {
                    setFichaEnTablero(i, j)
                    turnoJugador1 = !turnoJugador1
                    val estadoPartidaDespues = gameController.estadoPartida(false)
                    if (estadoPartidaDespues == GameController.EstadoPartida.JUGADOR2_GANO) {
                        showGameOverDialog("El Jugador 2 Ganó!")
                        winSound.start()
                        AndroidWins += 1
                        actualizarEstadisticas()
                    }
                    return
                }
            }
        }



        // Si no se puede ganar ni bloquear, realiza un movimiento aleatorio
        if (casillasLibres.isNotEmpty()) {
            val (i, j) = casillasLibres.random()
            setFichaEnTablero(i, j)
            turnoJugador1 = !turnoJugador1

            val estadoPartidaDespues = gameController.estadoPartida(false)
            if (estadoPartidaDespues == GameController.EstadoPartida.JUGADOR2_GANO) {
                showGameOverDialog("El Jugador 2 Ganó!")
                winSound.start()
                AndroidWins += 1
                actualizarEstadisticas()
            }
        }
    }


    private fun setFichaEnTablero(i: Int, j: Int) {
        if (i in 0 until 3 && j in 0 until 3 && tablero[i][j] == '-') {
            tablero[i][j] = if (turnoJugador1) 'X' else 'O'
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