package br.com.aline.receitasapp.presentation.receitaDetalhe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.aline.receitasapp.R
import br.com.aline.receitasapp.databinding.ItemPassoPreparoBinding
import br.com.aline.receitasapp.presentation.EventType
import br.com.aline.receitasapp.presentation.dialogs.DialogInput

class PassosModoPreparoAdapter(
    val context: Fragment, val checkboxStates: HashMap<Int, Boolean>, val onClick:(EventType, String) ->
Unit) :
    ListAdapter<String ,PassosModoPreparoAdapter.ViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassosModoPreparoAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPassoPreparoBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PassosModoPreparoAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemPassoPreparoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.passo.text = item
            binding.passo.isChecked = checkboxStates.get(adapterPosition) ?: false
            binding.passo.setOnCheckedChangeListener { _, isChecked ->
                checkboxStates[adapterPosition] = isChecked
            }

            binding.editPasso.setOnClickListener {
                DialogInput.show(
                    context.getString(R.string.editar_passo),
                    context.getString(R.string.descricao),
                    context.parentFragmentManager,
                    item
                )
                onClick(EventType.EDIT,item)
            }

            binding.deletePasso.setOnClickListener {
                onClick(EventType.DELETE,item)
            }
        }
    }
}

class DiffCallback: DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(p0: String, p1: String) = p0 == p1
    override fun areContentsTheSame(p0: String, p1: String) = p0.contentEquals(p1)
}