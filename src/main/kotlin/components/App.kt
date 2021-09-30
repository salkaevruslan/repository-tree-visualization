package components

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import react.*
import react.Props
import react.dom.*
import util.FolderInfo
import util.getGitHubTree
import util.getInfoList

external interface AppState : State {
    var currentRepo: String
    var isRunMade: Int
    var root: FolderInfo
}

@JsExport
class App : RComponent<Props, AppState>() {
    override fun RBuilder.render() {
        h1 {
            +"Repo tree visualizer"
        }
        div {
            h3 {
                +"Enter repo path"
            }
            inputField {
                value = ""
                onUpdateValue = { value ->
                    console.log("keks")
                    this.value = value
                    console.log("Repo name updated: ${this.value}")
                }
                onSubmitValue = {
                    setState {
                        currentRepo = value
                        isRunMade = 2
                        GlobalScope.launch(Dispatchers.Default) {
                            root = getGitHubTree(currentRepo)
                            isRunMade = 1
                        }
                    }
                    console.log("Repo name submitted: $value")
                }
            }
            repository {
                path = state.currentRepo
                onSetRunStatus = { status ->
                    setState {
                        isRunMade = status
                    }
                }
                onSetRoot = { info ->
                    setState {
                        root = info
                    }
                }
            }
            // for (folderInfo in getInfoList(state.root)) {
            //   folder {
            //        info = folderInfo
            //    }
            // }
        }
    }
}
