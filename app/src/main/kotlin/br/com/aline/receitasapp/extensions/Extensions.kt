package br.com.aline.receitasapp.extensions

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.aline.receitasapp.data.db.ReceitasDB
import br.com.aline.receitasapp.data.entity.Ingrediente
import br.com.aline.receitasapp.data.entity.ModoDePreparo
import br.com.aline.receitasapp.data.entity.Receita
import br.com.aline.receitasapp.data.entity.ReceitaCompleta
import br.com.aline.receitasapp.domain.model.IngredienteDTO
import br.com.aline.receitasapp.domain.model.ModoDePreparoDTO
import br.com.aline.receitasapp.domain.model.ReceitaCompletaDTO
import br.com.aline.receitasapp.domain.model.ReceitaDTO

fun Receita.toDTO() = ReceitaDTO(id=id, titulo=titulo, capa=capa)
fun ReceitaDTO.toEntity() = Receita(id=id,titulo=titulo, capa=capa)
fun Ingrediente.toDTO() = IngredienteDTO(id=id,nome=nome,idReceita=idReceita)
fun IngredienteDTO.toEntity() = Ingrediente(id=id,nome=nome, idReceita = idReceita)
fun ModoDePreparo.toDTO() = ModoDePreparoDTO(id=id,passos=passos,idReceita=idReceita)
fun ModoDePreparoDTO.toEntity() = ModoDePreparo(id=id,passos=passos, idReceita=idReceita)
fun ReceitaCompleta.toDTO() = ReceitaCompletaDTO(
    receitaDTO=receita.toDTO(),
    ingredientes=ingredientes.map { it.toDTO() },
    modoDePreparoDTO= modoDePreparo?.toDTO()
)
val Context.db
    get() = Room.databaseBuilder(applicationContext,ReceitasDB::class.java,"receitas_db")
        .addMigrations(MIGRATION_1_2,MIGRATION_2_3).build()

private val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `Ingrediente` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "`nome` TEXT NOT NULL, `id_receita` INTEGER NOT NULL)")
        db.execSQL("CREATE TABLE IF NOT EXISTS `ModoDePreparo` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "`passos` TEXT NOT NULL, `id_receita` INTEGER NOT NULL)")
    }
}

private val MIGRATION_2_3 = object : Migration(2,3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `Receita` ADD `capa` BLOB")
        db.execSQL("CREATE TABLE IF NOT EXISTS `Ingrediente_new` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "`nome`" +
                " TEXT NOT NULL, `id_receita` INTEGER NOT NULL, FOREIGN KEY(`id_receita`) REFERENCES `Receita`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        db.execSQL("CREATE TABLE IF NOT EXISTS `ModoDePreparo_new` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "`passos` TEXT NOT NULL, `id_receita` INTEGER NOT NULL, FOREIGN KEY(`id_receita`) REFERENCES `Receita`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        db.execSQL("INSERT INTO `Ingrediente_new` (id,nome,id_receita) SELECT id,nome,id_receita FROM Ingrediente")
        db.execSQL("DROP TABLE Ingrediente")
        db.execSQL("ALTER TABLE Ingrediente_new RENAME TO Ingrediente")
        db.execSQL("INSERT INTO `ModoDePreparo_new` (id,passos,id_receita) SELECT id,passos,id_receita FROM " +
                "ModoDePreparo")
        db.execSQL("DROP TABLE ModoDePreparo")
        db.execSQL("ALTER TABLE ModoDePreparo_new RENAME TO ModoDePreparo")
    }
}