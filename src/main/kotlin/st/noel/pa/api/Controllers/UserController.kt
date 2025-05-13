package st.noel.pa.api.Controllers

import org.example.st.noel.pa.Models.JsonNumber
import org.example.st.noel.pa.Models.JsonObject
import org.example.st.noel.pa.Models.JsonString
import org.example.st.noel.pa.api.Annotation.RestController
import st.noel.pa.api.Web.StatusUser
import st.noel.pa.api.Web.UserModel
import java.time.LocalDate


@RestController(path = "/api")
class UserController {
    val users = UserModel(
            id = 1, name = "teste HTTP GET",
            email = "teste@gmail.com",
            birthDate = LocalDate.of(1992, 12, 24),
            identificador = 123,
            status = StatusUser.ATIVO
        )
        /*UserModel(
            id = 2, name = "teste1 HTTP GET",
            email = "teste1@gmail.com",
            birthDate = LocalDate.of(1997, 12, 24),
            identificador = 1452, status = StatusUser.INATIVO
        )*/
    val obj = JsonObject(
        mutableMapOf(
            "name" to JsonString(users.name),
            "email" to JsonString(users.email.toString()),
            "birthDate" to JsonString(users.birthDate.toString()),
            "identificador" to JsonNumber(users.identificador),
            "status" to JsonString(users.status.toString())
        )
    )

    fun getAllUsers(){
        //val listUser = List<UserModel>()
    }

}

