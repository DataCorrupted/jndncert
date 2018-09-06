package net.named_data.jndncert.challenge;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.logging.Logger;

public class ChallengePin implements ChallengeModule {
    private static final String CHALLENGE_TYPE = "Pin";
    public String getChallengeType(){ return CHALLENGE_TYPE; }

    static public final String NEED_CODE = "need-code";
    static public final String WRONG_CODE = "wrong-code";
    static public final String FAILURE_TIMEOUT = "failure-timeout";
    static public final String FAILURE_MAXRETRY = "failure-max-retry";
    static public final String JSON_CODE_TP = "code-timepoint";
    static public final String JSON_PIN_CODE = "code";
    static public final String JSON_ATTEMPT_TIMES = "attempt-times";
    // TODO: not really useful now, unless you want a Java server.
    private int m_maxAttempTimes;
    private int m_secretLifeTimeSeconds;

    private Logger log = Logger.getLogger("ChallengePin");
    public ChallengePin(){
        this(3, 3600);
    }
    public ChallengePin(int maxAttempTimes, int secretLifeTimeSeconds){
        super();
        m_maxAttempTimes = maxAttempTimes;
        m_secretLifeTimeSeconds = secretLifeTimeSeconds;
    }

    // There is no requirement if you want to use Pin as  challenge.
    public ArrayList<String> getSelectRequirements(){
        return new ArrayList<>();
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
        assert paramList.size() == 0;
        return new JSONObject();
    }
    public JSONObject genValidateParamsJson(
            String status, ArrayList<String> paramList){
        assert paramList.size() == 1;
        JSONObject obj = new JSONObject();
        try {
            obj.put(JSON_PIN_CODE, paramList.get(0));
        } catch (JSONException e){
            log.warning(e.getMessage());
        }
        return obj;
    }
}
