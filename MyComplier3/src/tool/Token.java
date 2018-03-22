package tool;

public class Token {
	private int type;//token的类型
	private String typeString="";
	private String errorMessage="";
	public String getTypeString() {
		return typeString;
	}
	public void setTypeString(String typeString) {
		this.typeString = typeString;
	}
	private String value;//值
	private int lineNumber;//行号
	public Token() {
		// TODO Auto-generated constructor stub
	}
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
		if(type==101||type==102||type==103||type==104)
		{
			this.typeString="关系运算符";
		}
		if(type==105){
			this.typeString="自增符";
		}
		if(type==106){
			this.typeString="自减符";
		}
		if(type==107){
			this.typeString="字符串常量";
		}
		if(type==108||type==109){
			this.typeString="逻辑运算符";
		}
		if(type==110){
			this.typeString="注释";
		}
		if(type==111||type==32){
			this.typeString="字符常量";
			if(this.value.length()<=2)
			{
				this.errorMessage="不合法的字符常量！";
			}
		}
		if(type==27){
			this.typeString="浮点数";
			if(!(value.charAt(value.length()-1)<='9'&&value.charAt(value.length()-1)>='0'))
			{
				this.errorMessage="不合法的浮点数！";
			}
		}
		if(type==28){
			this.typeString="十六进制";
			char ch;
			for(int i=2;i<this.value.length();i++)
			{
				ch=this.value.charAt(i);
				if(!((ch>='0'&&ch<='9')||(ch>='a'&&ch<='f')||(ch>='A'&&ch<='F')))
				{
					this.errorMessage="不合法的十六进制数！";
				}
			}
		}
		if(type==29){
			this.typeString="八进制";
			char ch;
			for(int i=1;i<this.value.length();i++)
			{
				ch=this.value.charAt(i);
				if(!(ch>='0'&&ch<='7'))
				{
					this.errorMessage="不合法的八进制数！";
				}
			}
		}
		if(type==34||type==35){
			this.typeString="科学计数法";
			char ch;
			for(int i=1;i<this.value.length();i++)
			{
				if(!(value.charAt(value.length()-1)<='9'&&value.charAt(value.length()-1)>='0'))
				{
					this.errorMessage="不合法的科学计数法！";
				}
			}
		}
		if(type==1||type==4){
			if(isKey(this.value))
			{
				this.typeString="关键字";
				this.type=112;
			}
			else
			{
				this.typeString="标识符";
			}
			
		}
		if(type==2||type==3){
			this.typeString="数字";
		}
		if(type==5||type==6){
			this.typeString="关系运算符";
		}
		if(type==7){
			this.typeString="等号";
		}
		if(type==8||type==15||type==16){
			this.typeString="位运算符";
		}
		if(type==9||type==10||type==11||type==12){
			this.typeString="算术运算符";
		}
		if(type==9||type==10||type==11||type==12){
			this.typeString="算术运算符";
		}else if(type==13||type==14||type==17||type==18||type==19||type==20||type==20||type==21||type==22||type==23||type==24||type==25||type==26)
		{
			this.typeString="界符";
		}
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	private boolean isKey(String value) {
		String[] key= {"int","float","boolean","struct","if","else","do","while","for","proc","record","then","real"};
		for(int i=0;i<key.length;i++)
		{
			if(value.equals(key[i]))
			{
				return true;
			}
		}
		return false;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	
}
