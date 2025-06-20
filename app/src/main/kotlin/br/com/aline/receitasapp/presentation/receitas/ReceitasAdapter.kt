package br.com.aline.receitasapp.presentation.receitas

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.aline.receitasapp.R
import br.com.aline.receitasapp.databinding.ItemReceitaBinding
import br.com.aline.receitasapp.domain.model.ReceitaDTO
import br.com.aline.receitasapp.presentation.EventType
import br.com.aline.receitasapp.presentation.dialogs.DialogInput

class ReceitasAdapter(val context: Fragment,private val onClick: (EventType, ReceitaDTO) -> Unit) :
    ListAdapter<ReceitaDTO, ReceitasAdapter
    .ViewHolder>
    (DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceitasAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemReceitaBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReceitasAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            onClick(EventType.VIEW,getItem(position))
        }
    }

    inner class ViewHolder(private val binding: ItemReceitaBinding) :RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ReceitaDTO) {
            binding.tvTitulo.text = item.titulo
            binding.capa.setImageBitmap(item.capa)

            binding.editReceita.setOnClickListener {
                DialogInput.show(
                    context.getString(R.string.editar_receita),
                    context.getString(R.string.titulo_receita),
                    context.parentFragmentManager,
                    item.titulo,
                    isReceita = true
                )
                onClick(EventType.EDIT,item)
            }

            binding.deleteReceita.setOnClickListener {
                onClick(EventType.DELETE,item)
            }
        }
    }
}

class DiffCallback: DiffUtil.ItemCallback<ReceitaDTO>() {
    override fun areItemsTheSame(p0: ReceitaDTO, p1: ReceitaDTO) = p0 == p1

    override fun areContentsTheSame(p0: ReceitaDTO, p1: ReceitaDTO) = p0.id == p1.id
}