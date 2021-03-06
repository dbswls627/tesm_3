package com.team_3.accountbook;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
백그라운드
 */

public class ListenerService extends WearableListenerService {
    String TAG = "mobile Listener";
    AppDatabase db;
    LocalDate selectedDate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        db = AppDatabase.getInstance(ListenerService.this);

        if (messageEvent.getPath().equals("/message_path")) {
            final String message = new String(messageEvent.getData());      // message -> 'n,nnn원'(실시간 반영 전 setText 돼있던 금액)
            Log.v(TAG, "Message path received on phone is: " + messageEvent.getPath());
            Log.v(TAG, "Message received on phone is: " + message);

            // Broadcast message to MainActivity for display
            // 받은데이터 MainActivity 로 넘겨 주기
           /* Intent messageIntent = new Intent();
            messageIntent.setAction(Intent.ACTION_SEND);
            messageIntent.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);   // WearAc 의 onReceive()함수가 실행됨*/

            selectedDate = LocalDate.now();

            bluetooth(this, String.valueOf(db.dao().getAmountOfMonthForWatch(monthYearFromDate(LocalDate.now()), "expense")));

        }
        else {
            super.onMessageReceived(messageEvent);
        }

    }



    class SendThread extends Thread {
        String path;
        String message;
        Context context;
        //constructor
        SendThread(String p, String msg,Context context) {
            path = p;
            message = msg;
            this.context = context;
        }

        //sends the message via the thread.  this will send to all wearables connected, but
        //since there is (should only?) be one, no problem.
        public void run() {

            //first get all the nodes, ie connected wearable devices.
            Task<List<Node>> nodeListTask =
                    Wearable.getNodeClient(context.getApplicationContext()).getConnectedNodes();
            try {
                // Block on a task and get the result synchronously (because this is on a background
                // thread).
                List<Node> nodes = Tasks.await(nodeListTask);

                //Now send the message to each device.
                for (Node node : nodes) {
                    Task<Integer> sendMessageTask =
                            Wearable.getMessageClient(context).sendMessage(node.getId(), path, message.getBytes());    // 워치로 sum 금액을 보냄

                    try {
                        // Block on a task and get the result synchronously (because this is on a background
                        // thread).
                        Integer result = Tasks.await(sendMessageTask);

                        Log.v(TAG, "SendThread: message send to " + node.getDisplayName());

                    } catch (ExecutionException exception) {

                        Log.e(TAG, "Send Task failed: " + exception);

                    } catch (InterruptedException exception) {
                        Log.e(TAG, "Send Interrupt occurred: " + exception);
                    }

                }

            } catch (ExecutionException exception) {

                Log.e(TAG, "Node Task failed: " + exception);

            } catch (InterruptedException exception) {
                Log.e(TAG, "Node Interrupt occurred: " + exception);
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String monthYearFromDate(LocalDate date) {      // LocalDate 형식(YYYY-MM-DD)의 데이터를 '----년 --월' 형식으로 변환하는 함수
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY년 MM월");   // 변환 형식 formatter 구축. (MMMM: 01월, MM: 01)
        return date.format(formatter);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    void bluetooth(Context context, String message) {
        db = AppDatabase.getInstance(ListenerService.this);

        if (message != null && db.dao().getWatchOnOff()) {
            new SendThread("/message_path", message, context).start();
        } else {
            new SendThread("/message_path", "0",context).start();
        }
    }
}
