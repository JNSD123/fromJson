package st.noel.pa.api.Controllers

import org.example.st.noel.pa.Inference.JsonInferrer
import org.example.st.noel.pa.Interfaces.JsonValue
import org.example.st.noel.pa.Models.JsonArray
import org.example.st.noel.pa.Models.JsonObject
import org.example.st.noel.pa.Models.JsonString
import org.example.st.noel.pa.api.Annotation.GetMapping
import org.example.st.noel.pa.api.Annotation.PathVariable
import org.example.st.noel.pa.api.Annotation.RequestParam
import org.example.st.noel.pa.api.Annotation.RestController
import kotlin.collections.mutableMapOf

/**
 * Controller responsável por fornecer endpoints a usuários e dados de exemplo.
 *
 * O path base para todos os endpoints desta classe é "/api".
 */
@RestController(path = "/api")
class UserController {
    private val list_user = listOf(
        User(id=1, name = "TESTE1", credits = 112, status = false),
        User(id=2, name = "TESTE", credits = 11, status = true),
        User(id=3, name = "PA", credits = 120, status = true),
        User(id=4, name = "Admin", credits = 200, status = true),
        User(id=5, name = "USER", credits = 50, status = true),
        User(id=6, name = "USER BASIC", credits = 1, status = false)
    )
    /**
     * Endpoint GET "/api/ints"
     * Retorna uma lista de números inteiros.
     */
    @GetMapping("/ints")
    fun demo(): List<Int> = listOf(1, 2, 3,7)

    /**
     * Endpoint GET "/api/users"
     * Retorna todos os usuários cadastrados.
     *
     * @return Lista de objetos [User].
     */
    @GetMapping("/users")
    fun getAllUsers(): List<User> = list_user

    /**
     * Endpoint GET "/api/users/ative"
     * Retorna apenas os usuários que estão com status ativo (true).
     *
     * @return JsonArray contendo os usuários ativos.
     */
    @GetMapping("/users/ative")
    fun getUserStatusAtive(): JsonArray{
        val status_ative = list_user.filter { it.status }
        val result = JsonInferrer.infer(status_ative) as JsonArray
        //println(result)
        return result
    }
    /*@GetMapping("/users/{id}")
    fun getUserById(@PathVariable id: Int): User? =
        list_user.find { it.id == id }*/
    /**
     * Endpoint GET "/api/pair"
     * Retorna um par de strings como exemplo.
     *
     * @return Par contendo duas strings.
     */
    @GetMapping("/pair")
    fun obj(): Pair<String, String> = Pair("Teste", "Passou com os modelos da fase 2")
    /**
     * Endpoint GET "/api/path/{pathvar}"
     * Exemplo de endpoint com variável de caminho (path variable).
     *
     * @param pathvar Valor capturado da URL.
     * @return String contendo o valor recebido acrescido de "!".
     */
    @GetMapping("/path/{pathvar}")
    fun path(
        @PathVariable pathvar: String
    ): String = pathvar + "!"
    /**
     * Endpoint GET "/api/args"
     * Exemplo de endpoint que recebe parâmetros via query string.
     *
     * @param n Número de repetições da string 'text'.
     * @param text Texto a ser repetido.
     * @return Mapa onde a chave é 'text' e o valor é a repetição de 'text' 'n' vezes.
     */
    @GetMapping("/args")
    fun args(
        @RequestParam n: Int,
        @RequestParam text: String
    ): Map<String, String> = mapOf(text to text.repeat(n))

}
/**
 * Modelo de dados representando um usuário.
 *
 * @property name Nome do usuário.
 * @property credits Créditos associados ao usuário.
 * @property status Status ativo/inativo do usuário.
 */
data class User(
    val id: Int,
    val name: String,
    val credits: Int,
    val status: Boolean
)
/**
 * Modelo simples para encapsular um valor inteiro.
 *
 * @property value Valor inteiro encapsulado.
 */
data class Wrapper(val value: Int)