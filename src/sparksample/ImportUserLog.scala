package sparksample

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.SparkContext._
import org.apache.spark.streaming._
import org.apache.spark.streaming.StreamingContext._


object ImportUserLog {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("ImportUserLog").setMaster("spark://kudu01:7077")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Seconds(1))

    val lines = ssc.socketTextStream("kudu01", 12345)
    lines.foreachRDD(rdd => rdd.foreach(x => println(x)))

    ssc.start()

    ssc.awaitTermination()
  }
}

