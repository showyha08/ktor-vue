import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.thymeleaf.Thymeleaf
import io.ktor.thymeleaf.ThymeleafContent
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver

data class User(val name: String, val email: String)

fun main(args: Array<String>) {

    val server = embeddedServer(Netty, port = 8080) {

        install(Thymeleaf) {
            setTemplateResolver(ClassLoaderTemplateResolver().apply {
                prefix = "templates/thymeleaf/"
                suffix = ".html"
                characterEncoding = "utf-8"
            })
        }
        routing {
            get("/") {
                call.respondText("Hello World!", ContentType.Text.Plain)
            }
            get("/demo") {
                call.respondText("HELLO WORLD!")
            }

            //パラメータを利用して表示
            get("/user/{name}") {
                val name  = call.parameters["name"] ?:""
                call.respondText { "Hi $name" }
            }

            //Thymeleafを利用
            get("/thymeleaf") {
                val user = User("user name", "user@example.com")

                //templateにはhtml名を入力
                call.respond(ThymeleafContent("index", mapOf("user" to user)))
            }

            //どのパスが指定されたとき
            static("/vue"){

                //resorces配下のディレクトリを指定
                resources("vue")
            }

            static("/static"){
                resources("static")
            }
        }
    }
    server.start(wait = true)
}