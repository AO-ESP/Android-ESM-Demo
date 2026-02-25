fun Any.toPrettyString(indentSize: Int = 1): String {
    val indent = " ".repeat(indentSize)
    return toString()
        .replace("{", ",\n$indent")
        .replace("}", ",\n$indent")
        .replace("[", "")
        .replace("]", "")
        .replace(", ", ",\n$indent")
        .replace("(", "(\n$indent")
        .replace(")", ")\n$indent")
        .dropLast(1) + "\n)"
}