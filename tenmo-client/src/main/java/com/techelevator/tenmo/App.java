package com.techelevator.tenmo;

import com.techelevator.tenmo.models.AuthenticatedUser;
import com.techelevator.tenmo.models.Transfer;
import com.techelevator.tenmo.models.User;
import com.techelevator.tenmo.models.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.view.ConsoleService;
import io.cucumber.java.bs.A;

import java.math.BigDecimal;
import java.util.Scanner;

public class App {

private static final String API_BASE_URL = "http://localhost:8080/";
    
    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
	private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
	private static final String[] LOGIN_MENU_OPTIONS = { LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
	private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
	private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
	private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
	private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT };
	
    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    AccountService accountService = new AccountService(API_BASE_URL);
    TransferService transferService = new TransferService(API_BASE_URL);

    public static void main(String[] args) {
    	App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL));
    	app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService) {
		this.console = console;
		this.authenticationService = authenticationService;
	}

	public void run() {
		System.out.println("*********************");
		System.out.println("* Welcome to TEnmo! *");
		System.out.println("*********************");
		
		registerAndLogin();
		mainMenu();
	}

	private void mainMenu() {
		while(true) {
			String choice = (String)console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if(MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
				viewCurrentBalance();
			} else if(MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
				viewTransferHistory();
			} else if(MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
				viewPendingRequests();
			} else if(MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
				sendBucks();
			} else if(MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
				requestBucks();
			} else if(MAIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else {
				// the only other option on the main menu is to exit
				exitProgram();
			}
		}
	}

	private void viewCurrentBalance() {
		// TODO Auto-generated method stub
		System.out.println("Your current balance is $" + accountService.getBalance(currentUser.getToken()));
		
	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
		System.out.println("Transfer history: ");
		System.out.println("Transfer ID" + "         " + "From/To" + "           " + "Amount");
		for (int i = 0; i < transferService.getTransferHistory(currentUser.getToken()).length; i++) {
			Transfer transfer = transferService.getTransferHistory(currentUser.getToken())[i];

//make sure to always get the opposite person printed out with the brief details rather than yourself.
			String transferType = "";
			if (transfer.getAccountToName().equals(currentUser.getUser().getUsername())) {
				transferType = "Received From";
				System.out.println("    " + transfer.getTransferId() + "         " +
						transferType + ": " + transfer.getAccountFromName() + "       " + "$" + transfer.getAmount());
			} else if (transfer.getAccountFromName().equals(currentUser.getUser().getUsername())) {
				transferType = "Sent To";
				System.out.println("   " + transfer.getTransferId() + "          " +
						transferType + ": " + transfer.getAccountToName() + "       " + "$" + transfer.getAmount());
			}
		}
//prompt user for transfer ID they want to see details from
		System.out.println("Please enter the transfer ID to view details. (0 to cancel)");
		Scanner input = new Scanner(System.in);
		String inputValue = input.nextLine();
		int transferIdSelection = Integer.parseInt(inputValue);

		if (transferIdSelection == 0) {
			return;
		} else {
			System.out.println("Transfer details ");
			Transfer transferDetails = transferService.getTransferDetails(currentUser.getToken(), transferIdSelection);

			if (transferIdSelection == transferDetails.getTransferId()) {
				System.out.println("Id: " + transferDetails.getTransferId());
				System.out.println("From: " + transferDetails.getAccountFromName());
				System.out.println("To: " + transferDetails.getAccountToName());
				System.out.println("Type: " + transferDetails.getTransferType());
				System.out.println("Status: " + transferDetails.getTransferStatus());
				System.out.println("Amount: $" + transferDetails.getAmount());
			}
		}
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
		
	}

	private void sendBucks() {
		// TODO Auto-generated method stub
		System.out.println("User's ID 			Name");
		for (int i = 0; i < accountService.listUsers(currentUser.getToken()).length; i++){
			User user = accountService.listUsers(currentUser.getToken())[i];
			System.out.println(user.getId() + " 			" + user.getUsername());
		}
//prompt user for user ID they want to send money to
		System.out.println("Please enter the ID of the user you are sending to. (0 to cancel)");
		Scanner input = new Scanner(System.in);
		String inputValue = input.nextLine();
		Long userToIdSelection = Long.parseLong(inputValue);

		if (userToIdSelection == 0) {
			return;
		}

//make sure user isn't trying to send money to their own id.
		Long userFromId = Long.parseLong(currentUser.getUser().getId().toString());
		if (userFromId.equals(userToIdSelection)){
			System.out.println("Withdrawal ID can't be the same as the ID being sent to.");
			return;
		}
//get the amount user wants to transfer
		System.out.print("Enter amount: $");
		inputValue = input.nextLine();
		BigDecimal amountToSend = new BigDecimal(inputValue);

//make the transfer here
		String result = transferService.makeTransfer(currentUser.getToken(), userFromId, userToIdSelection, amountToSend);
		System.out.println(result);
	}

	private void requestBucks() {
		// TODO Auto-generated method stub
		
	}
	
	private void exitProgram() {
		System.exit(0);
	}

	private void registerAndLogin() {
		while(!isAuthenticated()) {
			String choice = (String)console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
			if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
				login();
			} else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
				register();
			} else {
				// the only other option on the login menu is to exit
				exitProgram();
			}
		}
	}

	private boolean isAuthenticated() {
		return currentUser != null;
	}

	private void register() {
		System.out.println("Please register a new user account");
		boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
            	authenticationService.register(credentials);
            	isRegistered = true;
            	System.out.println("Registration successful. You can now login.");
            } catch(AuthenticationServiceException e) {
            	System.out.println("REGISTRATION ERROR: "+e.getMessage());
				System.out.println("Please attempt to register again.");
            }
        }
	}

	private void login() {
		System.out.println("Please log in");
		currentUser = null;
		while (currentUser == null) //will keep looping until user is logged in
		{
			UserCredentials credentials = collectUserCredentials();
		    try {
				currentUser = authenticationService.login(credentials);
			} catch (AuthenticationServiceException e) {
				System.out.println("LOGIN ERROR: "+e.getMessage());
				System.out.println("Please attempt to login again.");
			}
		}
	}
	
	private UserCredentials collectUserCredentials() {
		String username = console.getUserInput("Username");
		String password = console.getUserInput("Password");
		return new UserCredentials(username, password);
	}
}
