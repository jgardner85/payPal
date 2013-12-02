package payPal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class InputOutput {
	public static void readFile(String fileName,char delim,String[][] arr)
	{
		try
		{
			FileReader reader=new FileReader(fileName);
			BufferedReader br = new BufferedReader(reader); 
			String s,temp; 
			int start,row,col;
			row=0;start=0;
			while((s = br.readLine()) != null) 
			{ 
				//System.out.println(s+" has length "+s.length());
				int i=0;
				while(s.charAt(i)==delim)
				{
					i++;
					start=i;
				}
				col=i;
				while(i<s.length())
				{
					//System.out.println("i is "+i);
					if (s.charAt(i)==delim)
					{
						//System.out.println("ROW:"+row+" COL:"+col+"; i is "+i+"; start is "+start);
						temp=s.substring(start,i);
						//System.out.println("TEMP:"+temp+";");
						arr[row][col]=temp;
						//System.out.println("ROW:"+row+" COL:"+col+" i is "+i);
						col++;
						start=i+1;
					}
					i++;
				}
				temp=s.substring(start,i);
				arr[row][col]=temp;
				//System.out.println("ROW:"+row+" COL:"+col);
				row++;col=0;start=0;
			} 
			reader.close(); 
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println("Something happened while reading "+fileName);
		}
	}
	public static void writeFile(String sFileName,char delim,String[][] arr)
	{
		PrintWriter out = null;
		BufferedWriter bw = null;
		FileWriter fw = null;
		try{
		     fw = new FileWriter(sFileName, true);
		     bw = new BufferedWriter(fw);
		     out = new PrintWriter(bw);
		     String data;
		      //  System.out.println("START");
				for(int row=0;row<arr.length;row++)
				{
					//System.out.println("ROW:"+row);
					data="";
					for(int col=0;col<arr[row].length;col++)
					{
						data+=arr[row][col]+delim;
						//writer.append(arr[row][col]);
						//writer.append(delim);
					}
					//writer.append('\n');
					//System.out.println(data);
					out.println(data);
					//bufferWriter.write(data);
				}
		     
		}
		  catch( IOException e ){
		     // File writing/opening failed at some stage.
		  }
		  finally{
		     try{
		        if( out != null ){
		           out.close(); // Will close bw and fw too
		        }
		        else if( bw != null ){
		           bw.close(); // Will close fw too
		        }
		        else if( fw != null ){
		           fw.close();
		        }
		        else{
		           // Oh boy did it fail hard! :3
		        }
		     }
		     catch( IOException e ){
		        // Closing the file writers failed for some obscure reason
		     }
		  }
		//////////
	}
}
