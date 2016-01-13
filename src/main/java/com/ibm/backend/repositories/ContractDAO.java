package com.ibm.backend.repositories;

import com.ibm.backend.domain.Contract;

/**
 * Created by Jan Valentik on 1/12/2016.
 */
public interface ContractDAO {
	Contract findByWbsId(String wbsId);
}
