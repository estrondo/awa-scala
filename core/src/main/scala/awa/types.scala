package awa

import zio.ZIO

type IO[A] = ZIO[Any, AwaException, A]

type RIO[R, A] = ZIO[R, AwaException, A]

type EA[E, A] = ZIO[Any, E, A]
