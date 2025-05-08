package com.example.nubank

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nubank.models.Llave

class LlaveAdapter(
    private val llaves: List<Llave>,
    private val onClick: (Llave) -> Unit
) : RecyclerView.Adapter<LlaveAdapter.LlaveViewHolder>() {

    inner class LlaveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtLlave = itemView.findViewById<TextView>(R.id.txtLlave)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LlaveViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_llave, parent, false)
        return LlaveViewHolder(view)
    }

    override fun onBindViewHolder(holder: LlaveViewHolder, position: Int) {
        val llave = llaves[position]
        holder.txtLlave.text = "${llave.tipo}: ${llave.valor}"
        holder.itemView.setOnClickListener { onClick(llave) }
    }

    override fun getItemCount(): Int = llaves.size
}
