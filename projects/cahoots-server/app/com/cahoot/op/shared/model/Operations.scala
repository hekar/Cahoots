package com.cahoot.op.shared.model

trait Operation {
  def time: OperationalTimeStamp
}

object Operation {
  object PriorityQueueSorting {
	  implicit def orderByOperationalTimeStamp(o: Operation): 
		  Ordered[Operation] = new Ordered[Operation] {
	    def compare(other: Operation) = o.time.tick.compare(other.time.tick)
	  }
  }
}

case class InsertOperation(
    time: OperationalTimeStamp,
    startIndex: Int,
    content: String
) extends Operation

case class ReplaceOperation(
    time: OperationalTimeStamp,
    startIndex: Int,
    endIndex: Int,
    content: String
) extends Operation

case class DeleteOperation(
    time: OperationalTimeStamp,
    startIndex: Int,
    endIndex: Int
) extends Operation
