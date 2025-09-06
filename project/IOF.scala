package awa.sbt

import sbt.io.IO
import java.io.File

object IOF {
  
  def createDirectory(f: File): File = {
    IO.createDirectory(f)
    f
  }
}
