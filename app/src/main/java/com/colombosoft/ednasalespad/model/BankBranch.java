package com.colombosoft.ednasalespad.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by thahzan on 1/5/15.
 */
public class BankBranch implements Serializable {

    private int branchId;
    private String branchName;

    public BankBranch() {
    }

    public BankBranch(int branchId, String branchName) {
        this.branchId = branchId;
        this.branchName = branchName;
    }

    public static BankBranch parseBranch(JSONObject instance) throws JSONException {

        if (instance != null) {
            BankBranch branch = new BankBranch(instance.getInt("id_bank_has_branch"), instance.getString("branch_name"));
            return branch;
        }

        return null;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    @Override
    public String toString() {
        return branchName;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof BankBranch) {
            BankBranch bankBranch = (BankBranch) o;
            if (bankBranch.hashCode() == this.hashCode()) {
                return true;
            } else {
                if (bankBranch.branchId == this.branchId) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }
}
