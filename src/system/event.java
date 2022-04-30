package system;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

public class event extends gui{


    int statusPort=1234 ,userPort=1235,passwordPort=1236,msgPort=1237,receiverPortFromSender=1238 ,newUserPort=1239,newPassPort=1240,senderPortFromSender=1241,senderPortToReceiver=1242,receiverPortToReceiver=1243,mailPortToReceiver=1244,EmailPortFromSender=1245,count=0;
    Socket statusSocket , usernameSocket ,passwordSocket , mailSocketFromSender,senderSocketFromSender,receiverSocketFromSender ,newUserSocket,newPassSocket, receiverSocketToReceiver,senderSocketToReceiver,mailSocketToReceiver;
    ServerSocket statusServerSocket , usernameServerSocket ,passwordServerSocket , msgServerSocket ,newUserServerSocket,newPassServerSocket, receiverServerToReceiver,senderServerToReceiver,mailServerToReceiver, mailServerFromSender,senderServerFromSender,receiverServerFromSender;
    DataOutputStream writeSignInResponse ,writeSignUpResponse,writeSendEmailResponse,writeEmail,writeSender;
    DataInputStream readUsername,readPassword,readNewUsername,readNewPassword,readMail,raedReceiver,readSender;
    ArrayList requests,responses,usernamesList,passwordsList,senders,receivers,mails;
    String MySQLURL = "jdbc:mysql://localhost:3306/email", databseUserName = "root", databasePassword = "root" ,sender;
    Connection con = null;
    ResultSet res;



    event(){
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    System.exit(1);
                }catch (Exception err){}
            }
        });




        createConnection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                requests=new ArrayList<String>();
                responses=new ArrayList<String>();
                usernamesList=new ArrayList<String>();
                passwordsList=new ArrayList<String>();

                //ArrayLists to store users data and information in:-
                senders=new ArrayList<String>();
                receivers=new ArrayList<String>();
                mails=new ArrayList<String>();

                try {
                    con = DriverManager.getConnection(MySQLURL, databseUserName, databasePassword);
                    if (con != null) {
                        System.out.println("Database connection is successful !!!!");
                    }
                } catch (Exception ex) {
                    System.out.println("ex");
                }


                try{
                    statusServerSocket=new ServerSocket(statusPort);
                    usernameServerSocket=new ServerSocket(userPort);
                    passwordServerSocket=new ServerSocket(passwordPort);
                    msgServerSocket=new ServerSocket(msgPort);
                    status.setText("Started!");
                }catch (Exception er){System.out.println("Error:"+er);}


                //handling clients connections:-
                Thread connectProcess= new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true){
                            try {
                                statusSocket=statusServerSocket.accept();
                            }catch (Exception er){System.out.println("Connection Error!");}
                        }
                    }
                });

                //handling signingIn requests:-
                Thread signInProcess=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(true){
                            try {
                                usernameSocket = usernameServerSocket.accept();
                                readUsername=new DataInputStream(usernameSocket.getInputStream());
                                passwordSocket=passwordServerSocket.accept();
                                readPassword=new DataInputStream(passwordSocket.getInputStream());
                                String username=readUsername.readUTF();
                                String password=readPassword.readUTF();

                                if((!username.equals("") || !username.equals(null)) && (!password.equals("") || !password.equals(null))){
                                    requests.add("request signIn with user:"+username+"\nand password:"+password+"\n\n");
                                    clientReq_txt.setText(Arrays.toString(requests.toArray()));
                                }

                                Statement statement=con.createStatement();
                                String signinQuery="select * from signIn;";
                                res=statement.executeQuery(signinQuery);
                               boolean flag = false;
                               while(res.next()){
                                if(username.equals(res.getString(1)) && password.equals(res.getString(2))){
                                    flag=true;
                                    break;
                                }
                            }


                               if(flag==true){
                                   writeSignInResponse=new DataOutputStream(statusSocket.getOutputStream());
                                   writeSignInResponse.writeUTF("yes");
                                   writeSignInResponse.flush();
                                   writeSignInResponse.close();
                                   responses.add("Server response:"+"yes\n\n");
                                   serverRes_txt.setText(Arrays.toString(responses.toArray()));
                               }else if(flag==false) {
                                   writeSignInResponse=new DataOutputStream(statusSocket.getOutputStream());
                                   writeSignInResponse.writeUTF("no");
                                   writeSignInResponse.flush();
                                   writeSignInResponse.close();
                                   responses.add("Server response:"+"no\n\n");
                                   serverRes_txt.setText(Arrays.toString(responses.toArray()));
                               }


                            }catch (Exception er){System.out.println("Error:"+er);}
                        }
                    }
                });

                //handling signingUp requests:-
                Thread signUpProcess=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            newUserServerSocket = new ServerSocket(newUserPort);
                            newPassServerSocket = new ServerSocket(newPassPort);
                        }catch (Exception er){

                        }
                        while(true){
                            try{
                                newUserSocket=newUserServerSocket.accept();
                                newPassSocket=newPassServerSocket.accept();
                                readNewUsername=new DataInputStream(newUserSocket.getInputStream());
                                readNewPassword=new DataInputStream(newPassSocket.getInputStream());
                                String user=readNewUsername.readUTF();
                                String pass=readNewPassword.readUTF();

                                requests.add("request signUp newUser:"+user+"\nand newPassword:"+pass+"\n\n");
                                clientReq_txt.setText(Arrays.toString(requests.toArray()));


                                if((!user.equals("")) || (!user.equals(null)) && (!pass.equals("") || !pass.equals(null))){
                                    if(!passwordsList.contains(pass) && !usernamesList.contains(user)) {

                                        usernamesList.add(user);
                                        passwordsList.add(pass);

                                        Statement statement = con.createStatement();
                                        String query = "insert into  signIn values ('" + user + "','" + pass + "')";
                                        statement.executeUpdate(query);

                                        writeSignUpResponse = new DataOutputStream(statusSocket.getOutputStream());
                                        writeSignUpResponse.writeUTF("Registered:yes!");
                                        writeSignUpResponse.flush();
                                        writeSignUpResponse.close();

                                        responses.add("Server response:" + "Registered:yes!\n\n");
                                        serverRes_txt.setText(Arrays.toString(responses.toArray()));
                                    }


                                }else {
                                    writeSignUpResponse=new DataOutputStream(statusSocket.getOutputStream());
                                    writeSignUpResponse.writeUTF("Registration:failed!");
                                    writeSignUpResponse.flush();
                                    writeSignUpResponse.close();

                                    responses.add("Server response:"+"Registered:no!\n\n");
                                    serverRes_txt.setText(Arrays.toString(responses.toArray()));
                                }


                            }catch (Exception er){
                                try {
                                    writeSignUpResponse = new DataOutputStream(statusSocket.getOutputStream());
                                    writeSignUpResponse.writeUTF("Registration:failed!");
                                    writeSignUpResponse.flush();
                                    writeSignUpResponse.close();

                                    responses.add("Server response:" + "Registered:no!\n\n");
                                    serverRes_txt.setText(Arrays.toString(responses.toArray()));
                                }catch (Exception err){}
                            }
                        }
                    }
                });

///////////////////////////////////////////////////////handling mailing requests:-/////////////////////////////////////////////////////////////////
                //Receiver thread to receive mails from any client nay time:-
                Thread mailProcessReceive=new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try{
                            senderServerFromSender=new ServerSocket(senderPortFromSender);
                            receiverServerFromSender=new ServerSocket(receiverPortFromSender);
                            mailServerFromSender=new ServerSocket(EmailPortFromSender);
                        }catch (Exception er){
                            System.out.println("Receiver-Thread-er:"+er);
                        }

                        while (true){
                            try{
                                 senderSocketFromSender=senderServerFromSender.accept();
                                 receiverSocketFromSender=receiverServerFromSender.accept();
                                 mailSocketFromSender=mailServerFromSender.accept();

                                DataInputStream readSender=new DataInputStream(senderSocketFromSender.getInputStream());
                                DataInputStream readReceiver=new DataInputStream(receiverSocketFromSender.getInputStream());
                                DataInputStream readMail=new DataInputStream(mailSocketFromSender.getInputStream());

                                String sender=readSender.readUTF();
                                String receiver=readReceiver.readUTF();
                                String mail=readMail.readUTF();

                                if((!sender.equals("") && !receiver.equals("") && !mail.equals("")) && (!sender.equals(null) && !receiver.equals(null) && !mail.equals(null))){

                                        senders.add(sender);
                                        receivers.add(receiver);
                                        mails.add(mail);
                                        count++;

                                        requests.add("request EmailSend From:" + sender + "\nand To:" + receiver + "\n\n");
                                        clientReq_txt.setText(Arrays.toString(requests.toArray()));
                                        responses.add("Server response:" + "Email-Sent:yes!\n\n");
                                        serverRes_txt.setText(Arrays.toString(responses.toArray()));

                                }else {
                                    requests.add("request EmailSend From:"+sender+"\nand To:"+receiver+"\n\n");
                                    clientReq_txt.setText(Arrays.toString(requests.toArray()));
                                    responses.add("Server response:" + "Email-Sent:no!\n\n");
                                    serverRes_txt.setText(Arrays.toString(responses.toArray()));
                                }


                            }catch (Exception err){
                                System.out.println("err:receiver Thread error:"+err);
                            }

                        }


                    }
                });


                //Sender thread to send the received mails from clients to targeted clients:-
                Thread mailProcessSend=new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try{
                            senderServerToReceiver=new ServerSocket(senderPortToReceiver);
                            receiverServerToReceiver=new ServerSocket(receiverPortToReceiver);
                            mailServerToReceiver=new ServerSocket(mailPortToReceiver);
                        }catch (Exception er){
                            System.out.println("serverSocket_ER:"+er);
                        }


                        while(true){

                            try{
                                 senderSocketToReceiver= senderServerToReceiver.accept();
                                 receiverSocketToReceiver= receiverServerToReceiver.accept();
                                 mailSocketToReceiver= mailServerToReceiver.accept();

                                for(int x=count-1;x>0;x--) {
                                    Thread.sleep(500);
                                    DataOutputStream writeSender = new DataOutputStream(senderSocketToReceiver.getOutputStream());
                                    writeSender.writeUTF(senders.get(x).toString());
                                    writeSender.flush();
                                    writeSender.close();

                                    DataOutputStream writeReceiver = new DataOutputStream(receiverSocketToReceiver.getOutputStream());
                                    writeReceiver.writeUTF(receivers.get(x).toString());
                                    writeReceiver.flush();
                                    writeReceiver.close();

                                    DataOutputStream writeMail = new DataOutputStream(mailSocketToReceiver.getOutputStream());
                                    writeMail.writeUTF(mails.get(x).toString());
                                    writeMail.flush();
                                    writeMail.close();
                                }


                            }catch (Exception err){
                                System.out.println("writing_data_ERR:"+err);
                            }

                        }


                    }
                });



                //starting Threads:-
                connectProcess.start();
                signInProcess.start();
                signUpProcess.start();
                mailProcessSend.start();
                mailProcessReceive.start();

            }
        });




        closeEverything.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    status.setText("Disconnected!");
                    statusSocket.close();
                    statusServerSocket.close();
                }catch (Exception err){System.out.println("Error:"+err);}


            }
        });



    }

}
