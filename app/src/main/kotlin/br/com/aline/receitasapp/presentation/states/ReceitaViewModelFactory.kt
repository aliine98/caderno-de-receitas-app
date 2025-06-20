package br.com.aline.receitasapp.presentation.states

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import br.com.aline.receitasapp.data.repository.ReceitaRepositoryImpl
import br.com.aline.receitasapp.extensions.db

class ReceitaViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val app = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
        val repo = ReceitaRepositoryImpl(app.db.receitaDAO())
        return ReceitasViewModel(repo) as T
    }
}