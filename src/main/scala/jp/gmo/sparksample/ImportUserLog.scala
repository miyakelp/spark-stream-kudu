package jp.gmo.sparksample

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.SparkContext._


object ImportUserLog {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("ImportUserLog").setMaster("spark://kudu01:7077")
    val sc = new SparkContext(conf)
    sc.stop()
  }
}

