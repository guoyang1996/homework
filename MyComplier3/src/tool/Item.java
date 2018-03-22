package tool;

public class Item {
	private Production production;
	private int index;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	private TerminalCharacter lfSymbol;

	public Production getProduction() {
		return production;
	}

	public void setProduction(Production production) {
		this.production = production;
	}

	public TerminalCharacter getLfSymbol() {
		return lfSymbol;
	}

	public void setLfSymbol(TerminalCharacter lfSymbol) {
		this.lfSymbol = lfSymbol;
	}

	public String toString() {
		String str = production.getLeft().value+"->";
		for(int i=0;i<production.getRight().size();i++)
		{
			MyCharacter ch=production.getRight().get(i);
			if(i==index)
			{
				str=str+"¡¤";
			}
			str=str+ch.value+" ";
		}
		return str+this.lfSymbol.value;
	}

	public boolean equals(Item item) {

		if (this.production.equals(item.production) && this.index == item.index&&this.lfSymbol.value.equals(item.lfSymbol.value)) {
			//System.out.println(this.production.toString() + " "+ item.production.toString());
			return true;
		}
		//System.out.println(this.production.toString() + " "+ item.production.toString());
		return false;
	}
}