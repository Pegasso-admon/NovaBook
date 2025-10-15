package controller;

import service.IPartnerService;
import model.Partner;
import exception.BusinessException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller for Partner (library member) management.
 */
public class PartnerController {

    private final IPartnerService partnerService;

    public PartnerController(IPartnerService partnerService) {
        this.partnerService = partnerService;
    }

    /**
     * Registers a new partner.
     * @param partner The Partner object to register.
     * @return The registered Partner object.
     * @throws BusinessException If business validation fails.
     * @throws SQLException Database access error.
     */
    public Partner registerPartner(Partner partner) throws BusinessException, SQLException {
        return partnerService.register(partner);
    }

    /**
     * Updates an existing partner's information.
     * @param partner The Partner object with updated data.
     * @return True if the update was successful.
     * @throws BusinessException If business validation fails.
     * @throws SQLException Database access error.
     */
    public boolean updatePartner(Partner partner) throws BusinessException, SQLException {
        return partnerService.update(partner);
    }

    /**
     * Finds a partner by ID.
     * @param id The partner ID.
     * @return The Partner object or null.
     * @throws SQLException Database access error.
     */
    public Partner findPartnerById(int id) throws SQLException {
        return partnerService.findById(id);
    }

    /**
     * Retrieves all partners.
     * @return List of all Partner objects.
     * @throws SQLException Database access error.
     */
    public List<Partner> getAllPartners() throws SQLException {
        return partnerService.findAll();
    }
}