package awa.generator

import java.util.Random
import java.util.concurrent.ThreadLocalRandom
import scala.annotation.tailrec

trait KeyGenerator:

  def apply(length: KeyGenerator.Length): String
  def generateL8(): String  = apply(KeyGenerator.Length.L8)
  def generateL16(): String = apply(KeyGenerator.Length.L16)
  def generateL32(): String = apply(KeyGenerator.Length.L32)
  def generateL64(): String = apply(KeyGenerator.Length.L64)

object KeyGenerator extends KeyGenerator:

  private val symbols = "0123456789aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ".toCharArray()

  enum Length(val value: Int):
    case L8  extends Length(1)
    case L16 extends Length(2)
    case L32 extends Length(4)
    case L64 extends Length(8)

  override def apply(length: Length): String =
    val random  = ThreadLocalRandom.current()
    val builder = StringBuilder()
    var count   = length.value
    while count != 0 do
      populate(random, builder)
      count -= 1

    builder.result()

  private def populate(random: Random, builder: StringBuilder): Unit =
    var bits  = random.nextLong()
    var count = 8
    while count != 0 do
      bits = addNextSymbol(builder, random, bits)
      count -= 1

  @tailrec
  private def addNextSymbol(builder: StringBuilder, random: Random, bits: Long): Long =
    val index = (bits & 0x3f).toInt
    if index < symbols.length then
      builder.addOne(symbols(index))
      bits >>> 6
    else addNextSymbol(builder, random, random.nextLong())
