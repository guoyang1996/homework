package tool;

import java.util.ArrayList;

public class Production {
	// 产生式的左部  
    private NonTerminalCharacter Left;  
    // 产生式的右部  
    private ArrayList<MyCharacter> right;
	public NonTerminalCharacter getLeft() {
		return Left;
	}
	public void setLeft(NonTerminalCharacter left) {
		Left = left;
	}
	public ArrayList<MyCharacter> getRight() {
		return right;
	}
	public void setRight(ArrayList<MyCharacter> right) {
		this.right = right;
	}
	public boolean equals(Production production) {
		if(this.getLeft().value.equals(production.getLeft().value)&&this.getRight().size()==production.getRight().size())
		{
			for(int i=0;i<this.getRight().size();i++)
			{
				if(!this.getRight().get(i).value.equals(production.getRight().get(i).value))
				{
					return false;
				}
				
			}return true;
		}
		return false;
	}
	
	public String toString() {
		String str = this.Left.value+"->";
		for(int i=0;i<this.right.size();i++)
		{
			str = str+" "+this.getRight().get(i).value;
		}
		return str;
	}
	
}
