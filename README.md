# SmartATM 2026 

Welcome to **SmartATM 2026**, a premium banking desktop application built for the **CodeSoft Java Internship Task 3 (ATM Interface)**.

This application demonstrates professional UI/UX design capabilities using purely standard Java 21 Swing, completely avoiding external GUI builders. It implements a fully functional mock ATM system with dynamic state management and clean architecture.

## 🔐 Evaluator / Demo Credentials
To test the ATM Interface, please use the following default credentials:
- **Default PIN:** `1234`
- **Initial Account Balance:** `₹ 25,000.00`

## 🌟 Project Features

- **Secure Login Gateway**: Authenticates users based on a 4-digit PIN.
- **Comprehensive Dashboard**: Displays live Date/Time, Available Balance, and Account Holder details beautifully.
- **Robust Banking Operations**:
    - Deposit Funds
    - Withdraw Funds (with rigorous sufficient balance checks)
    - Fast Cash (1-click withdrawal presets)
    - Inter-Account Transfers
    - PIN Change Module
- **Real-Time Mini Statement**: Generates dynamic, scrollable transaction histories with color-coded credits (Green) and debits (Red).
- **Glassmorphism UI Engine**: Implements custom `Java2D` rendering for frosted glass effects, glowing borders, and soft shadows.
- **Dynamic Theming**: Instantly toggle between a deep Navy/Gold Dark Mode and a crisp Professional Light Mode.
- **Data Encapsulation**: Strictly follows OOP & SOLID principles by separating the UI (`ATMDashboard`) from the Core Logic (`ATM`, `BankAccount`, `Transaction`).

## 📁 Folder Structure

```text
src
│
├── app
│      Main.java                 # Entry Point & JFrame setup
│
├── atm
│      ATM.java                  # Controller, Database Mock, Authentication
│      ATMDashboard.java         # Master View, Login, and Menu Router
│
├── model
│      BankAccount.java          # Entity handling Balance & Operations
│      Transaction.java          # Entity for History Logging
│
├── ui
│      GradientPanel.java         # High-quality Background Renderer
│      RoundedButton.java        # Interactive Animated Buttons
│      RoundedTextField.java     # Dual-purpose TextField/PasswordField
│      GlassPanel.java           # Frosted Glass containers
│
└── utils
       ThemeManager.java         # Singleton Color & Typography State
sounds
fonts
```

🔮 Future Improvements:

Database Integration: Link the ATM class to a MySQL or PostgreSQL database for persistent accounts.
Card Reader Simulation: Add a preliminary screen that asks for a Card Number before the PIN.



ScreenShots:  

<img width="789" height="664" alt="image" src="https://github.com/user-attachments/assets/8f265686-0b7a-4c98-95af-735521c49a0e" />

<img width="1352" height="666" alt="image" src="https://github.com/user-attachments/assets/fb198849-f64d-48e1-913a-2ee42f99a053" />

<img width="1344" height="669" alt="image" src="https://github.com/user-attachments/assets/5f8c9a57-4cb7-47c4-844f-f31a508ac0be" />

<img width="1360" height="687" alt="image" src="https://github.com/user-attachments/assets/c4976ed1-1489-432b-bdd6-694d1ca0a7a5" />

<img width="1355" height="678" alt="image" src="https://github.com/user-attachments/assets/a4035afc-9a06-4085-b801-8753eab2a9ea" />

<img width="1355" height="681" alt="image" src="https://github.com/user-attachments/assets/efff06e3-9df8-4036-ad4c-74ca3ec3a85b" />






