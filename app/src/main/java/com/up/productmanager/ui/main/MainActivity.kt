package com.up.productmanager.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.up.productmanager.data.model.ProductCreate
import com.up.productmanager.ui.main.ProductViewModel
import com.up.productmanager.R

class MainActivity : AppCompatActivity() {

    private val viewModel: ProductViewModel by viewModels()
    private lateinit var addBtn : FloatingActionButton
    private lateinit var deleteBtn : FloatingActionButton
    private lateinit var listContainer: LinearLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addBtn = findViewById<FloatingActionButton>(R.id.btnAdd)
        deleteBtn = findViewById<FloatingActionButton>(R.id.btnDelete)
        listContainer = findViewById<LinearLayout>(R.id.sampleList)


        // Observa mensajes (éxito / error)
        viewModel.message.observe(this, Observer { msg ->
            Log.i("API_MESSAGE", msg)
        })


        //----BOTONES------------
        //Add botón
        addBtn.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
            overridePendingTransition(0, 0)
        }

        deleteBtn.setOnClickListener {
            val selectedIds = mutableListOf<Int>()

            for (i in 0 until listContainer.childCount) {
                val card = listContainer.getChildAt(i)
                val cb = card.findViewById<CheckBox>(R.id.cbSelect) ?: continue
                if (cb.isChecked) {
                    // tag is Int (your Product.id)
                    val id = cb.getTag(R.id.tag_product_id) as? Int
                    if (id != null) selectedIds.add(id)
                }
            }

            if (selectedIds.isEmpty()) {
                Toast.makeText(this, "No products selected", Toast.LENGTH_SHORT).show()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Delete selected")
                    .setMessage("Delete ${selectedIds.size} product(s)?")
                    .setPositiveButton("Delete") { _, _ ->
                        selectedIds.forEach { viewModel.deleteProduct(it) }
                        viewModel.loadProducts()
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        }


        // Cargar productos y mostrarlos
        viewModel.products.observe(this) { renderProducts(it) }

    }

    override fun onResume(){
        super.onResume()
        viewModel.loadProducts()
    }
    private fun renderProducts(lista: List<com.up.productmanager.data.model.Product>) {
        listContainer.removeAllViews()

        val inflater = LayoutInflater.from(this)
        lista.forEach { p ->

            //Crear ProductCard
            val card = inflater.inflate(R.layout.item_product_card, listContainer, false)

            // Poner texto
            val tv = card.findViewById<TextView>(R.id.tvProduct)
            tv.text = "${p.nombre} — $${"%.2f".format(p.precio)} — ${p.cantidad_disponible} pcs"

            // Agregar id del producto al tag del checkbox del Product Card
            card.findViewById<CheckBox>(R.id.cbSelect).setTag(R.id.tag_product_id, p.id)

            //Agregar producto a la lista
            listContainer.addView(card)
        }
    }
}