package net.named_data.jndncert.challenge;

import org.json.JSONObject;

import java.util.ArrayList;

public class ChallengeEmail extends ChallengeModule {
    protected static final String CHALLENGE_TYPE = "Email";

    static final public String NEED_CODE = "need-code";
    static final public String WRONG_CODE = "wrong-code";

    static final public String FAILURE_INVALID_EMAIL = "failure-invalid-email";
    static final public String FAILURE_TIMEOUT = "timeout";
    static final public String FAILURE_MAXRETRY = "max-retry";

    static final public String JSON_EMAIL = "email";
    static final public String JSON_CODE_TP = "code-timepoint";
    static final public String JSON_CODE = "code";
    static final public String JSON_ATTEMPT_TIMES = "attempt-times";

    private String m_scriptPath;
    private int m_maxAttemptTimes;
    private int m_secretLifeTimeMinutes;

    // Again, Not really useful unless you implement CA server.
    public ChallengeEmail(){
        this("", 3, 20);
    }
    public ChallengeEmail(String scriptPath, int maxAttemptTimes, int secretLifeTimeMinutes){
        super();
        m_scriptPath = scriptPath;
        m_maxAttemptTimes = maxAttemptTimes;
        m_secretLifeTimeMinutes = secretLifeTimeMinutes;
    }

    public ArrayList<String> getSelectRequirements(){
        ArrayList<String> result = new ArrayList<>();
        result.add("Please input your email address: ");
        return result;
    }
    public ArrayList<String> getValidateRequirements(String status){
        ArrayList<String> result = new ArrayList<>();
        if (status == NEED_CODE){
            result.add("Please input your verification code: ");
        } else if (status == WRONG_CODE){
            result.add("Incorrect PIN code, please retry: ");
        } else {
            result.add("Invalid status. This should not happen, please contact your administrator.");
        }
        return result;
    }
    public JSONObject doGenSelectParamsJson(
            String status, ArrayList<String> paramList){
        assert status.equals(WAIT_SELECTION);
        assert paramList.size() == 1;
        return new JSONObject()
                .put(JSON_EMAIL, paramList.get(0));
    }
    public JSONObject doGenValidateParamsJson(
            String status, ArrayList<String> paramList){
        assert paramList.size() == 1;
        return new JSONObject()
                .put(JSON_CODE, paramList.get(0));
    }
}
