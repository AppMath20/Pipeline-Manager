package com.laboratory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Manager{

    private final Writer m_Writer;
    private final Reader m_Reader;
    private final Executor m_Executor;

    private FileInputStream m_input;
    private FileOutputStream m_output;
    private int m_buffSize;
    private int m_longSize;

    String[] m_parametersConfig;

    public Manager(String configFile) {

        ParserConfig parserConfig = new ParserConfig(configFile);
        m_parametersConfig = parserConfig.readConfig();


        analyzConfig();

        m_Reader = new Reader(m_input);
        m_Writer = new Writer(m_output);
        m_Executor = new Executor(m_buffSize,m_longSize);

    }

    public void analyzConfig()
    {
        int index = 0;
        try {

            if (m_parametersConfig.length != ParserConfig.parametersNumber) {
                System.out.println("Error: Not enough parameters from configuration file");
                return;
            }

            m_input = new FileInputStream(m_parametersConfig[index]);
            m_output = new FileOutputStream(m_parametersConfig[++index]);
            m_buffSize = Integer.parseInt(m_parametersConfig[++index]);
            m_longSize = Integer.parseInt(m_parametersConfig[++index]);


        }catch(FileNotFoundException e){
            System.out.println("Error: Can't access input of output file");
        }
    }

    boolean isReadyToStart(){
        if((m_Reader == null )&&(m_Writer == null)&&(m_Executor == null)) {
            return false;
        }else
            return  true;
    }

    public void Start(){
        if(isReadyToStart() == false){
            System.out.println("Error: Occurred problems with configuration file");
            return;
        }

        m_Executor.cyclicShift(m_Reader,m_Writer);
        m_Reader.Close();
        m_Writer.Close();
    }
}
