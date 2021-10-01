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
import util.isParent

external interface AppState : State {
    var currentRepo: String
    var currentBranch: String
    var runStatus: Int
    var root: FolderInfo
    var focusedFolderInfoList: MutableList<FolderInfo>
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
                repo = ""
                onUpdateRepo = { value ->
                    this.repo = value
                    console.log("Repo name updated: ${this.repo}")
                }
                onUpdateBranch = { value ->
                    this.branch = value
                    console.log("Branch name updated: ${this.branch}")
                }
                onSubmitValues = {
                    setState {
                        currentRepo = repo
                        currentBranch = branch
                        runStatus = 2
                    }
                    GlobalScope.launch(Dispatchers.Default) {
                        val result = getGitHubTree(state.currentRepo, state.currentBranch)
                        console.log("Now updating")
                        setState {
                            if (result.second) {
                                root = result.first!!
                                runStatus = 1
                            } else {
                                runStatus = 0
                            }
                            focusedFolderInfoList = mutableListOf(root)
                        }
                    }
                    console.log("Repo name submitted: $repo")
                }
            }
            repository {
                path = state.currentRepo
                isRunMade = state.runStatus
            }
            if (state.runStatus == 1) {
                for (folderInfo in getInfoList(state.root)) {
                    folder {
                        root = state.root
                        info = folderInfo
                        isRunMade = state.runStatus
                        focusedFolderInfoList = state.focusedFolderInfoList
                        updateFocusedFolderInfo = { info ->
                            setState {
                                if (focusedFolderInfoList.contains(info)) {
                                    focusedFolderInfoList.remove(info)
                                    val tmp = mutableListOf<FolderInfo>()
                                    for (element in focusedFolderInfoList) {
                                        if (isParent(element, info)) {
                                            tmp.add(element)
                                        }
                                    }
                                    focusedFolderInfoList.removeAll(tmp)
                                } else {
                                    focusedFolderInfoList.add(info)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
