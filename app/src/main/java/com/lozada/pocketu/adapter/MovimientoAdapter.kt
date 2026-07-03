package com.lozada.pocketu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lozada.pocketu.R
import com.lozada.pocketu.data.Movimiento

class MovimientoAdapter(
    private var movimientos: List<Movimiento>,
    private val onClick: (Movimiento) -> Unit,
    private val onLongClick: (Movimiento) -> Unit
) : RecyclerView.Adapter<MovimientoAdapter.MovimientoViewHolder>() {

    class MovimientoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val descripcion: TextView = view.findViewById(R.id.txtDescripcion)
        val monto: TextView = view.findViewById(R.id.txtMonto)
        val tipo: TextView = view.findViewById(R.id.txtTipo)
        val categoria: TextView = view.findViewById(R.id.txtCategoria)
        val fecha: TextView = view.findViewById(R.id.txtFecha)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovimientoViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movimiento, parent, false)

        return MovimientoViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MovimientoViewHolder,
        position: Int
    ) {

        val movimiento = movimientos[position]

        holder.descripcion.text = movimiento.descripcion
        holder.monto.text = "$${movimiento.monto}"
        holder.tipo.text = movimiento.tipo
        holder.categoria.text = movimiento.categoria
        holder.fecha.text = movimiento.fecha

        holder.itemView.setOnClickListener {
            onClick(movimiento)
        }

        holder.itemView.setOnLongClickListener {
            onLongClick(movimiento)
            true
        }
    }

    override fun getItemCount(): Int = movimientos.size

    fun actualizarLista(nuevaLista: List<Movimiento>) {
        movimientos = nuevaLista
        notifyDataSetChanged()
    }
}