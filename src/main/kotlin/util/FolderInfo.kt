package util

data class FolderInfo(
    var path: String,
    var isRoot: Boolean,
    var depth: Int,
    var parent: FolderInfo?,
    var children: MutableList<FolderInfo> = mutableListOf(),
    var topWordsList: List<Pair<String, Int>> = listOf()
)

fun getInfoList(root: FolderInfo): List<FolderInfo> {
    val list = mutableListOf(root)
    for (child in root.children) {
        list.addAll(getInfoList(child))
    }
    return list
}

fun showFolder(info: FolderInfo, focusedInfoSet: List<FolderInfo>, root: FolderInfo): Boolean {
    return focusedInfoSet.contains(info.parent) || info.parent == root
}

fun isParent(info: FolderInfo, parent: FolderInfo): Boolean {
    var tmp: FolderInfo? = info
    while (tmp != null) {
        if (parent == tmp) {
            return true
        }
        tmp = tmp.parent
    }
    return false
}