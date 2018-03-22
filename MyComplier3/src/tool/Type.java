package tool;
/**
 * 变量类型
 * @author guoyang
 *
 */
public class Type{
	private String type;
	private int num;
	private Type childType;
	public Type(String str)
	{
		this.type = str;
	}
	public Type(int num,Type type){
		this.num = num;
		this.childType = type;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public Type getChildType() {
		return childType;
	}
	public void setChildType(Type childType) {
		this.childType = childType;
	}
	public String toString() {
		if(this.getChildType()!=null)
		{
			return "array("+this.getNum()+","+this.getChildType()+")";
		}
		return this.getType();
	}
	public int getWidth() {
		if(this.childType==null)
		{
			switch(this.type)
			{
			case "int":
				return 4;
			case "real":
				return 8;
			}
		}
		return this.num*this.getChildType().getWidth();
	}
	
}
