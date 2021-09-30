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