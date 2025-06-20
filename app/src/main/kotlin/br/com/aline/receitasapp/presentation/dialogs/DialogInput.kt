package br.com.aline.receitasapp.presentation.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import br.com.aline.receitasapp.R
import br.com.aline.receitasapp.databinding.DialogBinding

class DialogInput : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = arguments?.getString(TITLE)
        val placeholder = arguments?.getString(PLACEHOLDER)
        val initialValue = arguments?.getString(INITIAL_VALUE)
        val isReceita = arguments?.getBoolean(IS_RECEITA)
        var imageUri: Uri? = null
        val binding = DialogBinding.inflate(requireActivity().layoutInflater)

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if(uri != null) {
                imageUri = uri
                binding.tvImagePath.append(DocumentFile.fromSingleUri(requireContext(),uri)?.name)
            }
        }

        binding.apply {
            dialogTitle.text = title
            dialogInput.hint = placeholder
            etInput.setText(initialValue)

            if(isReceita == true) {
                tvImagePath.isVisible = true
                capaReceitaPicker.isVisible = true
                capaReceitaPicker.setOnClickListener {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            }
        }
        val dialog = AlertDialog.Builder(requireActivity())
            .setView(binding.root)
            .setPositiveButton(getString(R.string.confirmar),null)
            .setNegativeButton(getString(R.string.cancelar)) { _,_ ->
                dismiss()
            }.create()
        dialog.setOnShowListener {
            val confirm = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            confirm.setOnClickListener {
                if(binding.etInput.text?.isBlank() == true) {
                    binding.dialogInput.error = getString(R.string.titulo_obrigatorio)
                } else {
                    binding.dialogInput.error = null
                    setFragmentResult(
                        FRAGMENT_RESULT,
                        bundleOf(INPUT_VALUE to binding.etInput.text.toString(), IMAGE_URI to (imageUri?.toString() ?: ""))
                    )
                    dismiss()
                }
            }
        }

        return dialog
    }

    companion object {
        const val FRAGMENT_RESULT = "FRAGMENT_RESULT"
        const val PLACEHOLDER = "PLACEHOLDER"
        const val TITLE = "TITLE"
        const val INPUT_VALUE = "INPUT_VALUE"
        const val IMAGE_URI = "IMAGE_URI"
        const val INITIAL_VALUE = "INITIAL_VALUE"
        const val IS_RECEITA = "IS_RECEITA"

        fun show(title: String, placeholder: String, fragmentManager: FragmentManager,initialValue: String?=null,
                 isReceita: Boolean=false) {
            DialogInput().apply {
                arguments = bundleOf(
                    TITLE to title,
                    PLACEHOLDER to placeholder,
                    INITIAL_VALUE to initialValue,
                    IS_RECEITA to isReceita
                )
           }.show(fragmentManager,DialogInput::class.simpleName.toString())
        }
    }
}