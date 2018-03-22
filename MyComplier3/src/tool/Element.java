package tool;
/**
 * ËÄÔªÊ½
 * @author guoyang
 *
 */
public class Element {
	private String op;
	private String addr1;
	private String addr2;
	private String addr3;
	public Element(String op, String addr1, String addr2, String addr3) {
		super();
		this.op = op;
		this.addr1 = addr1;
		this.addr2 = addr2;
		this.addr3 = addr3;
	}
	public String getAddr3() {
		return addr3;
	}
	public void setAddr3(String addr3) {
		this.addr3 = addr3;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public String getAddr1() {
		return addr1;
	}
	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}
	public String getAddr2() {
		return addr2;
	}
	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}

}
