package awa.generator

import java.util.Random
import java.util.concurrent.ThreadLocalRandom

trait KeyGenerator:

  def apply(length: KeyGenerator.Length): String

object KeyGenerator extends KeyGenerator:
  enum Length(val value: Int):
    case L4  extends Length(1)
    case L8  extends Length(2)
    case L16 extends Length(4)
    case L32 extends Length(8)
    case L64 extends Length(16)

  override def apply(length: Length): String =
    val random  = ThreadLocalRandom.current()
    val builder = StringBuilder()
    for _ <- 0 until length.value do builder.addAll(generate(random))
    builder.result()

  private def generate(random: Random): String =
    val value = random.nextInt() & 0xfffff
    val str   = for x <- Integer.toString(value, 32) yield if random.nextBoolean() then x.toUpper else x
    if value > 0x7fff then str
    else if value > 0x3ff then s"0$str"
    else if value > 0x1f then s"00$str"
    else s"000$str"
