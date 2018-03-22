package tool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public abstract class MyCharacter {
	private String offset="";
	public String getOffset() {
		return offset;
	}
	public void setOffset(String offset) {
		this.offset = offset;
	}
	/**
	 * 文法符号标签，因为一开始没有考虑全面，所以用的是value,对于标识符来说，此值为id
	 */
	public String value="";
	/**
	 * 文法符号实际的值，如表达式的值，数字的值，标识符的词素
	 */
	private String actualValue ="";
	/**
	 * 类型
	 */
	private Type type=new Type("");
	/**
	 * 所占字节
	 */
	private int width = 0;
	/**
	 * 地址
	 */
	private String  addr="";
	private int quad=0;
	private ArrayList<String> params=new ArrayList<String>();
	public ArrayList<String> getParams() {
		return params;
	}
	public void setParams(ArrayList<String> params) {
		this.params = params;
	}
	private ArrayList<Integer> nextList=new ArrayList<Integer>();
	private ArrayList<Integer> trueList=new ArrayList<Integer>();
	private ArrayList<Integer> falseList=new ArrayList<Integer>();
	public int getQuad() {
		return quad;
	}
	public void setQuad(int quad) {
		this.quad = quad;
	}
	public ArrayList<Integer> getNextList() {
		return nextList;
	}
	public void setNextList(ArrayList<Integer> nextList) {
		this.nextList = nextList;
	}
	public ArrayList<Integer> getTrueList() {
		return trueList;
	}
	public void setTrueList(ArrayList<Integer> trueList) {
		this.trueList = trueList;
	}
	public ArrayList<Integer> getFalseList() {
		return falseList;
	}
	public void setFalseList(ArrayList<Integer> falseList) {
		this.falseList = falseList;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	/**
	 * 行号
	 */
	private int lineNumber = 0;
	/**
	 * 文法符号的first集
	 */
	public Set<String> first;
	public MyCharacter()
	{
		this.first=new HashSet<String>();
	}
	public boolean equals(MyCharacter mc) {
		if(mc.value.equals(this.value))
		{
			return true;
		}
		return false;
	}
	public String getValue() {
		return value;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getActualValue() {
		return actualValue;
	}
	public void setActualValue(String actualValue) {
		this.actualValue = actualValue;
	}
	
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public Set<String> getFirst() {
		return first;
	}
	public void setFirst(Set<String> first) {
		this.first = first;
	}
	public String toString() {
		String str = this.value+":"+this.actualValue;
		if(this.lineNumber!=0)
		{
			str += lineNumber;
		}
		return str;
	}
	
}
