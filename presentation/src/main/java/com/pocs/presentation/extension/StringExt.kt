package com.pocs.presentation.extension

fun String.isValidEmail(): Boolean {
    return this.isNotEmpty() && Regex(
        "[a-zA-Z0-9+._%\\-]{1,256}" +
            "@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
    ).matches(this)
}

private fun String.isValidGithubUserUrl(): Boolean {
    return Regex("https://github\\.com/[a-zA-Z0-9\\-]{1,38}").matches(this)
}

fun String.canSaveAsGithubUrl(): Boolean {
    return this.isEmpty() || this.isValidGithubUserUrl()
}
