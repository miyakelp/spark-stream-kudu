package sparksample

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql._
import org.apache.spark.sql.types._
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.streaming._
import org.apache.spark.streaming.StreamingContext._
import org.apache.kudu.spark.kudu._
import org.apache.kudu.client._

case class Log(id:String, unixTime:Int, userId:Int, score:Int)


object ImportUserLog {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("ImportUserLog")
      .getOrCreate()
    val sc = spark.sparkContext
    val ssc = new StreamingContext(sc, Seconds(1))
    import spark.implicits._

    val kc = new KuduContext("kudu01:7051", sc)

    val schema = StructType(List(
      StructField("id", StringType, nullable = false),
      StructField("unix_time", StringType, nullable = true),
      StructField("user_id", StringType, nullable = true),
      StructField("score", StringType, nullable = true)
    ))

    val lines = ssc.socketTextStream("kudu01", 12345)
    lines
      .map(_.split(","))
      .map(x => (x(1), x))
      .reduceByKey((x, y) => y)
      .map(_._2)
      .foreachRDD(rdd => rdd.foreach( x => {
          //val row = sc.parallelize(Seq(Row(x(1), x(0).toInt, x(2).toInt, x(3).toInt)))
          //val df = spark.createDataFrame(row, schema)
          val kuduClient = kc.syncClient
          val kuduSession = kuduClient.newSession()
          kuduSession.setFlushMode(SessionConfiguration.FlushMode.AUTO_FLUSH_BACKGROUND)
          val table = kuduClient.openTable("impala::default.user_logs")
          val op = table.newInsert()
          val row = op.getRow
          row.addString("id", x(1))
          row.addInt("unix_time", x(0).toInt)
          row.addInt("user_id", x(2).toInt)
          row.addInt("score", x(3).toInt)
          kuduSession.apply(op)

          kuduSession.flush()
          kuduSession.close()
//          kc.insertRows(df, "user_logs")
        })
      )

    ssc.start()

    ssc.awaitTermination()
  }
}

