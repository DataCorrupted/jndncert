package net.named_data.jndncert.challenge;

public class ChallengeModule {
    public static final String WAIT_SELECTION = "wait-selection";
    public static final String SUCCESS = "success";
    public static final String PENDING = "pending";
    public static final String FAILURE = "failure";

    public final String challengeType;
    public ChallengeModule(String uniqueType){
        challengeType = uniqueType;
    }
}
