package com.example.renzo_steven_medina_seccioncurso

import android.R
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.renzo_steven_medina_seccioncurso.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {
    private  lateinit var viewBinding: ActivityMainBinding
    private var numberOutput:EditText?=null
    private var numberResult:EditText?=null
    private val NAME_SHARE:String = "decimal"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val categorias = arrayOf("Decimal", "Binario")

        val numberInput = viewBinding.edNumberIn.text
        numberOutput = viewBinding.edNumberOut
        numberResult = viewBinding.edNumberResult

        val adapter: ArrayAdapter<Any?> =
            ArrayAdapter<Any?>(this, R.layout.simple_spinner_item, categorias)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)

        viewBinding.spConvertidor.setAdapter(adapter)

        createChannel()
        viewBinding.btConvertir.setOnClickListener {
            when(adapter.getPosition(viewBinding.spConvertidor.selectedItem)){
                0->{
                    convertidorDecimal(numberInput.toString().toInt())
                    numberInput.clear()
                    createNotification("Convirtiendo.....","Binarios a Decimales")
                }
                1->{
                    convertidorBinario(numberInput.toString().toInt())
                    numberInput.clear()
                    createNotification("Convirtiendo.....", "Decimales a Binarios")
                }
                else->{
                    Toast.makeText(this,"No hay valores a convertir",Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewBinding.btPlus.setOnClickListener {
            createNotification("Sumando......","Se ha restado numeros tanto binarios como decimales")
            Toast.makeText(this,"Sumando...",Toast.LENGTH_SHORT).show()
            when(adapter.getPosition(viewBinding.spConvertidor.selectedItem)){
                0->{
                    plusDecimal()
                }
                1->{
                    plusBinario()
                }
                else->{
                Toast.makeText(this,"No se ejecuto ninguna acción",Toast.LENGTH_SHORT).show()
                }
            }

        }
        viewBinding.btMinus.setOnClickListener {
            createNotification("Restando.....", "Se ha restado numeros tanto binarios como decimales")
            Toast.makeText(this,"Restando...",Toast.LENGTH_SHORT).show()
            when(adapter.getPosition(viewBinding.spConvertidor.selectedItem)){
                0->{
                    minusDecimal()
                }
                1->{
                    minusBinario()
                }
                else->{
                    Toast.makeText(this,"No se ejecuto ninguna acción",Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewBinding.btResult.setOnClickListener {
            when(adapter.getPosition(viewBinding.spConvertidor.selectedItem)){
                0->{
                    totalDecimal()
                }
                1->{
                    totalBinario()
                }
            }
        }

        viewBinding.btClear.setOnClickListener {
            inputClear()
            numberInput.clear()
            val pref = getSharedPreferences(NAME_SHARE,Context.MODE_PRIVATE)
            with(pref.edit()){
                clear()
                apply()
            }
        }
    }

    //Functions
    private fun convertidorDecimal( valor:Int){
        val result = Integer.parseInt(valor.toString(),2)
        numberOutput?.hint = result.toString()
        val pref = getSharedPreferences(NAME_SHARE,Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("x_decimal", result.toString())
        editor.apply()
    }

    private  fun convertidorBinario(valor:Int){
        val valorBinario = Integer.toBinaryString(valor)
        val pref = getSharedPreferences(NAME_SHARE,Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("x_binario", valorBinario.toString())
        editor.apply()
        numberOutput?.hint = valorBinario

    }

    private fun inputClear(){
        numberResult?.hint = "0"
        numberOutput?.hint = "0"
    }

    private fun plusDecimal(){
        val pref= getSharedPreferences(NAME_SHARE, Context.MODE_PRIVATE)
        val x_decimal = pref.getString("x_decimal","")
        numberResult?.hint= x_decimal
        numberOutput?.hint = "0"
        with(pref.edit()){
            putString("y_decimal",x_decimal)
            putString("x_decimal","")
            putString("operation_decimal","plus")
            apply()
        }

    }
    private fun plusBinario(){
        val pref= getSharedPreferences(NAME_SHARE, Context.MODE_PRIVATE)
        val x_binario = pref.getString("x_binario","")
        numberResult?.hint= x_binario
        numberOutput?.hint = "0"
        with(pref.edit()){
            putString("y_binario",x_binario)
            putString("x_binario","")
            putString("operation_binario","plus")
            apply()
        }
    }
    private fun minusBinario(){
        val pref= getSharedPreferences(NAME_SHARE, Context.MODE_PRIVATE)
        val x_binario= pref.getString("x_binario","")
        numberResult?.hint= x_binario
        numberOutput?.hint = "0"
        with(pref.edit()){
            putString("y_binario",x_binario)
            putString("x_binario","")
            putString("operation_binario","minus")
            apply()
        }
    }
    private fun minusDecimal(){
        val pref= getSharedPreferences(NAME_SHARE, Context.MODE_PRIVATE)
        val x_decimal = pref.getString("x_decimal","")
        numberResult?.hint= x_decimal
        numberOutput?.hint = "0"
        with(pref.edit()){
            putString("y_decimal",x_decimal)
            putString("x_decimal","")
            putString("operation_decimal","minus")
            apply()
        }
    }

    private fun totalDecimal(){
        val pref= getSharedPreferences(NAME_SHARE, Context.MODE_PRIVATE)
        val x_decimal = pref.getString("x_decimal","")
        val y_decimal = pref.getString("y_decimal","")
        var resultPlus = 0
        when(pref.getString("operation_decimal","")){
            "plus"->{
                resultPlus = x_decimal.toString().toInt() +  y_decimal.toString().toInt()
            }
            "minus"->{
                resultPlus = x_decimal.toString().toInt() -  y_decimal.toString().toInt()
            }
            else->{
                Toast.makeText(this,"Los campos están vacios",Toast.LENGTH_SHORT).show()
            }
        }
        with(pref.edit()){
            putString("y_decimal",resultPlus.toString())
            putString("x_decimal","")
            apply()
        }
        numberOutput?.hint = "0"
        numberResult?.hint = resultPlus.toString()
        createNotification("Total Decimal", "EL valor de los numeros decimales sumados son $resultPlus")
    }


    private fun totalBinario(){
        val pref= getSharedPreferences(NAME_SHARE, Context.MODE_PRIVATE)
        val x_binairo:String = pref.getString("x_binario","").toString()
        val y_binario:String = pref.getString("y_binario","").toString()
        var resultPlus = 0
        when(pref.getString("operation_binario","")){
            "plus"->{
                resultPlus = plusBinary(x_binairo, y_binario).toInt()
            }
            "minus"->{
                resultPlus = minusBinary(x_binairo, y_binario).toInt()
            }
            else->{
                Toast.makeText(this,"Los campos están vacios",Toast.LENGTH_SHORT).show()
            }
        }
        with(pref.edit()){
            putString("y_decimal",resultPlus.toString())
            putString("x_decimal","")
            apply()
        }
        numberOutput?.hint = "0"
        numberResult?.hint = resultPlus.toString()
        createNotification("Total Binario", "EL valor de los numeros binarios sumados son $resultPlus")
    }

    //function operations of binary
    private fun plusBinary(field01: String, field02: String): String {

        val maxLength = maxOf(field01.length, field02.length)
        val paddedBin1 = field01.padStart(maxLength, '0')
        val paddedBin2 = field02.padStart(maxLength, '0')

        var carry = 0
        var result = ""

        for (i in maxLength - 1 downTo 0) {
            val bit1 = paddedBin1[i].toString().toInt()
            val bit2 = paddedBin2[i].toString().toInt()
            val plus = bit1 + bit2 + carry
            result = (plus % 2).toString() + result
            carry = plus / 2
        }
        if (carry != 0) {
            result = carry.toString() + result
        }
        return result
    }
    private fun minusBinary(field01: String, field02: String): String {

        val maxLength = maxOf(field01.length, field02.length)
        val paddedBin1 = field01.padStart(maxLength, '0')
        val paddedBin2 = field02.padStart(maxLength, '0')

        var carry = 0
        var result = ""

        for (i in maxLength - 1 downTo 0) {
            val bit1 = paddedBin1[i].toString().toInt()
            val bit2 = paddedBin2[i].toString().toInt()
            var minus = bit1 - bit2 - carry

            if(minus <0){
                minus +=2
                carry = 1
            }else{
                carry = 0
            }
        }
        result = result.trimStart('0')
        if(result.isEmpty()){
            result = "0"
        }
        return result
    }

    @SuppressLint("MissingPermission")
    private fun createNotification(texto:String, content:String){
        val builder = NotificationCompat.Builder(this, MYCHANEL )
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Convertidor")
            .setContentText(texto)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)){
            notify(1, builder.build())
        }
    }
    private fun createChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                MYCHANEL,
                "MyChannelSuper",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "TAREA S8 - HERPM1305"
            }
            val notificacionManager :NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificacionManager.createNotificationChannel(channel)
        }
    }
    companion object{
        const val MYCHANEL = "myChannel"
    }
}