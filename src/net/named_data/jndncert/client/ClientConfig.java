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
import java.util.ArrayList;
import java.util.function.Predicate;

public class ClientConfig {

    // TODO: Do this really need to be public?
    public ArrayList<ClientCaItem> m_caItems = new ArrayList<>();
    public String m_localNdncertAnchor = "";

    public void load(String fileName){
        try {
            File f = new File(fileName);
            InputStream inputFileStream = new FileInputStream(f);
            JsonReader reader = Json.createReader(inputFileStream);
            load(reader);
            reader.close();
        } catch (FileNotFoundException e) {
            // TODO: Try other logging method.
            System.err.println("Json File " + fileName + "not found.");
        };
    }

    public void load(JsonReader jsonReader){
        m_caItems.clear();
        JsonObject main_obj = jsonReader.readObject();
        JsonArray arr = main_obj.getJsonArray("ca-list");
        for (int idx = 0; idx < arr.size(); idx++){
            JsonObject temp_obj = arr.getJsonObject(idx);
            m_caItems.add(extractCaItem(temp_obj));
        }
        // TODO: This is not tested in unit test.
        m_localNdncertAnchor =
            getStringFromJsonObj(main_obj, "local-ndncert-anchor");
    }

    // TODO: This function appears in the ndncert, yet it's never implemented.
    // Please implement it after ndncert gets updated.
    public void addNewCaItem(ClientCaItem item){ ; }

    public void removeCaItem(Name caName){
        Predicate<ClientCaItem> caPredicate = p -> p.m_caName.toUri().equals(caName.toUri());
        m_caItems.removeIf(caPredicate);
    }

    /*
     * @brief: Reads a string from the Json file. Returns "" if it doesn't exist.
     *
     */
    private String getStringFromJsonObj(JsonObject obj, String name){
        return obj.getJsonString(name) != null ? obj.getString(name) : "";
    }

    private ClientCaItem extractCaItem(JsonObject jsonObj){
        ClientCaItem ca_itme = new ClientCaItem();

        // If these items are not there, Json parser will throw an NullPointerException
        // because the library is converting a nullptr to a String, which is meaningless.
        ca_itme.m_caName = new Name(jsonObj.getString("ca-prefix"));
        ca_itme.m_caInfo = getStringFromJsonObj(jsonObj, "ca-info");
        ca_itme.m_probe = getStringFromJsonObj(jsonObj, "probe");
        ca_itme.m_isListEnabled =
                getStringFromJsonObj(jsonObj, "is-list-enabled").equals("true");
        ca_itme.m_targetedList = getStringFromJsonObj(jsonObj, "target-list");

        // TODO: I am not sure how a certificate is issued here. Zhiyi please help.
        // ca_itme.m_anchor = new CertificateV2(jsonObj.getString("certificate"));
        return ca_itme;
    }

}
