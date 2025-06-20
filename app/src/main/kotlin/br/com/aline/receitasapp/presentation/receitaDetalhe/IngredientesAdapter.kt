package br.com.aline.receitasapp.presentation.receitaDetalhe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.aline.receitasapp.R
import br.com.aline.receitasapp.databinding.ItemIngredienteBinding
import br.com.aline.receitasapp.domain.model.IngredienteDTO
import br.com.aline.receitasapp.presentation.EventType
import br.com.aline.receitasapp.presentation.dialogs.DialogInput

class IngredientesAdapter(val context: Fragment,val onClick:(EventType, IngredienteDTO) -> Unit) :
    ListAdapter<IngredienteDTO,
        IngredientesAdapter.ViewHolder>
    (DiffIngredienteCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientesAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemIngredienteBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientesAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemIngredienteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: IngredienteDTO) {
            binding.ingredienteNome.text = item.nome
            binding.editIngrediente.setOnClickListener {
                DialogInput.show(
                    context.getString(R.string.editar_ingrediente),
                    context.getString(R.string.nome),
                    context.parentFragmentManager,
                    item.nome
                )
                onClick(EventType.EDIT,item)
            }

            binding.deleteIngrediente.setOnClickListener {
                onClick(EventType.DELETE,item)
            }
        }
    }
}

class DiffIngredienteCallback: DiffUtil.ItemCallback<IngredienteDTO>() {
    override fun areItemsTheSame(p0: IngredienteDTO, p1: IngredienteDTO) = p0.id == p1.id
    override fun areContentsTheSame(p0: IngredienteDTO, p1: IngredienteDTO) = p0.nome == p1.nome
}