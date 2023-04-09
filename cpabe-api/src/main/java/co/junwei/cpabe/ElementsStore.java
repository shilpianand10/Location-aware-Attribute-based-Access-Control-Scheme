package co.junwei.cpabe;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

public class ElementsStore {
    private static Element g;

    private static Pairing p;

    public static void initialize(Pairing pairing){
        if (p != null) return;
        p = pairing;
        g = p.getG1().newElement();
        g.setToRandom();
    }

    public static Element getG() {
        return g.duplicate();
    }
}
