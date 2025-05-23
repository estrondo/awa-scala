package awa

import awa.validation.FailureNote

abstract class AwaException(message: String, cause: Throwable = null)
    extends RuntimeException(message, cause, false, false)

object AwaException:

  class NotFound(message: String, cause: Throwable) extends AwaException(message, cause)

  class Unexpected(message: String, cause: Throwable) extends AwaException(message, cause)

  class Conversion(message: String, cause: Throwable) extends AwaException(message, cause)

  class Repository(message: String, cause: Throwable = null) extends AwaException(message, cause)

  class Kafka(message: String, cause: Throwable = null) extends AwaException(message, cause)

  class Decode(message: String, cause: Throwable = null) extends AwaException(message, cause)

  class Encode(message: String, cause: Throwable = null) extends AwaException(message, cause)

  class WithNote(message: String, val notes: Seq[FailureNote]) extends AwaException(message):

    override def getMessage(): String =
      val builder = StringBuilder()
      builder.addAll(super.getMessage())
      for note <- notes do builder.addAll(s"\n${note.note}: ${note.description}")
      builder.result()
