package RoomMateAccounting

import scala.collection.mutable.ListBuffer

class DebtTable(mems: List[String]) {

  var members: Seq[String] = mems
  val size = members.size
  val debtSq: scala.collection.mutable.ListBuffer[Double] = {
    val sz = mems.size
    //print(sz)
    //print((sz*sz-sz)/2)
    ListBuffer.fill((sz*sz-sz)/2)(0.0);
  };

  def getIndex(member: String) : Int = members.indexOf(member)

  def getName(i: Int): String = members(i)

  def toIndex(x: Int, y: Int): Int = {

    x*(size-1)-x-(x*x-x)/2+y-1;
  }

  def increase(x:Int, y:Int, amount: Double): Unit ={
    x match {
      case q if q<y => {debtSq(toIndex(q,y)) += amount }
      case s if s>y => {debtSq(toIndex(y,s)) -= amount}
      case r if r==y =>;
    }
  }

  def addDebt(spender:Int, amount: Double, receivers: Option[List[Int]]):Unit ={
    val rec = receivers.filter(l => !l.isEmpty).getOrElse((0 to size-1).toList)

    rec.foreach(x => increase(spender,x, amount/rec.size.toDouble))
  }

  def addDebt(spender:String, amount:Double, receivers: Option[List[String]]):Unit ={
    addDebt(members.indexOf(spender), amount, receivers.map(l => l.filter(s => members.contains(s)).map(s => members.indexOf(s))))
  }

  def getMemberString(x:Int):String = {
    members(x)+": "+(0 to size-1).map(i => this(x,i)+" to " + members(i)).mkString(", ")
  }

  def getRawString():String = (0 to size-1).map(x=>getMemberString(x)).mkString("\n")

  def getTotalsString():String = getDebtMapping().map{t=> t._1+" owes "+t._2.toString()}.mkString(",")

  //How much y owes to x
  def apply(x:Int, y:Int): Double = x match {
    case q if q<y => debtSq(toIndex(x,y))
    case p if p>y => -debtSq(toIndex(y,x))
    case r => 0.0
  }

  def getPayments(payers:List[(String,Double)], receivers:List[(String,Double)]):List[(String,String,Double)] = (payers, receivers) match {

    case (p,q) if p.isEmpty || q.isEmpty => List()
    case  (p,q) =>
      val sortedPayers = p.sortBy(-_._2)
      val sortedReceivers = q.sortBy(-_._2)


      val maxReceiver = sortedReceivers.head
      val maxPayer = sortedPayers.head

      val payment = (maxPayer._1, maxReceiver._1, Math.min(maxPayer._2, maxReceiver._2))

      maxPayer._2 match {
        case x if x>maxReceiver._2 => payment::getPayments((maxPayer._1,x-payment._3)::sortedPayers.tail, sortedReceivers.tail)
        case x if x<maxReceiver._2 => payment::getPayments(sortedPayers.tail, (maxReceiver._1, maxReceiver._2-payment._3)::sortedReceivers.tail)
        case x if x==maxReceiver._2 => payment::getPayments(sortedPayers.tail, sortedReceivers.tail)
      }
  }

  def splitGroup(roomMates:List[(String, Double)]):(List[(String,Double)], List[(String,Double)]) = {
    val groups = roomMates.partition(_._2>0)

    (groups._1, groups._2.map(x => (x._1, -x._2)))
  }

  def getPaymentList(roomMates:List[(String, Double)]):List[(String,String,Double)] = {
    val (payers, receivers) = splitGroup(roomMates)

    println(payers.mkString(","))
    println(receivers.mkString(" "))

    getPayments(payers, receivers)
  }

  def getPaymentListStrings(payments: List[(String, String, Double)]):String ={
    payments.map(x =>{
      x._1 + " owes "+"%05.2f".format(x._3)+" to "+x._2
    }).mkString("\r\n")
  }

  //Total debt from a roommate index
  def getTotalDebt(i: Int): Double = {
    (0 to size-1).filter(x => x!=i).map(x => this(x,i)).sum
  }

  def getTotals(): List[Double] = {

    (0 to size-1).map(getTotalDebt).toList

  }

  def getDebtMapping(): List[(String, Double)] = {

    members.zip(getTotals()).toList
  }

}
