package view;

import controller.PartnerController;
import model.Partner;
import util.AppLogger;

import javax.swing.JOptionPane;
import java.util.List;

/**
 * View for Partner (library members) management operations.
 */
public class PartnerView {

    private final PartnerController partnerController;

    public PartnerView(PartnerController partnerController) {
        this.partnerController = partnerController;
    }

    public void showPartnerMenu() {
        boolean running = true;

        while (running) {
            String menu = "=== Partners Management ===\n\n" +
                    UIHelper.createMenu(
                            "Register New Partner",
                            "List All Partners",
                            "Update Partner",
                            "Find Partner by ID"
                    );

            String input = JOptionPane.showInputDialog(null, menu, "Partners Menu", JOptionPane.PLAIN_MESSAGE);

            if (input == null || input.trim().equals("0")) {
                running = false;
                continue;
            }

            try {
                int option = Integer.parseInt(input.trim());

                switch (option) {
                    case 1: registerPartner(); break;
                    case 2: listAllPartners(); break;
                    case 3: updatePartner(); break;
                    case 4: findPartnerById(); break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid option.", "Error", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void registerPartner() {
        try {
            String name = JOptionPane.showInputDialog(null, "Enter Partner Name:", "Register Partner", JOptionPane.PLAIN_MESSAGE);
            if (name == null || name.trim().isEmpty()) return;

            String email = JOptionPane.showInputDialog(null, "Enter Partner Email:", "Register Partner", JOptionPane.PLAIN_MESSAGE);
            if (email == null || email.trim().isEmpty()) return;

            Partner partner = new Partner();
            partner.setName(name.trim());
            partner.setEmail(email.trim());
            partner.setActive(true);

            Partner registered = partnerController.registerPartner(partner);

            JOptionPane.showMessageDialog(
                    null,
                    "Partner registered successfully!\n\nID: " + registered.getId() + "\nName: " + registered.getName(),
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {
            AppLogger.logError("Failed to register partner", e);
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Registration Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listAllPartners() {
        try {
            List<Partner> partners = partnerController.getAllPartners();

            if (partners.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No partners found.", "Partners List", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String table = UIHelper.formatPartnersTable(partners);
            JOptionPane.showMessageDialog(null, table, "Partners List", JOptionPane.PLAIN_MESSAGE);

        } catch (Exception e) {
            AppLogger.logError("Failed to list partners", e);
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePartner() {
        try {
            String idStr = JOptionPane.showInputDialog(null, "Enter Partner ID to update:", "Update Partner", JOptionPane.PLAIN_MESSAGE);
            if (idStr == null || idStr.trim().isEmpty()) return;

            int id = Integer.parseInt(idStr.trim());
            Partner partner = partnerController.findPartnerById(id);

            if (partner == null) {
                JOptionPane.showMessageDialog(null, "Partner not found with ID: " + id, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String name = JOptionPane.showInputDialog(null, "New Name [" + partner.getName() + "]:", "Update Partner", JOptionPane.PLAIN_MESSAGE);
            if (name != null && !name.trim().isEmpty()) {
                partner.setName(name.trim());
            }

            String email = JOptionPane.showInputDialog(null, "New Email [" + partner.getEmail() + "]:", "Update Partner", JOptionPane.PLAIN_MESSAGE);
            if (email != null && !email.trim().isEmpty()) {
                partner.setEmail(email.trim());
            }

            partnerController.updatePartner(partner);

            JOptionPane.showMessageDialog(null, "Partner updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            AppLogger.logError("Failed to update partner", e);
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Update Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void findPartnerById() {
        try {
            String idStr = JOptionPane.showInputDialog(null, "Enter Partner ID:", "Find Partner", JOptionPane.PLAIN_MESSAGE);
            if (idStr == null || idStr.trim().isEmpty()) return;

            int id = Integer.parseInt(idStr.trim());
            Partner partner = partnerController.findPartnerById(id);

            if (partner == null) {
                JOptionPane.showMessageDialog(null, "Partner not found with ID: " + id, "Not Found", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String details = String.format(
                    "ID: %d\nName: %s\nEmail: %s\nStatus: %s",
                    partner.getId(),
                    partner.getName(),
                    partner.getEmail(),
                    partner.isActive() ? "ACTIVE" : "INACTIVE"
            );

            JOptionPane.showMessageDialog(null, details, "Partner Details", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            AppLogger.logError("Failed to find partner", e);
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}