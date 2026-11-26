package com.fufu.quiz.ui.util

import com.fufu.quiz.state.DEFAULT_BANK_GROUP_NAME
import com.fufu.quiz.state.QuizBank

fun bankDisplayPath(groupName: String, bankName: String): String {
    val cleanGroupName = groupName.ifBlank { DEFAULT_BANK_GROUP_NAME }
    return "$cleanGroupName / $bankName"
}

fun bankDisplayPath(bank: QuizBank): String {
    return bankDisplayPath(bank.groupName, bank.name)
}
