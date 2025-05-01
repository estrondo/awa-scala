package awa.grpc

import io.grpc.StatusException
import zio.ZIO

type GrpcIO = [A] =>> ZIO[Any, StatusException, A]
