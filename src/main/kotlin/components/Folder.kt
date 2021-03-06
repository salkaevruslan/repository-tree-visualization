package components

import react.*
import react.dom.p
import util.FolderInfo
import util.showFolder
import kotlinx.browser.document
import kotlinx.html.js.onClickFunction
import react.dom.*
import kotlin.math.max
import kotlin.math.min

external interface FolderProps : Props {
    var root: FolderInfo
    var info: FolderInfo
    var isRunMade: Int
    var focusedFolderInfoList: MutableList<FolderInfo>
    var updateFocusedFolderInfo: (FolderInfo) -> Unit
}


val Folder = fc<FolderProps> { props ->
    var topWords = if (props.info.topWordsList.isNotEmpty()) "Top words: " else "No words in file"
    var isFirst = true
    for (i in 0 until min(3, props.info.topWordsList.size)) {
        if (isFirst) {
            isFirst = false
        } else {
            topWords += ", "
        }
        topWords += "${props.info.topWordsList[i].first}(${props.info.topWordsList[i].second})"
    }
    val beginningLines = "----|".repeat(max(0, props.info.depth - 1))
    if (props.info.isRoot || !showFolder(props.info, props.focusedFolderInfoList, props.root)) {
        p {}
    } else {
        when (props.isRunMade) {
            2 -> p { }
            1 -> p {
                +"$beginningLines${props.info.path} ($topWords)"
                attrs {
                    onClickFunction = {
                        props.updateFocusedFolderInfo(props.info)
                    }
                }
            }
            else -> p { +"Please enter repository path" }
        }
    }
}

fun RBuilder.folder(handler: FolderProps.() -> Unit) = child(Folder) {
    attrs {
        handler()
    }
}