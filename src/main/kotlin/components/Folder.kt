package components

import react.*
import react.dom.ReactHTML.h3
import util.FolderInfo
import kotlin.math.min

external interface FolderProps : Props {
    var info: FolderInfo
    var isRunMade: Int
}


val Folder = fc<FolderProps> { props ->
    var topWords = if (props.info.topWordsList.isNotEmpty()) "Top words: " else "No words inf file"
    var isFirst = true
    for (i in 0 until min(3, props.info.topWordsList.size)) {
        topWords += "${props.info.topWordsList[i].first}(${props.info.topWordsList[i].second})"
        if (isFirst) {
            isFirst = false
        } else {
            topWords += ", "
        }
    }
    if (props.info.isRoot) {
        null
    }
    else {
        when (props.isRunMade) {
            2 -> h3 { }
            1 -> h3 { +"${props.info.path} $topWords" }
            0 -> h3 { +"Please enter repository path" }
        }
    }
}

fun RBuilder.folder(handler: FolderProps.() -> Unit) = child(Folder) {
    attrs {
        handler()
    }
}