package RoomMateAccounting

import scala.util.Random

class debtTableTest {

  def generateMemberSS(): List[String]= {

    val fullMembers = List("Henry", "Steve", "John", "albert", "Condoleeza")

    val num_members = Random.nextInt(4)+2

    Random.shuffle(fullMembers).take(num_members)

  }

  def getSpending(members: List[String]): (String, Double) = {

    return (members(Random.nextInt(members.size)), Random.nextDouble()*100)

  }

  def createSpendingList(numSimulations: Int, members: List[String]): List[(String, Double)] = {

    (0 to numSimulations).map(i => getSpending(members)).map(x => (x._1,x._2)).toList
  }





}
