package UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.DefaultTreeModel;

import tool.Element;
import tool.GrammaticalAnalysis;
import tool.LexicalAnalysis;
import tool.Record;
import tool.Token;
import tool.LexicalAnalysis.Error;

public class MainWin {

	private JFrame frame;
	private MyLineTextPane editPane = new MyLineTextPane();
	private JTable resultTable;//���������
	private JTable errorTable;//��������
	private JTable dfaTable;//DFAת����
	private String text = new String("");
	private List<Token> tokens = new ArrayList<Token>();
	private List<Error> errors = new ArrayList<Error>();
	/**
	 * ����ַָ������
	 */
	private ArrayList<String> addrList = new ArrayList<String>();
	/**
	 * ��Ԫ������
	 */
	private ArrayList<Element> eleList = new ArrayList<Element>();
	/**
	 * ���ű�
	 */
	private ArrayList<Record> recordTable = new ArrayList<Record>();
	private JTree tree = new JTree();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWin window = new MainWin();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWin() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame("����ԭ��ʵ��");
		
		
		
		
		FileDialog d1 = new FileDialog(frame, "ѡ����Ҫ���ļ�" , FileDialog.LOAD);
		FileDialog d2 = new FileDialog(frame, "ѡ�񱣴��ļ���·��" , FileDialog.SAVE);
		
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu fileMenu = new JMenu("�ļ�");
		menuBar.add(fileMenu);
		
		JMenu opMenu = new JMenu("����");
		menuBar.add(opMenu);
		
		JMenuItem meaningItem = new JMenuItem("����");
		opMenu.add(meaningItem);
		meaningItem.addActionListener(e ->
		{
			//�����������
			/*for(int l=0;l<addrList.size();l++)
			{
				System.out.println(addrList.get(l));
			}
			for(int n=0;n<recordTable.size();n++)
			{
				Record record = recordTable.get(n);
				System.out.println(record.getId()+" "+record.getType()+" "+record.getOffset());
			}*/
			resultTable.setModel(new RecordModel(this.recordTable));
			errorTable.setModel(new AddrModel(this.addrList,this.eleList));
		});
		
		JMenuItem grammaticalItem = new JMenuItem("�﷨����");
		opMenu.add(grammaticalItem);
		grammaticalItem.addActionListener(e ->
		{
			LexicalAnalysis la=new LexicalAnalysis();
			tokens = la.getTokens(this.text);
			errors=la.getErrors();
			GrammaticalAnalysis ga = new GrammaticalAnalysis();
			TreeNode root=ga.getParseTree(tokens);
			this.recordTable=ga.getRecordTable();
			this.addrList=ga.getAddrList();
			this.eleList=ga.getEleList();
			tree.setModel(new DefaultTreeModel(root));
			resultTable.setModel(new ActionInfoModel(ga.getActionInfo()));
		});
		
		JMenuItem lexicalItem = new JMenuItem("�ʷ�����");
		opMenu.add(lexicalItem);
		lexicalItem.addActionListener(e ->
		{
			LexicalAnalysis la=new LexicalAnalysis();
			tokens = la.getTokens(this.text);
			errors=la.getErrors();
			/*for(int i=0;i<tokens.size();i++)
			{
				Token token = tokens.get(i);
				System.out.println(token.getTypeString()+" "+token.getType()+" "+token.getValue()+" "+token.getLineNumber());
			}*/
			resultTable.setModel(new TokenTableModel(tokens));
			errorTable.setModel(new ErrorTableModel(errors));
		});
		
		JMenuItem newItem = new JMenuItem("�½�");
		fileMenu.add(newItem);
		
		JMenuItem openItem = new JMenuItem("��");
		fileMenu.add(openItem);
		openItem.addActionListener(e ->
		{
			d1.setVisible(true);
			// ��ӡ���û�ѡ����ļ�·�����ļ���
			System.out.println(d1.getDirectory()
				+ d1.getFile());
			try {
				getText(d1.getDirectory()+ d1.getFile());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		JMenuItem saveItem = new JMenuItem("����");
		fileMenu.add(saveItem);
		saveItem.addActionListener(e ->
		{
			d2.setVisible(true);
			// ��ӡ���û�ѡ����ļ�·�����ļ���
			//System.out.println(d2.getDirectory()+ d1.getFile());
		});
		
		
		
		//frame.add(new JScrollPane(editPane),BorderLayout.CENTER);
		//frame.add(new JScrollPane(tree),BorderLayout.EAST);
		
		
		resultTable = new JTable();
		errorTable = new JTable();
		dfaTable = new JTable();
		Box x = new Box(BoxLayout.Y_AXIS);
		Box y1 = new Box(BoxLayout.X_AXIS);
		Box y2 = new Box(BoxLayout.X_AXIS);
		x.add(y1);
		x.add(y2);
		y1.add(new JScrollPane(editPane));
		y1.add(new JScrollPane(tree));
		y2.add(new JScrollPane(resultTable));
		y2.add(new JScrollPane(errorTable));
		//y2.add(new JScrollPane(dfaTable));
		
		frame.add(x);
		
		final int inset = 50;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setBounds(inset, inset, screenSize.width - inset*2
			, screenSize.height - inset * 2);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	String getText(String dir) throws IOException
	{
		// �����ֽ�������
		FileInputStream fis = new FileInputStream(dir);
		// ����һ������Ϊ1024�ġ���Ͳ��
		byte[] bbuf = new byte[1024];
		// ���ڱ���ʵ�ʶ�ȡ���ֽ���
		int hasRead = 0;
		// ʹ��ѭ�����ظ���ȡˮ������
		while ((hasRead = fis.read(bbuf)) > 0 )
		{
			String str = new String(bbuf , 0 , hasRead );
			// ȡ������Ͳ����ˮ�Σ��ֽڣ������ֽ�����ת�����ַ������룡
			//System.out.print(str);
			text = text.concat(str);
		}
		// �ر��ļ�������������finally�������ȫ
		fis.close();
		//System.out.print(text);
		this.editPane.setText(text);
		return text;
	}

}
//��չAbstractTableModel�����ڽ�һ��tokens��װ��TableModel
class TokenTableModel extends AbstractTableModel   // ��
{
	private List<Token> tokens;
	// ����������ʼ��rs��rsmd��������
	public TokenTableModel(List<Token> tokens)
	{
		this.tokens = tokens;
		
	}
	// ��дgetColumnName����������Ϊ��TableModel��������
	public String getColumnName(int c)
	{
		switch(c)
		{
		case 0:
			return "ֵ";
		case 1:
			return "�ֱ���";
		case 2:
			return "�ֱ�";
		case 3:
			return "��";
		}
		
		return "";
	}
	// ��дgetColumnCount�������������ø�TableModel������
	public int getColumnCount()
	{
		
			return 4;
		
	}
	// ��дgetValueAt�������������ø�TableModelָ����Ԫ���ֵ
	public Object getValueAt(int r, int c)
	{
		int length = tokens.size();
		Object[][] resultData = new Object[length][];
		for(int i = 0;i<length;i++)
		{
			String[] a = new String[4];
			Token token = tokens.get(i);
			a[0] = token.getValue();
			a[1] = token.getType()+"";
			a[2] = token.getTypeString();
			a[3] = token.getLineNumber()+"";
			resultData[i] = a;
		}
			return resultData[r][c];
		
	}
	// ��дgetColumnCount�������������ø�TableModel������
	public int getRowCount()
	{
		
			return tokens.size();
		
	}
	// ��дisCellEditable����true����ÿ����Ԫ��ɱ༭
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return true;
	}
	// ��дsetValueAt()���������û��༭��Ԫ��ʱ�����ᴥ���÷���
	public void setValueAt(Object aValue , int row,int column)
	{
		
	}
}
//��չAbstractTableModel�����ڽ�һ��tokens��װ��TableModel
class ErrorTableModel extends AbstractTableModel   // ��
{
	private List<Error> errors;
	// ����������ʼ��rs��rsmd��������
	public ErrorTableModel(List<Error> errors)
	{
		this.errors = errors;
		
	}
	// ��дgetColumnName����������Ϊ��TableModel��������
	public String getColumnName(int c)
	{
		switch(c)
		{
		case 0:
			return "������Ϣ";
		case 1:
			return "�к�";
		}
		
		return "";
	}
	// ��дgetColumnCount�������������ø�TableModel������
	public int getColumnCount()
	{
		
			return 2;
		
	}
	// ��дgetValueAt�������������ø�TableModelָ����Ԫ���ֵ
	public Object getValueAt(int r, int c)
	{
		int length = errors.size();
		Object[][] errorData = new Object[length][];
		for(int i = 0;i<length;i++)
		{
			String[] a = new String[2];
			Error error = errors.get(i);
			a[0] = error.getMessage();
			a[1] = error.getLineNumber()+"";
			errorData[i] = a;
		}
			return errorData[r][c];
		
	}
	// ��дgetColumnCount�������������ø�TableModel������
	public int getRowCount()
	{
		
			return errors.size();
		
	}
	// ��дisCellEditable����true����ÿ����Ԫ��ɱ༭
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return true;
	}
	// ��дsetValueAt()���������û��༭��Ԫ��ʱ�����ᴥ���÷���
	public void setValueAt(Object aValue , int row,int column)
	{
		
	}
}

//��չAbstractTableModel�����ڽ�һ��tokens��װ��TableModel
class ActionInfoModel extends AbstractTableModel   // ��
{
	private List<String> infos;
	// ����������ʼ��rs��rsmd��������
	public ActionInfoModel(List<String> infos)
	{
		this.infos = infos;
		
	}
	// ��дgetColumnName����������Ϊ��TableModel��������
	public String getColumnName(int c)
	{
		switch(c)
		{
		case 0:
			return "�﷨�������";
		}
		
		return "";
	}
	// ��дgetColumnCount�������������ø�TableModel������
	public int getColumnCount()
	{
		
			return 1;
		
	}
	// ��дgetValueAt�������������ø�TableModelָ����Ԫ���ֵ
	public Object getValueAt(int r, int c)
	{
		int length = infos.size();
		Object[][] infoData = new Object[length][];
		for(int i = 0;i<length;i++)
		{
			String[] a = new String[2];
			String info = infos.get(i);
			a[0] = info;
			infoData[i] = a;
		}
			return infoData[r][c];
		
	}
	// ��дgetColumnCount�������������ø�TableModel������
	public int getRowCount()
	{
		
			return infos.size();
		
	}
	// ��дisCellEditable����true����ÿ����Ԫ��ɱ༭
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return true;
	}
	// ��дsetValueAt()���������û��༭��Ԫ��ʱ�����ᴥ���÷���
	public void setValueAt(Object aValue , int row,int column)
	{
		
	}
}
//��չAbstractTableModel�����ڽ�һ��tokens��װ��TableModel
class AddrModel extends AbstractTableModel   // ��
{
	private List<String> addrs;
	private  ArrayList<Element> eleList;
	// ����������ʼ��rs��rsmd��������
	public AddrModel(List<String> addrs, ArrayList<Element> eleList)
	{
		this.addrs = addrs;
		this.eleList=eleList;
	}
	// ��дgetColumnName����������Ϊ��TableModel��������
	public String getColumnName(int c)
	{
		switch(c)
		{
		case 0:
			return "���";
		case 1:
			return "����ַָ������";
		case 2:
			return "��Ԫ������";
		}	
		return "";
	}
	// ��дgetColumnCount�������������ø�TableModel������
	public int getColumnCount()
	{
		
			return 3;
		
	}
	// ��дgetValueAt�������������ø�TableModelָ����Ԫ���ֵ
	public Object getValueAt(int r, int c)
	{
		int length = addrs.size();
		Object[][] addrData = new Object[length][];
		for(int i = 0;i<length;i++)
		{
			String[] a = new String[3];
			String addr = addrs.get(i);
			Element ele = eleList.get(i);
			a[0] =  i+1+"";
			a[1] = addr;
			a[2]= "("+ele.getOp()+"  ,"+ele.getAddr1()+" ,"+ele.getAddr2()+" ,"+ele.getAddr3()+")";
			addrData[i] = a;
		}
		return addrData[r][c];
		
	}
	// ��дgetColumnCount�������������ø�TableModel������
	public int getRowCount()
	{
		
			return addrs.size();
		
	}
	// ��дisCellEditable����true����ÿ����Ԫ��ɱ༭
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return true;
	}
	// ��дsetValueAt()���������û��༭��Ԫ��ʱ�����ᴥ���÷���
	public void setValueAt(Object aValue , int row,int column)
	{
		
	}
}
//��չAbstractTableModel�����ڽ�һ��tokens��װ��TableModel
class RecordModel extends AbstractTableModel   // ��
{
	private ArrayList<Record> records = new ArrayList<Record>();
	// ����������ʼ��rs��rsmd��������
	public RecordModel(ArrayList<Record> records)
	{
		this.records = records;
		
	}
	// ��дgetColumnName����������Ϊ��TableModel��������
	public String getColumnName(int c)
	{
		switch(c)
		{
		case 0:
			return "id";
		case 1:
			return "����";
		case 2:
			return "ƫ����";
		}	
		return "";
	}
	// ��дgetColumnCount�������������ø�TableModel������
	public int getColumnCount()
	{
		
			return 3;
		
	}
	// ��дgetValueAt�������������ø�TableModelָ����Ԫ���ֵ
	public Object getValueAt(int r, int c)
	{
		int length = records.size();
		Object[][] recordData = new Object[length][];
		for(int i = 0;i<length;i++)
		{
			String[] a = new String[3];
			Record record = records.get(i);
			a[0] = record.getId();
			a[1] = record.getType().toString();
			a[2] = record.getOffset()+"";
			recordData[i] = a;
		}
			return recordData[r][c];
		
	}
	// ��дgetColumnCount�������������ø�TableModel������
	public int getRowCount()
	{
		
			return records.size();
		
	}
	// ��дisCellEditable����true����ÿ����Ԫ��ɱ༭
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return true;
	}
	// ��дsetValueAt()���������û��༭��Ԫ��ʱ�����ᴥ���÷���
	public void setValueAt(Object aValue , int row,int column)
	{
		
	}
}

