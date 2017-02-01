import java.util.*;
import java.io.*;

// Author: Petter

public class rconMaker{

	public static final String rconFileName = "rcon";

	public static void main(String[] args){
		try{
			Scanner input = new Scanner(new File(args[0]));
			Kattio io = new Kattio(System.in, new FileOutputStream(new File(rconFileName+args[0])));
			io.println("// \t\t\t\t\tMade with rconMaker 4.0. \tAuthor: Petter");
			printAscii(io);
			while(input.hasNextLine()){
				String s = input.nextLine();
				if(s.matches("")){
					io.println();
				}
				else if(s.substring(0,2).equals("//") || s.substring(0,4).equals("echo")){
					io.println(s);
				}
				else{
					io.print("rcon ");
					io.println(s);	
				}		
			}
			io.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void printAscii(Kattio io){
		io.println("//                           :                      :M");
		io.println("//                          XMX                   .HMM>");
		io.println("//                          MMMM.                dMMMM>");
		io.println("//                         'MMMMMX     .....   dMMMMMMX");
		io.println("//                         XMMMMMMMnMMMMMMMMMMMMMMMMMMM");
		io.println("//                        :MMMMMMMMMMMMMMMMMMMMMMMMMMMM>");
		io.println("//                        XMMMMM!\"    \"MMMMMM\"`  `\"MMMMM");
		io.println("//                        MMMM#         4MMf        `MMMX");
		io.println("//                       XMMM            MX          'MMM:");
		io.println("//                      'MMM~            '>            MMM");
		io.println("//                      MMMf       .     '>            `MMX");
		io.println("//                     MMMM>     :MMM    '>   :MMM      MMMX");
		io.println("//                    XMMMM      MMMM>   '>   XMMMX     MMMMk");
		io.println("//                   MMMMMM>     MMMM~   'k   MMMMX     MMMMMh");
		io.println("//                  MMMMMMMX     XMMM    XX   ?MMM     XMMMMMMM");
		io.println("//                  MMMMMMMMk     ^`    X 'h    `     :MM##MMM~");
		io.println("//                   ?MM>  ^?M.       .!    %.      .HM\"   MM");
		io.println("//                  .?M      '\"%+++!\".nMMMMn \"%++!*\" %.. 'M..");
		io.println("//                   `?M>+%L         <MMMMMMMM>       :   XM\"");
		io.println("//                     'X   %        XMMMMMMMM>      X   'f");
		io.println("//                       X   `M.      ?MMMMMM~    .HM   :`");
		io.println("//                        %.  `MMMx.          .xHMMM   X");
		io.println("//                   ..    `X  `MMMMMMMMMMMMMMMMMMM  :f");
		io.println("//                 :MMMMMMMh:.M. 4MM     \"     MM\" xMMMMMMMMMMh.");
		io.println("//               :MMMMMMMMMMMMMMM: `%x.......x\"`.HMMMMMMMMMMMMMM");
		io.println("//             .MMMMMMMMMMMMMMMMMMMMhx.......xHMMMMMMMMMMMMMMMMM");
		io.println("//     .nHMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM`MMMMMMMMMMMMMMMMX");
		io.println("//   :MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMdMMMMMMMMMMMMMMMMMMMM");
		io.println("//  MMMMMMMMMMMMMMMMMMMM\"``\"\"MMMMMMMMMMMM!MMMMMMMMMMMMMMMMMMMM~");
		io.println("// MMMMMMMMMMMMMMMMMMM!     XMMMMMMMMMMMf:HMMMMMMMMMMMMMMMMM!");
		io.println("// M?MMMMMMMMMMMMMMMM`    :MMMMMMMMMMMM!MMMMMMMMMMMMMMMMMMM~");
		io.println("// :MMMMMMMMMMMMMMMMX     MMMMMMMMMMMMXXMMMMMMMMMMMMMMMMM`");
		io.println("// MMMMMMMMMMMMMMMMMX    'MMMMMMMMMMMMM!MMMMMMMMMMMMMMMMX");
		io.println("// MMMMMMMMMMMMMMMMM~    'MMMMMMMMMMMMMM?MMMMMMMMMMMMMMM~");
		io.println("//  #M)MMMMMMMM!MMM       MMMMMMMMMMMMMMMM/MMMMMMMMMMMM~");
		io.println("//    ?MMMMMM\"-\"2MMMMMx   XMMMMMMMMMMMMMMMMX?**!:MMM\"`");
		io.println("//      ^\"\"    XMMMMMMMM  'MMMMMMMMMMM\"`MMMMMMMMMMM>");
		io.println("//            XMMMMMMMMML  MMMMMMMMMX .MMMMMMMMMMMf");
		io.println("//            XMMMMMMMMMML 4MMMMMMMMMXMMMMMMMMMMM~");
		io.println("//           XMMMMMMMMMMMMXMMMMMMMMMMXMMMMMMMMMx.");
		io.println("//             MMMMMMMMMMMMM!MMMMMMMMMMLMMMMMMMMMMMMx");
		io.println("//              #MMMMMMMMMMMMMMMMMMMMMMM!MMMMMMMMMMMMM");
		io.println("//               `MMMMMMMMMMMLMMMMMMMMMMM/MMMMMMMMMMMM>");
		io.println("//                  `*MMMMM!nMMMMMHh(?*MMM?MMMMMMMMMMM>");
		io.println("//                        XMMMMMMMMMMMMMMMX4MMMMMMMMMM");
		io.println("//                       XM#     `*MMMMMMMMXMMMMMMMM\"");
		io.println("//                      'M          `MMMMMMMX^\"*\"\"");
		io.println("//                      Xf            !?MMMMMM");
		io.println("//                      'X             X ?MMMMM");
		io.println("//                       !             `> `MMMMX");
		io.println("//                        #:     4     X>  'MMMM>");
		io.println("//                         `L     \"x.xM~    `MMMX");
		io.println("//                           %.    f         MMMX");
		io.println("//                            `#M``         :MMM~");
		io.println("//                               `Mx.      dMMM~");
		io.println("//                                  ``\"**MM*\"");


	}
}