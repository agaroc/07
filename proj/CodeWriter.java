package proj;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class CodeWriter 
{
    private FileWriter output;
    private int jumpN = 0;

    public CodeWriter(File file) throws IOException
    {
        try 
        {
            output = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFileName(String fileName)
    {

    }

    public void writeArithmetic(String command)
    {
        String assemCommand = null;
        if(command.equals("add"))
        {
            assemCommand = new StringBuilder().append("@SP\n").append("AM=M-1\n").append("D=M\n").append("A=A-1\n").append("M=M+D\n").toString();
        }
        else if(command.equals("sub"))
        {
            assemCommand = new StringBuilder().append("@SP\n").append("AM=M-1\n").append("D=M\n").append("A=A-1\n").append("M=M-D\n").toString();
        }
        else if(command.equals("neg"))
        {
            assemCommand = new StringBuilder().append("D=0\n").append("@SP\n").append("A=M-1\n").append("M=D-M\n").toString();

        }
        else if(command.equals("eq"))
        {
            assemCommand = new StringBuilder().append("@SP\n").append("AM=M-1\n").append("D=M\n").append("A=A-1\n").append(arithFormula("JNE")).toString();
            jumpN++;
        }
        else if(command.equals("gt"))
        {
            assemCommand = new StringBuilder().append("@SP\n").append("AM=M-1\n").append("D=M\n").append("A=A-1\n").append(arithFormula("JLE")).toString();
            jumpN++;
        }
        else if(command.equals("lt"))
        {
            assemCommand = new StringBuilder().append("@SP\n").append("AM=M-1\n").append("D=M\n").append("A=A-1\n").append(arithFormula("JGE")).toString();
            jumpN++;
        }
        else if(command.equals("and"))
        {
            assemCommand = new StringBuilder().append("@SP\n").append("AM=M-1\n").append("D=M\n").append("A=A-1\n").append("M=M&D\n").toString();
        }
        else if(command.equals("or"))
        {
            assemCommand = new StringBuilder().append("@SP\n").append("AM=M-1\n").append("D=M\n").append("A=A-1\n").append("M=M|D\n").toString();
        }
        else if(command.equals("not"))
        {
            assemCommand = new StringBuilder().append("@SP\n").append("AM=M-1\n").append("D=M\n").append("A=A-1\n").append("M=M!D\n").toString();
        }
        try
        {
            output.write(assemCommand);
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public String arithFormula(String str)
    {
        return new StringBuilder().append("D=M-D\n").append("@FALSE").append(jumpN).append("\n").append("D;").append(str).append("\n@SP\n").append("A=M-1\n").append("M=-1\n").append("@CONTINUE").append(jumpN).append("\n0;JMP\n").append("(FALSE").append(jumpN).append(")\n").append("@SP\n").append("A=M-1\n").append("M=0\n").append("(CONTINUE").append(jumpN).append(")\n").toString();
    }

    public void WritePushPop(CommandType c, String segment, int index)
    {
        String assemCommmand = null;
        if(c.equals(CommandType.C_PUSH))
        {
            if(segment.equals("static"))
            {
                assemCommmand = pushFormula2(String.valueOf(16+index));
            }
            else if(segment.equals("this"))
            {
                assemCommmand = pushFormula1("THIS", index);
            }
            else if(segment.equals("local"))
            {
                assemCommmand = pushFormula1("LCL", index);
            }
            else if(segment.equals("argument"))
            {
                assemCommmand = pushFormula1("ARG", index);
            }
            else if(segment.equals("that"))
            {
                assemCommmand = pushFormula1("THAT", index);
            }
            else if(segment.equals("constant"))
            {
                assemCommmand = "@" + index + "\n" + "D=A\n" + "@SP\n" + "A=M\n" + "M=D\n" + "@SP\n" + "M=M+1\n";
            }
            else if(segment.equals("pointer") && index == 0)
            {
                assemCommmand = pushFormula2("THIS");
            }
            else if(segment.equals("pointer") && index == 1)
            {
                assemCommmand = pushFormula2("THAT");
            }
            else if(segment.equals("temp"))
            {
                assemCommmand = pushFormula1("R5", index+5);
            }
        }
        else if(c.equals(CommandType.C_POP))
        {
             if(segment.equals("static"))
            {
                assemCommmand = popFormula2(String.valueOf(16+index));
            }
            else if(segment.equals("this"))
            {
                assemCommmand = popFormula1("THIS", index);
            }
            else if(segment.equals("local"))
            {
                assemCommmand = popFormula1("LCL", index);
            }
            else if(segment.equals("argument"))
            {
                assemCommmand = popFormula1("ARG", index);
            }
            else if(segment.equals("that"))
            {
                assemCommmand = popFormula1("THAT", index);
            }
            else if(segment.equals("constant"))
            {
                assemCommmand = new StringBuilder().append("@").append(index).append("\n").append("D=A\n").append("@SP\n").append("A=M\n").append("M=D\n").append("@SP\n").append("M=M+1\n").toString();
            }
            else if(segment.equals("pointer") && index == 0)
            {
                assemCommmand = popFormula2("THIS");
            }
            else if(segment.equals("pointer") && index == 1)
            {
                assemCommmand = popFormula2("THAT");
            }
            else if(segment.equals("temp"))
            {
                assemCommmand = popFormula1("R5", index+5);
            }
        }
        try
        {
            output.write(assemCommmand);
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public String popFormula1(String segment, int index)
    {
        String assemCommand = new StringBuilder().append("@").append(segment).append("\nD=M\n@").append(index).append("\n").append("D=D+A\n").append("@R13\n").append("M=D\n").append("@SP\n").append("AM=M-1\n").append("D=M\n").append("@R13\n").append("A=M\n").append("M=D\n").toString();
        return assemCommand;
    }

    public String popFormula2(String segment)
    {
        String assemCommand = new StringBuilder().append("@").append(segment).append("\nD=A\n").append("@R13\n").append("M=D\n").append("@SP\n").append("AM=M-1\n").append("D=M\n").append("@R13\n").append("A=M\n").append("M=D\n").toString();
        return assemCommand;
    }

    public String pushFormula1(String segment, int index)
    {
        String assemCommand = new StringBuilder().append("@").append(segment).append("\nD=M\n@").append(index).append("\n").append("A=D+A\n").append("D=M\n").append("@SP\n").append("A=M\n").append("M=D\n").append("@SP\n").append("M=M+1\n").toString();
        return assemCommand;
    }

    public String pushFormula2(String segment)
    {
        String assemCommand = new StringBuilder().append("@").append(segment).append("\nD=M\n").append("@SP\n").append("A=M\n").append("M=D\n").append("@SP\n").append("M=M+1\n").toString();
        return assemCommand;
    }

    public void close() throws IOException
    {
        try
        {
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
