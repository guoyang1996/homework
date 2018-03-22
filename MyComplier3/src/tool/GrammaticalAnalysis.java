package tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.swing.tree.DefaultMutableTreeNode;

public class GrammaticalAnalysis {
	private String t="";//��ʱ����t
	private int w =0;//��ʱ����w
	int temp = 0;//��ʱ������
	/**
	 * �ķ����ż�
	 */
	ArrayList<MyCharacter> characters = new ArrayList<MyCharacter>();
	/**
	 * �������ս����first��
	 */
	Map<String, Set<String>> firsts;
	/**
	 * ����ʽ
	 */
	public List<Production> productions = new ArrayList<Production>();
	/**
	 * ���
	 */
	public ArrayList<ArrayList<Item>> itemLists = new ArrayList<ArrayList<Item>>();
	/**
	 * ������Ϣ
	 */
	private ArrayList<String> actionInfo = new ArrayList<String>();
	/**
	 * ����ַָ������
	 */
	private ArrayList<String> addrList = new ArrayList<String>();
	/**
	 * ��Ԫ������
	 */
	private ArrayList<Element> eleList = new ArrayList<Element>();
	/**
	 * ƫ����
	 */
	private int offset = 0;
	private int getOffset() {
		return offset;
	}

	private void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * ���ű�
	 */
	private ArrayList<Record> recordTable = new ArrayList<Record>();
	
	
	public ArrayList<String> getAddrList() {
		return addrList;
	}

	public void setAddrList(ArrayList<String> addrList) {
		this.addrList = addrList;
	}

	public ArrayList<Record> getRecordTable() {
		return recordTable;
	}

	public void setRecordTable(ArrayList<Record> recordTable) {
		this.recordTable = recordTable;
	}

	public ArrayList<String> getActionInfo() {
		return actionInfo;
	}

	public void setActionInfo(ArrayList<String> actionInfo) {
		this.actionInfo = actionInfo;
	}

	public GrammaticalAnalysis() {
	}

	/**
	 * ��ȡ�����Ŀ�հ������
	 * 
	 * @param itemList
	 * @param mch
	 * @return
	 */
	private int getNextItemList(ArrayList<Item> itemList, MyCharacter mch) {
		ArrayList<Item> gotoList = GOTO(itemList, mch);
		for (int i = 0; i < itemLists.size(); i++) {
			ArrayList<Item> iList = itemLists.get(i);
			if (isEqual(iList, gotoList)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * ��ö�Ӧ����ʽ������
	 * 
	 * @param p
	 * @return
	 */
	private int getProductionIndex(Production p) {
		for (int i = 0; i < productions.size(); i++) {
			Production production = productions.get(i);
			if (production.equals(p)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * ���ļ��ж�ȡ����ʽ��������first��
	 */
	private void readProduction() {
		// ��$�������ķ����ż��У���Ϊ�ڲ���ʽ�в�����������ķ����ţ�������Ҫ�ֶ����
		TerminalCharacter last = new TerminalCharacter("$");
		characters.add(last);
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream("grammar.txt")));
			String line = "";

			while ((line = reader.readLine()) != null) {
				String[] str = line.split(" ");
				Production p = new Production();
				NonTerminalCharacter nt = new NonTerminalCharacter(str[0]);
				if (canAdd(nt))
					characters.add(nt);
				p.setLeft(nt);
				ArrayList<MyCharacter> right = new ArrayList<MyCharacter>();
				for (int i = 1; i < str.length; i++) {
					if (str[i].charAt(0) >= 'A' && str[i].charAt(0) <= 'Z') {
						NonTerminalCharacter n = new NonTerminalCharacter(
								str[i]);
						if (canAdd(n))
							characters.add(n);
						right.add(n);
					} else {
						TerminalCharacter n = new TerminalCharacter(str[i]);
						if (canAdd(n))
							characters.add(n);
						right.add(n);
					}
				}
				p.setRight(right);
				productions.add(p);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ��һ��Map��������Ÿ����ķ����ŵ�FIRST��
		firsts = new HashMap<String, Set<String>>();
		boolean flag = true;// ����Ƿ�ı�FIRST��
		// ��ʼ����Ϊÿһ�����ս�����FIRST��
		for (int i = 0; i < productions.size(); i++) {
			Set<String> first = new HashSet<String>();
			firsts.put(productions.get(i).getLeft().value, first);
		}
		do {
			flag = false;
			for (int i = 0; i < productions.size(); i++) {
				Production p = productions.get(i);
				// ��ŵ�i������ʽ�󲿷��ս����FIRST��
				Set<String> first = firsts.get(p.getLeft().value);
				if (p.getRight().size() < 1) {
					if (first.add(null)) {
						flag = true;
					}
				} else {
					int j = 0;
					MyCharacter ch = p.getRight().get(0);// ����ʽ�Ҳ���һ���ķ�����
					while (j < p.getRight().size()) {
						if (ch instanceof TerminalCharacter) {
							break;
						} else if (firsts.get(ch.value).contains(null)) {
							j++;
							first.addAll(ch.first);
							flag=true;
							ch = p.getRight().get(j);
						} else {
							break;
						}
					}
					if(j==p.getRight().size())
					{
						first.add(null);
					}
					if (ch instanceof TerminalCharacter) {
						if (first.addAll(ch.first)) {
							flag = true;
						}
					} else if (first.addAll(firsts.get(ch.value))) {
						flag = true;
					}
				}
				firsts.put(p.getLeft().value, first);
			}
		} while (flag);
	}

	/**
	 * Ϊ�ռ��ķ�������׼���Ĺ��ߺ�������ֹ�ظ���� ÿ���һ���ķ�����ǰ���ȼ���Ƿ��֮ǰ������
	 * ������ˣ��ͷ���false������˵��������ӣ�����true
	 * 
	 * @param nt
	 * @return
	 */
	private boolean canAdd(MyCharacter nt) {
		for (int i = 0; i < characters.size(); i++) {
			if (characters.get(i).value.equals(nt.value)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * ���ķ������
	 */
	private void getItemLists() {
		Item startItem = new Item();
		startItem.setProduction(productions.get(0));
		startItem.setIndex(0);
		startItem.setLfSymbol(new TerminalCharacter("$"));
		ArrayList<Item> it = new ArrayList<Item>();
		it.add(startItem);
		itemLists.add(getClosure(it));
		// ����һ����ǣ�����Ƿ��ܹ���������������
		boolean flag = false;
		do {
			flag = false;

			for (int i = 0; i < itemLists.size(); i++) {
				ArrayList<Item> itemList = itemLists.get(i);
				for (int j = 0; j < this.characters.size(); j++) {
					MyCharacter mch = this.characters.get(j);
					if (GOTO(itemList, mch) != null
							&& canAdd(GOTO(itemList, mch))) {
						this.itemLists.add(GOTO(itemList, mch));
						flag = true;
					}
				}
			}
		} while (flag);
	}

	/**
	 * ��ǰ�����Ŀ���Ƿ�ɼ�����Ŀ���壬���Է���true������false
	 * 
	 * @param goto1
	 * @return
	 */
	private boolean canAdd(ArrayList<Item> gotoItemList) {
		boolean flag = true;// ����һ����ǣ�����ҵ�һ����goto��ͬ�ģ���Ϊfalse
		for (ArrayList<Item> itemList : itemLists) {
			// �����ǰ��Ŀ����goto�ȼۣ���˵��goto���ܱ����
			if (isEqual(itemList, gotoItemList)) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	/**
	 * �ж�������Ŀ���Ƿ�ĵȼ�
	 * 
	 * @param itemList
	 * @param gotoItemList
	 * @return
	 */
	private boolean isEqual(ArrayList<Item> itemList,
			ArrayList<Item> gotoItemList) {
		// System.out.println(itemList.size() + " " + gotoItemList.size());
		if (itemList.size() != gotoItemList.size()) {
			return false;
		} else {
			boolean isBelong = false;
			for (int i = 0; i < itemList.size(); i++) {
				isBelong = false;
				for (int j = 0; j < gotoItemList.size(); j++) {
					if (itemList.get(i).equals(gotoItemList.get(j))) {
						isBelong = true;
						break;
					}
				}
				if (!isBelong) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * ��������Ŀ��
	 * 
	 * @param itemList
	 * @param mch
	 * @return
	 */
	private ArrayList<Item> GOTO(ArrayList<Item> itemList, MyCharacter mch) {
		ArrayList<Item> gotoItemList = new ArrayList<Item>();
		for (Item item : itemList) {
			if (item.getIndex() < item.getProduction().getRight().size()
					&& item.getProduction().getRight().get(item.getIndex())
							.equals(mch)) {
				Item nextItem = new Item();
				nextItem.setIndex(item.getIndex() + 1);
				nextItem.setLfSymbol(item.getLfSymbol());
				nextItem.setProduction(item.getProduction());
				if (canAdd(gotoItemList, nextItem)) {
					gotoItemList.add(nextItem);
				}
			}
		}
		if (gotoItemList.isEmpty()) {
			return null;
		}
		return getClosure(gotoItemList);
	}

	/**
	 * ����Ŀ���հ�
	 * 
	 * @param i
	 * @return
	 */
	private ArrayList<Item> getClosure(ArrayList<Item> i) {
		ArrayList<Item> closure = new ArrayList<Item>();
		closure.addAll(i);
		// ����һ����ǣ�����Ƿ��ܹ�������closure�������
		boolean flag = false;
		do {
			flag = false;
			for (int i1 = 0; i1 < closure.size(); i1++) {
				Item item = closure.get(i1);
				Production p = item.getProduction();
				if (item.getIndex() < p.getRight().size()) {
					MyCharacter b = (MyCharacter) p.getRight().get(
							item.getIndex());
					TerminalCharacter a = item.getLfSymbol();
					Set<String> first = new HashSet<String>();
					ArrayList<MyCharacter> beta = new ArrayList<MyCharacter>();
					for(int n=item.getIndex()+1;n<p.getRight().size();n++)
					{
						beta.add((MyCharacter) p.getRight().get(n));
					}
					beta.add(a);
					//��beta+a��FIRST��
					for(int v=0;v<beta.size();v++)
					{
						MyCharacter ch = beta.get(v);
						if(ch instanceof NonTerminalCharacter)
						{
							
							//System.out.println(ch.value);
							first.addAll(firsts.get(ch.value));
							if(containsNull(firsts.get(ch.value)))
							{
								//System.out.println(12);
							}else{
								break;
							}
						}else{
							first.add(ch.value);
							break;
						}
					}
					//System.out.println(beta.get(0).value+first.size());
					for (Production production : productions) {
						if (production.getLeft().value.equals(b.value)) {
							for (String st : first) {
								if (st == null) {
									continue;
								}
								Item newItem = new Item();
								newItem.setProduction(production);
								newItem.setIndex(0);
								newItem.setLfSymbol(new TerminalCharacter(st));
								if (canAdd(closure, newItem)) {
									closure.add(newItem);
									flag = true;
								}
							}
						}
					}
				}
			}

		} while (flag);
		return closure;
	}
	private boolean containsNull(Set<String> first) {
		for(String str:first)
		{
			//System.out.println(str);
			if(str==null)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * ��д���жϼ����е�item�Ƿ��ظ��ķ��������û���ظ����������ӣ�����true�����򷵻�false
	 * 
	 * @param closure
	 * @param item
	 * @return
	 */
	private boolean canAdd(ArrayList<Item> closure, Item item) {
		for (int i = 0; i < closure.size(); i++) {
			if (item.equals(closure.get(i))) {
				return false;
			}
		}
		return true;
	}

	public DefaultMutableTreeNode getParseTree(List<Token> tokens) {
		readProduction();
		getItemLists();
		// ���ԣ���ӡÿ����Ŀ����ÿһ��
		try {
			FileOutputStream out = new FileOutputStream(new File("item.txt"));
			for (int k = 0; k < itemLists.size(); k++) {
				ArrayList<Item> itemList = itemLists.get(k);
				for (Item item : itemList) {
					String str = "��" + k + "����Ŀ��"
							+item.toString()+"\n";
					out.write(str.getBytes());
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//��ӡ����ʽ
		/*for(int j=0;j<productions.size();j++)
		{
			System.out.println(productions.get(j).toString());
		}*/

		// ��ӡFIRST��
		
		 /* for (int k = 0; k < characters.size(); k++) { MyCharacter mch =
		  characters.get(k); System.out.println(mch.value + " " +
		  firsts.get(mch.value)); }*/
		 
		int s = itemLists.size();// ״̬��
		int a = this.characters.size();// �ķ�������
		int[][] action = new int[s][a];
		Item endItem = new Item();
		endItem.setProduction(productions.get(0));
		endItem.setLfSymbol(new TerminalCharacter("$"));
		endItem.setIndex(1);
		// ���������ʼ������ȫ������Ϊ����
		for (int i = 0; i < s; i++) {
			for (int j = 0; j < a; j++) {
				action[i][j] = -1;
			}
		}
		for (int i = 0; i < s; i++) {
			ArrayList<Item> itemList = itemLists.get(i);
			for (int j = 0; j < a; j++) {
				for (Item item : itemList) {
					MyCharacter mch = this.characters.get(j);
					Production p = item.getProduction();
					int index = item.getIndex();
					// ��Լ�����״̬
					if (index == p.getRight().size()
							&& mch.equals(item.getLfSymbol())) {
						// if [S'��S��, $] ��Ii then ACTION [ i, $ ]=acc;����״̬
						if (p.equals(productions.get(0))
								&& mch.equals(new TerminalCharacter("$"))) {
							action[i][j] = 0;
						}
						// if [A������, a ] ��Ii��A �� S' then ACTION[ i, a ]=rj
						else {
							// �õڼ�������ʽ���й�Լ��Ϊ�������붯�����ֿ���������ֵȡ��������ͬʱҲ���ֿ�����
							action[i][j] = getProductionIndex(p) * (-1) - 2;
						}
					} else
					// if [A������a��, b ] ��Ii
					// System.out.println(index+" "+p.getRight().size());
					if (p.getRight().size() > index
							&& p.getRight().get(index).equals(mch)) {
						// ����״̬
						action[i][j] = getNextItemList(itemList, mch);
					}
				}
			}
		}
		// ��ӡ������

		
		/* for (int i = 0; i < s; i++) { System.out.print("��" + i + "��\t"); for
		  (int j = 0; j < a; j++) { System.out.print(action[i][j] + "" + '\t');
		  } System.out.println(); }*/
		 

		int currentState = 0;
		// ״̬ջ
		Stack<Integer> state = new Stack<Integer>();
		// ����ջ
		Stack<MyCharacter> character = new Stack<MyCharacter>();
		//���νṹ��ջ
		Stack<DefaultMutableTreeNode> tree = new Stack<DefaultMutableTreeNode>();
		// ��ʼ��
		state.push(currentState);
		//character.push();
		MyCharacter ach=new TerminalCharacter("$");
		//��ǰ�к�
		int lineNumber = 0;
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(new NonTerminalCharacter("start"));
		tree.push(root);
		for (int i = 0; i <= tokens.size(); i++) {
			MyCharacter next;
			if (i < tokens.size()) {
				Token token = tokens.get(i);
				next = getMyCharacter(token);
				lineNumber = token.getLineNumber();
			} else {
				next = new TerminalCharacter("$");
			}
			int nextIndex = getIndexOfCharacter(next);
				character.push(ach);
				if(ach instanceof TerminalCharacter)
				{
					DefaultMutableTreeNode achNode =  new DefaultMutableTreeNode(ach);
					tree.push(achNode);
				}
				actionInfo.add("���� "+":" + ach.value+"�к�"+lineNumber);
			if (action[currentState][nextIndex] > 0) {
				currentState = action[currentState][nextIndex];
				state.push(currentState);
				ach=next;
			} else if (action[currentState][nextIndex] < -1) {
				Stack<MyCharacter> chStackOfRight = new Stack<MyCharacter>();
				int indexOfProduction = (action[currentState][nextIndex] + 2)
						* (-1);
				Production p = productions.get(indexOfProduction);
				int num = p.getRight().size();// ��Ҫ�����ķ��ŵĸ���
				DefaultMutableTreeNode left =  new DefaultMutableTreeNode(p.getLeft());
				for (int k = 0; k < num; k++) {
					MyCharacter chOfRight = character.pop();
					chStackOfRight.add(chOfRight);
					DefaultMutableTreeNode right = tree.pop();//�˴���tree��һ��ջ�ṹ������ȡ��ûȡ�õĹ�T_T
					left.add(right);
				    state.pop();
				}
				actionInfo.add(p.toString());
				//System.out.println(p.toString());
				currentState = state.peek();
				currentState = getNextItemList(itemLists.get(currentState),
						p.getLeft());
				state.push(currentState);
				//System.out.println("��Լʱ���Ѳ���ʽ�󲿽ڵ㣺ѹ��ջ"+left.getUserObject()+" "+left.getChildCount());
				tree.push(left);
				
				//�������������Ϊ�ķ��������ø������ԣ��Լ���������ַ�룬����Ϊ�ķ�������������Ե����ķ�����
				ach = addMeaning(new NonTerminalCharacter(p.getLeft().value),chStackOfRight,indexOfProduction,character);i--;
				
			} else if (action[currentState][nextIndex] == 0) {
				Stack<MyCharacter> chStackOfRight = new Stack<MyCharacter>();
				Production p = productions.get(0);
				int num = p.getRight().size();// ��Ҫ�����ķ��ŵĸ���
				DefaultMutableTreeNode left =  new DefaultMutableTreeNode(p.getLeft());
				for (int k = 0; k < num; k++) {
					MyCharacter chOfRight = character.pop();
					chStackOfRight.push(chOfRight);
					DefaultMutableTreeNode right = tree.pop();;
					left.add(right);
				    state.pop();
				}
				actionInfo.add(p.toString());
				//System.out.println(p.toString());
				state.push(currentState);
				//�������������Ϊ�ķ��������ø������ԣ��Լ���������ַ�룬����Ϊ�ķ�������������Ե����ķ�����
				ach = addMeaning(p.getLeft(),chStackOfRight,0,character);i--;
				left.setUserObject(ach);
				tree.push(left);
				System.out.println("acc");
				//�����������
				/*for(int l=0;l<addrList.size();l++)
				{
					System.out.println(addrList.get(l));
				}*/
				/*for(int n=0;n<recordTable.size();n++)
				{
					Record record = recordTable.get(n);
					System.out.println(record.getId()+" "+record.getType()+" "+record.getOffset());
				}*/
				return tree.peek();
			} else {
				return null;
				// System.out.println("err");
			}
		}
		
		return root;
	}

	/**
	 * ��������ؼ��������������ԣ���������ַ�룬��� 
	 * @param left
	 * @param chStackOfRight 
	 * @param character 
	 * @param indexOfProdction 
	 * @return
	 */
	private MyCharacter addMeaning(MyCharacter left, Stack<MyCharacter> chStackOfRight, int indexOfProduction, Stack<MyCharacter> character) {
		
		switch(indexOfProduction)
		{
			case 0:					//Start->P
				return left;
			case 1:					//P->D
				return left;
			case 2:					//P->S
			{
				return left;
			}
			case 3:					//S->S A0 S
			{
			 	//S.nextlist = S2.nextlist ; 
				MyCharacter S1 = chStackOfRight.pop();
				MyCharacter M = chStackOfRight.pop();
				MyCharacter S2= chStackOfRight.pop();
				backpatch(S1.getNextList(),M.getQuad());
				left.setNextList(S2.getNextList());
				System.out.println("S->S A0 S:"+M.getQuad());
				return left;
			}
			case 4:					//A0->��
			{
				int quad = this.addrList.size();
				left.setQuad(quad);
				return left;
			}
			case 5:					
				return left;
			case 6:					//D->proc id ; D S
				return left;
			case 7:					//D -> T id ;
			{
				Record record = new Record();
				MyCharacter T = chStackOfRight.pop();
				MyCharacter id = chStackOfRight.pop();
				record.setId(id.getActualValue());
				record.setOffset(this.getOffset());
				record.setType(T.getType());
				//System.out.println(record.getId()+" "+record.getType());
				this.recordTable.add(record);
				this.setOffset(this.getOffset()+T.getWidth());
				return left;
			}
			case 8:					//T -> X D0 C
			{
				chStackOfRight.pop();
				chStackOfRight.pop();
				MyCharacter C = chStackOfRight.pop();
				left.setType(C.getType());
				left.setWidth(C.getWidth());
				//System.out.println("T: "+left.getType());
				return left;
			}
			case 9:					//D0->��
			{
				MyCharacter X = character.peek();
				left.setType(X.getType());
				left.setWidth(X.getWidth());
				t=X.getType().getType();
				w=X.getWidth();
				return left;
			}
			case 10:					//T->record D
			{
				left.setType(new Type("record"));
				return left;
			}
			case 11:				//X->int
			{
				left.setType(new Type("int"));
				left.setWidth(4);
				return left;
			}
			case 12:				//X->real
			{
				left.setType(new Type("real"));
				left.setWidth(8);
				return left;
			}
			case 13:				//C->[ num ] C
			{
				chStackOfRight.pop();
				MyCharacter num = chStackOfRight.pop();
				chStackOfRight.pop();
				MyCharacter C = chStackOfRight.pop();
				int n = Integer.parseInt(num.getActualValue());
				left.setType(new Type(n,C.getType()));
				left.setWidth(n*C.getWidth());
				//System.out.println("C: "+left.getType());
				return left;
			}
			case 14:				//C->��
			{
				left.setType(new Type(t));
				left.setWidth(w);
				System.out.println("C: "+left.getType());
				return left;
			}
			case 15:				//S->id = E ;
			{
				MyCharacter id = chStackOfRight.pop();
				if(!inRecord(id.getActualValue()))
				{
					System.out.println("err:"+id.getActualValue()+"δ������");
				}
				String p = id.getActualValue(); 
				chStackOfRight.pop();
				MyCharacter E = chStackOfRight.pop();
				this.addrList.add(p+"="+E.getAddr());
				Element e = new Element("=",E.getAddr(),"",p);
				this.eleList.add(e);
				return left;
			}
			case 16:				//S->L = E ;
			{
				MyCharacter L = chStackOfRight.pop();
				chStackOfRight.pop();
				MyCharacter E = chStackOfRight.pop();
				//gen( L.array ��[�� L.offset ��]�� ��=�� E.addr ); 
				this.addrList.add(L.getAddr()+"["+L.getOffset()+"]"+"="+E.getAddr());
				Element e = new Element("=",L.getOffset(),"",L.getAddr());
				this.eleList.add(e);
				return left;
			}
			case 17:				//E->E + E
			{
				MyCharacter E1 = chStackOfRight.pop();
				chStackOfRight.pop();
				MyCharacter E2 = chStackOfRight.pop();
				String addr = "t"+temp;
				temp++;
				left.setAddr(addr);
				this.addrList.add(left.getAddr()+"="+E1.getAddr()+"+"+E2.getAddr());
				Element e = new Element("+",E1.getAddr(),E1.getAddr(),left.getAddr());
				this.eleList.add(e);
				//System.out.println("E1-"+E1.getAddr()+"+E2-"+E2.getAddr());
				return left;
			}
			case 18:				//E->E * E
			{
				MyCharacter E1 = chStackOfRight.pop();
				chStackOfRight.pop();
				MyCharacter E2 = chStackOfRight.pop();
				String addr = "t"+temp;
				temp++;
				left.setAddr(addr);
				this.addrList.add(left.getAddr()+"="+E1.getAddr()+"*"+E2.getAddr());
				//System.out.println("E1:"+E1.getAddr()+"*E2:"+E2.getAddr());
				Element e = new Element("*",E1.getAddr(),E1.getAddr(),left.getAddr());
				this.eleList.add(e);
				return left;
			}
			case 19:				//E->- E
			{
				chStackOfRight.pop();
				MyCharacter E = chStackOfRight.pop();
				String addr = "t"+temp;
				temp++;
				left.setAddr(addr);
				this.addrList.add(left.getAddr()+"=-"+E.getAddr());
				Element e = new Element("-",E.getAddr(),"",left.getAddr());
				this.eleList.add(e);
				return left;
			}
			case 20:				//E->( E )
			{
				chStackOfRight.pop();
				MyCharacter E = chStackOfRight.pop();
				String addr = "t"+temp;
				temp++;
				left.setAddr(addr);
				this.addrList.add(left.getAddr()+"="+E.getAddr());
				Element e = new Element("=",E.getAddr(),"",left.getAddr());
				this.eleList.add(e);
				return left;
			}
			case 21:				//E->id
			{
				
				MyCharacter id = chStackOfRight.pop();
				if(!inRecord(id.getActualValue()))
				{
					System.out.println("err:"+id.getActualValue()+"δ������");
				}
				left.setAddr(id.getActualValue());
				return left;
			}
			case 22:				//E->num
			{
				MyCharacter num = chStackOfRight.pop();
				String addr = "t"+temp;
				temp++;
				left.setAddr(addr);
				this.addrList.add(left.getAddr()+"="+num.getActualValue());
				Element e = new Element("=",num.getAddr(),"",left.getAddr());
				this.eleList.add(e);
				return left;
			}
			case 23:				//E->L
			{
				MyCharacter L = chStackOfRight.pop();
				left.setAddr("t"+temp);
				temp++;
				this.addrList.add(left.getAddr()+"="+L.getAddr()+"["+L.getOffset()+"]");
				Element e = new Element("=",L.getAddr(),L.getOffset(),left.getAddr());
				this.eleList.add(e);
				return left;
			}
			case 24:				//L->id [ E ]
			{
				MyCharacter id = chStackOfRight.pop();
				chStackOfRight.pop();
				MyCharacter E = chStackOfRight.pop();
				if(!inRecord(id.getActualValue()))
				{
					System.out.println("err:"+id.getActualValue()+"δ������");
				}
				left.setAddr(id.getActualValue());
				id.setType(getTypeInRecord(id));
				left.setType(id.getType().getChildType());
				String offsetOfL = "t"+temp;
				left.setOffset(offsetOfL);
				temp++;
				this.addrList.add(offsetOfL+"="+E.getAddr()+"*"+id.getType().getChildType().getWidth());
				Element e = new Element("*",E.getAddr(),id.getType().getChildType().getWidth()+"",offsetOfL);
				this.eleList.add(e);
				return left;	
			}
			case 25:				//L->L [ E ]
			{
				MyCharacter L = chStackOfRight.pop();
				chStackOfRight.pop();
				MyCharacter E = chStackOfRight.pop();
				left.setAddr(L.getAddr());
				left.setType(L.getType().getChildType());
				String tt ="t"+temp;
				temp++;
				this.addrList.add(tt+"="+E.getAddr()+"*"+L.getType().getWidth());
				Element e = new Element("*",E.getAddr(),L.getType().getWidth()+"",tt);
				this.eleList.add(e);
				String offsetOfL = "t"+temp;
				left.setOffset(offsetOfL);
				temp++;
				this.addrList.add(offsetOfL+"="+L.getOffset()+"+"+tt);
				Element e1 = new Element("+",L.getOffset(),tt,offsetOfL);
				this.eleList.add(e1);
				return left;
			}
			case 26:			//S->if B then A0 S
			{
				chStackOfRight.pop();
				MyCharacter B = chStackOfRight.pop();
				chStackOfRight.pop();
				MyCharacter M = chStackOfRight.pop();
				MyCharacter S = chStackOfRight.pop();
				backpatch(B.getTrueList(), M.getQuad());
				left.getNextList().addAll(B.getFalseList());
				left.getNextList().addAll(S.getNextList());
				System.out.println("S->if B then A0 S:"+M.getQuad());
				return left;
			}
			case 27:			//S->if B then A0 S B0 else A0 S
			{
				chStackOfRight.pop();//if
				MyCharacter B = chStackOfRight.pop();
				chStackOfRight.pop();//then
				MyCharacter M1 = chStackOfRight.pop();
				MyCharacter S1 = chStackOfRight.pop();
				MyCharacter N = chStackOfRight.pop();
				chStackOfRight.pop();//else
				MyCharacter M2= chStackOfRight.pop();
				MyCharacter S2 = chStackOfRight.pop();
				backpatch(B.getTrueList(), M1.getQuad());
			 	backpatch(B.getFalseList(), M2.getQuad());
			 	left.getNextList().addAll(S1.getNextList());
			 	left.getNextList().addAll(N.getNextList());
			 	left.getNextList().addAll(S2.getNextList());
				//S.nextlist = merge( merge(S1.nextlist,N.nextlist), S2.nextlist ); 
			 	//System.out.println("S->if B then A0 S B0 else A0 S:"+M1.getQuad()+" "+M2.getQuad());
				return left;
			}	
			case 28:			//S->while A0 B do A0 S
			{
				chStackOfRight.pop();//while
				MyCharacter M1= chStackOfRight.pop();
				MyCharacter B= chStackOfRight.pop();
				chStackOfRight.pop();//do
				MyCharacter M2= chStackOfRight.pop();
				MyCharacter S= chStackOfRight.pop();
				backpatch( S.getNextList(), M1.getQuad() );
			 	backpatch( B.getTrueList(), M2.getQuad() );
			 	S.setNextList(B.getFalseList());
			 	this.addrList.add("goto "+M1.getQuad());	//gen(��goto�� M1.quad); 
			 	Element e1 = new Element("j","","",M1.getQuad()+"");
				this.eleList.add(e1);
			 	//System.out.println("S->while A0 B do A0 S:"+M1.getQuad()+" "+M2.getQuad());
			 	return left;
			} 
			case 29:			//B->B || A0 B
			{
				MyCharacter B1 = chStackOfRight.pop();
				chStackOfRight.pop();
				MyCharacter M = chStackOfRight.pop();
				MyCharacter B2 = chStackOfRight.pop();
				backpatch(B1.getFalseList(), M.getQuad());
				left.getTrueList().addAll(B1.getTrueList());
				left.getTrueList().addAll(B2.getTrueList());
			 	left.setFalseList(B2.getFalseList());
				return left;
			}
			case 30:			//B->B && A0 B
			{
				MyCharacter B1 = chStackOfRight.pop();
				chStackOfRight.pop();
				MyCharacter M = chStackOfRight.pop();
				MyCharacter B2 = chStackOfRight.pop();
				backpatch(B1.getTrueList(), M.getQuad());
				left.getFalseList().addAll(B1.getFalseList());
				left.getFalseList().addAll(B2.getFalseList());
			 	left.setTrueList(B2.getTrueList());
				return left;
			}
			case 31:			//B-> ! B
			{
				chStackOfRight.pop();
				MyCharacter B1 = chStackOfRight.pop();
				left.setFalseList(B1.getTrueList());
				left.setTrueList(B1.getFalseList());
				return left;
			}
			case 32:			//B->( B )
			{
				chStackOfRight.pop();
				MyCharacter B1 = chStackOfRight.pop();
				left.setTrueList(B1.getTrueList());
				left.setFalseList(B1.getFalseList());
				return left;
			}
			case 33:			//B->E < E
			{
				MyCharacter E1 = chStackOfRight.pop();
				MyCharacter relop= chStackOfRight.pop();
				MyCharacter E2 = chStackOfRight.pop();
				int nextquad = addrList.size();
				ArrayList<Integer> trueList = new ArrayList<Integer>();
				trueList.add(nextquad);
				ArrayList<Integer> falseList = new ArrayList<Integer>();
				falseList.add(nextquad+1);
				left.setTrueList(trueList);
				left.setFalseList(falseList);
				this.addrList.add("if"+ " "+E1.getAddr()+" "+ relop.getValue()+" "+ E2.getAddr()+"goto ");
				Element e1 = new Element("j"+relop.getValue(),E1.getAddr(),E2.getAddr(),"");
				this.eleList.add(e1);
				this.addrList.add("goto ");
				Element e = new Element("j","","","");
				this.eleList.add(e);
				return left;
			}
			case 34:			//B->E <= E
			{
				MyCharacter E1 = chStackOfRight.pop();
				MyCharacter relop= chStackOfRight.pop();
				MyCharacter E2 = chStackOfRight.pop();
				int nextquad = addrList.size();
				ArrayList<Integer> trueList = new ArrayList<Integer>();
				trueList.add(nextquad);
				ArrayList<Integer> falseList = new ArrayList<Integer>();
				falseList.add(nextquad+1);
				left.setTrueList(trueList);
				left.setFalseList(falseList);
				this.addrList.add("if "+ E1.getAddr()+" "+ relop.getValue()+" "+ E2.getAddr()+" goto ");
				Element e = new Element("j"+ relop.getValue(),E1.getAddr(),E2.getAddr(),"");
				this.eleList.add(e);
				this.addrList.add("goto ");
				Element e1 = new Element("j","","","");
				this.eleList.add(e1);
				return left;
				
			}
			case 35:			//B->E == E
			{
				MyCharacter E1 = chStackOfRight.pop();
				MyCharacter relop= chStackOfRight.pop();
				MyCharacter E2 = chStackOfRight.pop();
				int nextquad = addrList.size();
				ArrayList<Integer> trueList = new ArrayList<Integer>();
				trueList.add(nextquad);
				ArrayList<Integer> falseList = new ArrayList<Integer>();
				falseList.add(nextquad+1);
				left.setTrueList(trueList);
				left.setFalseList(falseList);
				this.addrList.add("if "+ E1.getAddr()+" "+ relop.getValue()+" "+ E2.getAddr()+"goto ");
				Element e = new Element("j"+ relop.getValue(),E1.getAddr(),E2.getAddr(),"");
				this.eleList.add(e);
				this.addrList.add("goto ");
				Element e1 = new Element("j","","","");
				this.eleList.add(e1);
				return left;
			}
			case 36:			//B->E != E
			{
				MyCharacter E1 = chStackOfRight.pop();
				MyCharacter relop= chStackOfRight.pop();
				MyCharacter E2 = chStackOfRight.pop();
				int nextquad = addrList.size();
				ArrayList<Integer> trueList = new ArrayList<Integer>();
				trueList.add(nextquad);
				ArrayList<Integer> falseList = new ArrayList<Integer>();
				falseList.add(nextquad+1);
				left.setTrueList(trueList);
				left.setFalseList(falseList);
				this.addrList.add("if "+ E1.getAddr()+" "+ relop.getValue()+" "+ E2.getAddr()+" goto ");
				Element e1 = new Element("j"+ relop.getValue(), E1.getAddr(), E2.getAddr(),"");
				this.eleList.add(e1);
				this.addrList.add("goto ");
				Element e = new Element("j","","","");
				this.eleList.add(e);
				return left;
			}
			case 37:			//B->E > E
			{
				MyCharacter E1 = chStackOfRight.pop();
				MyCharacter relop= chStackOfRight.pop();
				MyCharacter E2 = chStackOfRight.pop();
				int nextquad = addrList.size();
				ArrayList<Integer> trueList = new ArrayList<Integer>();
				trueList.add(nextquad);
				ArrayList<Integer> falseList = new ArrayList<Integer>();
				falseList.add(nextquad+1);
				left.setTrueList(trueList);
				left.setFalseList(falseList);
				this.addrList.add("if"+ E1.getAddr()+ relop.getValue()+ E2.getAddr()+"goto ");
				Element e1 = new Element("j"+ relop.getValue(), E1.getAddr(), E2.getAddr(),"");
				this.eleList.add(e1);
				this.addrList.add("goto ");
				Element e = new Element("j","","","");
				this.eleList.add(e);
				return left;
			}
			case 38:			//B->E >= E
			{
				MyCharacter E1 = chStackOfRight.pop();
				MyCharacter relop= chStackOfRight.pop();
				MyCharacter E2 = chStackOfRight.pop();
				int nextquad = addrList.size();
				ArrayList<Integer> trueList = new ArrayList<Integer>();
				trueList.add(nextquad);
				ArrayList<Integer> falseList = new ArrayList<Integer>();
				falseList.add(nextquad+1);
				left.setTrueList(trueList);
				left.setFalseList(falseList);
				this.addrList.add("if "+ E1.getAddr()+" "+ relop.getValue()+" "+ E2.getAddr()+" goto ");
				Element e1 = new Element("j"+ relop.getValue(), E1.getAddr(), E2.getAddr(),"");
				this.eleList.add(e1);
				this.addrList.add("goto ");
				Element e = new Element("j","","","");
				this.eleList.add(e);
				return left;
			}
			case 39:				//S->id ( Elist )
			{
				MyCharacter id = chStackOfRight.pop();
				chStackOfRight.pop();
				MyCharacter Elist= chStackOfRight.pop();	
				for(String param:Elist.getParams())
				{	
					this.addrList.add("param "+ param);
					Element e = new Element("param",param,"","");
					this.eleList.add(e);
				}
				this.addrList.add("call "+ id.getAddr()+","+Elist.getParams().size());
				Element e = new Element("call",id.getAddr(),Elist.getParams().size()+"","");
				this.eleList.add(e);
				//gen(��call�� id.addr ��,�� n);
				return left;
			}
			case 40:				//Elist->Elist , E
			{
				MyCharacter Elist = chStackOfRight.pop();
				chStackOfRight.pop();
				MyCharacter E = chStackOfRight.pop();
				left.getParams().addAll(Elist.getParams());
				left.getParams().add(E.getAddr());
				return left;
			}
			case 41:				//Elist->E
			{
				MyCharacter E = chStackOfRight.pop();
				left.getParams().add(E.getAddr());
				return left;
			}
			case 42:				//N->��  
			{
				int quad = addrList.size();
				 left.getNextList().add(quad+1);                  
				 this.addrList.add("goto ");
				 Element e = new Element("j","","","");
				this.eleList.add(e);
				return left;
			}			
		}
		return left;
	}

	public ArrayList<Element> getEleList() {
		return eleList;
	}

	public void setEleList(ArrayList<Element> eleList) {
		this.eleList = eleList;
	}

	private Type getTypeInRecord(MyCharacter id) {
		for(int i=0;i<this.recordTable.size();i++)
		{
			Record record = recordTable.get(i);
			//System.out.println(record.getId()+" "+record.getType()+" "+i);
			if(record.getId().equals(id.getActualValue()))
			{
				return record.getType();
			}
		}
		return null;
	}

	private void backpatch(ArrayList<Integer> nextList, int quad) {
		quad++;
		for(int i=0;i<nextList.size();i++)
		{
			int temp = nextList.get(i);
			String[] str =this.addrList.get(temp).split(" ");
			String newStr ="";
			for(int j=0;j<str.length;j++)
			{
				newStr+=" "+str[j];
			}
			newStr+=" "+quad;
			this.addrList.set(temp,newStr);
			this.eleList.get(temp).setAddr3(quad+"");
		}
		
	}

	private boolean inRecord(String actualValue) {
		for(int i=0;i<this.recordTable.size();i++)
		{
			Record record = recordTable.get(i);
			//System.out.println(record.getId()+" "+record.getType()+" "+i);
			if(record.getId().equals(actualValue))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * ��ȡtoken��Ӧ���ķ�����
	 * 
	 * @param token
	 * @return
	 */
	private MyCharacter getMyCharacter(Token token) {
		MyCharacter mch;
		if (token.getTypeString().equals("��ʶ��")) {
			mch = new TerminalCharacter("id");
			mch.setActualValue(token.getValue());
			return mch;
		} else if (token.getTypeString().equals("�ؼ���")) {
			mch = new TerminalCharacter(token.getValue());
		} else if (token.getTypeString().equals("����")) {
			mch = new TerminalCharacter("num");
			mch.setActualValue(token.getValue());
		} else {
			mch = new TerminalCharacter(token.getValue());
		}

		return mch;
	}

	/**
	 * ��ȡ�ķ����Ŷ�Ӧ����
	 * 
	 * @param last
	 * @return
	 */
	private int getIndexOfCharacter(MyCharacter mach1) {
		for (int i = 0; i < characters.size(); i++) {
			MyCharacter mch = characters.get(i);
			if (mch.equals(mach1)) {
				return i;
			}
		}
		return -1;
	}

	public static void main(String[] args) {
		GrammaticalAnalysis ga = new GrammaticalAnalysis();
		ga.readProduction();
		ga.getItemLists();
		//
		//ArrayList<Item> itemList38 = ga.itemLists.get(38);
		//ga.getClosure(itemList38);
	}
}
