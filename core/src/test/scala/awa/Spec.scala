package awa

import scala.reflect.ClassTag
import zio.test.ZIOSpecDefault

abstract class Spec extends ZIOSpecDefault:

  protected def nameOf[T: ClassTag]: String = summon[ClassTag[T]].runtimeClass.getSimpleName
