package com.lozada.pocketu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lozada.pocketu.R
import com.lozada.pocketu.data.Usuario

class UsuarioAdapter(
    private var usuarios: List<Usuario>,
    private val onClick: (Usuario) -> Unit,
    private val onLongClick: (Usuario) -> Unit
) : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {

    class UsuarioViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val nombre: TextView = view.findViewById(R.id.txtNombre)
        val correo: TextView = view.findViewById(R.id.txtCorreo)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UsuarioViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_usuario, parent, false)

        return UsuarioViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: UsuarioViewHolder,
        position: Int
    ) {

        val usuario = usuarios[position]

        holder.nombre.text = usuario.nombre
        holder.correo.text = usuario.correo

        holder.itemView.setOnClickListener {
            onClick(usuario)
        }

        holder.itemView.setOnLongClickListener {
            onLongClick(usuario)
            true
        }
    }

    override fun getItemCount(): Int = usuarios.size

    fun actualizarLista(nuevaLista: List<Usuario>) {
        usuarios = nuevaLista
        notifyDataSetChanged()
    }
}