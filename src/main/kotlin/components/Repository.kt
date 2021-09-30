package components


import react.*
import react.dom.ReactHTML.h3
import react.dom.ReactHTML.p
import util.FolderInfo

external interface RepositoryProps : Props {
    var path: String
    var isRunMade: Int
    var onSetRoot: (FolderInfo) -> Unit
    var onSetRunStatus: (Int) -> Unit
}

val Repository = fc<RepositoryProps> { props ->
    if (props.isRunMade != 0) {
        if (props.isRunMade == 1) {
            h3 {
                +"Tree of ${props.path}"
            }
        } else {
            console.log(props.isRunMade)
            h3 {
                +"Loading..."
            }
        }
    } else {
        p {}
    }
}

fun RBuilder.repository(handler: RepositoryProps.() -> Unit) = child(Repository) {
    attrs {
        handler()
    }
}