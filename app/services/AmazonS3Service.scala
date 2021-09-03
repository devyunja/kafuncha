package services

import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.model.{Bucket, CannedAccessControlList, CopyObjectResult, DeleteObjectsRequest, DeleteObjectsResult, ObjectMetadata, PutObjectResult, S3Object, S3ObjectSummary}
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import play.api.Configuration

import java.io.{File, FileInputStream}
import java.nio.file.{Files, Paths}
import javax.inject.{Inject, Singleton}
import collection.JavaConverters._

case class AWSCredential(accessKey: String, secretKey: String)

object FileMimeType extends Enumeration {
  val TEXT: Value = Value("text/plain")
  val CSV: Value = Value("text/csv")
  val JSON: Value = Value("application/json")
  val XML: Value = Value("text/xml")
  val PDF: Value = Value("application/pdf")
  val ZIP: Value = Value("application/zip")
  val JPG: Value = Value("image/jpeg")
  val JPEG: Value = Value("image/jpeg")
  val PNG: Value = Value("image/png")
}

@Singleton
class AmazonS3Service @Inject()(config: Configuration) {
  val awsCredential: AWSCredential = AWSCredential(
    config.get[String]("aws.credentials.access-key"),
    config.get[String]("aws.credentials.secret-key")
  )

  val client: AmazonS3 = AmazonS3ClientBuilder
    .standard()
    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(
      awsCredential.accessKey, awsCredential.secretKey
    )))
    .withRegion(Regions.AP_SOUTHEAST_2)
    .build()

  val fileExtensionMap: Map[String, FileMimeType.Value] = Map(
    "txt" -> FileMimeType.TEXT,
    "csv" -> FileMimeType.CSV,
    "json" -> FileMimeType.JSON,
    "xml" -> FileMimeType.XML,
    "pdf" -> FileMimeType.PDF,
    "zip" -> FileMimeType.ZIP,
    "jpg" -> FileMimeType.JPG,
    "jpeg" -> FileMimeType.JPEG,
    "png" -> FileMimeType.PNG
  )

  def createBucket(bucketName: String): Bucket =
    client.createBucket(bucketName)

  def deleteBucket(bucketName: String): Unit = client.deleteBucket(bucketName)

  def listBuckets(): Seq[Bucket] = client.listBuckets().asScala

  def findBucket(bucketName: String): Boolean = listBuckets().exists(_.getName.equals(bucketName))

  def uploadObject(bucketName: String,
                   sourcePath: String,
                   objectKey: String,
                   contentType: FileMimeType.Value): PutObjectResult = {

    val bytes = Files.readAllBytes(Paths.get(sourcePath))
    val meta = new ObjectMetadata()
    meta.setContentType(contentType.toString)
    meta.setContentLength(bytes.length)
    client.putObject(bucketName, objectKey, new FileInputStream(new File(sourcePath)), meta)
  }

  def getContentType(file: String): FileMimeType.Value =
    fileExtensionMap.getOrElse(new File(file).getName.split("\\.").reverse.head, FileMimeType.TEXT)

  def uploadDirectory(bucketName: String, sourceDirectory: String, objectDirectory: String): Seq[PutObjectResult] = {
    val files = new File(sourceDirectory).list
    files.map { file =>
      uploadObject(
        bucketName,
        s"$sourceDirectory/$file",
        s"$objectDirectory/$file",
        getContentType(file))
    }
  }

  def setObjectPublic(bucketName: String, objectKey: String): Unit =
    client.setObjectAcl(bucketName, objectKey, CannedAccessControlList.PublicRead)

  def listObjects(bucketName: String): Seq[S3ObjectSummary] = client
    .listObjects(bucketName)
    .getObjectSummaries
    .asScala

  def deleteObject(bucketName: String, objectKey: String): Unit =
    client.deleteObject(bucketName, objectKey)

  def deleteObjects(bucketName: String, objectKeys: Seq[String]): DeleteObjectsResult = client
    .deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(objectKeys:_*))

  def deleteAllObjects(bucketName: String): Unit = listObjects(bucketName).foreach { obj =>
    deleteObject(bucketName, obj.getKey)
  }

  def copyObject(sourceBucketName: String,
                 sourceObjectKey: String,
                 destinationBucketName: String,
                 destinationObjectKey: String): CopyObjectResult = client
    .copyObject(sourceBucketName, sourceObjectKey, destinationBucketName, destinationObjectKey)

  def getObject(bucketName: String, objectKey: String): S3Object = client.getObject(bucketName, objectKey)

  def getObjectBytes(bucketName: String, objectKey: String): Array[Byte] =
    client.getObject(bucketName, objectKey).getObjectContent.readAllBytes()
}
