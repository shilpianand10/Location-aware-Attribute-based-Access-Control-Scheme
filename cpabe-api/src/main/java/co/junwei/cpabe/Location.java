package co.junwei.cpabe;

import co.junwei.bswabe.Bswabe;
import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.DefaultCurveParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

import java.io.ByteArrayInputStream;
import java.security.NoSuchAlgorithmException;

public class Location {
    public String locationName;

    public String formatDescription;

    private Element l_k;

    Element gamma_k;

    private Element v_k;

    public Pairing pairing;

    public Location(String locationName,
                    String formatDescription,
                    Pairing p) {
        this.locationName = locationName;
        this.formatDescription = formatDescription;
        this.pairing = p;

        setup();
    }

    private void setup() {
        Pairing p = this.pairing;

        gamma_k = p.getZr().newElement();
        gamma_k.setToRandom();

        l_k = ElementsStore.getG();
        l_k = l_k.powZn(gamma_k);

        v_k = pairing.getZr().newElement();
        v_k.setToRandom();
    }

    public Element generateSecret() {
        Element new_s_k_x = pairing.getZr().newElement();
        new_s_k_x.setToRandom();
        return new_s_k_x;
    }

    public static void main(String[] args) {
        String curveParams = "type a\n"
                + "q 87807107996633125224377819847540498158068831994142082"
                + "1102865339926647563088022295707862517942266222142315585"
                + "8769582317459277713367317481324925129998224791\n"
                + "h 12016012264891146079388821366740534204802954401251311"
                + "822919615131047207289359704531102844802183906537786776\n"
                + "r 730750818665451621361119245571504901405976559617\n"
                + "exp2 159\n" + "exp1 107\n" + "sign1 1\n" + "sign0 1\n";
        CurveParameters params = new DefaultCurveParameters()
                .load(new ByteArrayInputStream(curveParams.getBytes()));
        Pairing p = PairingFactory.getPairing(params);

        Location loc = new Location("abc", "12.12,23.23", p);
        loc.setup();
        System.out.println(loc.locationName + ", " + loc.formatDescription + ", [" + loc.gamma_k + "], " + loc.l_k);
    }

    public Pair<Element, Element> generateTrapdoor(Element s_k_x) throws NoSuchAlgorithmException {
        Pairing p = pairing;

        Element Ax = ElementsStore.getG();
        Ax = Ax.powZn(v_k);

        Element Bx = s_k_x.duplicate();
        Element tmp0 = p.getG1().newElement();
        // tmp0 = H1(f_loc_k)
        Bswabe.elementFromString(tmp0, formatDescription);
        Element tmp1 = p.pairing(tmp0, l_k);
        // tmp1 = e(H1(f_loc_k), l_k)^{v_k}
        tmp1 = tmp1.powZn(v_k);
        Element tmp2 = p.getZr().newElement();
        // tmp2 = H2(e(H1(f_loc_k, l_k))^{v_k})
        Bswabe.elementFromElement(tmp1, tmp2);

        Bx = Bx.add(tmp2);

        return new Pair<>(Ax, Bx);
    }
}
