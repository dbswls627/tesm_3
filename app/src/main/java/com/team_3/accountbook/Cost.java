package com.team_3.accountbook;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity()
public class Cost {
    @PrimaryKey(autoGenerate = true) private int costId;
    private int amount;             // 금액
    private String content;         // 내용
    private String useDate;         // 날짜
    private int balance;            // 잔액
    private String sortName;        // 분류명
    private String division;        // 구분
    private String FK_wayName;      // 수단명(외래키)
    private long ms;                // 문자 수신 마이크로초
    private boolean forGoal;        // 목표금액 반영 여부
    private boolean forInEx;        // 수입or지출 반영 여부

    public Cost(int costId, int amount, String content, String useDate, int balance, String sortName, String division, String FK_wayName, long ms) {
        this.costId = costId;
        this.amount = amount;
        this.content = content;
        this.useDate = useDate;
        this.balance = balance;
        this.sortName = sortName;
        this.division = division;
        this.FK_wayName = FK_wayName;
        this.ms = ms;
    }

    public int getCostId() {
        return costId;
    }

    public void setCostId(int costId) {
        this.costId = costId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUseDate() {
        return useDate;
    }

    public void setUseDate(String useDate) {
        this.useDate = useDate;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getFK_wayName() {
        return FK_wayName;
    }

    public void setFK_wayName(String FK_wayName) {
        this.FK_wayName = FK_wayName;
    }

    public long getMs() {
        return ms;
    }

    public void setMs(long ms) {
        this.ms = ms;
    }

    public boolean isForGoal() {
        return forGoal;
    }

    public void setForGoal(boolean forGoal) {
        this.forGoal = forGoal;
    }

    public boolean isForInEx() {
        return forInEx;
    }

    public void setForInEx(boolean forInEx) {
        this.forInEx = forInEx;
    }
}
