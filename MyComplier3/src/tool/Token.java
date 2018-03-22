package tool;

public class Token {
	private int type;//token������
	private String typeString="";
	private String errorMessage="";
	public String getTypeString() {
		return typeString;
	}
	public void setTypeString(String typeString) {
		this.typeString = typeString;
	}
	private String value;//ֵ
	private int lineNumber;//�к�
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
			this.typeString="��ϵ�����";
		}
		if(type==105){
			this.typeString="������";
		}
		if(type==106){
			this.typeString="�Լ���";
		}
		if(type==107){
			this.typeString="�ַ�������";
		}
		if(type==108||type==109){
			this.typeString="�߼������";
		}
		if(type==110){
			this.typeString="ע��";
		}
		if(type==111||type==32){
			this.typeString="�ַ�����";
			if(this.value.length()<=2)
			{
				this.errorMessage="���Ϸ����ַ�������";
			}
		}
		if(type==27){
			this.typeString="������";
			if(!(value.charAt(value.length()-1)<='9'&&value.charAt(value.length()-1)>='0'))
			{
				this.errorMessage="���Ϸ��ĸ�������";
			}
		}
		if(type==28){
			this.typeString="ʮ������";
			char ch;
			for(int i=2;i<this.value.length();i++)
			{
				ch=this.value.charAt(i);
				if(!((ch>='0'&&ch<='9')||(ch>='a'&&ch<='f')||(ch>='A'&&ch<='F')))
				{
					this.errorMessage="���Ϸ���ʮ����������";
				}
			}
		}
		if(type==29){
			this.typeString="�˽���";
			char ch;
			for(int i=1;i<this.value.length();i++)
			{
				ch=this.value.charAt(i);
				if(!(ch>='0'&&ch<='7'))
				{
					this.errorMessage="���Ϸ��İ˽�������";
				}
			}
		}
		if(type==34||type==35){
			this.typeString="��ѧ������";
			char ch;
			for(int i=1;i<this.value.length();i++)
			{
				if(!(value.charAt(value.length()-1)<='9'&&value.charAt(value.length()-1)>='0'))
				{
					this.errorMessage="���Ϸ��Ŀ�ѧ��������";
				}
			}
		}
		if(type==1||type==4){
			if(isKey(this.value))
			{
				this.typeString="�ؼ���";
				this.type=112;
			}
			else
			{
				this.typeString="��ʶ��";
			}
			
		}
		if(type==2||type==3){
			this.typeString="����";
		}
		if(type==5||type==6){
			this.typeString="��ϵ�����";
		}
		if(type==7){
			this.typeString="�Ⱥ�";
		}
		if(type==8||type==15||type==16){
			this.typeString="λ�����";
		}
		if(type==9||type==10||type==11||type==12){
			this.typeString="���������";
		}
		if(type==9||type==10||type==11||type==12){
			this.typeString="���������";
		}else if(type==13||type==14||type==17||type==18||type==19||type==20||type==20||type==21||type==22||type==23||type==24||type==25||type==26)
		{
			this.typeString="���";
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
