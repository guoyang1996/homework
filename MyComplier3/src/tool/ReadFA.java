package tool;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ReadFA {
	private int[][] FA;
	public int[][] getFA()
	{
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
	public static void main(String[] args)
	{
		new ReadFA().getFA();
	}
	
}
