package droletours

import java.util.concurrent.atomic.AtomicInteger

import cats.data._
import cats.instances.all._
import cats.syntax.all._
import com.twitter.demo.thriftscala.{User, UserService}
import com.twitter.finagle.Thrift
import com.twitter.util.{Await, Duration, Future}

import scala.language.postfixOps




object Main extends App {
  type LiOpt[A] = OptionT[List,A]

  val isEven = (n:Int) => n % 2 == 0
  val optEvent = (n: Int) => if (n % 2 == 0) Some(n) else None
  val eitherEven = (n: Int) => if(n % 2 == 0) Right(n) else Left(s"$n is not even")
  val validateEven = (n: Int) => if(n % 2 == 0) Validated.valid(n) else Validated.invalid(NonEmptyVector.one(s"$n is not even"))
  val s = 32.pure[LiOpt]
  println(s)
  val nums = (1 to 20).toList
  val someEven = OptionT(nums.map(optEvent))
  println(someEven.value.sequence)
  println(someEven)
  println(someEven.map(a => a *2))
  val nostack = nums.map(optEvent)
  println(nostack)
  println(nostack.sequence)
  println(nostack filter {_.isDefined} sequence)
  println(nums.traverse(validateEven))
  println(nums filter isEven traverse optEvent)
  println(nums traverse eitherEven)
  println(nums traverse validateEven)

  val address = "localhost:1234"

  val server2 = Thrift.server.serveIface(
    address,
    new UserService[Future] {
      private val userIdCounter: AtomicInteger = new AtomicInteger(0)

      override def createUser(name: String): Future[User] = {
        val id = userIdCounter.incrementAndGet()
        Future.value(User(id, name))
      }
    }
  )

  val client = Thrift.client.newIface[UserService.FinagledClient](address,"myClient")

  val user1 = client.createUser("hehei").foreach(a => println(a))
  val user2 = client.createUser("hello").foreach(a => println(a))

  Await.all(List(user1,user2),Duration.fromSeconds(2))
  server2.close()
  client.service.close()
}
