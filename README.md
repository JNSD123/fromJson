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


## Exemplo de Controlador
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