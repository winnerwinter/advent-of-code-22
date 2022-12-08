import java.util.*

/**
 * Human note:
 * This code was almost entirely written by ChatGPT. I influenced it in design and built it up in parts and tried
 * to review the code as we went along. I tried to ask questions about code I saw that was incorrect and tried not to suggest anything.
 * At the very last step I however lost all the chat history on accident. Nevertheless it turned out quite well.
 */
fun main(args: Array<String>) {
    // Human note: I added the test check myself
    val testInput = readInput("Day07_test")
    val testResult = part1(testInput)
    check(testResult == 95437)

    val input = readInput("Day07")

    // Part 1
    val result1 = part1(input)
    println("Part 1: $result1")

    // Part 2
    // val result2 = part2(input)
    // println("Part 2: $result2")
}

// Human note: I made this sealed.
sealed interface FileSystemElement {
    val name: String
}

class File(override val name: String, val parent: Directory?, val size: Int) : FileSystemElement

class Directory(override val name: String, val parent: Directory?, val files: MutableMap<String, FileSystemElement>) : FileSystemElement

fun buildFileSystem(input: List<String>): Directory {
    // Create the root directory
    val rootDirectory = Directory("/", null, mutableMapOf())

    var iterator = input.iterator()

    // Keep track of the current directory
    var currentDirectory = rootDirectory

    while (iterator.hasNext()) {
        // Get the next line in the input
        val line = iterator.next()
        // Parse the line to extract the command and its arguments
        val (command, arguments) = parseLine(line)
        // Execute the command
        when (command) {
            "cd" -> currentDirectory = handleCdCommand(arguments, currentDirectory)

            // Human note: At this point I fat-fingered ChatGPT and lost all my chat history. We were debugging the very last issue here.
            // Since we were working with an iterator, we needed to copy it somehow as to not exhaust the list.

            // Final broken step.
            // "ls" -> handleLsCommand(inputList.subList(inputList.indexOf(line) + 1, inputList.size), currentDirectory)

            // My implementation
            "ls" -> {
                val iteratorCopy = iterator.asSequence().toList()
                handleLsCommand(iteratorCopy, currentDirectory)
                iterator = iteratorCopy.iterator()
            }
        }
    }

    return rootDirectory
}

fun handleCdCommand(arguments: List<String>, currentDirectory: Directory): Directory {
    return when {
        // Human note: It even handles edge cases!
        arguments.isEmpty() -> currentDirectory // Do nothing if the cd command has no arguments
        arguments[0] == ".." -> {
            if (currentDirectory.parent != null) {
                currentDirectory.parent // Move to the parent directory
            } else {
                currentDirectory // The current directory is already the root directory, so do nothing
            }
        }
        arguments[0] == "/" -> {
            if (currentDirectory.name == "/") {
                // The current directory is already the root directory, so do nothing
                currentDirectory
            } else {
                // Iterate through the current directory's parent directories until reaching the root directory
                var directory = currentDirectory
                while (directory.parent != null) {
                    directory = directory.parent!!
                }
                directory
            }
        }
        else -> {
            if (arguments[0] in currentDirectory.files) {
                currentDirectory.files[arguments[0]] as Directory // Move to the specified subdirectory
            } else {
                currentDirectory // The specified subdirectory does not exist, so do nothing
            }
        }
    }
}

fun handleLsCommand(input: List<String>, currentDirectory: Directory) {
    // Take all the lines from the input until the next command
    val outputLines = input.takeWhile { !it.startsWith("$") }
    // Parse each output line and add the information to the files Map of the currentDirectory object
    outputLines.forEach {
        // Parse the line of output and add the resulting FileSystemElement to the currentDirectory object
        val fileSystemElement = parseOutputLine(it, currentDirectory)
        currentDirectory.files[fileSystemElement.name] = fileSystemElement
    }
}

fun parseLine(line: String): Pair<String, List<String>> {
    // Split the line by spaces to extract the command and its arguments
    val parts = line.split(" ")
    // Remove the first part of the line (which is the $ character)
    val commandAndArguments = parts.drop(1)
    // The first part is the command
    val command = commandAndArguments[0]
    // The remaining parts are the arguments
    val arguments = commandAndArguments.drop(1)

    return Pair(command, arguments)
}

fun parseOutputLine(line: String, currentDirectory: Directory): FileSystemElement {
    // Split the line by spaces to extract the type and name
    val parts = line.split(" ")
    // The first part is the type (either "dir" or a number indicating the size of a file)
    val type = parts[0]
    // The second part is the name
    val name = parts[1]

    // Check if the type is "dir" or a number
    return if (type == "dir") {
        // If the type is "dir", return a new Directory object
        Directory(name, currentDirectory, mutableMapOf())
    } else {
        // If the type is a number, convert it to an Int and return a new File object
        File(name, currentDirectory, type.toInt())
    }
}

fun part1(input: List<String>): Int {
    val fileSystem = buildFileSystem(input)
    // Traverse the file system starting from the root directory
    var totalSize = 0
    // Human note: Breadth First Search!!
    val queue = LinkedList<Directory>()
    queue.add(fileSystem)
    while (queue.isNotEmpty()) {
        val dir = queue.poll()
        // If the directory has a total size of at most 100000, add its size to the total size
        if (getDirectorySize(dir) <= 100000) {
            totalSize += getDirectorySize(dir)
        }
        // Add all its subdirectories to the queue
        queue.addAll(dir.files.values.filterIsInstance<Directory>())
    }
    return totalSize
}

fun getDirectorySize(dir: Directory): Int {
    // Return the sum of the sizes of all files in the directory, including nested directories
    return dir.files.values.sumBy {
        when (it) {
            is Directory -> getDirectorySize(it)
            is File -> it.size
        }
    }
}

//fun part2(input: List<String>): Int {
//    val fileSystem = buildFileSystem(input)
//    // Use the file system to solve part 2 of the puzzle
//}
