package st.noel.pa.api.Server

import com.sun.net.httpserver.HttpServer
import st.noel.pa.api.Framework.Core.ControllerRegistry
import java.net.InetSocketAddress

/**
 * Servidor HTTP simples que escuta requisições na porta especificada e
 * delega o processamento das rotas para o [ControllerRegistry].
 *
 * O servidor cria um contexto raiz ("/") que captura todas as requisições,
 * extrai o caminho e parâmetros query da URL e chama o método de resolução
 * das rotas para retornar uma resposta JSON.
 */
object AppServer {

    //private const val PORT = 8087

    /**
     * Inicia o servidor HTTP na porta informada (padrão 8087).
     *
     * @param port Porta TCP onde o servidor irá escutar.
     *
     * Para cada requisição:
     * - Extrai o caminho da URL e parâmetros query.
     * - Usa [ControllerRegistry] para resolver a rota e obter o resultado.
     * - Retorna o resultado serializado em JSON ou erro 404 caso não encontrado.
     */
    fun start(port: Int = 8087) {
        val server = HttpServer.create(InetSocketAddress(port), 0)
        server.createContext("/") { exchange ->
            val uri = exchange.requestURI
            val path = uri.path
            val queryParams = uri.rawQuery?.split("&")
                ?.mapNotNull {
                    val parts = it.split("=")
                    if (parts.size == 2) parts[0] to parts[1] else null
                }?.toMap() ?: emptyMap()

            //val result = ControllerRegistry1.register(path, queryParams)
            val result = ControllerRegistry.resolveWithParams(path, queryParams)

            val responseText = result?.toJsonString() ?: """{"error": "Not Found"}"""
            val statusCode = if (result == null) 404 else 200

            exchange.sendResponseHeaders(statusCode, responseText.toByteArray().size.toLong())
            exchange.responseBody.use { it.write(responseText.toByteArray()) }
        }

        server.executor = null
        server.start()
        println("Servidor iniciado em http://localhost:$port")
    }
}

