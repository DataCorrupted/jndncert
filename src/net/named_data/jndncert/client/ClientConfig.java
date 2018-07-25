package net.named_data.jndncert.client;

import net.named_data.jndn.Name;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Predicate;

public class ClientConfig {

    // TODO: Do this really need to be public?
    public List<ClientCaItem> m_caItems;
    public String m_localNdncertAnchor;

    public void load(String fileName){
        try {
            File f = new File(fileName);
            InputStream inputFileStream = new FileInputStream(f);
            JsonReader reader = Json.createReader(inputFileStream);
            load(reader);
        } catch (FileNotFoundException e) {
            // TODO: Try other logging method.
            System.err.println("Json File " + fileName + "not found.");
        };
    }

    public void load(JsonReader jsonReader){
        m_caItems.clear();
        JsonObject main_obj = jsonReader.readObject();
        JsonArray arr = main_obj.getJsonArray("ca-list");
        for (int idx = 0; !arr.isNull(idx); idx++){
            JsonObject temp_obj = arr.getJsonObject(idx);
            m_caItems.add(extractCaItem(temp_obj));

        }
    }

    // TODO: This function appears in the ndncert, yet it's never implemented.
    // Please implement it after ndncert gets updated.
    public void addNewCaItem(ClientCaItem item){ ; }

    public void removeCaItem(Name caName){
        Predicate<ClientCaItem> caPredicate = p -> p.m_caName == caName;
        m_caItems.removeIf(caPredicate);
    }

    ClientCaItem extractCaItem(JsonObject jsonObj){
        ClientCaItem ca_itme = new ClientCaItem();

        // Don't worry if these items are not there, they are all properly initialized.
        ca_itme.m_caName = new Name(jsonObj.getString("ca-prefix"));
        ca_itme.m_caInfo = jsonObj.getString("ca-info");
        ca_itme.m_probe = jsonObj.getString("probe");
        ca_itme.m_isListEnabled = jsonObj.getString("is-list-enabled").equals("true");
        ca_itme.m_targetedList = jsonObj.getString("target-list");

        // TODO: I am not sure how a certificate is issued here. Zhiyi please help.
        // ca_itme.m_anchor = new CertificateV2(jsonObj.getString("certificate"));
        return ca_itme;
    }

}
