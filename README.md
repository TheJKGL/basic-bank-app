# Local run guide
1. Navigate to ./basic-bank-application directory and run `docker compose up`
2. Open pgAdmin in localhost:5050. 
   - Use these credentials: 
     - email: admin@admin.com, 
     - password: password
3. Create new db server with "host name/address" = "pg-database".
4. Add new table with name "basic-bank-app-db". 
5. Run BasicBankApplication.

This application is a basic banking system for managing accounts and transactions,
supporting deposits, withdrawals, and transfers with secure, validated operations.