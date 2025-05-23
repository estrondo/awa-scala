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

type FStream[A] = ZStream[Any, AwaException, A]

type RStream[R, A] = ZStream[R, AwaException, A]

type EStream[E, A] = ZStream[Any, E, A]

type REStream[R, E, A] = ZStream[R, E, A]
