package net.named_data.jndncert.challenge

import javax.json.JsonObject


class ChallengeEmailTest extends GroovyTestCase {
    void testGetSelectRequirementsAndGenParamsJson() {
        ChallengeEmail challengeEmail = new ChallengeEmail()
        ArrayList<String> requirementList =
                challengeEmail.getSelectRequirements();
        assert requirementList.size() == 1
        assert requirementList.get(0) == "Please input your email address: "

        requirementList.clear();
        requirementList.add("PeterRong96@gmail.com")
        JsonObject obj = challengeEmail.doGenSelectParamsJson(
                ChallengeEmail.WAIT_SELECTION, requirementList)
        assert obj.getString(ChallengeEmail.JSON_EMAIL, "") == "PeterRong96@gmail.com"
    }

    void testGetValidateRequirementsAndGenParamsJson() {
        ChallengeEmail challengeEmail = new ChallengeEmail()
        ArrayList<String> requirementList =
                challengeEmail.getValidateRequirements(ChallengeEmail.NEED_CODE);
        assert requirementList.size() == 1;
        assert requirementList.get(0) == "Please input your verification code: "

        requirementList.clear()
        requirementList.add("961030")
        JsonObject obj = challengeEmail.doGenValidateParamsJson(
                ChallengeEmail.NEED_CODE, requirementList
        )
        assert obj.getString(ChallengeEmail.JSON_CODE, "") == "961030"
    }
}
