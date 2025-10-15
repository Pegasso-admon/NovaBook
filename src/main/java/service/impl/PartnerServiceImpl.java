package service.impl;

import dao.IPartnerDAO;
import exception.BusinessException;
import model.Partner;
import service.IPartnerService;
import java.sql.SQLException;
import java.util.List;

public class PartnerServiceImpl implements IPartnerService {

    private final IPartnerDAO partnerDAO;

    public PartnerServiceImpl(IPartnerDAO partnerDAO) {
        this.partnerDAO = partnerDAO;
    }

    @Override
    public Partner register(Partner partner) throws BusinessException, SQLException {
        // Business Logic: Delegation for simple insertion
        return partnerDAO.insert(partner);
    }

    // Simple delegation for the rest of the methods
    @Override
    public boolean update(Partner partner) throws BusinessException, SQLException {
        return partnerDAO.update(partner);
    }

    @Override
    public Partner findById(int id) throws SQLException {
        return partnerDAO.findById(id);
    }

    @Override
    public List<Partner> findAll() throws SQLException {
        return partnerDAO.findAll();
    }

    @Override
    public boolean isActive(int id) throws SQLException {
        return partnerDAO.isActive(id);
    }
}