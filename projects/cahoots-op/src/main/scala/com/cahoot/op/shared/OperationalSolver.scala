package com.cahoot.op.shared

import com.cahoot.op.shared.model._
import scala.collection.mutable.SynchronizedPriorityQueue
import scala.collection.mutable.PriorityQueue

class OperationalSolver {
  
  import Operation.PriorityQueueSorting._
  
  val batch: PriorityQueue[Operation] = 
    new SynchronizedPriorityQueue[Operation]()
  
  def queue(opt: Operation) = {
    batch += opt
  }

  def solve(document: OperationalDocument) = {
    // TODO: update git
	batch.toList.map { opt =>
	  opt match {
	    case o: InsertOperation =>
	      document.content.insert(o.startIndex, o.content)
	    case o: ReplaceOperation =>
	      document.content.replace(o.startIndex, o.endIndex, o.content)
	    case o: DeleteOperation =>
	      document.content.delete(o.startIndex, o.endIndex)
	    case _ =>
	      null
	  }
	}
  }
}