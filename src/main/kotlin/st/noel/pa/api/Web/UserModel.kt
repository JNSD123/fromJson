package st.noel.pa.api.Web

import org.example.st.noel.pa.api.Annotation.Column
import org.example.st.noel.pa.api.Annotation.Entity
import org.example.st.noel.pa.api.Annotation.Ignore
import java.time.LocalDate

@Entity(tableName = "Estudantes")
data class UserModel(
    @Column(primaryKey = true)
    val id: Int,

    @Column(name = "name")
    val name: String,

    @Column(name = "email")
    val email: String? = null,

    @Column
    val birthDate: LocalDate? = null,

    @Column(name = "identifier")
    val identificador: Number,

    @Column(name = "status")
    val status: StatusUser?,

    @Ignore
    val temporaryData: String = ""
)
enum class StatusUser {
    ATIVO, INATIVO
}
