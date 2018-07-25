package net.named_data.jndncert.client;

import net.named_data.jndn.Name;
import net.named_data.jndncert.client.ClientCaItem;
import java.util.List;

public class ClientConfig {

    // TODO: Do this really need to be public?
    public List<ClientCaItem> m_caItems;
    public String m_localNdncertAnchor;

    public void load(String fileName){
        // TODO: Read that file and format it into a JSON parser, call load(/*JsonReader*/);
        ;
    }

    public void load(/*JsonReader*/){
        // TODO: Put suitable JSON parser here.
        ;
    }

    public void addNewCaItem(ClientCaItem item){
        // TODO:
        ;
    }

    public void removeCaItem(Name caName){
        // TODO:
        ;
    }

    ClientCaItem extractCaItem(/*Json*/){
        // TODO: Determine Json parser here.
        ;
    }

}
