import React, { useState } from "react";
import Button from "@mui/material/Button";
import Drawer from "@mui/material/Drawer";
import Typography from "@mui/material/Typography";
import TextField from "@mui/material/TextField";
import Box from "@mui/material/Box";
import alertify from "alertifyjs";
import { bindActionCreators } from "redux";
import { connect } from "react-redux";
import * as accountActions from "../../redux/actions/accountActions";
import Select from "@mui/material/Select";
import MenuItem from "@mui/material/MenuItem";
import apiClient from "../../apiClient";  // Import the apiClient

function AccountForm({
  onSaveAccount,
  open = true,
  onClose,
  actions,
  currentAccount,
}) {
  const [amount, setAmount] = useState("");
  const [targetAccount, setTargetAccount] = useState("");
  const [accountInfo, setAccountInfo] = useState({
    transactionType: "", 
  });
  const [paymentInfo, setPaymentInfo] = useState({
    beneficiary: "", 
    accountNumber: "", 
    reference: "", 
    paymentAmount: "", 
  });

  // Replace axios with apiClient for making API requests
  const postRequestToApi = async (apiUrl, jsonData) => {

    try {
      const response = await apiClient.post(apiUrl, jsonData, {
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (response.status === 200) {
        alertify.success("Transaction successful.");
      }
    } catch (error) {
      alertify.error("Something went wrong");
    }

    actions.getAccounts();
    actions.getTotalBalance();
    actions.getTransactionHistory();

    onSaveAccount(accountInfo);
    onClose(); 
  };

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setPaymentInfo({
      ...paymentInfo,
      [name]: value,
    });

    console.log(paymentInfo);
  };

  const handleTransactionTypeChange = (event) => {
    const { value } = event.target;
    setAccountInfo({
      ...accountInfo,
      transactionType: value,
    });
  };

  const handleAmountChange = (event) => {
    setAmount(event.target.value);
  };

  const handleTargetAccountChange = (event) => {
    setTargetAccount(event.target.value);
  };

  const handleDepositMoney = async (event) => {
    event.preventDefault();

    const jsonData = {
      accountId: currentAccount.accountId,
      depositAmount: amount,
    };

    const apiUrl = "/transact/deposit";  // Use relative URL
    postRequestToApi(apiUrl, jsonData);
  };

  const handleWithdrawMoney = (event) => {
    event.preventDefault();

    const jsonData = {
      accountId: currentAccount.accountId,
      withdrawalAmount: amount,
    };

    const apiUrl = "/transact/withdraw";  // Use relative URL
    postRequestToApi(apiUrl, jsonData);
  };

  const handleTransferMoney = (event) => {
    event.preventDefault();

    const jsonData = {
      sourceAccount: currentAccount.accountId,
      targetAccount: targetAccount,
      amount: amount,
    };

    const apiUrl = "/transact/transfer";  // Use relative URL
    postRequestToApi(apiUrl, jsonData);
  };

  const handlePaymentTransaction = (event) => {
    event.preventDefault();

    const jsonData = {
      ...paymentInfo,
      accountId: currentAccount.accountId,
    };

    const apiUrl = "/transact/payment";  // Use relative URL
    postRequestToApi(apiUrl, jsonData);
  };

  return (
    <Drawer
      anchor="right"
      open={open}
      onClose={onClose}
      ModalProps={{
        disableScrollLock: true,
      }}
    >
      <div
        style={{
          width: "600px",
          padding: "16px",
        }}
        role="presentation"
        onClick={onClose}
        onKeyDown={onClose}
      >
        <Typography variant="h6" style={{ marginTop: "60px" }}>
          Transaction
        </Typography>
        <Select
          name="transactionType"
          label="Transaction Type"
          value={accountInfo.transactionType}
          onChange={handleTransactionTypeChange}
          fullWidth
          margin="normal"
          onClick={(event) => {
            event.stopPropagation();
          }}
          onKeyDown={(event) => {
            event.stopPropagation();
          }}
        >
          <MenuItem value="Deposit Transaction">Deposit Transaction</MenuItem>
          <MenuItem value="Transfer Transaction">Transfer Transaction</MenuItem>
          <MenuItem value="Withdraw Transaction">Withdraw Transaction</MenuItem>
          <MenuItem value="Payment Transaction">Payment Transaction</MenuItem>
        </Select>

        {accountInfo.transactionType === "Deposit Transaction" ? (
          <>
            <TextField
              readonly
              disabled
              name="depositId"
              label="Account Id"
              value={currentAccount.accountId}
              fullWidth
              margin="normal"
              onClick={(event) => {
                event.stopPropagation();
              }}
              onKeyDown={(event) => {
                event.stopPropagation();
              }}
            />
            <TextField
              name="depositAmount"
              label="Amount"
              value={amount}
              onChange={handleAmountChange}
              fullWidth
              margin="normal"
              onClick={(event) => {
                event.stopPropagation();
              }}
              onKeyDown={(event) => {
                event.stopPropagation();
              }}
            />
            
            <Button
              variant="contained"
              color="primary"
              sx={{ backgroundColor: "#F5BD52" }}
              onClick={handleDepositMoney}
            >
              Transact
            </Button>

            <img src="card.jpg" alt="Deposit" style={{ marginTop: '100px', maxWidth: '100%', height: 'auto' }} />
          </>
        ) : null}

        {accountInfo.transactionType === "Transfer Transaction" ? (
          <>
            <TextField
              readonly
              disabled
              name="sourceAccountId"
              label="Source Account Id"
              value={currentAccount.accountId}
              fullWidth
              margin="normal"
              onClick={(event) => {
                event.stopPropagation();
              }}
              onKeyDown={(event) => {
                event.stopPropagation();
              }}
            />
            <TextField
              name="targetAccountId"
              label="Target Account Id"
              value={targetAccount}
              onChange={handleTargetAccountChange}
              fullWidth
              margin="normal"
              onClick={(event) => {
                event.stopPropagation();
              }}
              onKeyDown={(event) => {
                event.stopPropagation();
              }}
            />
            <TextField
              name="transferAmount"
              label="Amount"
              value={amount}
              onChange={handleAmountChange}
              fullWidth
              margin="normal"
              onClick={(event) => {
                event.stopPropagation();
              }}
              onKeyDown={(event) => {
                event.stopPropagation();
              }}
            />
            <Button
              variant="contained"
              color="primary"
              sx={{ backgroundColor: "#F5BD52" }}
              onClick={handleTransferMoney}
            >
              Transact
            </Button>

            <img src="transact.jpg" alt="Transfer" style={{ marginTop: '100px', maxWidth: '100%', height: 'auto' }} />
          </>
        ) : null}

        {accountInfo.transactionType === "Withdraw Transaction" ? (
          <>
            <TextField
              readonly
              disabled
              name="withdrawId"
              label="Account Id"
              value={currentAccount.accountId}
              fullWidth
              margin="normal"
              onClick={(event) => {
                event.stopPropagation();
              }}
              onKeyDown={(event) => {
                event.stopPropagation();
              }}
            />
            <TextField
              name="withdrawAmount"
              label="Amount"
              value={amount}
              onChange={handleAmountChange}
              fullWidth
              margin="normal"
              onClick={(event) => {
                event.stopPropagation();
              }}
              onKeyDown={(event) => {
                event.stopPropagation();
              }}
            />
            <Button
              variant="contained"
              color="primary"
              sx={{ backgroundColor: "#F5BD52" }}
              onClick={handleWithdrawMoney}
            >
              Transact
            </Button>

            <img src="transact.jpg" alt="Withdraw" style={{ marginTop: '100px', maxWidth: '100%', height: 'auto' }} />
          </>
        ) : null}

        {accountInfo.transactionType === "Payment Transaction" ? (
          <>
            <TextField
              readonly
              disabled
              name="accountId"
              label="Account Id"
              value={currentAccount.accountId}
              onChange={handleInputChange}
              fullWidth
              margin="normal"
              onClick={(event) => {
                event.stopPropagation();
              }}
              onKeyDown={(event) => {
                event.stopPropagation();
              }}
            />
            <TextField
              name="beneficiary"
              label="Beneficiary"
              value={paymentInfo.beneficiary}
              onChange={handleInputChange}
              fullWidth
              margin="normal"
              onClick={(event) => {
                event.stopPropagation();
              }}
              onKeyDown={(event) => {
                event.stopPropagation();
              }}
            />
            <TextField
              name="accountNumber"
              label="Account Number"
              value={paymentInfo.accountNumber}
              onChange={handleInputChange}
              fullWidth
              margin="normal"
              onClick={(event) => {
                event.stopPropagation();
              }}
              onKeyDown={(event) => {
                event.stopPropagation();
              }}
            />
            <TextField
              name="reference"
              label="Reference"
              value={paymentInfo.reference}
              onChange={handleInputChange}
              fullWidth
              margin="normal"
              onClick={(event) => {
                event.stopPropagation();
              }}
              onKeyDown={(event) => {
                event.stopPropagation();
              }}
            />
            <TextField
              name="paymentAmount"
              label="Payment Amount"
              value={paymentInfo.paymentAmount}
              onChange={handleInputChange}
              fullWidth
              margin="normal"
              onClick={(event) => {
                event.stopPropagation();
              }}
              onKeyDown={(event) => {
                event.stopPropagation();
              }}
            />
            <Button
              variant="contained"
              color="primary"
              sx={{ backgroundColor: "#F5BD52" }}
              onClick={handlePaymentTransaction}
            >
              Transact
            </Button>

            <img src="payment.jpg" alt="Payment" style={{ marginTop: '100px', maxWidth: '100%', height: 'auto' }} />
          </>
        ) : null}

        <Box mt={2}></Box>
      </div>
    </Drawer>
  );
}

function mapDispatchToProps(dispatch) {
  return {
    actions: {
      getAccounts: bindActionCreators(accountActions.getAccounts, dispatch),
      getTotalBalance: bindActionCreators(accountActions.getTotalBalance,dispatch),
      getTransactionHistory: bindActionCreators(accountActions.getTransactionHistory,dispatch)
    },
  };
}

function mapStateToProps(state) {
  return {
    currentAccount: state.changeAccountReducer,
  };
}

export default connect(mapStateToProps, mapDispatchToProps)(AccountForm);
