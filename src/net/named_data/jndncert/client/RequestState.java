package net.named_data.jndncert.client;

import net.named_data.jndn.security.pib.PibKey;

import java.util.ArrayList;

public class RequestState {

    public ClientCaItem m_ca;
    public PibKey m_key;

    public String m_requestId;
    public String m_status;
    public String m_challengeType;
    public ArrayList<String> m_challengeList;

    public Boolean m_isInstalled = false;
}
