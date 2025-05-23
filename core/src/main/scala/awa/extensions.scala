package awa

import awa.typeclass.CanBeEmpty
import zio.Cause
import zio.ZIO
import zio.ZIOAspect
import zio.stream.ZStream
import zio.stream.ZStreamAspect

extension [R, E, A](self: ZIO[R, E, A])

  inline def logDebug(inline f: A => String): ZIO[R, E, A] =
    self.tap { a =>
      ZIO.logDebug(f(a))
    }

  inline def annotated(inline annotations: (String, String)*): ZIO[R, E, A] =
    self @@ ZIOAspect.annotated(annotations*)

  inline def mapErrorToAwa(inline f: E => AwaException): ZIO[R, AwaException, A] =
    self.mapError(x => f(x))

  inline def logDebug(inline message: => String): ZIO[R, E, A] =
    self.tap(_ => ZIO.logDebug(message))

  inline def logWarn(inline message: => String): ZIO[R, E, A] =
    self.tapErrorCause(
      ZIO.logWarningCause(message, _),
    )

extension [T](self: Option[T])
  inline def getOrEmpty(using e: CanBeEmpty[T]): T =
    self match
      case Some(value) => value
      case None        => e.empty

extension (self: ZStream.type)
  inline def logWarningCause(inline message: => String, inline cause: => Cause[Any]): ZStream[Any, Nothing, Unit] =
    self.fromZIO(ZIO.logWarningCause(message, cause))

extension [R, E, A](self: ZStream[R, E, A])
  inline def annotated(inline annotations: (String, String)*): ZStream[R, E, A] =
    self @@ ZStreamAspect.annotated(annotations*)
