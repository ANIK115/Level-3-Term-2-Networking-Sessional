package network.client;

import java.io.*;
import java.net.Socket;

public class ClientThread implements Runnable{

    Thread clientThread;
    Socket client;
    OutputStream os;
    InputStream is;
    File file;

    public ClientThread(Socket client, String filename) throws IOException {
        this.client = client;
        this.os = this.client.getOutputStream();
        this.is = this.client.getInputStream();
        this.file = new File(filename);
        clientThread = new Thread(this);
        clientThread.start();
    }


    @Override
    public void run() {
        PrintWriter pr = null;

        try {
            pr = new PrintWriter(client.getOutputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        if(file.exists())
        {
            String requst = "UPLOAD "+file.toString();
            System.out.println("Requested file: "+requst);
            pr.write(requst);
            pr.write("\r\n");
            pr.flush();

            String response = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(this.is));

            if(br != null)
            {
                try {
                    response = br.readLine();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            if(response.equals("Accepted"))
            {
                byte[] fileChunk = new byte[1024];
                int count = 0;
                BufferedInputStream bis = null;
                try {
                    bis = new BufferedInputStream(new FileInputStream(file));
                } catch (FileNotFoundException e) {
                    System.out.println(e.getMessage());
                }
                try{
                    while ((count = bis.read(fileChunk)) > 0)
                    {
                        os.write(fileChunk, 0, count);
                        os.flush();
                    }

                }catch (IOException e)
                {
                    System.out.println(e.getMessage());
                }

                try {
                    bis.close();
                    os.close();
                    client.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }else
            {
                String msg = "Cannot upload such files";
                System.out.println(msg);
//                pr.write(msg);
//                pr.write("\r\n");
//                pr.flush();
            }
        }else
        {
            System.out.println(file.toString()+" Not Found!");
            String message = "NO "+file.toString();
            pr.write(message);
            pr.write("\r\n");
            pr.flush();
        }
    }
}
