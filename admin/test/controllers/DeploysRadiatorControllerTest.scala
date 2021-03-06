package controllers.admin

import common.ExecutionContexts
import controllers.Helpers.DeploysTestHttpRecorder
import model.deploys._
import org.scalatest.{BeforeAndAfterAll, DoNotDiscover, Matchers, WordSpec}
import play.api.libs.json.JsArray
import play.api.libs.ws.WSClient
import play.api.test.FakeRequest
import play.api.test.Helpers._
import test.{ConfiguredTestSuite, WithMaterializer, WithTestWsClient}
import scala.concurrent.duration._

@DoNotDiscover class DeploysControllerTest
  extends WordSpec
    with Matchers
    with ConfiguredTestSuite
    with ExecutionContexts
    with BeforeAndAfterAll
    with WithMaterializer
    with WithTestWsClient {

  val existingBuild = "3123"

  class TestHttpClient(wsClient: WSClient) extends HttpLike {
    override def GET(url: String, queryString: Map[String, String] = Map.empty, headers: Map[String, String] = Map.empty) = {
      import implicits.Strings.string2encodings
      val urlWithParams = url + "?" + queryString.updated("key", "").toList.sortBy(_._1).map(kv=> kv._1 + "=" + kv._2).mkString("&").encodeURIComponent
      DeploysTestHttpRecorder.load(urlWithParams, headers) {
        wsClient.url(url).withQueryString(queryString.toSeq: _*).withHeaders(headers.toSeq: _*).withRequestTimeout(10.seconds).get()
      }
    }
  }

  class DeploysControllerStub extends DeploysController {
    private val httpClient = new TestHttpClient(wsClient)
    override val riffRaff = new RiffRaffService(httpClient)
  }

  lazy val controller = new DeploysControllerStub()

  "GET /deploys" should {
    val getDeploysRequest = FakeRequest(method = "GET", path = "/deploys")
    "returns 200 with expected amount of results" in {
      val projectName = "dotcom:all"
      val stage = "PROD"
      val pageSize = 10
      val response = call(controller.getDeploys(stage = Some(stage), pageSize = Some(pageSize)), getDeploysRequest)

      status(response) should be(OK)

      val jsonResponse = contentAsJson(response)
      (jsonResponse \ "response").as[JsArray].value.size should be(pageSize)
      (jsonResponse \ "status").as[String] should be("ok")
      (jsonResponse \ "response" \\ "projectName").map(_.as[String]).distinct should equal(List(projectName))
      (jsonResponse \ "response" \\ "projectName").map(_.as[String]).distinct should equal(List(projectName))
      (jsonResponse \ "response" \\ "stage").map(_.as[String]).distinct should equal(List(stage))
    }
  }
}

