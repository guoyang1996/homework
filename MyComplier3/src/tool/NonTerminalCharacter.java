package tool;

import java.util.ArrayList;
import java.util.List;

public class NonTerminalCharacter extends MyCharacter{
	 private ArrayList<String> Follow;

	public NonTerminalCharacter(String what) {  
	        super();  
	        this.value = what;  
	        Follow = new ArrayList<String>();  
	    }
	public boolean equals(NonTerminalCharacter ntc) {
		if(ntc.value.equals(value))
		{
			return true;
		}
		return false;
	}
	
}
