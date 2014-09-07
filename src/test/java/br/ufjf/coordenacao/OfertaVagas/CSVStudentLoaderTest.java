package br.ufjf.coordenacao.OfertaVagas;

import java.io.File;
import java.io.IOException;

import br.ufjf.coordenacao.OfertaVagas.loader.CSVStudentLoader;
import br.ufjf.coordenacao.OfertaVagas.model.StudentsHistory;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class CSVStudentLoaderTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public CSVStudentLoaderTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( CSVStudentLoader.class );
    }

    /**
     * Rigourous Test :-)
     * @throws IOException 
     */
    public void testApp() throws IOException
    {
        CSVStudentLoader csv = new CSVStudentLoader(new File("dados/alunos.txt"));
        StudentsHistory sh = csv.getStudentsHistory();
        System.out.println(sh);
    }
}
