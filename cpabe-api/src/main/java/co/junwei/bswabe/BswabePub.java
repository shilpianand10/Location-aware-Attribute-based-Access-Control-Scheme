package co.junwei.bswabe;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

/**
 * G0xG0 -> G1
 * G1xG2 -> GT
 */

public class BswabePub{
	/*
	 * A public key
	 */
	public String pairingDesc;
	public Pairing p;
	public Element g;		// An element from G1 		/* G_1 */
	public Element h;		// h = g^beta 				/* G_1 */
	public Element f;		// f = g^(1/beta) 			/* G_1 */
	public Element gp;		// An element from G2 		/* G_2 */
	public Element g_hat_alpha;	// = {e(g,g)}^alpha		/* G_T */
}
