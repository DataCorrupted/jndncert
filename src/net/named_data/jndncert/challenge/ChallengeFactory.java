package net.named_data.jndncert.challenge;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChallengeFactory {
    static private Logger log = Logger.getLogger("jNDNCert");
    private static Map<String, Class> map =
            Collections.unmodifiableMap(new HashMap<String,Class>() {{
                put("Pin", ChallengePin.class);
                put("Email", ChallengeEmail.class);
                put("Credential", ChallengeCredential.class);
            }});

    public static void registerChallenge(String n, Class<ChallengeModule> c){
        map.put(n, c);
    }

    public static ChallengeModule createChallengeModule(String challenge){
        try{
            return (ChallengeModule) map.get(challenge).newInstance();
        } catch (Exception e){
            log.log(Level.WARNING, e.getMessage());
        }
        return null;
    }
}
