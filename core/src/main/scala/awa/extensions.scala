package awa

import zio.ZIO
import zio.ZIOAspect

extension [R, A](self: ZIO[R, Throwable, A])
  inline def mapErrorToAwa(inline f: Throwable => AwaException): RIO[R, A] =
    self.mapError(f)

extension [R, A](self: ZIO[R, AwaException, A])
  inline def annotated(inline annotations: (String, String)*): RIO[R, A] =
    self @@ ZIOAspect.annotated(annotations*)

extension [R, E, A](self: ZIO[R, E, A])

  inline def logDebug(inline f: A => String): ZIO[R, E, A] =
    self.tap { a =>
      ZIO.logDebug(f(a))
    }

  inline def logDebug(inline message: => String): ZIO[R, E, A] =
    self.tap(_ => ZIO.logDebug(message))

  inline def logWarn(inline message: => String): ZIO[R, E, A] =
    self.tapErrorCause(
      ZIO.logWarningCause(message, _),
    )
