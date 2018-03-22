package tool;
/**
 * 符号表记录
 * @author guoyang
 *
 */
public class Record {

	/**
	 * 标识符id
	 */
	private String id;
	/**
	 * 标识符的类型
	 */
	private Type type;
	/**
	 * 在符号表中的偏移量
	 */
	private int offset;
	/**
	 * 所占字节
	 */
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	
}
