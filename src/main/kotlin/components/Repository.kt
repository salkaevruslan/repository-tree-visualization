package components


import react.*
import react.dom.ReactHTML.h3

external interface RepositoryProps : Props {
    var path: String
    var isRunMade: Int
}

val Repository = fc<RepositoryProps> { props ->
    when (props.isRunMade) {
        2 -> h3 {
            +"Loading..."
        }
        1 -> h3 {
            +"Tree of  ${props.path}"
        }
        0 -> h3 {
            +"Repository or branch not found"
        }
        else -> h3 {}
    }
}

fun RBuilder.repository(handler: RepositoryProps.() -> Unit) = child(Repository) {
    attrs {
        handler()
    }
}