package service;

import exception.BusinessException;
import model.Partner;
import java.sql.SQLException;
import java.util.List;

public interface IPartnerService {
    Partner register(Partner partner) throws BusinessException, SQLException;
    boolean update(Partner partner) throws BusinessException, SQLException;
    Partner findById(int id) throws SQLException;
    List<Partner> findAll() throws SQLException;

    // Validation methods (mainly used by LoanService, but public here)
    boolean isActive(int id) throws SQLException;
}