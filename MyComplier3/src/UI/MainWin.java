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
	private JTable resultTable;//分析结果集
	private JTable errorTable;//错误结果集
	private JTable dfaTable;//DFA转换表
	private String text = new String("");
	private List<Token> tokens = new ArrayList<Token>();
	private List<Error> errors = new ArrayList<Error>();
	/**
	 * 三地址指令序列
	 */
	private ArrayList<String> addrList = new ArrayList<String>();
	/**
	 * 四元组序列
	 */
	private ArrayList<Element> eleList = new ArrayList<Element>();
	/**
	 * 符号表
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
		
		frame = new JFrame("编译原理实验");
		
		
		
		
		FileDialog d1 = new FileDialog(frame, "选择需要打开文件" , FileDialog.LOAD);
		FileDialog d2 = new FileDialog(frame, "选择保存文件的路径" , FileDialog.SAVE);
		
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu fileMenu = new JMenu("文件");
		menuBar.add(fileMenu);
		
		JMenu opMenu = new JMenu("操作");
		menuBar.add(opMenu);
		
		JMenuItem meaningItem = new JMenuItem("语义");
		opMenu.add(meaningItem);
		meaningItem.addActionListener(e ->
		{
			//语义分析测试
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
		
		JMenuItem grammaticalItem = new JMenuItem("语法分析");
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
		
		JMenuItem lexicalItem = new JMenuItem("词法分析");
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
		
		JMenuItem newItem = new JMenuItem("新建");
		fileMenu.add(newItem);
		
		JMenuItem openItem = new JMenuItem("打开");
		fileMenu.add(openItem);
		openItem.addActionListener(e ->
		{
			d1.setVisible(true);
			// 打印出用户选择的文件路径和文件名
			System.out.println(d1.getDirectory()
				+ d1.getFile());
			try {
				getText(d1.getDirectory()+ d1.getFile());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		JMenuItem saveItem = new JMenuItem("保存");
		fileMenu.add(saveItem);
		saveItem.addActionListener(e ->
		{
			d2.setVisible(true);
			// 打印出用户选择的文件路径和文件名
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
		// 创建字节输入流
		FileInputStream fis = new FileInputStream(dir);
		// 创建一个长度为1024的“竹筒”
		byte[] bbuf = new byte[1024];
		// 用于保存实际读取的字节数
		int hasRead = 0;
		// 使用循环来重复“取水”过程
		while ((hasRead = fis.read(bbuf)) > 0 )
		{
			String str = new String(bbuf , 0 , hasRead );
			// 取出“竹筒”中水滴（字节），将字节数组转换成字符串输入！
			//System.out.print(str);
			text = text.concat(str);
		}
		// 关闭文件输入流，放在finally块里更安全
		fis.close();
		//System.out.print(text);
		this.editPane.setText(text);
		return text;
	}

}
//扩展AbstractTableModel，用于将一个tokens包装成TableModel
class TokenTableModel extends AbstractTableModel   // ①
{
	private List<Token> tokens;
	// 构造器，初始化rs和rsmd两个属性
	public TokenTableModel(List<Token> tokens)
	{
		this.tokens = tokens;
		
	}
	// 重写getColumnName方法，用于为该TableModel设置列名
	public String getColumnName(int c)
	{
		switch(c)
		{
		case 0:
			return "值";
		case 1:
			return "种别码";
		case 2:
			return "种别";
		case 3:
			return "行";
		}
		
		return "";
	}
	// 重写getColumnCount方法，用于设置该TableModel的列数
	public int getColumnCount()
	{
		
			return 4;
		
	}
	// 重写getValueAt方法，用于设置该TableModel指定单元格的值
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
	// 重写getColumnCount方法，用于设置该TableModel的行数
	public int getRowCount()
	{
		
			return tokens.size();
		
	}
	// 重写isCellEditable返回true，让每个单元格可编辑
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return true;
	}
	// 重写setValueAt()方法，当用户编辑单元格时，将会触发该方法
	public void setValueAt(Object aValue , int row,int column)
	{
		
	}
}
//扩展AbstractTableModel，用于将一个tokens包装成TableModel
class ErrorTableModel extends AbstractTableModel   // ①
{
	private List<Error> errors;
	// 构造器，初始化rs和rsmd两个属性
	public ErrorTableModel(List<Error> errors)
	{
		this.errors = errors;
		
	}
	// 重写getColumnName方法，用于为该TableModel设置列名
	public String getColumnName(int c)
	{
		switch(c)
		{
		case 0:
			return "错误信息";
		case 1:
			return "行号";
		}
		
		return "";
	}
	// 重写getColumnCount方法，用于设置该TableModel的列数
	public int getColumnCount()
	{
		
			return 2;
		
	}
	// 重写getValueAt方法，用于设置该TableModel指定单元格的值
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
	// 重写getColumnCount方法，用于设置该TableModel的行数
	public int getRowCount()
	{
		
			return errors.size();
		
	}
	// 重写isCellEditable返回true，让每个单元格可编辑
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return true;
	}
	// 重写setValueAt()方法，当用户编辑单元格时，将会触发该方法
	public void setValueAt(Object aValue , int row,int column)
	{
		
	}
}

//扩展AbstractTableModel，用于将一个tokens包装成TableModel
class ActionInfoModel extends AbstractTableModel   // ①
{
	private List<String> infos;
	// 构造器，初始化rs和rsmd两个属性
	public ActionInfoModel(List<String> infos)
	{
		this.infos = infos;
		
	}
	// 重写getColumnName方法，用于为该TableModel设置列名
	public String getColumnName(int c)
	{
		switch(c)
		{
		case 0:
			return "语法分析结果";
		}
		
		return "";
	}
	// 重写getColumnCount方法，用于设置该TableModel的列数
	public int getColumnCount()
	{
		
			return 1;
		
	}
	// 重写getValueAt方法，用于设置该TableModel指定单元格的值
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
	// 重写getColumnCount方法，用于设置该TableModel的行数
	public int getRowCount()
	{
		
			return infos.size();
		
	}
	// 重写isCellEditable返回true，让每个单元格可编辑
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return true;
	}
	// 重写setValueAt()方法，当用户编辑单元格时，将会触发该方法
	public void setValueAt(Object aValue , int row,int column)
	{
		
	}
}
//扩展AbstractTableModel，用于将一个tokens包装成TableModel
class AddrModel extends AbstractTableModel   // ①
{
	private List<String> addrs;
	private  ArrayList<Element> eleList;
	// 构造器，初始化rs和rsmd两个属性
	public AddrModel(List<String> addrs, ArrayList<Element> eleList)
	{
		this.addrs = addrs;
		this.eleList=eleList;
	}
	// 重写getColumnName方法，用于为该TableModel设置列名
	public String getColumnName(int c)
	{
		switch(c)
		{
		case 0:
			return "标号";
		case 1:
			return "三地址指令序列";
		case 2:
			return "四元组序列";
		}	
		return "";
	}
	// 重写getColumnCount方法，用于设置该TableModel的列数
	public int getColumnCount()
	{
		
			return 3;
		
	}
	// 重写getValueAt方法，用于设置该TableModel指定单元格的值
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
	// 重写getColumnCount方法，用于设置该TableModel的行数
	public int getRowCount()
	{
		
			return addrs.size();
		
	}
	// 重写isCellEditable返回true，让每个单元格可编辑
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return true;
	}
	// 重写setValueAt()方法，当用户编辑单元格时，将会触发该方法
	public void setValueAt(Object aValue , int row,int column)
	{
		
	}
}
//扩展AbstractTableModel，用于将一个tokens包装成TableModel
class RecordModel extends AbstractTableModel   // ①
{
	private ArrayList<Record> records = new ArrayList<Record>();
	// 构造器，初始化rs和rsmd两个属性
	public RecordModel(ArrayList<Record> records)
	{
		this.records = records;
		
	}
	// 重写getColumnName方法，用于为该TableModel设置列名
	public String getColumnName(int c)
	{
		switch(c)
		{
		case 0:
			return "id";
		case 1:
			return "类型";
		case 2:
			return "偏移量";
		}	
		return "";
	}
	// 重写getColumnCount方法，用于设置该TableModel的列数
	public int getColumnCount()
	{
		
			return 3;
		
	}
	// 重写getValueAt方法，用于设置该TableModel指定单元格的值
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
	// 重写getColumnCount方法，用于设置该TableModel的行数
	public int getRowCount()
	{
		
			return records.size();
		
	}
	// 重写isCellEditable返回true，让每个单元格可编辑
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return true;
	}
	// 重写setValueAt()方法，当用户编辑单元格时，将会触发该方法
	public void setValueAt(Object aValue , int row,int column)
	{
		
	}
}

