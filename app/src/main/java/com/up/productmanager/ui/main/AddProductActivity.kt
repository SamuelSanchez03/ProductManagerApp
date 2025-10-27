package com.up.productmanager.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.up.productmanager.R
import androidx.activity.viewModels
import com.up.productmanager.data.model.ProductCreate

class AddProductActivity : AppCompatActivity() {

    private val viewModel: ProductViewModel by viewModels()

    private lateinit var saveBtn : Button
    private lateinit var nameTxt : EditText
    private lateinit var priceTxt : EditText
    private lateinit var qtyTxt : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        saveBtn = findViewById<Button>(R.id.btnSave)
        nameTxt = findViewById<EditText>(R.id.txtName)
        priceTxt = findViewById<EditText>(R.id.txtPrice)
        qtyTxt = findViewById<EditText>(R.id.txtQty)


        val scrim = findViewById<View>(R.id.scrim)
        val card = findViewById<View>(R.id.popupCard)

        //Animación de entrada
        scrim.alpha = 0f
        card.alpha = 0f
        card.scaleX = 0.98f
        card.scaleY = 0.98f
        card.translationY = 20f  // px is fine for a tiny drift

        scrim.animate().alpha(1f).setDuration(150).start()
        card.animate().alpha(1f).scaleX(1f).scaleY(1f).translationY(0f).setDuration(150).start()

        //Animación de salida (fade out smoothly)
        val close: (View) -> Unit = {
            scrim.animate().alpha(0f).setDuration(150).start()
            card.animate()
                .alpha(0f).scaleX(0.98f).scaleY(0.98f).translationY(10f)
                .setDuration(150)
                .withEndAction {
                    finish()
                }.start()
        }

        //Close botón
        findViewById<View>(R.id.btnClose).setOnClickListener(close)

        //Add botón -----------------
        saveBtn.setOnClickListener{

            //Obtener input
            val name = nameTxt.text.toString()
            val priceStr = priceTxt.text.toString()
            val qtyStr = qtyTxt.text.toString()


            //Validar que los campos no estén vacíos
            if(name.isNotEmpty() && priceStr.isNotEmpty() && qtyStr.isNotEmpty()){
                val price = priceStr.toDouble()
                val qty = qtyStr.toInt()

                //crear nuevo producto
                viewModel.createProduct(ProductCreate(name, price, qty))
                Toast.makeText(this, "Product added successfully!", Toast.LENGTH_SHORT).show()
                close(it) //regresar a MainActivity
            }else{
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}