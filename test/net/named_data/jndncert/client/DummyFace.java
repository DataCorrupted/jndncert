package net.named_data.jndncert.client;

import net.named_data.jndn.*;

import java.io.IOException;

public class DummyFace extends Face {
    private Interest interest_;
    private OnData onData_;
    private Boolean testInterest = true;
    private Boolean testResponse = false;
    @Override
    public long expressInterest(
            Interest interest,
            OnData onData, OnTimeout onTimeout, OnNetworkNack onNetworkNack
    ) throws IOException {
        interest_ = interest;
        onData_ = onData;
        return 0;
    }
    public Interest getInterest(){ return interest_; }
    public void feedData(Data data) { onData_.onData(interest_, data); }
}
