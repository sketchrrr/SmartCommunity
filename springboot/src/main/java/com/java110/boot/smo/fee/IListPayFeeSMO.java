package com.java110.boot.smo.fee;

import com.java110.core.context.IPageData;
import org.springframework.http.ResponseEntity;

public interface IListPayFeeSMO {
    public ResponseEntity<String> list(IPageData pd);
}
