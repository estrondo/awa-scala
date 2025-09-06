package awa.sbt

import java.io.File
import sys.process.Process
import sys.process.ProcessLogger

class FailedProcessOutcomeException(
  val processInvocation: ProcessInvocation, 
  val code: Int, 
  val out: Seq[String], 
  val err: Seq[String],
) extends RuntimeException(processInvocation.command.mkString(" ") + "\n" + err.mkString("\n"))
sealed trait ProcessOutcome

case class ProcessInvocation(command: Seq[String], cwd: File, env: Map[String, String])

case class SuccessProcessOutcome(out: Seq[String], err: Seq[String]) extends ProcessOutcome

case class FailedProcessOutcome(processInvocation: ProcessInvocation, code: Int, out: Seq[String], err: Seq[String]) 
  extends ProcessOutcome {
  def exception: FailedProcessOutcomeException = new FailedProcessOutcomeException(processInvocation, code,out, err)
}


trait ProcessHandler {
  
  protected def runProcess(command: Seq[String], cwd: File, env: Map[String, String] = Map.empty): ProcessOutcome = {
    val out = Vector.newBuilder[String]
    val err = Vector.newBuilder[String]

    val code = Process(command, cwd, env.toSeq:_*) ! ProcessLogger(out.+=, err.+=)
    if (code == 0) 
      SuccessProcessOutcome(out.result(), err.result()) 
    else 
      FailedProcessOutcome(ProcessInvocation(command, cwd, env), code, out.result(), err.result())
  }
}
