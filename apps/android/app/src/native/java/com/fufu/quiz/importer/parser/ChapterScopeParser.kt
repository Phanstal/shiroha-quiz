package com.fufu.quiz.importer.parser

/**
 * 章节标题识别。
 *
 * 题库文档里常见的章标题写法：
 *   第1章 信息化发展 / 第 1 章 信息化发展 / 第一章 信息化发展 / 第1章
 * 归属统一归一成 `第N章`（N 用阿拉伯数字），章名单独保留，用于展示和练习范围。
 */
object ChapterScopeParser {
    private val chapterRegex = Regex("""第\s*([一二三四五六七八九十百0-9]+)\s*章""")
    private val chapterHeadingRegex = Regex("""^\s*第\s*([一二三四五六七八九十百0-9]+)\s*章\s*(.*)$""")
    private val questionStemCue = Regex("""(?:[（(]\s*[)）]|下列|以下|哪[一项种个]|不正确|属于|包括|是指|正确的是)""")

    fun normalizeChapter(raw: String): String {
        val token = chapterRegex.find(raw)?.groupValues?.getOrNull(1).orEmpty()
        if (token.isBlank()) return ""
        val number = normalizeQuestionIndex(token)
        return if (number.isNotBlank()) "第${number}章" else "第${token}章"
    }

    fun hasChapter(raw: String): Boolean = normalizeChapter(raw).isNotBlank()

    /**
     * 识别「一整行就是章标题」的情况，返回归一后的章节键和章名。
     * 题干里顺带出现“第1章”不会命中这里，因为要求整行以「第X章」开头且没有题干特征。
     */
    fun parseHeading(rawLine: String): ChapterHeading? {
        val line = rawLine.trim()
        if (line.isBlank()) return null
        val match = chapterHeadingRegex.find(line) ?: return null
        val key = normalizeChapter(line)
        if (key.isBlank()) return null
        val title = match.groupValues[2].trim().trim(':', '：', '.', '、', '-', '—')
        if (questionStemCue.containsMatchIn(title)) return null
        if (title.length > 40) return null
        return ChapterHeading(key = key, title = title)
    }
}

data class ChapterHeading(val key: String, val title: String) {
    /** 用于展示和保存的章标题：有章名时带上，例如「第1章 信息化发展」。 */
    val displayName: String get() = if (title.isBlank()) key else "$key $title"
}
