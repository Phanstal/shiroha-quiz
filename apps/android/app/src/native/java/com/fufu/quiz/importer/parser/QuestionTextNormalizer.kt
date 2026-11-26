package com.fufu.quiz.importer.parser

object QuestionTextNormalizer {
    fun normalize(raw: String): String {
        if (raw.isBlank()) return ""
        return raw
            .replace("\uFEFF", "")
            .replace("\uFE0F", "")
            .replace("\r\n", "\n")
            .replace('\r', '\n')
            .replace('\u3000', ' ')
            .let(::normalizeFullWidthAscii)
            .replace("（", "(")
            .replace("）", ")")
            .replace("：", ":")
            .replace("．", ".")
            .replace("，", ",")
            .replace("；", ";")
            .replace("【", "[")
            .replace("】", "]")
            .replace("✔", "√")
            .replace("✅", "√")
            .replace("☑", "√")
            .replace("✘", "×")
            .replace("✖", "×")
            .replace("❌", "×")
            .replace("❎", "×")
            .replace("√", "√")
            .replace("×", "×")
            .lineSequence()
            .map { stripMarkdownDecorations(it.trimEnd()) }
            .map { stripProfileHeaderBeforeQuestion(it) }
            .filterNot { isNoiseLine(it.trim()) }
            .joinToString("\n")
            .replace(Regex(""" {2,}"""), " ")
            .replace(Regex("""\n{3,}"""), "\n\n")
            .trim()
    }

    /**
     * 去掉 Markdown 装饰：标题井号、加粗星号、行内代码反引号。
     * 不碰下划线：填空题依赖 `___` 作为横线标记。
     */
    private fun stripMarkdownDecorations(line: String): String {
        var text = line.replace(Regex("""^\s*#{1,6}\s*"""), "")
        text = text.replace("**", "")
        text = text.replace(Regex("""`([^`]*)`"""), "$1")
        return text.trimEnd()
    }

    private fun stripProfileHeaderBeforeQuestion(line: String): String {
        val marker = Regex("""^.*?(?:姓名|单位|岗位|班级|部门)\s*[:：]\s*(?=(?:第\s*)?\d{1,4}\s*(?:题)?\s*[.、．:：)）])""")
        return marker.replace(line, "").trimStart()
    }

    private fun normalizeFullWidthAscii(text: String): String {
        return buildString(text.length) {
            text.forEach { ch ->
                val code = ch.code
                append(
                    when (code) {
                        0x3000 -> ' '
                        in 0xFF01..0xFF5E -> (code - 0xFEE0).toChar()
                        else -> ch
                    }
                )
            }
        }
    }

    private fun isNoiseLine(line: String): Boolean {
        if (line.isBlank()) return false
        if (Regex("""^第\s*\d+\s*页\s*(?:共\s*\d+\s*页)?$""").matches(line)) return true
        if (Regex("""^-?\d{8,}$""").matches(line)) return true
        if (Regex("""^(?:绝密|请勿外传|忽略此处\.\.)$""").matches(line)) return true
        return false
    }
}
