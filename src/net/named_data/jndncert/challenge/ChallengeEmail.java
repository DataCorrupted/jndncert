package net.named_data.jndncert.challenge;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.logging.Logger;

public class ChallengeEmail implements ChallengeModule {
    private static final String CHALLENGE_TYPE = "Email";
    public String getChallengeType(){ return CHALLENGE_TYPE; }

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

    private Logger log = Logger.getLogger("ChallengeEmail");
    // Again, Not really useful unless you implement CA server.
    public ChallengeEmail(){
        this("", 3, 20);
    }
    public ChallengeEmail(String scriptPath, int maxAttemptTimes, int secretLifeTimeMinutes){
        m_scriptPath = scriptPath;
        m_maxAttemptTimes = maxAttemptTimes;
        m_secretLifeTimeMinutes = secretLifeTimeMinutes;
        //ChallengeFactory.registerChallenge("Email", this);
    }

    public ArrayList<String> getSelectRequirements(){
        ArrayList<String> result = new ArrayList<>();
        result.add("Please input your email address: ");
        return result;
    }
    public ArrayList<String> getValidateRequirements(String status){
        ArrayList<String> result = new ArrayList<>();
        if (status.equals(NEED_CODE)){
            result.add("Please input your verification code: ");
        } else if (status.equals(WRONG_CODE)){
            result.add("Incorrect PIN code, please retry: ");
        } else {
            result.add("Invalid status. This should not happen, please contact your administrator.");
        }
        return result;
    }
    public JSONObject genSelectParamsJson(
            String status, ArrayList<String> paramList){
        assert status.equals(WAIT_SELECTION);
        assert paramList.size() == 1;
        JSONObject obj = new JSONObject();
        try{
            obj.put(JSON_EMAIL, paramList.get(0));
        } catch (JSONException e){
            log.warning(e.getMessage());
        }
        return obj;
    }
    public JSONObject genValidateParamsJson(
            String status, ArrayList<String> paramList){
        assert paramList.size() == 1;
        JSONObject obj = new JSONObject();
        try{
            obj.put(JSON_CODE, paramList.get(0));
        } catch (JSONException e){
            log.warning(e.getMessage());
        }
        return obj;
    }
}
