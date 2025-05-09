import org.example.st.noel.pa.Interfaces.JsonValue
import org.example.st.noel.pa.Interfaces.JsonVisitor
import org.example.st.noel.pa.Models.*

class TestVisitor : JsonVisitor {
    val visit_accept = mutableListOf<String>()
    override fun visit(obj: JsonObject) {
        visit_accept.add("visit(JsonObject)")
    }

    override fun visit(arr: JsonArray) {
        visit_accept.add("visit(JsonArray)")
    }

    override fun visit(str: JsonString) {
        visit_accept.add("visit(JsonString: ${str.value})")
    }

    override fun visit(numb: JsonNumber) {
        visit_accept.add("visit(JsonNumber: ${numb.value})")
    }

    override fun visit(bool: JsonBoolean) {
        visit_accept.add("visit(JsonBoolean: ${bool.value})")
    }

    override fun visit(nul: JsonNull) {
        visit_accept.add("visit(JsonNull)")
    }

    override fun visitProperty(str: String, value: JsonValue) {
        visit_accept.add("visitProperty($str)")
    }

}