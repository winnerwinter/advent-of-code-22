import java.util.*

/**
 * Human note:
 * This attempt with Chat GPT involved me just copy-pasting the entire problem statement
 * and seeing what it produced. Does compile but not very correct. Guess I'm not out of a job afterall. Its fairly close tho.
 */
fun main() {

    class File(val name: String, val size: Int, val children: List<File>) {
        val totalSize: Int
            get() = size + children.sumBy { it.totalSize }
    }

    fun parseFileSystem(commands: List<String>, outputs: List<String>): File {
        // Create the root directory
        val root = File("/", 0, emptyList())

        // Create a stack to keep track of the current directory
        val stack = Stack<File>()
        stack.push(root)

        for ((command, output) in commands.zip(outputs)) {
            when (command) {
                // If the command is "cd", navigate to the specified directory
                "cd" -> {
                    val dirName = output.trim()
                    if (dirName == "/") {
                        // If the directory name is "/", switch to the root directory
                        stack.clear()
                        stack.push(root)
                    } else if (dirName == "..") {
                        // If the directory name is "..", move out of the current directory
                        stack.pop()
                    } else {
                        // Otherwise, move into the specified child directory
                        val curDir = stack.peek()
                        val childDir = curDir.children.find { it.name == dirName }
                            ?: throw IllegalArgumentException("Invalid directory: $dirName")
                        stack.push(childDir)
                    }
                }
                // If the command is "ls", add the listed files and directories to the current directory
                "ls" -> {
                    val curDir = stack.peek()
                    val childFiles = output.trim().split("\n").map { fileInfo ->
                        val parts = fileInfo.trim().split(" ")
                        if (parts[1].endsWith(".")) {
                            // If the file has a period in its name, it is a file
                            File(parts[1], parts[0].toInt(), emptyList())
                        } else {
                            // Otherwise, it is a directory
                            File(parts[1], 0, emptyList())
                        }
                    }
                    stack.pop()
                    stack.push(File(curDir.name, curDir.size, curDir.children + childFiles))
                }

                else -> throw IllegalArgumentException("Invalid command: $command")
            }
        }

        return root
    }

    // Parse the commands and outputs and create the root File object
    val commands = listOf(
        "cd /",
        "ls",
        "cd a",
        "ls",
        "cd e",
        "ls",
        "cd ..",
        "cd ..",
        "cd d",
        "ls"
    )
    val outputs = listOf(
        "dir a\n14848514 b.txt\n8504156 c.dat\ndir d",
        "dir a\n29116 f\n2557 g\n62596 h.lst",
        "dir e\n584 i",
        "cd ..",
        "cd ..",
        "cd d",
        "4060174 j\n8033020 d.log\n5626152 d.ext\n7214296 k"
    )
    val root = parseFileSystem(commands, outputs)

    // Recursive helper function to find all of the small directories in a given directory
    fun findSmallDirs(dir: File, maxSize: Int): List<File> {
        return if (dir.totalSize <= maxSize) {
            // If the current directory is small, return it and all of its small child directories
            listOf(dir) + dir.children.flatMap { findSmallDirs(it, maxSize) }
        } else {
            // Otherwise, return only its small child directories
            dir.children.flatMap { findSmallDirs(it, maxSize) }
        }
    }

    // Find all of the directories with a total size of at most 100000
    val smallDirs = root.children.flatMap { findSmallDirs(it, 100000) }

    // Calculate the sum of their total sizes
    val sum = smallDirs.sumBy { it.totalSize }

    // Print the result
    println(sum) // 1723892
}
