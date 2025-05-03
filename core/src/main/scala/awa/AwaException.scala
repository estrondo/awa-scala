package awa

import awa.validation.FailureNote

abstract class AwaException(message: String, cause: Throwable = null)
    extends RuntimeException(message, cause, false, false)

object AwaException:

  class NotFound(message: String, cause: Throwable) extends AwaException(message, cause)

  class Unexpected(message: String, cause: Throwable) extends AwaException(message, cause)

  class Conversion(message: String, cause: Throwable) extends AwaException(message, cause)

  class WithFailureNote(message: String, val failures: Seq[FailureNote]) extends AwaException(message):

    override def getMessage(): String =
      val buiilder = StringBuilder()
      buiilder.addAll(super.getMessage())
      for note <- failures do buiilder.addAll(s"\n${note.note}: ${note.description}")
      buiilder.result()
