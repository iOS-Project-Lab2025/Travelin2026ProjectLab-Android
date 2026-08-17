package retrofit2

class HttpException(
    private val statusCode: Int,
    private val errorMessage: String?
) : Exception() {

    fun code(): Int = statusCode

    fun message(): String? = errorMessage
}