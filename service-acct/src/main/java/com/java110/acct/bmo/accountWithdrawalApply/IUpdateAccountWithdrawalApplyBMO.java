package com.java110.acct.bmo.accountWithdrawalApply;
import com.java110.po.account.AccountWithdrawalApplyPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateAccountWithdrawalApplyBMO {


    /**
     * 修改账户提现
     * add by wuxw
     * @param accountWithdrawalApplyPo
     * @return
     */
    ResponseEntity<String> update(AccountWithdrawalApplyPo accountWithdrawalApplyPo);


}
