package com.chimm.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huangshuai
 * @date 2019-05-12
 */
public class StaffData {

    String department;
    List<Staff> staffList = new ArrayList<>();

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<Staff> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<Staff> staffList) {
        this.staffList = staffList;
    }
}
