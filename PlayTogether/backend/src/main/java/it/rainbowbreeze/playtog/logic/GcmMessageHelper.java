package it.rainbowbreeze.playtog.logic;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import it.rainbowbreeze.playtog.domain.RegistrationRecord;

import static it.rainbowbreeze.playtog.OfyService.ofy;

/**
 * Created by alfredomorresi on 07/01/15.
 */
public class GcmMessageHelper {
    private static final Logger log = Logger.getLogger(GcmMessageHelper.class.getName());

    /**
     * Api Keys can be obtained from the google cloud console
     */
    private static final String API_KEY = System.getProperty("gcm.api.key");


    public void sendMessage(Message message) throws IOException {
        Sender sender = new Sender(API_KEY);

        List<RegistrationRecord> records = ofy().load().type(RegistrationRecord.class).limit(30).list();
        log.info("Sending to " + records.size() + " clients message " + message);
        for (RegistrationRecord record : records) {
            Result result = sender.send(message, record.getRegId(), 5);
            if (result.getMessageId() != null) {
                log.info("Message sent to " + record.getRegId());
                String canonicalRegId = result.getCanonicalRegistrationId();
                if (canonicalRegId != null) {
                    // if the regId changed, we have to update the datastore
                    log.info("Registration Id changed for " + record.getRegId() + " updating to " + canonicalRegId);
                    record.setRegId(canonicalRegId);
                    ofy().save().entity(record).now();
                }
            } else {
                String error = result.getErrorCodeName();
                if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
                    log.warning("Registration Id " + record.getRegId() + " no longer registered with GCM, removing from datastore");
                    // if the device is no longer registered with Gcm, remove it from the datastore
                    ofy().delete().entity(record).now();
                } else {
                    log.warning("Error when sending message : " + error);
                }
            }
        }
    }
}
