package com.team_3.accountbook;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {
    private ArrayList<String> daysOfMonth;
    private OnItemClick mCallback;
    public interface OnItemClick {
        void onClick (ArrayList<item> arrayList);
    }
    ArrayList<item> arrayList = new ArrayList<>();      // arrayList2에서 item 으로 추린 리스트
    ArrayList<Cost> arrayList2 = new ArrayList<>();     // 클릭한 날의 Cost 테이블 전체 정보
    Context context;
    AppDatabase db;

    public CalendarAdapter(ArrayList<String> daysOfMonth, Context context,OnItemClick listener) {
        this.daysOfMonth = daysOfMonth;
        this.context=context;
        this.mCallback = listener;
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }


    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();        // LayoutParams: 부모 레이아웃 안에서 View(뷰)가 어떻게 배치될지를 정의하는 속성.   .xml 에서 'layout_~~'과 같음.
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);      // 그리드뷰 중 한 줄의 높이 설정

        return new CalendarViewHolder(view);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY년 MM월");      // 변환 형식 formatter 구축.
        db =AppDatabase.getInstance(context);

        holder.dayOfMonth.setText(daysOfMonth.get(position));
        holder.amount.setText(db.dao().getAmount(HomeActivity.selectedDate.format(formatter) + " " +
                                                daysOfMonth.get(position) + "일", "expense"));     // 날짜의 총 지출값 출력

        holder.itemView.setOnClickListener((i)->{    // 달력 날짜 클릭시
            arrayList.clear();
            arrayList2 = (ArrayList<Cost>) db.dao().getDate(HomeActivity.selectedDate.format(formatter) + " " +
                                                            daysOfMonth.get(position) + "일");       // 클릭한 날짜의 Cost 테이블 정보만 받아옴
            arrayList2.forEach(it -> {
                arrayList.add(new item(it.getUseDate(), it.getContent(), it.getAmount()));           // 받아온 Cost 데이터를 item 에 뿌려줌
            });
            mCallback.onClick(arrayList);   // 만든 arrayList 를 연결해야 하지만 어댑터에서 하지 못함. interface 사용해 HomeActivity 로 리스트를 넘김.
        });
    }



    public class CalendarViewHolder extends RecyclerView.ViewHolder {
        TextView dayOfMonth, amount;        // 일, 지출값

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            dayOfMonth = itemView.findViewById(R.id.cellDayText);
            amount = itemView.findViewById(R.id.amount);
        }
    }
}
