package system;
import javax.swing.*;
import java.awt.*;

public class gui {

    JFrame app;
    JLabel  clientReq_lbl, clientRes_lbl , server_status , status;
    JTextArea clientReq_txt, serverRes_txt;
    JButton createConnection, closeEverything ,exit;
    JScrollPane scroll1 ,scroll2;
    Image serverIcon;

    gui(){
        //defining icons:-
        serverIcon = Toolkit.getDefaultToolkit().getImage("email (1).png");
        //creating the frame:-
        app=new JFrame("Server-Administration");
        app.setIconImage(serverIcon);

        //creating the components:-
        server_status=new JLabel("status:-");
        server_status.setBounds(370,250,100,30);


        status=new JLabel("Disconnected!");
        status.setBounds(350,270,100,30);


        clientReq_lbl=new JLabel("clients_requests:-");
        clientReq_lbl.setFont(new Font("Verdana", Font.BOLD, 15));
        clientReq_lbl.setBounds(50,20,180,20);

        clientReq_txt=new JTextArea();
        clientReq_txt.setFont(new Font("Verdana", Font.PLAIN, 15));
        clientReq_txt.setToolTipText("Client-Requests");
        clientReq_txt.setEditable(false);
        scroll1 = new JScrollPane (clientReq_txt, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll1.setBounds(50,50,280,400);


        clientRes_lbl=new JLabel("server_responses:-");
        clientRes_lbl.setFont(new Font("Verdana", Font.BOLD, 16));
        clientRes_lbl.setBounds(450,20,180,20);


        serverRes_txt=new JTextArea();
        serverRes_txt.setFont(new Font("Verdana", Font.PLAIN, 16));
        serverRes_txt.setToolTipText("Server-Responses");
        serverRes_txt.setEditable(false);
        scroll2 = new JScrollPane (serverRes_txt, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scroll2.setBounds(450,50,280,400);



        exit=new JButton("Exit!");
        exit.setBounds(350,480,80,50);


        createConnection=new JButton("StartServer");
        createConnection.setBounds(580,480,150,50);


        closeEverything=new JButton("StopServer");
        closeEverything.setBounds(50,480,150,50);




        //adding the components into the frame:-
        app.add(scroll1);
        app.add(scroll2);
        app.add(clientReq_lbl);
        app.add(clientRes_lbl);
        app.add(server_status);
        app.add(status);
        app.add(createConnection);
        app.add(closeEverything);
        app.add(exit);

        //setting up the properties of the JFrame:-
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setSize(800,600);
        app.setResizable(false);
        app.setLayout(null);
        app.setVisible(true);

    }

}
