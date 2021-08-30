package services

import models.SparkSessionProvider
import org.apache.spark.sql.{DataFrame, SaveMode}

import javax.inject.{Inject, Singleton}

object SnapshotPaths extends Enumeration {
  val BASE_PATH: Value = Value("conf/snapshots")
  val LAST_LEFT_MEMBERS: Value = Value(s"${BASE_PATH.toString}/last_left_member_snapshot.csv")
}

@Singleton
class SnapshotService @Inject()(fileService: FileService) extends SparkSessionProvider {
  def exportSnapshot(snapshotDataFrame: DataFrame, path: String, overwrite: Boolean = false): Unit = {
    if (!fileService.isExist(path)) {
      snapshotDataFrame
        .coalesce(1)
        .write
        .option("header", "true")
        .option("sep", ",")
        .csv(path)
    }
    else if (fileService.isExist(path) && overwrite) {
      snapshotDataFrame
        .coalesce(1)
        .write
        .mode(SaveMode.Overwrite)
        .option("header", "true")
        .option("sep", ",")
        .csv(path)
    }
    else throw new RuntimeException(s"File is already exist: $path")
  }
}
