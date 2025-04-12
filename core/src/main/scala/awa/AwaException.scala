package awa

abstract class AwaException(message: String, cause: Throwable = null)
    extends RuntimeException(message, cause, false, false)

object AwaException:

  class NotFound(message: String, cause: Throwable) extends AwaException(message, cause)

  class Unexpected(message: String, cause: Throwable) extends AwaException(message, cause)
