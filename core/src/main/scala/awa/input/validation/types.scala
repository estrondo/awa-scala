package awa.input.validation

type Valid[A] = Either[Seq[FailureNote], A]
