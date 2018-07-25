package net.named_data.jndncert.client;

import net.named_data.jndn.Name;
import net.named_data.jndn.security.v2.CertificateV2;

/**
 * @brief The configuration of a trusted CA from the client's perspective.
 */
public class ClientCaItem {

    // Name of the CA. See "ca-prefix" in conf file.
    public Name m_caName;

    // A brief intro of CA. See "ca-info" in conf file.
    public String m_caInfo;

    // An instructor for the client ot use _PROBE. See "probe" in conf file.
    public String m_probe;

    // Whether list function is supported.
    public Boolean m_isListEnabled;

    // See "target-list" in conf file.
    public String m_targetedList;

    // CA's certificate.
    public CertificateV2 m_anchor;

}
