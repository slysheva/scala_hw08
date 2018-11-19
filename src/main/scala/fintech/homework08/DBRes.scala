package fintech.homework08

import java.sql.{Connection, DriverManager, PreparedStatement, ResultSet}

import scala.collection.mutable.ListBuffer

case class DBRes[A](run: Connection => A) {
  def execute(uri: String): A = {
    println("Opening connection to DB...")
    val conn = DriverManager.getConnection(uri)
    val res = run(conn)
    conn.close()
    println("Closing connection to DB...")
    res
  }

  def map[B](f: A => B): DBRes[B] = DBRes { conn => f(run(conn)) }

  def flatMap[B](f: A => DBRes[B]): DBRes[B] = DBRes { conn =>
    f(run(conn)).run(conn)
  }
}

object DBRes {
  def select[A](sql: String, params: Seq[Any])(read: ResultSet => A): DBRes[List[A]] = DBRes { conn =>
    val rs = prepare(sql, params, conn).executeQuery()
    readResultSet(rs, read)
  }

  def update(sql: String, params: Seq[Any]): DBRes[Unit] = DBRes { conn =>
    prepare(sql, params, conn).executeUpdate()
  }

  private def prepare(sql: String, params: Seq[Any], conn: Connection): PreparedStatement = {
    val ps = conn.prepareStatement(sql)

    for ((p, i) <- params.zipWithIndex)
      ps.setObject(i + 1, p)

    ps
  }

  private def readResultSet[A](rs: ResultSet, read: ResultSet => A): List[A] = {
    val buffer = new ListBuffer[A]
    while(rs.next()) {
      buffer.append(read(rs))
    }
    buffer.toList
  }
}

