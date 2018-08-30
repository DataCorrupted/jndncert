package net.named_data.jndncert.challenge

import org.json.JSONObject

class ChallengePinTest extends GroovyTestCase {
    void testGetSelectRequirementsAndGenParamsJson() {
        ChallengePin challengePin = new ChallengePin()
        ArrayList<String> requirementList =
                challengePin.getSelectRequirements()
        assert requirementList.size() == 0

        JSONObject obj = challengePin.doGenSelectParamsJson(
                ChallengeModule.WAIT_SELECTION, requirementList)
        assert obj.isNull()
    }

    void testGetValidateRequirementsAndGetParamsJson() {
        ChallengePin challengePin = new ChallengePin()
        ArrayList<String> requirementList =
                challengePin.getValidateRequirements(ChallengeEmail.NEED_CODE)
        assert requirementList.size() == 1
        assert requirementList.get(0) == "Please input your verification code: "

        requirementList.clear();
        requirementList.add("961030")
        JSONObject obj = challengePin.doGenValidateParamsJson(
                ChallengePin.NEED_CODE, requirementList
        )
        assert obj.optString(ChallengePin.JSON_PIN_CODE, "") == "961030"
    }
}
