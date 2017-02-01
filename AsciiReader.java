import java.util.*;
import java.io.*;

public class AsciiReader{
	public static void main(String[] args){
		try{
			Scanner input = new Scanner(new File(args[0]));
			Kattio io = new Kattio(System.in, new FileOutputStream(new File("java"+args[0])));
			while(input.hasNextLine()){
				String s = input.nextLine();
				s.replaceAll("\"", "\\\"");
				io.println("io.println(\"// " + s + "\");");
			}
			io.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}	

	}
}