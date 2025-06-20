package br.com.aline.receitasapp.domain.model

data class ReceitaCompletaDTO(
    val receitaDTO: ReceitaDTO,
    val ingredientes: List<IngredienteDTO>,
    val modoDePreparoDTO: ModoDePreparoDTO?
)
