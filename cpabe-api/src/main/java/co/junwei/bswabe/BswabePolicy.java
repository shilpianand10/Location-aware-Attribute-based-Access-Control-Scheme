package co.junwei.bswabe;

import java.util.ArrayList;

import co.junwei.cpabe.Location;
import co.junwei.cpabe.TrapDoor;
import it.unisa.dia.gas.jpbc.Element;

public class BswabePolicy {
	/* serialized */

	/* Reference of the node. */
	public int id;

	/* k=1 if leaf, otherwise threshould */
	public int k;
	/* attribute string if leaf, otherwise null */
	public String attr;
	public Element c;			/* G_1 only for leaves */
	public Element cp;		/* G_1 only for leaves */
	/* array of BswabePolicy and length is 0 for leaves */
	public BswabePolicy[] children;

	public TrapDoor trapDoor;
	
	/* only used during encryption */
	public BswabePolynomial q;

	/* only used during decription */
	public boolean satisfiable;
	public int min_leaves;
	int attri;
	ArrayList<Integer> satl = new ArrayList<Integer>();
}
