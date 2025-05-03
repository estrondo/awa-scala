package awa

import zio.ZIO

type F[A] = ZIO[Any, AwaException, A]

type RF[R, A] = ZIO[R, AwaException, A]

type EF[E, A] = ZIO[Any, E, A]

type REF[R, E, A] = ZIO[R, E, A]
