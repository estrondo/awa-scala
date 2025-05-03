package awa

import zio.ZIO
import zio.ZIOAspect

extension [R, E, A](self: ZIO[R, E, A])

  inline def logDebug(inline f: A => String): ZIO[R, E, A] =
    self.tap { a =>
      ZIO.logDebug(f(a))
    }

  inline def annotated(inline annotations: (String, String)*): ZIO[R, E, A] =
    self @@ ZIOAspect.annotated(annotations*)

  inline def mapErrorToAwa(inline f: Throwable => AwaException)(using
      inline e: E <:< Throwable,
  ): ZIO[R, AwaException, A] =
    self.mapError(x => f(e(x)))

  inline def logDebug(inline message: => String): ZIO[R, E, A] =
    self.tap(_ => ZIO.logDebug(message))

  inline def logWarn(inline message: => String): ZIO[R, E, A] =
    self.tapErrorCause(
      ZIO.logWarningCause(message, _),
    )
