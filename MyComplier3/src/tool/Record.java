package tool;
/**
 * ���ű��¼
 * @author guoyang
 *
 */
public class Record {

	/**
	 * ��ʶ��id
	 */
	private String id;
	/**
	 * ��ʶ��������
	 */
	private Type type;
	/**
	 * �ڷ��ű��е�ƫ����
	 */
	private int offset;
	/**
	 * ��ռ�ֽ�
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
