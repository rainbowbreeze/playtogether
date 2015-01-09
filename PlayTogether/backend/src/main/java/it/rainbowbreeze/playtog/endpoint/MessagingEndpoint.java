/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Backend with Google Cloud Messaging" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/GcmEndpoints
*/

package it.rainbowbreeze.playtog.endpoint;

import com.google.android.gcm.server.Message;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiNamespace;

import java.io.IOException;
import java.util.logging.Logger;

import javax.inject.Named;

import it.rainbowbreeze.playtog.common.Bag;
import it.rainbowbreeze.playtog.logic.GcmMessageHelper;

/**
 * An endpoint to send messages to devices registered with the backend
 * <p/>
 * For more information, see
 * https://developers.google.com/appengine/docs/java/endpoints/
 * <p/>
 * NOTE: This endpoint does not use any form of authorization or
 * authentication! If this app is deployed, anyone can access this endpoint! If
 * you'd like to add authentication, take a look at the documentation.
 */
@Api(
        name = "messaging",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = Bag.API_OWNER_DOMAIN,
                ownerName = Bag.API_OWNER_NAME,
                packagePath = Bag.API_PACKAGE_PATH)
)
public class MessagingEndpoint {
    private static final Logger log = Logger.getLogger(MessagingEndpoint.class.getName());

    /**
     * Api Keys can be obtained from the google cloud console
     */
    private static final String API_KEY = System.getProperty("gcm.api.key");

    /**
     * Send to the first 10 devices (You can modify this to send to any number of devices or a specific device)
     *
     * @param message The message to send
     */
    public void sendMessage(@Named("message") String message) throws IOException {
        if (message == null || message.trim().length() == 0) {
            log.warning("Not sending message because it is empty");
            return;
        }
        // crop longer messages
        if (message.length() > 1000) {
            message = message.substring(0, 1000) + "[...]";
        }
        Message msg = new Message.Builder()
                .addData(Bag.EXTRA_MESSAGE, message)
                .addData(Bag.EXTRA_GCMACTION_TYPE, Bag.GCMACTION_SEARCH_FOR_PLAYERS)
                        // 113100264827945975278 - Play Together
                        // 108670469644954045753 - User test 1
                        // 108659852221654912818 - User test 2 cool
                .addData(Bag.EXTRA_PLAYER_ID, "108670469644954045753")
                .build();

        GcmMessageHelper messageHelper = new GcmMessageHelper();
        messageHelper.sendMessage(msg);
    }
}
