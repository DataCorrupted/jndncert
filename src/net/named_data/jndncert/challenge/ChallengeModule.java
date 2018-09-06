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

    // TODO: I don't think the following is necessary.
    // For Client
    public ArrayList<String> getRequirementForSelection(){
        return getSelectRequirements();
    }

    // For Client
    public ArrayList<String> getRequirementForValidate(String status){
        return getValidateRequirements(status);
    }

    // For Client
    public JSONObject genSelectParamsJson(
            String status, ArrayList<String> paramList
    ){
        return doGenSelectParamsJson(status, paramList);
    }

    // For Client
    public JSONObject genValidateParamsJson(
            String status, ArrayList<String> paramList
    ){
        return doGenValidateParamsJson(status, paramList);
    }

    abstract public ArrayList<String> getSelectRequirements();
    abstract public ArrayList<String> getValidateRequirements(String status);
    abstract public JSONObject doGenSelectParamsJson(
            String status, ArrayList<String> paramList);
    abstract public JSONObject doGenValidateParamsJson(
            String status, ArrayList<String> paramList);
}
