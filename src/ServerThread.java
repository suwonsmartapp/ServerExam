import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by massivCode on 2015-12-26.
 */
public class ServerThread extends Thread {

    static List<DataOutputStream> list = Collections.synchronizedList(new ArrayList<DataOutputStream>());
    Socket socket;
    DataOutputStream output;


    public ServerThread(Socket socket) {
        this.socket = socket;
        try {
            System.out.println("아웃풋 스트림 생성 시작");
            output = new DataOutputStream(socket.getOutputStream());
            System.out.println("아웃풋 스트림 생성 완료");
            list.add(output);
            System.out.println("아웃풋 스트림 추가 완료");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        String name = null;


        try {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            System.out.println("인풋스트림 생성 완료");

            // 수신된 첫번째 문자열을 닉네임으로 지정합니다.
            name = in.readUTF();
            System.out.println("접속자 : " + name);
            sendAll("#" + name + "님이 들어오셨습니다");

            while (true) {
                String str = in.readUTF();

                if (str == null) {
                    break;
                }

                // 수신된 메시지 앞에 대화명을 붙여서 모든 클라이언트로 보냅니다.
                sendAll(name + ">" + str);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            list.remove(output);
            // 해당 사용자가 채팅을 종료했다는 메시지를 모든 클라이언트로 보냅니다.
            sendAll("#" + name + "님이 나가셨습니다");

            try {
                socket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }
    }


    private void sendAll(String str) {
        for (DataOutputStream writer : list) {
            // 서버에 연결된 모든 클라이언트로 똑같은 메세지를 보냅니다.
            System.out.println("sendAll");
            try {
                writer.writeUTF(str);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
