package ru.khasanov;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception {
        DiscardServer discardServer = new DiscardServer(8080);
        discardServer.run();
    }
}
