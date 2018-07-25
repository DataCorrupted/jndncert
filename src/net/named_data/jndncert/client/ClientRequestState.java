package net.named_data.jndncert.client;

import net.named_data.jndn.security.pib.PibKey;
import java.util.List;

public class ClientRequestState {

    public ClientCaItem m_ca;
    // TODO: There is no security::Key in jndn.
    public PibKey m_key;

    public String m_requestId;
    public String m_status;
    public String m_challengeType;
    public List<String> m_challengeList;

    public Boolean m_isInstalled = false;
}
