package ca.beogotechnologies.deliverymanager_mobileapp.util;

import java.util.UUID;

/**
 * Created by fabrice on 2017-03-18.
 */
public class RandomGUIDGenerator {
    public static String generateRandomGUID() {
        return UUID.randomUUID().toString().toUpperCase();
    }

}
