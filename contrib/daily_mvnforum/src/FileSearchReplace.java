import java.io.*;

public class FileSearchReplace {

public static void main(String[] args) throws IOException {
       int argc = 0;

       if (args.length == 0) {
               System.out.println("Utility to update file contents using simple search/replace strings");
               System.out.println("Usage: FileSearchReplace <filename> {<searchstr> <replacestr>}");
               System.exit(0);
       }

       String filename = args[argc++];
       // System.out.println("["+filename+"]");
       String fileContents = Utility.loadFile(filename);

       while (argc < args.length) {
               String searchStr = args[argc++];
               String replaceStr = args[argc++];
               // System.out.println("["+searchStr+"],"+"["+replaceStr+"]");

               fileContents = Utility.replace(fileContents, searchStr, replaceStr);
       }

       // System.out.println(fileContents);
       Utility.saveFile(filename, fileContents);
}

}
