import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.example.st.noel.pa.Models.*
import org.example.st.noel.pa.Validator.JsonArrayTypeValidator
import org.example.st.noel.pa.Validator.JsonObjectValidator


data class Course(
    val name: String,
    val credits: Int,
    val evaluation: List<EvalItem>
)
data class EvalItem(
    val name: String,
    val percentage: Double,
    val mandatory: Boolean,
    val type: EvalType?
)

enum class EvalType {
    TEST, PROJECT, EXAM
}
/**
 * Classe de testes unitários para validar o comportamento da serialização JSON
 * e operações de filtragem, mapeamento e validação nas classes JSON personalizadas.
 */
class JsonModelTest {
    /**
     * Testa a serialização de um objeto [Course] em um [JsonObject].
     *
     * Cria um objeto Course com vários itens de avaliação e converte manualmente
     * para a estrutura JSON personalizada, depois imprime o JSON resultante.
     */
    @Test
    fun send_course_JsonValue() {
        val course = Course(
            "PA", 6, listOf(
                EvalItem("quizzes", .2, false, null),
                EvalItem("project", .8, true, EvalType.PROJECT),
                EvalItem("Java Developer", .7, true, EvalType.EXAM),
                EvalItem("Teste Kotlin", .9, false, EvalType.TEST),
            )
        )
        val obj = JsonObject(
            mutableMapOf(
                "name" to JsonString(course.name),
                "credits" to JsonNumber(course.credits),
                "\n evaluation" to JsonArray(
                    course.evaluation.map { value ->
                        JsonObject(
                            mutableMapOf(
                                "name" to JsonString(value.name),
                                "percentage" to JsonNumber(value.percentage),
                                "mandatory" to JsonBoolean(value.mandatory),
                                "type" to JsonString(value.type.toString())
                            )
                        )
                    }.toMutableList()
                )
            )
        )
        println("${obj.toJsonString()}")
    }
    /**
     * Testa se a criação de um [JsonObject] com tipos básicos (string, boolean, número)
     * funciona corretamente e se a comparação entre objetos JSON produz o resultado esperado.
     */
    @Test
    fun should_serialize_JsonObject_correctly() {
        val obj = JsonObject(
            mutableMapOf(
                "name" to JsonString("Test"),
                "active" to JsonBoolean(true),
                "score" to JsonNumber(9.5)
            )
        )

        val expected = JsonObject(
            mutableMapOf(
                "name" to JsonString("Test"),
                "active" to JsonBoolean(true),
                "score" to JsonNumber(9.5)
            )
        )
        assertEquals(expected, obj)
        println(obj.toJsonString())
    }
    /**
     * Testa a filtragem de propriedades de um [JsonObject], removendo uma chave específica.
     *
     * Verifica se a propriedade com a chave "a" foi removida corretamente.
     */
    @Test
    fun should_filter_JsonObject_correctly() {
        val obj = JsonObject(
            mutableMapOf(
                "a" to JsonNumber(1),
                "b" to JsonNumber(2),
                "c" to JsonNumber(3),
                "d" to JsonString("Teste")
            )
        )

        val filtered = obj.filterObject { key, _ -> key != "a"}
        val expected = JsonObject(
            mutableMapOf(
                "b" to JsonNumber(2),
                "c" to JsonNumber(3),
                "d" to JsonString("Teste")
            )
        )
        assertEquals(expected, filtered)
        println("valor experado: ${expected.toJsonString()}")
        println("valor filtrado: ${filtered.toJsonString()}")
    }
    /**
     * Testa a filtragem de elementos de um [JsonArray] baseado em condições específicas.
     *
     * Remove strings específicas e filtra números abaixo de 15.
     */
    @Test
    fun should_filter_JsonArray_correctly() {
        val array_filter = JsonArray().apply {
            addElmentArray(JsonString("teste"))
            addElmentArray(JsonString("Remove"))
            addElmentArray(JsonNumber(15))
            addElmentArray(JsonNumber(12))
            addElmentArray(JsonNumber(8))
            addElmentArray(JsonBoolean(true))
            addElmentArray(JsonString("filtro de Array na classe Modelo JsonArray"))
        }
        val filtered = array_filter.filterArray {
            when(it){
                is JsonString -> it.value != "Remove"
                is JsonNumber -> it.value.toInt() >= 15
                else -> true
            }
        }
        val expected = JsonArray(
            mutableListOf(
                JsonString("teste"),
                JsonNumber(15),
                JsonBoolean(true),
                JsonString("filtro de Array na classe Modelo JsonArray")
            )
        )
        assertEquals(expected, filtered)
        println("valor experado: ${expected.toJsonString()}")
        println("valor filtrado: ${filtered.toJsonString()}")
    }
    /**
     * Testa o mapeamento dos elementos de um [JsonArray].
     *
     * No teste, os números do array são convertidos para inteiros e verificados no JSON.
     */
    @Test
    fun should_map_JsonArray_correctly() {

        val jsonArray = JsonArray(mutableListOf(JsonNumber(5), JsonNumber(2)))
        val mapped = jsonArray.map { JsonNumber((it as JsonNumber).value.toInt()) }
        assertEquals("[5,2]", mapped.toJsonString())
        println("Experado: [5,2] Valor mapeado no vetor: ${mapped.toJsonString()}")
    }
    /**
     * Testa a validação de chaves únicas em um [JsonObject].
     *
     * Verifica se o validador não encontra erros em objetos JSON válidos.
     */
    @Test
    fun should_validate_unique_keys_in_JsonObject() {
        val visitor = JsonObjectValidator
        val json = JsonObject(
            mutableMapOf(
                "data" to JsonArray(
                    mutableListOf(
                        JsonNumber(1),
                        JsonNumber(1),
                        JsonString("x"),
                        JsonNull
                    )
                )
            )
        )
        val filtered = json.filterObject { key, value -> key == "data"}
        println(filtered.toJsonString())
        //"""{"data": [1,1,"x",null]}"""
        val expected = JsonObject(
            mutableMapOf(
                "data" to JsonArray().apply {
                    addElmentArray(JsonNumber(1))
                    addElmentArray(JsonNumber(1))
                    addElmentArray(JsonString("x"))
                    addElmentArray(JsonNull)
                }
            )
        )
        assertEquals(expected, filtered)
        val error = visitor.validateObject(json)
        error.forEach {
            println(it.toInt().toString())
        }
        assertTrue(error.isEmpty())
    }
    //OKAY 100%
    /**
     * Testa se o validador aceita um [JsonObject] com chaves únicas,
     * mesmo que chaves repetidas sejam declaradas (última prevalece).
     */
    @Test
    fun valid_JsonObject_errors() {
        val json = JsonObject(
            mutableMapOf(
                "name" to JsonString("Test"),
                "age" to JsonNumber(31),
                "age" to JsonString("30"),
                "active" to JsonBoolean(true)
            )
        )
        println(json.toJsonString())
        val errors = JsonObjectValidator.validateObject(json)
        println(errors.map { it.toString() })
        assertTrue(errors.isEmpty())
    }
    //Validação do visitor.visit do acceot do JsonObject
    //Okay 100%
    /**
     * Testa o padrão Visitor aplicado em [JsonObject].
     *
     * Verifica se o visitante visita as propriedades e valores na ordem correta.
     */
    @Test
    fun accept_JsonObject() {
        val visitor = TestVisitor()
        val json = JsonObject(
            mutableMapOf(
                "name" to JsonString("Test"),
                "age" to JsonNumber(31),
                "active" to JsonBoolean(true)
            )
        )
        json.accept(visitor)
        val valor_expe = listOf(
            "visit(JsonObject)",
            "visitProperty(name)",
            "visit(JsonString: Test)",
            "visitProperty(age)",
            "visit(JsonNumber: 31)",
            "visitProperty(active)",
            "visit(JsonBoolean: true)"
        )
        println(visitor.visit_accept)
        assertEquals(valor_expe,visitor.visit_accept)
    }
    //Mostrar valores para JsonObject sem valores duplicados
    /**
     * Exibe o JSON de um [JsonObject] sem valores duplicados.
     *
     * Verifica se o validador detecta a ausência de erros no JSON.
     */
    @Test
    fun show_JsonObject_without_duplicates() {
        // Arrange
        val json = JsonObject(
            listOf(
                "name" to JsonString("João Noel"),
                "age" to JsonNumber(32),
                "status" to JsonBoolean(true),
                "array" to JsonArray().apply {
                    addElmentArray(JsonNumber(23))
                    addElmentArray(JsonString("teste"))
                    addElmentArray(JsonNull)
                }
            )
        )
        println(json.toJsonString())

        // Act
        val errors = JsonObjectValidator.validateObject(json)
        errors.forEach {
            println(it.toString())
        }
        assertTrue(errors.isEmpty())
    }
    //Remover valores ou elementos duplicados no JsonArray/ JsonObject
    /**
     * Testa a remoção de valores ou elementos duplicados em [JsonObject] e [JsonArray].
     *
     * Verifica se as duplicações são removidas corretamente e imprime os JSONs antes e depois.
     */
    @Test
    fun remove_duplicate_JsonArray_JsonObject(){
        val obj = JsonObject(
            listOf(
                "name" to JsonString("Noel"),
                "name" to JsonString("teste"),
                "status" to JsonBoolean(true)
            )
        )
        val clearObj = obj.removeDuplicateKeys()
        println("\n Conteudo de Json antigo: ${obj.toJsonString()}")
        println("\n Conteudo Json apos a remoção: ${clearObj.toJsonString()}")

        val arr = JsonArray(
            mutableListOf(
                JsonNumber(15),
                JsonString("teste"),
                JsonNull,
                JsonBoolean(true),
                JsonString("teste"),
                JsonString("Teste"),
                JsonBoolean(true)
            )
        )
        val clearArray = arr.removeDuplicateElements()
        println("\n Conteudo Json antigo: ${arr.toJsonString()}")
        println("\n Conteudo Json apos a remoção: ${clearArray.toJsonString()}")

    }

    //Verificação de duplicação dos parametros do objecto OKAY
    /**
     * Valida a duplicação em objetos JSON construídos com chaves repetidas.
     *
     * Demonstra que o validador não acusa erro devido à sobreposição no mapa.
     */
    @Test
    fun valida_duplicação_construído() {
        // Simula dois objetos diferentes que por lógica devem conter a mesma chave
        val inner = JsonObject(
            listOf(
                "name" to JsonString("teste"),
                "idade" to JsonNumber(12),
                "email" to JsonString("a@example.com"),
                "email" to JsonArray(
                    mutableListOf(
                        JsonString("b@example.com")
                    )
                )
            )
        )

        val errors = JsonObjectValidator.validateObject(inner)
        errors.forEach {
            println(it.toInt())
        }
        println(errors.size.toInt())
        println(inner.toJsonString())
        // O validador não vai acusar erro, porque só vê uma chave no final
        assertEquals(0, errors.size) // CORRETO COMPORTAMENTO dado o uso do Map
    }

    //Teste para validação array com diferentes tipos para
    // cada @elemento(String, Number, Boolean, Array e Null)
    @Test
    fun valida_tipo_array() {
        val json = JsonArray().apply {
            addElmentArray(JsonString("Teste"))
            addElmentArray(JsonNumber(123))
            addElmentArray(
                JsonArray().apply {
                    addElmentArray(
                        JsonArray().apply {
                            addElmentArray(JsonNumber(1))
                            addElmentArray(JsonString("x"))
                            addElmentArray(JsonNull)
                        }
                    )
                }
            )
        }

        val erros = JsonArrayTypeValidator.validateArrayTypeValidator(json)

        // Debug
        println("Erros encontrados (${erros.size}):")
        erros.forEach { println(it) }
        println("\n Conteudo JSON:")
        json.forEach { println(it.toJsonString()) }

        println(erros.size.toInt())
        assertEquals(2, erros.size)
        assertTrue(erros.any { it.contains("[] contém diferentes tipos:") })
        assertTrue(erros.any { it.contains("[2][0] contém diferentes tipos:") })
    }

    /**
     * O teste esta funcionando para verificação de multiplos arrays
     * arrays aninhados
     */
    @Test
    fun arrays_multiplos() {
        val json = JsonArray().apply {
            addElmentArray(JsonString("root"))
            addElmentArray(JsonArray().apply {
                addElmentArray(JsonNumber(10))
                addElmentArray(JsonString("abc"))
                addElmentArray(JsonArray().apply {
                    addElmentArray(JsonBoolean(true))
                    addElmentArray(JsonNumber(99))
                    addElmentArray(JsonString("Fase de implementação"))
                    addElmentArray(JsonNull)
                })
                addElmentArray(JsonObject(
                        mutableMapOf(
                            "data" to JsonArray(
                                mutableListOf(
                                    JsonString("teste"),
                                    JsonNumber(23),
                                    JsonBoolean(true),
                                    JsonNumber(12)
                                )
                            )
                        )
                    )
                )
            })
        }
        val erros = JsonArrayTypeValidator.validateArrayTypeValidator(json)
        for (valor in json){
            println("Json: ${valor.toJsonString()}")
        }
        erros.forEach { println(it.toString()) }
        assertTrue(erros.isNotEmpty())
        assertTrue(erros.any { it.contains("Array na posição [1] contém diferentes tipos:") })
        assertTrue(erros.any { it.contains("Array na posição [1][2] contém diferentes tipos:") })
    }
    @Test
    fun arrays_aninhados() {
        val json = JsonArray().apply {
            addElmentArray(JsonArray().apply {
                addElmentArray(JsonNumber(1))
                addElmentArray(JsonNumber(2))
                addElmentArray(JsonNumber(150))
            })
            addElmentArray(JsonArray().apply {
                addElmentArray(JsonString("a"))
                addElmentArray(JsonString("b"))
                addElmentArray(JsonString("teste"))
            })
        }

        val erros = JsonArrayTypeValidator.validateArrayTypeValidator(json)
        //json.forEach { key,value }
        for (value in json){
            println("valores de Array: ${value.toJsonString()}")
        }
        erros.forEach { println(it.toInt().toString()) }
        assertTrue(erros.isEmpty())
    }
}

