package com.java110.acct.bmo.shopVipAccountDetail;
import com.java110.po.shop.ShopVipAccountDetailPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteShopVipAccountDetailBMO {


    /**
     * 修改会员账户交易
     * add by wuxw
     * @param shopVipAccountDetailPo
     * @return
     */
    ResponseEntity<String> delete(ShopVipAccountDetailPo shopVipAccountDetailPo);


}
