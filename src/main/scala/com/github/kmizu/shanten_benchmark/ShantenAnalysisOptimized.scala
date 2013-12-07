package com.github.kmizu.shanten_benchmark

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

object ShantenAnalysisOptimized {

  val NUM_PIDS = 9 * 3 + 7
  var mentsus:ArrayBuffer[Array[Int]] = null

  def main(args: Array[String]): Unit = {
    val result = benchmark{
      mentsus = createMentsus()
      val src = Source.fromFile("shanten_benchmark_data.num.txt")
      for (line <- src.getLines) {
        val sline = line.stripLineEnd
        val row = sline split " "
        val pids = row.slice(0, row.length - 1).map(_.toInt)
        val expectedShantensu = row(row.length - 1).toInt
        val actualShantensu = calculateShantensu(pids)
        if(actualShantensu != expectedShantensu) {
          throw new RuntimeException(
            "Shantensu mismatch: %d != %d: %s" format (actualShantensu, expectedShantensu, sline))
        }
      }
    }
    printResult(result.median)
  }

  def createMentsus(): ArrayBuffer[Array[Int]] = {
    var mentsus = new ArrayBuffer[Array[Int]]()
    for(pid <- 0 until NUM_PIDS) {
      mentsus.append(Array(pid, pid, pid))
    }
    for(t <- 0 until 3) {
      for(n <- 0 until 7) {
        val pid = t * 9 + n
        mentsus.append(Array(pid, pid + 1, pid + 2))
      }
    }
    mentsus
  }

  def calculateShantensu(pids: Seq[Int]): Int = {
    val countVector = pidsToCountVector(pids)
    calculateShantensuInternal(countVector, new Array[Int](NUM_PIDS), 4, 0, Int.MaxValue)
  }

  def pidsToCountVector(pids:Seq[Int]): Array[Int] = {
    val countVector = new Array[Int](NUM_PIDS)
    for(pid <- pids) {
      countVector(pid) += 1
    }
    countVector
  }

  def calculateShantensuInternal(
    currentVector: Array[Int],
    targetVector: Array[Int],
    leftMentsu: Int,
    minMentsuId: Int,
    foundMinShantensu: Int
  ): Int = {
    var minShantensu = foundMinShantensu
    if(leftMentsu == 0) {
      for(pid <- 0 until NUM_PIDS) {
        targetVector(pid) += 2
        if(isValidTargetVector(targetVector)) {
          val shantensu = calculateShantensuLowerbound(currentVector, targetVector)
          minShantensu = minShantensu.min(shantensu)
        }
        targetVector(pid) -= 2
      }
    } else {
      for(mentsuId <- minMentsuId until mentsus.length) {
        addMentsu(targetVector, mentsuId)
        val lowerbound = calculateShantensuLowerbound(currentVector, targetVector)
        if(isValidTargetVector(targetVector) && lowerbound < foundMinShantensu) {
          val shantensu = calculateShantensuInternal(
            currentVector, targetVector, leftMentsu - 1, mentsuId, minShantensu)
          minShantensu = minShantensu.min(shantensu)
        }
        removeMentsu(targetVector, mentsuId)
      }
    }
    minShantensu
  }

  def calculateShantensuLowerbound(currentVector: Array[Int], targetVector: Array[Int]): Int = {
    var count = 0
    for(pid <- 0 until NUM_PIDS) {
      if(targetVector(pid) > currentVector(pid)) {
        count += targetVector(pid) - currentVector(pid)
      }
    }
    count - 1
  }

  def isValidTargetVector(targetVector: Array[Int]): Boolean = {
    var pid = 0
    while (pid < targetVector.length) {
      if (targetVector(pid) > 4) return false
      pid += 1
    }
    true
  }

  def addMentsu(targetVector :Array[Int], mentsuId :Int): Unit = {
    for(pid <- mentsus(mentsuId)) {
      targetVector(pid) += 1
    }
  }

  def removeMentsu(targetVector: Array[Int], mentsuId:Int): Unit = {
    for(pid <- mentsus(mentsuId)) {
      targetVector(pid) -= 1
    }
  }

}
