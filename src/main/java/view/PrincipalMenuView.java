package view;

import controller.*;
import model.User;
import util.AppLogger;

import javax.swing.JOptionPane;

/**
 * Main menu view - dispatches to different sub-menus based on user selection.
 */
public class PrincipalMenuView {

    private final User currentUser;
    private final BookController bookController;
    private final UserController userController;
    private final PartnerController partnerController;
    private final LoanController loanController;

    // Sub-views
    private BookView bookView;
    private UserView userView;
    private PartnerView partnerView;
    private LoanView loanView;
    private ExportView exportView;

    public PrincipalMenuView(
            User currentUser,
            BookController bookController,
            UserController userController,
            PartnerController partnerController,
            LoanController loanController
    ) {
        this.currentUser = currentUser;
        this.bookController = bookController;
        this.userController = userController;
        this.partnerController = partnerController;
        this.loanController = loanController;

        // Initialize sub-views
        this.bookView = new BookView(bookController);
        this.userView = new UserView(userController, currentUser);
        this.partnerView = new PartnerView(partnerController);
        this.loanView = new LoanView(loanController, bookController, partnerController);
        this.exportView = new ExportView(bookController, loanController);
    }

    /**
     * Displays the main menu and handles navigation.
     */
    public void showMainMenu() {
        boolean running = true;

        while (running) {
            String menu = "=== NovaBook - Main Menu ===\n\n" +
                    "Logged in as: " + currentUser.getUsername() + " [" + currentUser.getRole() + "]\n\n" +
                    UIHelper.createMenu(
                            "Books Management",
                            "Partners Management",
                            "Loans Management",
                            "Users Management" + (currentUser.getRole().equals("ADMIN") ? "" : " [ADMIN ONLY]"),
                            "Export Data"
                    );

            String input = JOptionPane.showInputDialog(
                    null,
                    menu,
                    "NovaBook - Main Menu",
                    JOptionPane.PLAIN_MESSAGE
            );

            if (input == null || input.trim().equals("0")) {
                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to logout?",
                        "Confirm Logout",
                        JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    running = false;
                }
                continue;
            }

            try {
                int option = Integer.parseInt(input.trim());

                switch (option) {
                    case 1:
                        bookView.showBookMenu();
                        break;
                    case 2:
                        partnerView.showPartnerMenu();
                        break;
                    case 3:
                        loanView.showLoanMenu();
                        break;
                    case 4:
                        if (currentUser.getRole().equals("ADMIN")) {
                            userView.showUserMenu();
                        } else {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Access Denied: ADMIN role required.",
                                    "Permission Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                        break;
                    case 5:
                        exportView.showExportMenu();
                        break;
                    default:
                        JOptionPane.showMessageDialog(
                                null,
                                "Invalid option. Please select 1-5 or 0 to exit.",
                                "Invalid Input",
                                JOptionPane.WARNING_MESSAGE
                        );
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                        null,
                        "Please enter a valid number.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                );
            } catch (Exception e) {
                AppLogger.logError("Error in main menu", e);
                JOptionPane.showMessageDialog(
                        null,
                        "An unexpected error occurred: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}