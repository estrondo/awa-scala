package awa

import zio.ZIO
import zio.ZLayer
import zio.stream.ZStream

type F[A] = ZIO[Any, AwaException, A]

type RF[R, A] = ZIO[R, AwaException, A]

type EF[E, A] = ZIO[Any, E, A]

type REF[R, E, A] = ZIO[R, E, A]

type L[A] = ZLayer[Any, AwaException, A]

type UL[A] = ZLayer[Any, Nothing, A]

type S[A] = ZStream[Any, AwaException, A]

type RS[R, A] = ZStream[R, AwaException, A]

type ES[E, A] = ZStream[Any, E, A]

type RES[R, E, A] = ZStream[R, E, A]
