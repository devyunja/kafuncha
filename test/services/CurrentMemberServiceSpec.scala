package services

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Injecting

class CurrentMemberServiceSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  "CurrentMemberService#currentMembers" should {
    "get current member list in chat room" in {
      val currentMemberService = inject[CurrentMemberService]
      val currentMembers = currentMemberService.currentMembers
      currentMembers.zipWithIndex.foreach(memberWithIndex => println(s"${memberWithIndex._2}: ${memberWithIndex._1}") )
    }
  }
}
