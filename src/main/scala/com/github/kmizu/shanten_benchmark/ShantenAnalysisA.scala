package com.github.kmizu
package shanten_benchmark

import scala.collection.mutable.ArrayBuffer
import scala.io.Source

object ShantenAnalysisA {

  val NUM_PIDS = 9 * 3 + 7
  var mentsus:ArrayBuffer[Array[Int]] = null

  def main(args: Array[String]) {
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

  def createMentsus():ArrayBuffer[Array[Int]] = {
    var mentsus = new ArrayBuffer[Array[Int]]()
    var pid = 0
    while(pid < NUM_PIDS) {
      mentsus.append(Array(pid, pid, pid))
      pid += 1
    }
    var t = 0
    while(t < 3) {
      var n = 0
      while(n < 7) {
        val pid = t * 9 + n
        mentsus.append(Array(pid, pid + 1, pid + 2))
        n += 1
      }
      t += 1
    }
    return mentsus
  }

  def calculateShantensu(pids:Seq[Int]):Int = {
    val countVector = pidsToCountVector(pids)
    return calculateShantensuInternal(countVector, new Array[Int](NUM_PIDS), 4, 0, Int.MaxValue)
  }

  def pidsToCountVector(pids:Seq[Int]):Array[Int] = {
    var countVector = new Array[Int](NUM_PIDS)
    var i = 0
    while(i < pids.length) {
      countVector(pids(i)) += 1
      i += 1
    }
    return countVector
  }

  def calculateShantensuInternal(
                                  currentVector:Array[Int],
                                  targetVector:Array[Int],
                                  leftMentsu:Int,
                                  minMentsuId:Int,
                                  foundMinShantensu:Int):Int = {
    var minShantensu = foundMinShantensu
    if(leftMentsu == 0) {
      var pid = 0
      while(pid < NUM_PIDS) {
        targetVector(pid) += 2
        if(isValidTargetVector(targetVector)) {
          val shantensu = calculateShantensuLowerbound(currentVector, targetVector)
          minShantensu = minShantensu.min(shantensu)
        }
        targetVector(pid) -= 2
        pid += 1
      }
    } else {
      var mentsuId = minMentsuId
      while (mentsuId < mentsus.length) {
        addMentsu(targetVector, mentsuId)
        val lowerbound = calculateShantensuLowerbound(currentVector, targetVector)
        if(isValidTargetVector(targetVector) && lowerbound < foundMinShantensu) {
          val shantensu = calculateShantensuInternal(
            currentVector, targetVector, leftMentsu - 1, mentsuId, minShantensu)
          minShantensu = minShantensu.min(shantensu)
        }
        removeMentsu(targetVector, mentsuId)
        mentsuId += 1
      }
    }
    return minShantensu
  }

  def calculateShantensuLowerbound(currentVector:Array[Int], targetVector:Array[Int]):Int = {
    var count = 0
    var pid = 0
    while (pid < NUM_PIDS) {
      if(targetVector(pid) > currentVector(pid)) {
        count += targetVector(pid) - currentVector(pid)
      }
      pid += 1
    }
    return count - 1
  }

  def isValidTargetVector(targetVector:Array[Int]):Boolean = {
    return targetVector.forall(_ <= 4)
  }

  def addMentsu(targetVector:Array[Int], mentsuId:Int) = {
    var mentsu = mentsus(mentsuId)
    var i = 0
    while(i < mentsu.length) {
      targetVector(mentsu(i)) += 1
      i += 1
    }
  }

  def removeMentsu(targetVector:Array[Int], mentsuId:Int) = {
    var mentsu = mentsus(mentsuId)
    var i = 0
    while(i < mentsu.length) {
      targetVector(mentsu(i)) -= 1
      i += 1
    }
  }

}
