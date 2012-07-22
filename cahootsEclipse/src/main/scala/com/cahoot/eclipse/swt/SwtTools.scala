package com.cahoot.eclipse.swt
import org.eclipse.swt.widgets.Shell
import shapeless._
import Functions._
import com.cahoot.eclipse.scala.FunctionTypes
import org.eclipse.swt.widgets.Display

object SwtTools {

  val SwtDefaultFailure = {(t:Throwable) =>
    CahootMessage.error(null, "Error", t.getMessage())
    t.printStackTrace()
  }
  
  /**
   * TODO: Explain this function
   */
  def async[F, A <: HList, R](
    shell: Shell,
    success: F,
    failure: FunctionTypes.Failure
    )(implicit h: FnHListerAux[F, A => Unit],
              u: FnUnHListerAux[A => Unit, F]): F =
    { (args: A) =>

      require(shell != null, "Shell cannot be null")
      require(shell.getDisplay() != null, "The shell must have a display")

      val display = shell.getDisplay()
      display.asyncExec(new Runnable() {
        def run(): Unit = {
          try {
            success.hlisted(args)
          } catch {
            case e: Throwable =>
              failure(e)
          }
        }
      })
    }.unhlisted

  /**
   * TODO: Remove duplication
   */
  def sync[F, A <: HList, R](
    shell: Shell,
    success: F,
    failure: FunctionTypes.Failure
    )(implicit h: FnHListerAux[F, A => Unit],
              u: FnUnHListerAux[A => Unit, F]): F =
    { (args: A) =>

      require(shell != null, "Shell cannot be null")
      require(shell.getDisplay() != null, "The shell must have a display")

      val display = shell.getDisplay()
      display.syncExec(new Runnable() {
        def run(): Unit = {
          try {
            success.hlisted(args)
          } catch {
            case e: Throwable =>
              failure(e)
          }
        }
      })

    }.unhlisted

}