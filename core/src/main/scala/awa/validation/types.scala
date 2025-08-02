package awa.validation

type IsValid[A] = Either[Seq[FailureNote], A]
