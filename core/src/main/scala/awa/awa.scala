package awa

import zio.ZIO
import zio.ZIOAspect

type IO[A] = ZIO[Any, AwaException, A]

type RIO[R, A] = ZIO[R, AwaException, A]

extension [R, A](self: ZIO[R, Throwable, A])
  inline def mapErrorToAwa(inline f: Throwable => AwaException): RIO[R, A] =
    self.mapError(f)

extension [R, A](self: ZIO[R, AwaException, A])
  inline def annotated(inline annotations: (String, String)*): RIO[R, A] =
    self @@ ZIOAspect.annotated(annotations*)
