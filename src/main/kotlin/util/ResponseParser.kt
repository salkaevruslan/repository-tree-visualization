package util

import kotlinx.browser.window
import kotlinx.coroutines.await

suspend fun getGitHubTree(path: String): FolderInfo {
    val result = window.fetch("https://api.github.com/repos/${path}/git/trees/main?recursive=1").await().text().await()
    console.log(result)
    return buildFileTree(path, result)
}

suspend fun buildFileTree(repository: String, result: String): FolderInfo {
    val files = getFiles(result)
    val globalRoot = FolderInfo(
        path = "",
        isRoot = true,
        parent = null,
        depth = 0,
    )
    var root = globalRoot
    for (file in files) {
        while (!file.startsWith("${root.path}${if (root.isRoot) "" else "/"}")) {
            if (root.parent != null) {
                root = root.parent!!
            }
        }
        val info = FolderInfo(
            path = file,
            isRoot = false,
            parent = root,
            depth = root.depth + 1,
        )
        root.children.add(info)
        root = info
    }
    countWords(repository, globalRoot)
    return globalRoot
}

fun getFiles(result: String): List<String> {
    return result.split("\n").filter { line -> line.trim().startsWith("\"path\": ") }
        .map { line -> line.trim().replace("\"path\": ", "").replace("\"", "").replace(",", "") }.toList()
}

suspend fun countWords(repository: String, root: FolderInfo) {
    if (root.children.isEmpty() && !root.isRoot) {
        val result =
            window.fetch("https://raw.githubusercontent.com/${repository}/master/${root.path}").await().text().await()
        countWordsInFile(result, root)
    } else {
        val wordsCount = mutableMapOf<String, Int>()
        for (child in root.children) {
            countWords(repository, child)
            for (entry in child.topWordsList) {
                val currentCount = wordsCount[entry.first]
                wordsCount[entry.first] = if (currentCount != null) currentCount + entry.second else entry.second
            }
        }
        getTopWords(wordsCount, root)
    }
}

fun countWordsInFile(result: String, root: FolderInfo) {
    val wordsCount = mutableMapOf<String, Int>()
    val words = Regex("[^A-Za-z0-9 ]").replace(result, " ").split(" ")
    for (word in words) {
        if (word.isEmpty()) continue
        val currentCount = wordsCount[word]
        wordsCount[word] = if (currentCount != null) currentCount + 1 else 1
    }
    getTopWords(wordsCount, root)
}

fun getTopWords(wordsCount: Map<String, Int>, root: FolderInfo) {
    root.topWordsList = wordsCount.toList().sortedBy { (_, value) -> value }.toList()
}
