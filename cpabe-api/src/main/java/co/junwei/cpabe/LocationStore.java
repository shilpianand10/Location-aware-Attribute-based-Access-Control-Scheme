package co.junwei.cpabe;

import co.junwei.bswabe.Bswabe;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static co.junwei.bswabe.Bswabe.elementFromElement;
import static co.junwei.bswabe.Bswabe.elementFromString;

public class LocationStore {
    private final Map<String, Location> locations;

    /**
     * location k, node x -> s_k_x
     */
    private final Map<Pair<String, Integer>, Element> secretsMap = new HashMap<>();

    private static final LocationStore store = new LocationStore();

    private LocationStore() {
        locations = new TreeMap<>();
    }

    public static void addLocation(Location l) {
        store.locations.put(l.locationName, l);
    }

    public static Location getLocation(String locationName) {
        Location l = store.locations.get(locationName);
        return l;
    }

    public static boolean contains(String locationName) {
        return store.locations.containsKey(locationName);
    }

    public static Element createToken(TrapDoor trapDoor, Pairing pairing, String userLocationFormatDesc) {
        Element h1 = trapDoor.Ax.getField().newElement();
        Element Ax = trapDoor.Ax.duplicate();
        Element Bx = trapDoor.Bx.duplicate();

        try {
            Bswabe.elementFromString(h1, userLocationFormatDesc);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        Location l = getLocation(trapDoor.locationName);

        Element e1 = h1.powZn(l.gamma_k);
        Element e2 = Ax;

        Element e = pairing.pairing(e1, e2);
        Element h2 = Bx.getField().newElement();

        try {
            Bswabe.elementFromElement(e, h2);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }

        Element res = Bx.sub(h2);

        Element actual_s_k_x = getSecret(trapDoor.X, trapDoor.locationName);
        if (res.equals(actual_s_k_x)) {
            System.out.println("Token retrieved successfully! Trapdoor: " +  trapDoor + ".");
            res = ElementsStore.getG().powZn(res);
        } else {
            System.out.println("[ERROR] Token does not match! Trapdoor: " + trapDoor + ".");
            res = null;
        }
        trapDoor.setToken(res);

        return res;
    }

    public static Element getSecret(int X, String locationName) {
        Pair<String, Integer> key = new Pair<>(locationName, X);
        if (store.secretsMap.containsKey(key)) {
            return store.secretsMap.get(key);
        }

        Element s_k_x = getLocation(locationName).generateSecret();
        store.secretsMap.put(key, s_k_x);
        return s_k_x;
    }

    public static Pair<Element, Element> generateTrapdoor(int X, String locationName)
            throws NoSuchAlgorithmException {

        Pair<String, Integer> key = new Pair<>(locationName, X);
        if (!store.secretsMap.containsKey(key)) {
            store.secretsMap.put(key, getSecret(X, locationName));
        }

        Element s_k_x = store.secretsMap.get(key);
        Location l = store.locations.get(locationName);
        return l.generateTrapdoor(s_k_x);
    }
}
