package network.server;

import java.io.*;
import java.net.Socket;
import java.util.Date;

public class ServerThread implements Runnable{
    int count = 0;
    Thread serviceThread;
    Socket client;
    OutputStream os;
    InputStream is;

    public ServerThread(Socket client) throws IOException {
        this.client = client;
        this.os = this.client.getOutputStream();
        this.is = this.client.getInputStream();
        serviceThread = new Thread(this);
        serviceThread.start();
    }

    public String directoryContents(File directory)
    {
        String name = directory.toString();
        String[] contents = directory.list();
        String webpage="<html>\n" +
                "  <head>\n" +
                "    <title>Home Page</title>\n" +
                "    <meta http-equiv=\\\"Content-Type\\\" content=\\\"text/html; charset=UTF-8\\\">\n" +
                "    <link rel=\\\"icon\\\" href=\\\"data:,\\\">\n" +
                "  </head>\n" +
                "  <body>\n";
        for(String s : contents)
        {
            webpage+= "<a href= \"/"+name+"\\"+s+"\">"+s+"<br>";
        }
        webpage+= "</body>\n" +
                "</html>\n";
        return webpage;
    }

    public String showTextFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while(( line = br.readLine()) != null ) {
            sb.append( line );
            sb.append( "<br>" );
        }
        String content = sb.toString();
        String webpage = "<html>\n" +
                "  <head>\n" +
                "    <title>Home Page</title>\n" +
                "    <meta http-equiv=\\\"Content-Type\\\" content=\\\"text/html; charset=UTF-8\\\">\n" +
                "    <link rel=\\\"icon\\\" href=\\\"data:,\\\">\n" +
                "  </head>\n" +
                "  <body>";
        webpage += content;
        webpage+= "</body>\n" +
                "</html>\n";
        return webpage;
    }

    public String showImageFile(String file) throws IOException {
        FileInputStream fis = new FileInputStream(new File(file));
        BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while(( line = br.readLine()) != null ) {
            sb.append( line );
            sb.append( "\n" );
        }
        String content = sb.toString();
        return content;
    }

    public String showImage(String file)
    {
        String webpage = "<html>\n" +
                "  <head>\n" +
                "    <meta http-equiv=\\\"Content-Type\\\" content=\\\"text/html; charset=UTF-8\\\">\n" +
                "    <link rel=\\\"icon\\\" href=\\\"data:,\\\">\n" +
                "  </head>\n" +
                "  <body>\n";
        webpage += "<img src=\""+file+"\">\n";
        webpage+= "</body>\n" +
                "</html>\n";
        System.out.println(webpage);
        return webpage;
    }


    @Override
    public void run() {
       BufferedReader inputbr = new BufferedReader(new InputStreamReader(this.is));
       if(inputbr == null)
       {
           System.out.println("input is null");
       }
       PrintWriter pr = null;
       try {
           pr= new PrintWriter(client.getOutputStream());
       }catch (IOException e)
       {
           System.out.println(e.getMessage());
       }

       try {
           String input = inputbr.readLine();
           if(input != null) {
               StringBuffer sb = new StringBuffer();
               String temp = "";

               if(input.startsWith("GET"))
               {
                   String[] tokens = input.split(" ");
                   if(tokens[1].length()==1)
                   {
                       System.out.println("input: "+input);
                       sb.append("<html>\n");
                       sb.append("<head>\n");
                       sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
                       sb.append( "<link rel=\"icon\" href=\"data:,\">\n" );
                       sb.append("</head>\n");
                       sb.append("<body>\n");
                       sb.append("<a href=\"root\">root</a>\n");
                       sb.append("</body>\n");
                       sb.append("</html>\n");
                       String content = sb.toString();
                       pr.write("HTTP/1.1 200 OK\r\n");
                       pr.write("Server: Java HTTP Server: 1.0\r\n");
                       pr.write("Date: " + new Date() + "\r\n");
                       pr.write("Content-Type: text/html\r\n");
                       pr.write("Content-Length: " + content.length() + "\r\n");
                       pr.write("\r\n");
                       pr.write(content);
                       pr.flush();
                       System.out.println("Contents shown");
                   }if(tokens[1].length() > 1){
                       if(tokens[1].lastIndexOf(".") == -1)
                       {
                           System.out.println("input: "+input);
                           File dir = new File(tokens[1].substring(1));
                           if(dir.exists())
                           {
                               String content = directoryContents(dir);
                               pr.write("HTTP/1.1 200 OK\r\n");
                               pr.write("Server: Java HTTP Server: 1.0\r\n");
                               pr.write("Date: " + new Date() + "\r\n");
                               pr.write("Content-Type: text/html\r\n");
                               pr.write("Content-Length: " + content.length() + "\r\n");
                               pr.write("\r\n");
                               pr.write(content);
                               pr.flush();
                           }else {
                               System.out.println(dir.toString());
                               System.out.println("dot not found");
                           }
                       }else
                       {
                           int lastIndexOfDot = tokens[1].lastIndexOf(".");
                           String ext = tokens[1].substring(lastIndexOfDot+1);
                           System.out.println(ext);
                           if(ext.equals("txt"))
                           {
                               int ind = tokens[1].lastIndexOf("/");
                               String fileName = tokens[1].substring(1);
                               String content = showTextFile(new File(fileName));
                               pr.write("HTTP/1.1 200 OK\r\n");
                               pr.write("Server: Java HTTP Server: 1.0\r\n");
                               pr.write("Date: " + new Date() + "\r\n");
                               pr.write("Content-Type: text/html\r\n");
                               pr.write("Content-Length: " + content.length() + "\r\n");
                               pr.write("\r\n");
                               pr.write(content);
                               pr.flush();
                           }
                           if(ext.equals("jpg"))
                           {
                               String fileName = tokens[1].substring(1);
                               File file = new File(fileName);
                               pr.write("HTTP/1.1 200 OK\r\n");
                               pr.write("Server: Java HTTP Server: 1.0\r\n");
                               pr.write("Date: " + new Date() + "\r\n");
                               pr.write("Content-Type: image/jpg\r\n");
                               pr.write("Content-Length: " + file.length() + "\r\n");
                               pr.write("\r\n");
                               pr.flush();
                               byte[] imgChunk = new byte[1024];
                               int count = 0;
                               BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

                               while((count = bis.read(imgChunk)) > 0)
                               {
                                   os.write(imgChunk, 0, count);
                                   os.flush();
                               }
                           }
                       }


               }

               }
           }
           client.close();
       }catch (IOException e)
       {
           System.out.println(e.getMessage());
       }
    }
}
