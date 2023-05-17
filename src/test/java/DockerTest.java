import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class DockerTest {
    /**
     * clear all docker containers & images prior to starting test suite
     */
    @Before
    public void setUp() throws IOException, InterruptedException {
//        remove all containers
        runCommandReturnOutput("docker container prune --force");
//        remove all images
        runCommandReturnOutput("docker images prune -a --force");
    }

    /**
     * utility method used to run a one-line command and return a multi line output as a single string
     * @param cmd
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public String runCommandReturnOutput(String cmd) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(cmd);
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(process.getInputStream()));
        String result = "";
        String outputLine = "";
        do{
            outputLine = reader.readLine();
            if(outputLine!=null)
                result += outputLine + "\n";
        }while(outputLine!=null);
        process.waitFor();
        return result;
    }
    /**
     * read until the first non-commented line, and verify that the container gets pulled
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void pullTest() throws IOException, InterruptedException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/Lab.txt"));
        String in;
        do{
            in = bufferedReader.readLine();
        }while((in.length() > 0 && in.charAt(0)=='#'));
        System.out.println("running your first command: "+ in);
        String result = runCommandReturnOutput(in);
        System.out.println(result);
        System.out.println("getting all images:");
        String images = runCommandReturnOutput("docker images");
        System.out.println(images);
//      verify that 'alpine' is in the list of images.
        Assert.assertTrue(images.contains("alpine"));
    }
    /**
     * read until the second non-commented line, and verify that the container gets run
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void runTest() throws IOException, InterruptedException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/Lab.txt"));
        String in;
//        read to the first non-commented line
        do{
            in = bufferedReader.readLine();
        }while((in.length() > 0 && in.charAt(0)=='#'));
//        run the pull command
        System.out.println("running your first command: "+ in);
        runCommandReturnOutput(in);
//        read to the second non-commented line
        do{
            in = bufferedReader.readLine();
        }while(in.length() > 0 && in.charAt(0)=='#');
        System.out.println("running your second command: "+ in);
//        run the run command
        String result = runCommandReturnOutput(in);
        System.out.println(result);
//        get the list of running containers
        System.out.println("getting all containers:");
        String containers = runCommandReturnOutput("docker ps -a");
        System.out.println(containers);
//      verify that there is an 'alpine' container
        Assert.assertTrue(containers.contains("alpine"));
    }
}
