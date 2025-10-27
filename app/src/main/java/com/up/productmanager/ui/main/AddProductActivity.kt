package com.up.productmanager.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.up.productmanager.R

class AddProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

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
        // -> obtener input y crear nuevo producto
            // Ejemplo: Crear un producto nuevo
            //val newProduct = ProductCreate("Test", 299.99, 15)
            //viewModel.createProduct(newProduct)
        // Poner mensaje con toast que diga algo de "Product added"
        //Regresar a pantalla inicial MainActivity

    }
}