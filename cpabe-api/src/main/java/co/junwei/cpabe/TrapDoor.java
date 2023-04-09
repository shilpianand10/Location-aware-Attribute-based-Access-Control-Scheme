package co.junwei.cpabe;

import co.junwei.bswabe.BswabePub;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static co.junwei.bswabe.Bswabe.elementFromElement;
import static co.junwei.bswabe.Bswabe.elementFromString;
import static co.junwei.bswabe.SerializeUtils.*;

public class TrapDoor {
    public Element Ax;
    public Element Bx;

    public String locationName;

    public int X;

    public Element token;

    public TrapDoor(int X, String locationName) throws NoSuchAlgorithmException {
        Pair<Element, Element> pair = LocationStore.generateTrapdoor(X, locationName);
        this.locationName = locationName;
        this.X = X;
        Ax = pair.getKey();
        Bx = pair.getValue();
    }

    public void serialize(ArrayList<Byte> bytes) {
        serializeString(bytes, locationName);
        serializeUint32(bytes, X);
        serializeElement(bytes, Ax);
        serializeElement(bytes, Bx);
    }

    public void setToken(Element token) {
        this.token = token;
    }

    public Element exposeTrapdoor(Pairing pairing, Element d_prime) {
        System.out.println(this.token + ":" + d_prime);
        return pairing.pairing(this.token, d_prime);
    }

    @Override
    public String toString(){
        return this.locationName + ":" + this.X;
    }
}
