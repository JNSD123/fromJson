# fromJson
API_JSON_MODEL
# JSON Manipulation Library - Advanced Programming - Project 2024/2025
## Estrutura do Projeto
### Objetivo do Projeto
O objetivo é criar uma biblioteca Kotlin para gerar e manipular JSON usando modelos em memória,
envolvendo todos os seus tipos de valores: Objectos, Matrizes, Strings, Números,Booleanos, Nulos

Este projeto implementa uma framework leve em Kotlin baseada no padrão MVC. Suporta endpoints HTTP `GET`,
anotações personalizadas (`@RestController`, `@GetMapping`, `@RequestParam`, `@PathVariable`) e serialização JSON personalizada.

# Algumas caraterísticas do projeto
Para elaboração do projeto seguiu-se  enunciado proposto pelo professor.
A lista abaixo mostra os pontos que estão presentes e funcionais no nosso projeto:
`fromJson` é um projeto para modelar e gerar representações gráficas de estruturas JSON, com suporte a geração de diagramas UML para visualização de modelos de dados utilizados em APIs REST.

## Funcionalidades

- Conversão de respostas JSON em modelos de dados.
- Geração de diagramas UML com PlantUML.
- Suporte à estruturação automática de modelos a partir de chamadas HTTP.
- Integração com ferramentas de desenvolvimento baseadas em Gradle.

## Requisitos

- Java 11 ou superior
- Gradle (`./gradlew`)
- PlantUML (para gerar os diagramas UML)

## Como utilizar

### 1. Clonar o repositório
 
```bash

git clone https://github.com/seu-usuario/fromJson.git
cd fromJson
```

### 2. Construir o projeto

```bash
./gradlew build
```

### 3. Executar a aplicação

Dependendo da estrutura, se houver uma `main` definida:
```bash
./gradlew run
```
o servidor será iniciado na porta 8087
# Exemplo da biblioteca Kotlin para gerar e manipular JSON usando modelos em memória
O modelo de teste TDD da framework JSON em Kotlin encontra-se no diretório `src/test/JsonModelTest.kt`,
O ficheiro TestVisitor permite validar os metodos do modelos.

### Nesta sessão contém apenas alguns exemplos de teste TDD, o resto encontra-se no diretório acima mencionado

```
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
}
```





## Exemplo de Controlador Fase 3 HTTP_GET
```
@RestController(path ="/api")
class UserController {
    
    private val list_user = listOf(
        User(name = "TESTE1", credits = 112, status = false),
        User(name = "TESTE", credits = 11, status = true),
        User(name = "PA", credits = 120, status = true),
        User(name = "Admin", credits = 200, status = true))
    @GetMapping("/users")
    fun getAllUsers(): List<User> = list_user

    @GetMapping("/users/{id}")
    fun getUserById(@PathVariable id: Int): User? =
        list_user.find { it.id == id }

    @GetMapping("/users/ative")
    fun getUserStatusAtive(): JsonArray{
        val status_ative = list_user.filter { it.status }
        val result = JsonInferrer.infer(status_ative) as JsonArray
        return result
    }
}
data class User(
val id: Int,
val name: String,
val credits: Int,
val status: Boolean
)

fun main() {
ControllerRegistry.register(UserController())
AppServer.start(8087)
}
```

### 4. Gerar diagramas PlantUML

Execute os scripts ou comandos definidos para gerar os arquivos `.puml` e convertê-los em imagens `.png`:

```bash

#UML do Modelos e Classes Fase 1 e 2
plantuml diagram.puml
#UML HTTP GET Fase 3
plantuml diagram_http_get.puml

```


Arquivos de exemplo:

- `diagram_http_get.png`
- `diagram_json_model.png`

## Nota técnica
O servidor HTTP embutido é simples, ideal para testes e prototipagem.
A serialização JSON é feita com classes personalizadas com JsonArray, JsonObject, JsonString, etc.
O mapeamento de rotas é feita via reflexão, respeitando as anotações definidas no código.


```
fromJson/
├── src/                        # Código fonte
├── build.gradle.kts            # Script de build Gradle em Kotlin
├── diagram.puml                # Script diagramas PlantUML Modelos e Classes
├── diagram_htt_get.puml        # Script diagramas PlantUML Modelos e Classes
├── diagram_json_model.png      # Diagrama gerado dos Modelos e classes
├── diagram_http_get.png        # Diagrama gerado fase 3 HTTP_GET
├── README.md                   # Este ficheiro
```

## Licença

Este projeto está licenciado sob a licença MIT. Veja o ficheiro [LICENSE](LICENSE) para mais detalhes.