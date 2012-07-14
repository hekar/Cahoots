package com.cahoot.eclipse.core

object ThreadQueueRunner {

  /**
   * Run a thread from a closure
   */
  def run(closure: () => Unit): Thread = {
    val thread = new Thread {
      new Runnable() {
        def run(): Unit = {
          closure.apply();
        }
      }
    }

    thread.start

    thread
  }

}