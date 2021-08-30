package services

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Injecting

class SnapshotServiceSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  "SnapshotService" should {
    "work correctly" in {
      val snapshotService = inject[SnapshotService]

      snapshotService.exportSnapshot(snapshotService.chatDataFrame, SnapshotPaths.LAST_LEFT_MEMBERS.toString, overwrite = true)
    }
  }
}
