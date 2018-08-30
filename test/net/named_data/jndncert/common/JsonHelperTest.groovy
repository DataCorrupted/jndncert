package net.named_data.jndncert.common

import net.named_data.jndn.Name
import org.json.JSONArray
import org.json.JSONObject


class JsonHelperTest extends GroovyTestCase {
    void testGenProbeResponseJson() {
        JSONObject probeResponse = JsonHelper.genProbeResponseJson(
                new Name("/ndn/edu/ShanghaiTech/SIST/peter/ThinkPad"),
                new Name("/ndn/edu/ShanghaiTech/SIST/peter/ca-info")
        )
        assert probeResponse
                .optString(JsonHelper.JSON_IDENTIFIER) == "/ndn/edu/ShanghaiTech/SIST/peter/ThinkPad"
        assert probeResponse
                .optString(JsonHelper.JSON_CA_INFO) == "/ndn/edu/ShanghaiTech/SIST/peter/ca-info"
    }

    void testGenNewResponseJson(){
        ArrayList<String> challenges = new ArrayList<>()
        challenges.add("PIN")
        challenges.add("EMAIL")
        JSONObject newResponse = JsonHelper.genNewResponseJson(
                "69850764", "wait-selection", challenges
        );
        assert newResponse.optString(JsonHelper.JSON_REQUEST_ID) == "69850764"
        assert newResponse.optString(JsonHelper.JSON_STATUS) == "wait-selection"
        JSONArray array = newResponse.optJSONArray(JsonHelper.JSON_CHALLENGES)
        assert array.getJSONObject(0).optString(JsonHelper.JSON_CHALLENGE_TYPE) == "PIN"
        assert array.getJSONObject(1).optString(JsonHelper.JSON_CHALLENGE_TYPE) == "EMAIL"
    }

    void testGenChallengeResponseJson() {
        JSONObject challengeResponse = JsonHelper.genChallengeResponseJson(
                "69850764", "PIN", "need-code"
        );
        assert challengeResponse.optString(JsonHelper.JSON_REQUEST_ID) == "69850764"
        assert challengeResponse.optString(JsonHelper.JSON_CHALLENGE_TYPE) == "PIN"
        assert challengeResponse.optString(JsonHelper.JSON_STATUS) == "need-code"
        assert challengeResponse.optString(JsonHelper.JSON_CERTIFICATE, "Should get nothing") == "Should get nothing"
    }

    void testGenChallengeResponseJson1() {
        JSONObject challengeResponse = JsonHelper.genChallengeResponseJson(
                "69850764", "PIN",
                "need-code", new Name("/ndn/test")
        );
        assert challengeResponse.optString(JsonHelper.JSON_REQUEST_ID) == "69850764"
        assert challengeResponse.optString(JsonHelper.JSON_CHALLENGE_TYPE) == "PIN"
        assert challengeResponse.optString(JsonHelper.JSON_STATUS) == "need-code"
        assert challengeResponse.optString(JsonHelper.JSON_CERTIFICATE) == "/ndn/test"
    }

    void testGenFailureJson() {
        JSONObject failure = JsonHelper.genFailureJson(
                "69850764", "PIN",
                "failure", "This certificate exists"
        );
        assert failure.optString(JsonHelper.JSON_STATUS) == "failure"
        assert failure.optString(JsonHelper.JSON_FAILURE_INFO) == "This certificate exists"
    }

    void testString2Json(){
        String jsonStr = "{" +
                "\"Name\":\"Peter Rong\"," +
                "\"School\":\"ShanghaiTech University\"" +
                "}"
        JSONObject object = JsonHelper.string2Json(jsonStr);
        assert object.optString("Name") == "Peter Rong"
        assert object.optString("School") == "ShanghaiTech University"
    }
}
