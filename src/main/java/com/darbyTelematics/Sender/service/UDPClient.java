package com.darbyTelematics.Sender.service;



import com.pojo.Messages;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.*;

@Service
public class UDPClient {
    public String run(Messages receivedData) throws IOException, ClassNotFoundException {
//      serialization of an object or received data's object. Because datagram packet takes only byte array input
        byte[] buf;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try{
            out = new ObjectOutputStream(bos);
            out.writeObject(receivedData);
            buf = bos.toByteArray();
        }finally {
            try{
                bos.close();;
            }catch (Exception e){
                System.out.println(e);
            }
        }

        DatagramSocket socket = new DatagramSocket();
        InetAddress address =  InetAddress.getLocalHost();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 8787);
        socket.send(packet);
        System.out.println(buf);
        return "Message has been sent to Listener using Datagram Packet";
    }
}