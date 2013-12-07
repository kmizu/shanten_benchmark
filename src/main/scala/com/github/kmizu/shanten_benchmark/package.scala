package com.github.kmizu
import scala.testing.Benchmark

package object shanten_benchmark {
  class SimpleBenchmark[A](body: => A) extends Benchmark {
    def run(): Unit = body
  }

  def benchmark[A](body: => A, numberOfTimes: Int = 1): List[Long] = {
    new SimpleBenchmark[A](body).runBenchmark(numberOfTimes)
  }
}
