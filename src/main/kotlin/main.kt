import components.App
import kotlinx.browser.document
import org.w3c.dom.HTMLElement
import react.dom.*

fun main() {
    render(document.getElementById("root")) {
        child(App::class) {}
    }
}