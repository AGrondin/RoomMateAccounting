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

  def testGetIndex(table: DebtTable): Unit = {

  }

  def main(args: Array[String]):Unit = {
    val roomMates = List("Alexis", "Antoine", "Loic", "Sarah")

    val fileName = "src/RoomMateAccounting/account_entries.txt"

    val spent = readFile(fileName)
    //print(table.debtSq.length)

    val table = new DebtTable(roomMates)

    print((0 to 3).map(i => (0 to 3).map(j => table.toIndex(i,j)).mkString(",")).mkString("\n"))

    spent.foreach{x => (table.addDebt(x._1, x._2, x._3))}

    println(table.getRawString())

    println(table.getTotalsString())
    println()
    println()

    println(table.getDebtMapping().mkString(", "))

    println(table.getPaymentListStrings(table.getPaymentList(table.getDebtMapping())))
  }
}
