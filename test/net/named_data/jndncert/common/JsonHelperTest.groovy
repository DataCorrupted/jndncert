package net.named_data.jndncert.common

import net.named_data.jndn.Name

import javax.json.JsonArray
import javax.json.JsonObject

class JsonHelperTest extends GroovyTestCase {
    void testGenProbeResponseJson() {
        JsonObject probeResponse = JsonHelper.genProbeResponseJson(
                new Name("/ndn/edu/ShanghaiTech/SIST/peter/ThinkPad"),
                new Name("/ndn/edu/ShanghaiTech/SIST/peter/ca-info")
        )
        assert probeResponse.getString(JsonHelper.JSON_IDENTIFIER) ==
                "/ndn/edu/ShanghaiTech/SIST/peter/ThinkPad"
        assert probeResponse.getString(JsonHelper.JSON_CA_INFO) ==
                "/ndn/edu/ShanghaiTech/SIST/peter/ca-info"
        System.err.println("JsonHelper.genProbeResponseJson(...): Passed.");
    }

    void testGenNewResponseJson(){
        ArrayList<String> challenges = new ArrayList<>()
        challenges.add("PIN")
        challenges.add("EMAIL")
        JsonObject newResponse = JsonHelper.genNewResponseJson(
                "69850764", "wait-selection", challenges
        );
        assert newResponse.getString(JsonHelper.JSON_REQUEST_ID) == "69850764"
        assert newResponse.getString(JsonHelper.JSON_STATUS) == "wait-selection"

        JsonArray array = newResponse.getJsonArray(JsonHelper.JSON_CHALLENGES)
        assert array.getJsonObject(0).getString(JsonHelper.JSON_CHALLENGE_TYPE) == "PIN"
        assert array.getJsonObject(1).getString(JsonHelper.JSON_CHALLENGE_TYPE) == "EMAIL"
        System.err.println("JsonHelper.genNewResponseJson(...): Passed.");
    }

    void testGenChallengeResponseJson() {
        JsonObject challengeResponse = JsonHelper.genChallengeResponseJson(
                "69850764", "PIN", "need-code"
        );
        assert challengeResponse.getString(JsonHelper.JSON_REQUEST_ID) == "69850764"
        assert challengeResponse.getString(JsonHelper.JSON_CHALLENGE_TYPE) == "PIN"
        assert challengeResponse.getString(JsonHelper.JSON_STATUS) == "need-code"
        assert challengeResponse.getString(JsonHelper.JSON_CERTIFICATE, "Should get nothing") == "Should get nothing"
        System.err.println("JsonHelper.genChallengeResponseJson(...): Passed.");
    }

    void testGenChallengeResponseJson1() {
        JsonObject challengeResponse = JsonHelper.genChallengeResponseJson(
                "69850764", "PIN",
                "need-code", new Name("/ndn/test")
        );
        assert challengeResponse.getString(JsonHelper.JSON_REQUEST_ID) == "69850764"
        assert challengeResponse.getString(JsonHelper.JSON_CHALLENGE_TYPE) == "PIN"
        assert challengeResponse.getString(JsonHelper.JSON_STATUS) == "need-code"
        assert challengeResponse.getString(JsonHelper.JSON_CERTIFICATE) == "/ndn/test"
        System.err.println("JsonHelper.genChallengeResponseJson(..., Name): Passed.");
    }

    void testGenFailureJson() {
        JsonObject failure = JsonHelper.genFailureJson(
                "69850764", "PIN",
                "failure", "This certificate exists"
        );
        assert failure.getString(JsonHelper.JSON_STATUS) == "failure"
        assert failure.getString(JsonHelper.JSON_FAILURE_INFO) == "This certificate exists"
        System.err.println("JsonHelper.genFailureJson(...): Passed.");
    }
}
