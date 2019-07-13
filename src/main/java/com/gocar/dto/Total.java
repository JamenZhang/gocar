package com.gocar.dto;

/**
 *汇总Bean 
 */
public class Total {
    //租金流水
    private int rentCount;
    //押金
    private float haveCash;

    private int totalCarCount;

    private int inStoreCarCount;

    private int repairCarCount;

    private int beakdownCarCount;

    private int totalStudentCount;

    private int borrowedCount;

    private float beakdownMoney;

    private float profitMoney;

    public int getRentCount() {
        return rentCount;
    }

    public void setRentCount(int rentCount) {
        this.rentCount = rentCount;
    }

    public float getHaveCash() {
        return haveCash;
    }

    public void setHaveCash(float haveCash) {
        this.haveCash = haveCash;
    }

    public int getTotalCarCount() {
        return totalCarCount;
    }

    public void setTotalCarCount(int totalCarCount) {
        this.totalCarCount = totalCarCount;
    }

    public int getInStoreCarCount() {
        return inStoreCarCount;
    }

    public void setInStoreCarCount(int inStoreCarCount) {
        this.inStoreCarCount = inStoreCarCount;
    }

    public int getRepairCarCount() {
        return repairCarCount;
    }

    public void setRepairCarCount(int repairCarCount) {
        this.repairCarCount = repairCarCount;
    }

    public int getBeakdownCarCount() {
        return beakdownCarCount;
    }

    public void setBeakdownCarCount(int beakdownCarCount) {
        this.beakdownCarCount = beakdownCarCount;
    }

    public int getTotalStudentCount() {
        return totalStudentCount;
    }

    public void setTotalStudentCount(int totalStudentCount) {
        this.totalStudentCount = totalStudentCount;
    }

    public int getBorrowedCount() {
        return borrowedCount;
    }

    public void setBorrowedCount(int borrowedCount) {
        this.borrowedCount = borrowedCount;
    }

    public float getBeakdownMoney() {
        return beakdownMoney;
    }

    public void setBeakdownMoney(float beakdownMoney) {
        this.beakdownMoney = beakdownMoney;
    }

    public float getProfitMoney() {
        return profitMoney;
    }

    public void setProfitMoney(float profitMoney) {
        this.profitMoney = profitMoney;
    }

    @Override
    public String toString() {
        return "Total{" +
                "rentCount=" + rentCount +
                ", haveCash=" + haveCash +
                ", totalCarCount=" + totalCarCount +
                ", inStoreCarCount=" + inStoreCarCount +
                ", repairCarCount=" + repairCarCount +
                ", beakdownCarCount=" + beakdownCarCount +
                ", totalStudentCount=" + totalStudentCount +
                ", borrowedCount=" + borrowedCount +
                ", beakdownMoney=" + beakdownMoney +
                ", profitMoney=" + profitMoney +
                '}';
    }
}

