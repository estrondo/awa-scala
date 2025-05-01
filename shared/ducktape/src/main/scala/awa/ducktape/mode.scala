package awa.ducktape

import awa.input.validation.FailureNote
import io.github.arainko.ducktape.Mode

//noinspection ScalaFileName
given Mode.Accumulating.Either[FailureNote, Seq]()
