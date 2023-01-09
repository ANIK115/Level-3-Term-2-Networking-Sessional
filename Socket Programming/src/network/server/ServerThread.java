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
    PrintWriter logWriter;

    public ServerThread(Socket client) throws IOException {
        logWriter = new PrintWriter(new FileWriter("log.txt", true));
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
        fis.close();
        br.close();
        return webpage;
    }

    public void helperWriter(PrintWriter writer, String responseHeader,Date date, String contentType,long length,String content)
    {
        writer.write(responseHeader);
        writer.write("Server: Java HTTP Server: 1.0\r\n");
        writer.write("Date: " + date + "\r\n");
        if(length > 0)
        {
            writer.write(contentType);
            writer.write("Content-Length: " + length + "\r\n");
        }
        writer.write("\r\n");
        if(content.length() > 0)
        {
            writer.write(content);
        }
        writer.flush();
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
           logWriter.write("\n\nHTTP request: "+input);
           logWriter.write("\nHTTP response: ");
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
                       Date date = new Date();
                       helperWriter(pr,"HTTP/1.1 200 OK\r\n", date, "Content-Type: text/html\r\n", content.length(), content );

                       //writing log file
                       helperWriter(logWriter,"HTTP/1.1 200 OK\r\n", date,  "Content-Type: text/html\r\n", content.length(), "Content:\n"+content );

                       System.out.println("Contents shown");
                   }else if(tokens[1].length() > 1){
                       if(tokens[1].lastIndexOf(".") == -1)
                       {
                           System.out.println("input: "+input);
                           File dir = new File(tokens[1].substring(1));
                           if(dir.exists())
                           {
                               String content = directoryContents(dir);
                               Date date = new Date();
                               helperWriter(pr, "HTTP/1.1 200 OK\r\n", date, "Content-Type: text/html\r\n", content.length(), content);
                               //writing log file
                               helperWriter(logWriter, "HTTP/1.1 200 OK\r\n", date, "Content-Type: text/html\r\n", content.length(), content);
                           }else {
                               File errorFile = new File("src/network/server/view/error.html");
                               FileInputStream fis = new FileInputStream(errorFile);
                               BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
                               String line;
                               while(( line = br.readLine()) != null ) {
                                   sb.append( line );
                                   sb.append( "\n" );
                               }
                               String content = sb.toString();
                               Date date = new Date();
                               helperWriter(pr, "HTTP/1.1 404 NOT FOUND\r\n", date, "", 0, content);

                               //writing log file:
                               helperWriter(logWriter, "HTTP/1.1 404 NOT FOUND\r\n", date, "", 0, "Content:\n"+content);

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
                               Date date = new Date();
                               helperWriter(pr, "HTTP/1.1 200 OK\r\n", date,"Content-Type: text/html\r\n", content.length(), content );

                               //writing log file:
                               helperWriter(logWriter, "HTTP/1.1 200 OK\r\n", date,"Content-Type: text/html\r\n", content.length(), "Content:\n"+content );

                           }
                           else if(ext.equalsIgnoreCase("jpg"))
                           {
                               String fileName = tokens[1].substring(1);
                               File file = new File(fileName);
                               Date date = new Date();
                               helperWriter(pr, "HTTP/1.1 200 OK\r\n", date, "Content-Type: image/jpg\r\n", file.length(), "");
                               byte[] imgChunk = new byte[1024];
                               int count = 0;
                               BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

                               while((count = bis.read(imgChunk)) > 0)
                               {
                                   os.write(imgChunk, 0, count);
                                   os.flush();
                               }

                               //writing log file
                               helperWriter(logWriter, "HTTP/1.1 200 OK\r\n", date, "Content-Type: image/jpg\r\n", file.length(), "Content:\n"+file.toString());
                           }else if(ext.equalsIgnoreCase("jpeg"))
                           {
                               String fileName = tokens[1].substring(1);
                               File file = new File(fileName);
                               Date date = new Date();
                               helperWriter(pr, "HTTP/1.1 200 OK\r\n", date, "Content-Type: image/jpeg\r\n", file.length(), "");
                               byte[] imgChunk = new byte[1024];
                               int count = 0;
                               BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

                               while((count = bis.read(imgChunk)) > 0)
                               {
                                   os.write(imgChunk, 0, count);
                                   os.flush();
                               }

                               //writing log file
                               helperWriter(logWriter, "HTTP/1.1 200 OK\r\n", date, "Content-Type: image/jpeg\r\n", file.length(), "Content:\n"+file.toString());
                           }else if(ext.equalsIgnoreCase("png"))
                           {
                               String fileName = tokens[1].substring(1);
                               File file = new File(fileName);
                               Date date = new Date();
                               helperWriter(pr, "HTTP/1.1 200 OK\r\n", date, "Content-Type: image/png\r\n", file.length(), "");
                               byte[] imgChunk = new byte[1024];
                               int count = 0;
                               BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

                               while((count = bis.read(imgChunk)) > 0)
                               {
                                   os.write(imgChunk, 0, count);
                                   os.flush();
                               }

                               //writing log file
                               helperWriter(logWriter, "HTTP/1.1 200 OK\r\n", date, "Content-Type: image/png\r\n", file.length(), "Content:\n"+file.toString());
                           }else
                           {
                               String filename = tokens[1].substring(1);
                               File file = new File(filename);
                               if(file.exists())
                               {
                                   Date date = new Date();
                                   helperWriter(pr, "HTTP/1.1 200 OK\r\n", date, "Content-Type: application/x-force-download\r\n", file.length(), "");

                                   byte[] fileChunk = new byte[1024];
                                   int count = 0;
                                   BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                                   while((count = bis.read(fileChunk)) >0)
                                   {
                                       os.write(fileChunk, 0, count);
                                       os.flush();
                                   }

                                   //writing log file
                                   helperWriter(logWriter, "HTTP/1.1 200 OK\r\n", date, "Content-Type: application/x-force-download\r\n", file.length(), "Content:\n"+file.toString());

                               }else
                               {
                                   File errorFile = new File("src/network/server/view/error.html");
                                   FileInputStream fis = new FileInputStream(errorFile);
                                   BufferedReader br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
                                   String line;
                                   while(( line = br.readLine()) != null ) {
                                       sb.append( line );
                                       sb.append( "\n" );
                                   }
                                   String content = sb.toString();
                                   Date date = new Date();
                                   helperWriter(pr, "HTTP/1.1 404 NOT FOUND\r\n", date, "", 0, content);

                                   //writing log file
                                   helperWriter(logWriter, "HTTP/1.1 404 NOT FOUND\r\n", date, "", 0, "Content:\n"+content);

                               }
                           }
                       }
               }

               }else if(input.startsWith("UPLOAD"))
               {
                   String filename = input.substring(7);
                   System.out.println("Requested file from client: "+filename);
                   int lastIndex = input.lastIndexOf(".");
                   if(lastIndex < 0)
                   {
                       String response = "This is not a valid file name!";
                       pr.write(response);
                       pr.write("\r\n");
                       pr.flush();
                       System.out.println(response);
                       return;
                   }
                   String extension = input.substring(lastIndex+1);
                   if(extension.equals("txt") || extension.equals("png") || extension.equals("jpg") || extension.equals("mp4")
                   || extension.equals("jpeg") || extension.equals("mkv"))
                   {
                       String response = "Accepted";
                       pr.write(response);
                       pr.write("\r\n");
                       pr.flush();

                       String filePath = "src/network/server/uploaded/"+filename;
                       File file = new File(filePath);
                       FileOutputStream fos = new FileOutputStream(file);

                       byte[] fileChunk = new byte[1024];
                       int count = 0;

                       while((count = is.read(fileChunk)) > 0)
                       {
                           fos.write(fileChunk, 0, count);
                           fos.flush();
                       }
                       System.out.println(filename+" Uploaded successfully");
                       fos.close();
                       is.close();
                   }else
                   {
                       String response = "Rejected";
                       System.out.println("File upload request: "+response);
                       pr.write(response);
                       pr.write("\r\n");
                       pr.flush();
                   }


               }else
               {
                   System.out.println("Not a valid request!");
               }
           }
           client.close();
           pr.close();
           logWriter.close();
       }catch (IOException e)
       {
           System.out.println(e.getMessage());
       }
    }
}
