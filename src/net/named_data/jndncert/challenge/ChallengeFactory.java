package net.named_data.jndncert.challenge;

import com.sun.media.jfxmedia.logging.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ChallengeFactory {
    private static Map<String, Class> map =
            Collections.unmodifiableMap(new HashMap<String,Class>() {{
                put("Pin", ChallengePin.class);
                put("Email", ChallengeEmail.class);
                put("Credential", ChallengeCredential.class);
            }});

    public static void registerChallenge(String n, Class<ChallengeModule> c){
        map.put(n, c);
    }

    public static ChallengeModule getChallenge(String challenge){
        try{
            return (ChallengeModule) map.get(challenge).newInstance();
        } catch (Exception e){
            Logger.logMsg(Logger.WARNING, e.getMessage());
        }
        return null;
    }
}
