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
	// ������ĵ�����ͨ�ı����������
	private SimpleAttributeSet normalAttr = new SimpleAttributeSet();
	public MyLineTextPane() {
		this.doc = super.getStyledDocument();
		// ���ø��ĵ���ҳ�߾�
		this.setMargin(new Insets(3, 40, 0, 0));
	}

	// �ػ�������������к�
	public void paint(Graphics g) {
		super.paint(g);
		Element root = doc.getDefaultRootElement();
		// ����к�
		int line = root.getElementIndex(doc.getLength());
		// ������ɫ
		g.setColor(new Color(230, 230, 230));
		// ������ʾ�����ľ��ο�
		g.fillRect(0, 0, this.getMargin().left - 10, getSize().height);
		// �����кŵ���ɫ
		g.setColor(new Color(40, 40, 40));
		// ÿ�л���һ���к�
		for (int count = 0, j = 1; count <= line; count++, j++) {
			g.drawString(String.valueOf(j), 3,
					(int) ((count + 1) * 1.535 * StyleConstants
							.getFontSize(normalAttr)));
		}
	}
	public static void main(String[] args)
	{
		JFrame frame = new JFrame("�ı��༭��");
		// ʹ��MyTextPane
		frame.getContentPane().add(new JScrollPane(new MyLineTextPane()));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final int inset = 50;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setBounds(inset, inset, screenSize.width - inset*2
			, screenSize.height - inset * 2);
		frame.setVisible(true);
	}
}
