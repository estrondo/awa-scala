package awa.validation

type Valid[A] = Either[Seq[FailureNote], A]
