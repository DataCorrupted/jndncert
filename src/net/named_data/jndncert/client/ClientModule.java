package net.named_data.jndncert.client;

import net.named_data.jndn.Face;
import net.named_data.jndn.Name;
import net.named_data.jndn.security.KeyChain;
import net.named_data.jndn.security.pib.PibKey;

import java.util.ArrayList;


public class ClientModule {
    // TODO: Fill this class.
    protected ClientConfig m_config;
    protected Face m_face;
    protected KeyChain m_keyChain;
    protected int m_retryTimes;

    // Constructor
    // Java has no default function arguments, using function
    // overload to achieve that.
    public ClientModule(Face face, KeyChain keyChain){
        this(face, keyChain, 2);
    }
    public ClientModule(Face face, KeyChain keyChain, int retryTimes){
        m_face = face;
        m_retryTimes = retryTimes;
        m_keyChain = keyChain;
    }

    // Interfaces used to define callback functions.
    public interface LocalhostListCallback{
        void localhostListCb(ClientConfig clientConfig);
    }
    public interface ListCallback{
        void listCb(ArrayList<Name> caList, Name assignedName, Name schema);
    }
    public interface RequestCallback{
        void requestCb(ClientRequestState state);
    }
    public interface ErrorCallback{
        void errCb(String errInfo);
    }
}
