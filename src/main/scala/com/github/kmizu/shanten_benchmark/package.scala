package com.github.kmizu
import scala.testing.Benchmark

package object shanten_benchmark {
  class SimpleBenchmark[A](body: => A) extends Benchmark {
    def run(): Unit = body
  }

  def benchmark[A](body: => A, numberOfTimes: Int = 10): List[Long] = {
    new SimpleBenchmark[A](body).runBenchmark(numberOfTimes)
  }

  implicit class RichListLong(self: List[Long]) {
    def median: Long = {
      val sorted = self.sorted
      val size = self.size
      if (size % 2 != 0) {
        sorted(size / 2)
      } else {
        val a = sorted((size / 2) - 1)
        val b = sorted(size / 2)
        (a + b) / 2
      }
    }
  }

  def printResult(milliSeconds: Long): Unit = {
    println(s"${milliSeconds}ms")
  }
}
