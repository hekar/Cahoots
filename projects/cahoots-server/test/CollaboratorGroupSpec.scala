
import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json.Json.toJson
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import com.cahoots.tools.JsonTools.clean

@RunWith(classOf[JUnitRunner])
class CollaboratorGroupSpec extends Specification with GroupServiceComponent {

  "group" should {
    "be created" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        val groupName = "group1"

        val json = toJson(
          Map(
            "name" -> toJson(groupName),
            "collabs" -> toJson(Seq(
              toJson("james"),
              toJson("hekar"),
              toJson("rob"),
              toJson("sam"),
              toJson("alice"),
              toJson("tom")
            ))
          ))

        val result = groupService.createGroup(json)

        clean(result \ "status") mustNotEqual "BAD"
        clean(result \ "message") mustNotEqual "%s deleted".format(groupName)

        clean(result \ "status") mustEqual "OK"
        clean(result \ "message") mustEqual "%s created".format(groupName)
      }
    }

    "be edited with an addition" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        // Create group
        val groupName = "edit_group"

        val create = toJson(
          Map(
            "name" -> toJson(groupName),
            "collabs" -> toJson(Seq(
              toJson("james"),
              toJson("rob")
            ))
          ))

        groupService.createGroup(create)
        
        val edit = toJson(
          Map(
            "name" -> toJson(groupName),
            "newName" -> toJson(groupName),
            "collabs" -> toJson(Seq(
              toJson("james"),
              toJson("rob"),
              toJson("sam")
            ))
          ))

        val result = groupService.editGroup(edit)
        

        clean(result \ "status") mustNotEqual "BAD"
        clean(result \ "message") mustNotEqual "%s created".format(groupName)

        clean(result \ "status") mustEqual "OK"
        clean(result \ "message") mustEqual "%s edited".format(groupName)
      }
    }
    
    "be edited with a new name" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {

        // Create group
        val groupName = "edit_group"

        val create = toJson(
          Map(
            "name" -> toJson(groupName),
            "collabs" -> toJson(Seq(
              toJson("james"),
              toJson("rob")
            ))
          ))

        groupService.createGroup(create)
        
        val edit = toJson(
          Map(
            "name" -> toJson(groupName),
            "newName" -> toJson(groupName),
            "collabs" -> toJson(Seq(
              toJson("james"),
              toJson("rob"),
              toJson("sam")
            ))
          ))

        val result = groupService.editGroup(edit)
        

        clean(result \ "status") mustNotEqual "BAD"
        clean(result \ "message") mustNotEqual "%s created".format(groupName)

        clean(result \ "status") mustEqual "OK"
        clean(result \ "message") mustEqual "%s edited".format(groupName)
      }
    }
    
  }

}