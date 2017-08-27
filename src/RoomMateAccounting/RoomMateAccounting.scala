package RoomMateAccounting
import scala.io.Source

object RoomMateAccounting {

  def readFile(filePath: String): List[(String, Double, Option[List[String]])] = {
    val file = Source.fromFile(filePath)

    val input = file.getLines().map(s => s.split(" ")).map(l => l match {
      case l if l.length == 2 => (l(0),l(1).toDouble,None)
      case l if l.length > 2 => (l(0), l(1).toDouble, Some(l.drop(2).toList))
    }).toList

    input
  }

  def compareOutputs(numSim: Int):List[Boolean] = {

    val tester = new debtTableTest()

    (0 to numSim-1).map(i => {
      val members = tester.generateMemberSS()
      val payments = tester.createSpendingList(50, members)
      compareComputer(members, payments)
    }).toList
  }

  def compareComputer(fakeMembers: List[String], fakePayments:List[(String, Double)]): Boolean = {

    val table = new DebtTable(fakeMembers)

    val totalDirect: Seq[Double] = table.getTotals(fakePayments.map(x => (x._1, x._2, None)))

    table.addDebts(fakePayments.map(x => (x._1, x._2, None)))

    val totalIndirect: Seq[Double] = table.getTotals()

    return !totalDirect.zip(totalIndirect).exists(x => !approximatelyEqual(x._1, x._2))

  }

  def approximatelyEqual(f: Double, s: Double): Boolean = {

    return ((f-s).abs < 0.0001)

  }


  def main(args: Array[String]):Unit = {

      println(compareOutputs(100).mkString(System.lineSeparator()))


//    val roomMates = List("Alexis", "Antoine", "Loic", "Sarah")
//
//    val fileName = "src/RoomMateAccounting/account_entries.txt"
//
//    val spent = readFile(fileName)
//    //print(table.debtSq.length)
//
//    val table = new DebtTable(roomMates)
//
//    //print((0 to 3).map(i => (0 to 3).map(j => table.toIndex(i,j)).mkString(",")).mkString("\n"))
//
//    spent.foreach{x => (table.addDebt(x._1, x._2, x._3))}
//
//    println("Raw string from filled table"+System.lineSeparator())
//
//    println(table.getRawString())
//
//    println(table.getTotalsString())
//    println()
//    println()
//
//    val totals = table.getTotals(spent)
//
//    println("Table with direct totals computation")
//
//    println(table.getPaymentListStrings(table.getPaymentList(table.getDebtMapping(totals))))
//
//    println("Table with indirect totals computation")
//
//    println(table.getDebtMapping().mkString(", "))
//
//    println(table.getPaymentListStrings(table.getPaymentList(table.getDebtMapping())))
  }
}
