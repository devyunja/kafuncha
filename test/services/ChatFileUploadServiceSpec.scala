package services

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Injecting

class ChatFileUploadServiceSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
 "ChatFileUploadService" should {
   "work correctly" in {
     val chatFileUploadService = inject[ChatFileUploadService]
     val uuid = ChatFileUploadService.uuid
     println(s"uuid: $uuid")
   }
 }
}
