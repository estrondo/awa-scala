package awa.sbt

import sbt.util.Logger

trait LoggerHelper {
  
  def logger(implicit ctx: BuildContext): Logger = ctx.logger
}
