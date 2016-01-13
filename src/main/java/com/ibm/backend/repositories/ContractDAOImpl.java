package com.ibm.backend.repositories;

import com.ibm.backend.domain.Contract;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Jan Valentik on 1/10/2016.
 */
@UIScope
@Repository
public class ContractDAOImpl implements ContractDAO {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}


	public Contract findByWbsId(String wbsId) {
		String sql = "SELECT * FROM USER04064.CONTRACTS WHERE WBS_ID = ?";
		Contract contract = jdbcTemplate.queryForObject(sql, new Object[]{wbsId}, new RowMapper<Contract>() {
			@Override
			public Contract mapRow(ResultSet resultSet, int i) throws SQLException {
				Contract contract1 = new Contract();
				contract1.setContractNumber(resultSet.getString("LEGAL_CONTRACT"));
				contract1.setCustomerName(resultSet.getString("CUSTOMER_NAME"));
				contract1.setPeNotes(resultSet.getString("PE_NOTES"));
				contract1.setPmaNotesId(resultSet.getString("PMA_NOTES"));
				contract1.setSapContract(resultSet.getString("SAP_CONTRACT"));
				contract1.setWbsId(resultSet.getString("WBS_ID"));
				return contract1;
			}
		});
		return contract;
	}
}


