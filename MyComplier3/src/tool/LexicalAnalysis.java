package tool;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LexicalAnalysis {
	private List<Token> tokens = new ArrayList<Token>();
	private List<Error> errors = new ArrayList<Error>();
	private int LineNumber = 1;
	private int[][] FA;
	
	public List<Error> getErrors()
	{
		return errors;
	}

	public List<Token> getTokens(String text) {
		//System.out.println(text);
		this.FA = getFA();
		int state = 0;
		Token currentToken = new Token();
		String token = "";
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			int symbolNum = getSymbolNum(ch);
			if (symbolNum == -1) {//当前字符为空白符
				//如果当前换行且不在注释中且在字符串匹配过程中
				if ((ch == '\n'||ch==' '||ch=='\t')&& state != 30 && state != 31) {
					if (state == 33) {
						errors.add(new Error("字符串未完！", LineNumber));
					}else if(state>0)
					{
						currentToken.setLineNumber(LineNumber);
						currentToken.setValue(token);
						currentToken.setType(state);
						if(currentToken.getErrorMessage().length()>1)
						{
							errors.add(new Error(currentToken.getErrorMessage(),LineNumber));
						}else
						{
							tokens.add(currentToken);
						}
						
					}
					currentToken = new Token();
					token = "";
					state = 0;
					
				}else if((ch==' ')&&token.length()>0&&state!=30&&state!=31&&state!=33)
				{
					currentToken.setLineNumber(LineNumber);
					//System.out.println(token+state+LineNumber);
					currentToken.setValue(token);
					currentToken.setType(state);
					if(currentToken.getErrorMessage().length()>1)
					{
						errors.add(new Error(currentToken.getErrorMessage(),LineNumber));
					}else
					{
						tokens.add(currentToken);
					}
					currentToken = new Token();
					token = "";
					state = 0;
				}

			} else if (FA[state][symbolNum] == -1) {
				errors.add(new Error("非法字符！", LineNumber));
				System.out.println(token+" "+state+" "+LineNumber);
			} else if (FA[state][symbolNum] > 100) {
				token = token + ch;
				currentToken.setLineNumber(LineNumber);
				currentToken.setValue(token);
				currentToken.setType(FA[state][symbolNum]);
				if(currentToken.getErrorMessage().length()>1)
				{
					errors.add(new Error(currentToken.getErrorMessage(),LineNumber));
				}else
				{
					tokens.add(currentToken);
				}
				currentToken = new Token();
				token = "";
				state = 0;
			} else if (FA[state][symbolNum] == 0) {
				currentToken.setLineNumber(LineNumber);
				currentToken.setValue(token);
				currentToken.setType(state);
				if(currentToken.getErrorMessage().length()>1)
				{
					errors.add(new Error(currentToken.getErrorMessage(),LineNumber));
				}else
				{
					tokens.add(currentToken);
				}
				currentToken = new Token();
				token = "";
				i--;
				state = 0;
			} else {
				if(i==text.length()-1)
				{
					token = token + ch;
					state = FA[state][symbolNum];
					currentToken.setLineNumber(LineNumber);
					currentToken.setValue(token);
					currentToken.setType(state);
					tokens.add(currentToken);
					//state = FA[state][symbolNum];
				}else
				{
					token = token + ch;
					state = FA[state][symbolNum];
				}
				
			}
		}
		if (state == 30 || state == 31) {
			errors.add(new Error("注释未完！", LineNumber));
		}
		if (state == 33 || state == 14) {
			errors.add(new Error("字符串未完！", LineNumber));
		}
		return tokens;
	}
	
	private int getSymbolNum(char ch) {
		if(ch=='e'||ch=='E')
		{
			return 26;
		}
		if(ch=='0')
		{
			return 2;
		}
		if(ch=='x')
		{
			return 3;
		}
		if ((ch >='a' && ch <= 'z') || (ch <= 'Z' && ch >='A') || ch == '_') {
			return 0;// letter
		} else if (ch >= '1' && ch <= '9') {
			return 1;// digit
		} else if (ch == '<') {
			return 4;
		} else if (ch == '>') {
			return 5;
		} else if (ch == '=') {
			return 6;
		} else if (ch == '!') {
			return 7;
		} else if (ch == '+') {
			return 8;
		} else if (ch == '-') {
			return 9;
		} else if (ch == '/') {
			return 10;
		} else if (ch == '*') {
			return 11;
		} else if (ch == '\'') {
			return 12;
		} else if (ch == '\"') {
			return 13;
		} else if (ch == '&') {
			return 14;
		} else if (ch == '|') {
			return 15;
		} else if (ch == '[') {
			return 16;
		} else if (ch == ']') {
			return 17;
		} else if (ch == '{') {
			return 18;
		} else if (ch == '}') {
			return 19;
		} 
		else if (ch == '.') {
			return 20;
		} 
		else if (ch == '(') {
			return 22;
		}
		else if (ch == ')') {
			return 23;
		}else if (ch == ';') {
			return 24;
		}
		else if (ch == ',') {
			return 25;
		}else if (ch == '\n') {
			this.LineNumber++;
			return -1;
		}else if(ch==' '||ch=='\t'||ch=='\r')
		{
			return -1;
		}else{
			return 21;
		}

	}

	public int[][] getFA()
	{
		int[][] FA;
		List list=new ArrayList();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("FA.txt")));
			String line ="";
			while((line=reader.readLine())!=null)
			{
				String[] str = line.split("\t");
				list.add(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		FA = new int[list.size()][];
		String[] str={""};
		for(int i=0;i<list.size();i++)
		{
			 str = (String[]) list.get(i);
			int[] a=new int[str.length];
			for(int j=0;j<str.length;j++)
			{
				a[j] = Integer.parseInt(str[j]);
			}
			FA[i]= a;
			
		}
		/*for(int i=0;i<list.size();i++)
		{
			for(int j=0;j<str.length;j++)
			{
				System.out.print(FA[i][j]+""+'\t');
			}
			System.out.println();
		}*/
		
		return FA;
	}
	public class Error{
		String message;
		int LineNumber;
		public String getMessage() {
			return message;
		}
		public Error(String message, int lineNumber) {
			super();
			this.message = message;
			LineNumber = lineNumber;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public int getLineNumber() {
			return LineNumber;
		}
		public void setLineNumber(int lineNumber) {
			LineNumber = lineNumber;
		}
	}

}
