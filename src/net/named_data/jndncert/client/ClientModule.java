package net.named_data.jndncert.client;

import net.named_data.jndn.Face;
import net.named_data.jndn.security.pib.PibKey;

public class ClientModule {
    // TODO: Fill this class.
    protected ClientConfig m_config;
    protected Face m_face;
    protected PibKey m_keyChain;
    protected int m_retryTimes;
}
