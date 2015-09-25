package com.colombosoft.ednasalespad.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thahzan on 1/5/15.
 */
public class Bank implements Serializable {

    private int bankId, selectedBranchId;
    private String bankName;
    private List<BankBranch> branches;

    public Bank() {}

    public Bank(int bankId,  String bankName) {
        this.bankId = bankId;
        this.bankName = bankName;
    }

    public Bank(int bankId, int selectedBranchId, String bankName, List<BankBranch> branches) {
        this.bankId = bankId;
        this.selectedBranchId = selectedBranchId;
        this.bankName = bankName;
        this.branches = branches;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public int getSelectedBranchId() {
        return selectedBranchId;
    }

    public void setSelectedBranchId(int selectedBranchId) {
        this.selectedBranchId = selectedBranchId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public List<BankBranch> getBranches() {
        return branches;
    }

    public void setBranches(List<BankBranch> branches) {
        this.branches = branches;
    }

    public static Bank parseBank(JSONObject instance) throws JSONException {

        if(instance != null) {
            Bank bank = new Bank(instance.getInt("id_bank"), instance.getString("bank_name"));
            List<BankBranch> branchList = new ArrayList<BankBranch>();
            JSONArray branchesArray = instance.getJSONArray("Branch");
            for(int i=0; i<branchesArray.length(); i++){
                BankBranch branch = BankBranch.parseBranch(branchesArray.getJSONObject(i));
                if(branch != null) {
                    branchList.add(branch);
                }
            }
            bank.setBranches(branchList);

            return bank;
        }


        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(bankName);
        if(branches != null){
            for(BankBranch branch : branches){
                builder.append("\n").append(branch.getBranchName());
            }
        }
        return builder.toString();
    }
}
