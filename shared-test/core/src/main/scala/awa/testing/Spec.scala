package awa.testing

import awa.UL
import org.locationtech.jts.geom.GeometryFactory
import scala.reflect.ClassTag
import zio.Runtime
import zio.ZLayer
import zio.logging.backend.SLF4J
import zio.test.TestAspect
import zio.test.TestEnvironment
import zio.test.ZIOSpecDefault
import zio.test.testEnvironment

abstract class Spec extends ZIOSpecDefault:

  override val bootstrap: ZLayer[Any, Any, TestEnvironment] =
    Runtime.removeDefaultLoggers >>> SLF4J.slf4j >>> testEnvironment

  protected def nameOf[T: ClassTag]: String =
    (summon[ClassTag[T]].runtimeClass.getSimpleName: String).replaceAll("""\$+""", "")

  protected def typeOf(value: AnyRef): String =
    value.getClass().getSimpleName().replaceAll("""\$+""", "")

  protected val geometryFactoryLayer: UL[GeometryFactory] =
    val geometryFactory = GeometryFactory()
    ZLayer.succeed(geometryFactory)

  protected val ignore = TestAspect.ignore
