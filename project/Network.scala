package awa.sbt

import java.net.ServerSocket
import java.net.Socket

object Network {
  def getRandomPort(): Int = {
    val socket = new ServerSocket(0)
    try {
      val port = socket.getLocalPort()
      socket.close()
      port
    } finally {
      socket.close()
    }
  }

  def isOnline(port: Int): Boolean = {
    try {
      val socket = new Socket("localhost", port)
      socket.close()
      true
    } catch {
      case _ : Throwable => false
    }
  }
}
