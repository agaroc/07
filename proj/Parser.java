package proj;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;

public class Parser
{
    private static List<String> artihmeticCommands = Arrays.asList("add", "sub", "neg", "eq", "gt", "lt", "and", "or", "not");
    private BufferedReader buff;
    private String currentLine;
    private String nextLine;

    public Parser(File file) throws IOException 
    {
             if(file == null)
        {
            throw new IOException("invalid file");
        }
        if (!file.exists()) {
			throw new FileNotFoundException(file.getAbsolutePath());
		}
        this.buff = new BufferedReader(new FileReader(file));
        this.currentLine = null;
        this.nextLine = this.getNext();
    }

    private String getNext() throws IOException
    {
        String nextLine;
        do
        {
            nextLine = this.buff.readLine();
            if(nextLine == null)
            {
                return null;
            }
        } while(nextLine.trim().isEmpty() || this.commentFlag(nextLine));

        int commentIndex = nextLine.indexOf("//");
        if(commentIndex != -1)
        {
            nextLine = nextLine.substring(0, commentIndex-1);
        }
        return nextLine;
    }

    private boolean commentFlag(String str)
    {
        return str.trim().startsWith("//");
    }

    public boolean hasMoreCommands()
    {
        return (this.nextLine != null);
    }

    public void advance() throws IOException
    {
        if(hasMoreCommands())
        {
            this.currentLine = this.nextLine;
            this.nextLine = this.getNext();
        }
        else
        {
            System.err.println("No more commands");
        }
    }

    public CommandType commandType()
    {
        String trimLine = this.currentLine.trim();
        String[] line = trimLine.split(" ");
        if(line[0].equals("push"))
        {
            return CommandType.C_PUSH;
        }
        else if(line[0].equals("pop"))
        {
            return CommandType.C_POP;
        }
        else if(artihmeticCommands.contains(line[0]))
        {
            return CommandType.C_ARTHICMETIC;
        }
        else
        {
            return null;
        }
    }

    public String arg1()
    {
        String trimLine = this.currentLine.trim();
        String[] line = trimLine.split(" ");
        if(!this.commandType().equals(CommandType.C_RETURN))
        {
            if(this.commandType().equals(CommandType.C_ARTHICMETIC))
            {
                return line[0];
            }
            else
            {
                return line[1];
            }
        }
        else
        {
            return null;
        }
    }

    public int arg2()
    {
        String trimLine = this.currentLine.trim();
        String[] line = trimLine.split(" ");
        if(this.commandType().equals(CommandType.C_PUSH)|| this.commandType().equals(CommandType.C_POP) || this.commandType().equals(CommandType.C_FUNCTION) || this.commandType().equals(CommandType.C_CALL))
        {
            return Integer.parseInt(line[2]);
        }
        else
        {
            return 0;
        }
    }
    
}