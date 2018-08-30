package net.named_data.jndncert.challenge;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

abstract public class ChallengeModule {
    public static final String WAIT_SELECTION = "wait-selection";
    public static final String SUCCESS = "success";
    public static final String PENDING = "pending";
    public static final String FAILURE = "failure";
    protected static final Logger log = Logger.getLogger("ChallengeModule");
    protected String CHALLENGE_TYPE = "";
    public ChallengeModule(){}

    // TODO: Eliminated all CA functions.

    // For Client
    public ArrayList<String> getRequirementForSelection(){
        return null;
    }

    // For Client
    public ArrayList<String> getRequirementForValidate(String status){
        return null;
    }

    // For Client
    public JSONObject genSelectParamsJson(
            String status, ArrayList<String> paramList
    ){
        return null;
    }

    // For Client
    public JSONObject genValidateParamsJson(
            String status, ArrayList<String> paramList
    ){
        return null;
    }

    abstract public ArrayList<String> getSelectRequirements();
    abstract public ArrayList<String> getValidateRequirements(String status);
    abstract public JSONObject doGenSelectParamsJson(
            String status, ArrayList<String> paramList);
    abstract public JSONObject doGenValidateParamsJson(
            String status, ArrayList<String> paramList);

    static public String generateSecretCode(){
        int secretInt = new Random().nextInt(1000000);
        return String.format("%06d", secretInt);
    }
}
