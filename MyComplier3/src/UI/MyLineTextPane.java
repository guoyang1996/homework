package UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;



public class MyLineTextPane extends JTextPane{

	protected StyledDocument doc;
	// 定义该文档的普通文本的外观属性
	private SimpleAttributeSet normalAttr = new SimpleAttributeSet();
	public MyLineTextPane() {
		this.doc = super.getStyledDocument();
		// 设置该文档的页边距
		this.setMargin(new Insets(3, 40, 0, 0));
	}

	// 重画该组件，设置行号
	public void paint(Graphics g) {
		super.paint(g);
		Element root = doc.getDefaultRootElement();
		// 获得行号
		int line = root.getElementIndex(doc.getLength());
		// 设置颜色
		g.setColor(new Color(230, 230, 230));
		// 绘制显示行数的矩形框
		g.fillRect(0, 0, this.getMargin().left - 10, getSize().height);
		// 设置行号的颜色
		g.setColor(new Color(40, 40, 40));
		// 每行绘制一个行号
		for (int count = 0, j = 1; count <= line; count++, j++) {
			g.drawString(String.valueOf(j), 3,
					(int) ((count + 1) * 1.535 * StyleConstants
							.getFontSize(normalAttr)));
		}
	}
	public static void main(String[] args)
	{
		JFrame frame = new JFrame("文本编辑器");
		// 使用MyTextPane
		frame.getContentPane().add(new JScrollPane(new MyLineTextPane()));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final int inset = 50;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setBounds(inset, inset, screenSize.width - inset*2
			, screenSize.height - inset * 2);
		frame.setVisible(true);
	}
}
