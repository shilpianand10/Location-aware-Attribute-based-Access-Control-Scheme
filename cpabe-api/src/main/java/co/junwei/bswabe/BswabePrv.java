package co.junwei.bswabe;

import java.util.ArrayList;

import it.unisa.dia.gas.jpbc.Element;

public class BswabePrv {
	/*
	 * A private key
	 */
	Element d; /* G_2 */

	Element d_prime; // d_prime = g^r

	ArrayList<BswabePrvComp> comps; /* BswabePrvComp */
}