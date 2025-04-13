package awa.testing

import zio.test.ZIOSpecDefault

import scala.reflect.ClassTag

abstract class Spec extends ZIOSpecDefault:

  protected def nameOf[T: ClassTag]: String = summon[ClassTag[T]].runtimeClass.getSimpleName
